import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class JourneyPlanner {

    private DirectedGraph metroGraph;

    private PathOptimization pathOptimization;
    private Integer transferLimit;

    public enum PathOptimization {
        MinimumTime,
        FewerStops,
    }

    /**
     * Constructs a new JourneyPlanner object.
     * @param metroFilePath a valid file path to the metro stations file.
     * @param walkEdgesFilePath a file path to the walk edges file. Walk edges are not included
     *                          if this value is null.
     */
    public JourneyPlanner(String metroFilePath, String walkEdgesFilePath) throws FileNotFoundException {

        this.pathOptimization = PathOptimization.FewerStops;
        this.transferLimit = null;

        this.metroGraph = new DirectedGraph();

        Scanner scanner = new Scanner(new File(metroFilePath));

        // Ignore first line.
        scanner.nextLine();

        MetroStop previousMetroStop = new MetroStop(scanner.nextLine());

        while (scanner.hasNextLine()) {

            MetroStop metroStop = new MetroStop(scanner.nextLine());

            if (metroStop.getDirectionId() != previousMetroStop.getDirectionId()) {
                previousMetroStop = metroStop;
                continue;
            }

            MetroStation previousMetroStation = (MetroStation) this.metroGraph.getVertex(previousMetroStop.getStopName());
            MetroStation metroStation = (MetroStation) this.metroGraph.getVertex(metroStop.getStopName());

            if (previousMetroStation == null) {
                previousMetroStation = new MetroStation(previousMetroStop.getStopName());
            }

            if (metroStation == null) {
                metroStation = new MetroStation(metroStop.getStopName());
            }

            previousMetroStation.addStop(previousMetroStop);
            metroStation.addStop(metroStop);

            int timePassed = Math.abs(metroStop.getArrivalTime() - previousMetroStop.getArrivalTime());

            this.metroGraph.addEdge(previousMetroStation, metroStation, timePassed);

            previousMetroStop = metroStop;
        }

        // Add walk edges.
        if (walkEdgesFilePath != null) {
            scanner = new Scanner(new File(walkEdgesFilePath));

            while (scanner.hasNextLine()) {

                String[] walkEdges = scanner.nextLine().split(",");

                MetroStation metroStationOrigin = (MetroStation) this.metroGraph.getVertex(walkEdges[0]);
                MetroStation metroStationEnd = (MetroStation) this.metroGraph.getVertex(walkEdges[1]);

                metroStationOrigin.addNeighbourStation(metroStationEnd);

                this.metroGraph.addEdge(metroStationOrigin, metroStationEnd, 0);
            }
        }
    }

    /**
     * Suggest a path that goes from parameter originStation to parameter destinationStation.
     * Function will limit transfer count according to the classes transferLimit variable.
     * Will suggest walk routes if they are enabled from the constructor.
     */
    public void suggestPath(String originStation, String destinationStation) {

        System.out.printf("Origin station: %s\n", originStation);
        System.out.printf("Destination station: %s\n", destinationStation);
        System.out.printf("Preferation: %s\n", this.pathOptimization.name());

        LinkedList<Vertex> path = new LinkedList<>();

        // A lambda object for restricting paths.
        // This lambda ensures that transfer count is limited by the this.transferLimit variable.
        DirectedGraph.PathFinderLambdaInterface graphLambda = (Vertex currentVertex) -> {

            // Check transfer count. Two consecutive walks are not allowed.
            int transferCount = 0;
            String oldRoute = null;

            boolean walked = false;

            while (currentVertex != null) {

                Vertex parentVertex = currentVertex.getParent();

                if (parentVertex == null) {
                    break;
                }

                MetroStop metroStop = ((MetroStation)parentVertex).getStop((MetroStation)currentVertex);

                if (metroStop == null) {

                    if (((MetroStation) parentVertex).hasNeighbourStation((MetroStation)currentVertex)) {
                        if (walked) {
                            return false;
                        }
                        transferCount += 1;
                        walked = true;
                    }
                } else if (!metroStop.getRouteShortName().equals(oldRoute)) {
                    transferCount += 1;
                    oldRoute = metroStop.getRouteShortName();
                    walked = false;
                }

                if (this.transferLimit != null && transferCount > this.transferLimit) {
                    return false;
                }

                currentVertex = parentVertex;
            }

            return true;
        };

        int totalTimePassed;
        if (this.pathOptimization == PathOptimization.FewerStops) {
            totalTimePassed = this.metroGraph.getShortestPath(originStation, destinationStation, path, graphLambda);
        } else {
            totalTimePassed = this.metroGraph.getCheapestPath(originStation, destinationStation, path, graphLambda);
        }

        if (path.size() <= 1) {
            System.out.println("There is no path\n");
            return;
        }

        // Print the result.
        System.out.print("Suggestion:");

        String currentRoute = null;
        int transferCount = 0;

        for (int i = 0; i < path.size(); ++i) {

            MetroStation currentStation = (MetroStation) path.get(i);
            MetroStop currentStop;

            if (i != path.size() - 1) {
                MetroStation nextStation = (MetroStation) path.get(i + 1);
                currentStop = currentStation.getStop(nextStation);
            } else {
                MetroStation previousStation = (MetroStation) path.get(i - 1);
                currentStop = currentStation.getStop(previousStation);
            }

            if (currentRoute == null || currentStop == null || !currentRoute.equals(currentStop.getRouteShortName())) {

                if (i > 0) {
                    MetroStop routeLastStop = currentStation.getStop((MetroStation) path.get(i - 1));
                    if (routeLastStop != null) {
                        System.out.printf(", %s", routeLastStop);
                    }
                }


                if (currentStop == null) {
                    // There is no stop between stations. Stations are neighbour.
                    if (i < path.size() - 1) {
                        System.out.printf("\nWalk between (%s - %s)", currentStation.getName(), ((MetroStation) path.get(i + 1)).getName());
                        transferCount += 1;
                    }

                    currentRoute = null;
                    continue;
                } else {
                    System.out.printf("\n%s\n   ", currentStop.getRouteLongName());
                    currentRoute = currentStop.getRouteShortName();
                    transferCount += 1;
                }
            } else {
                System.out.print(", ");
            }

            System.out.print(currentStop);
        }

        System.out.printf("\nTotal %d stations. Journey will take %d minutes. Transfer count: %d\n\n", path.size(), totalTimePassed / 60, transferCount);
    }

    public Integer getTransferLimit() {
        return transferLimit;
    }

    public void setTransferLimit(Integer transferLimit) {
        this.transferLimit = transferLimit;
    }


    public PathOptimization getPathOptimization() {
        return pathOptimization;
    }

    public void setPathOptimization(PathOptimization pathOptimization) {
        this.pathOptimization = pathOptimization;
    }

}
