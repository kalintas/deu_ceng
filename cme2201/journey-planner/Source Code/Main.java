import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        // Read the test cases and suggest paths.
        Scanner scanner = new Scanner(new File("./Test100.csv"));
        JourneyPlanner journeyPlanner = new JourneyPlanner("./Paris_RER_Metro_v2.csv", "./walk_edges.txt");

        journeyPlanner.setTransferLimit(null);

        scanner.nextLine(); // Ignore first line.

        var startTime = System.nanoTime();
        int queries = 0;

        while (scanner.hasNext()) {

            String[] stationsSplit = scanner.nextLine().split(",");

            if (stationsSplit[2].equals("0")) {
                journeyPlanner.setPathOptimization(JourneyPlanner.PathOptimization.FewerStops);
            } else {
                journeyPlanner.setPathOptimization(JourneyPlanner.PathOptimization.MinimumTime);
            }

            journeyPlanner.suggestPath(stationsSplit[0], stationsSplit[1]);
            queries += 1;
        }

        var endTime = System.nanoTime();
        var queryTime = (endTime - startTime) / 1000000; // Convert to milliseconds.

        System.out.printf("\nAverage query time: %fms\n", (double)queryTime / (double) queries);
    }
}