import java.util.HashMap;
import java.util.HashSet;

public class MetroStation extends Vertex {
    private HashMap<String, MetroStop> stops;

    private HashSet<String> neighbourStations;

    public MetroStation(String name) {
        super(name);
        this.stops = new HashMap<>();
        this.neighbourStations = new HashSet<>();
    }

    public void addNeighbourStation(MetroStation metroStation) {
        this.neighbourStations.add(metroStation.getName());
    }

    public boolean hasNeighbourStation(MetroStation metroStation) {
        return this.neighbourStations.contains(metroStation.getName());
    }

    public void addStop(MetroStop metroStop) {
        this.stops.put(metroStop.getRouteShortName() + "-" + metroStop.getDirectionId(), metroStop);
    }

    /**
     * @return the stop that this station has that is connected to the given parameter station.
     */
    public MetroStop getStop(MetroStation metroStation) {

        for (MetroStop stop : metroStation.stops.values()) {

            MetroStop metroStop = this.stops.get(stop.getRouteShortName() + "-" + stop.getDirectionId());

            if (metroStop != null) {
                return metroStop;
            }
        }
        return null;
    }
}
