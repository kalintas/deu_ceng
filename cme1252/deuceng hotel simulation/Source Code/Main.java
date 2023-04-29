import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Hotel hotel = new Hotel();

        try {
            // Read commands.txt file and run the commands
            File file = new File("./commands.txt");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                System.out.println(input);
                hotel.executeCommand(input);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Cannot found ./commands.txt file");
            e.printStackTrace();
        }

        // Change scanners source to stdin to read commands from the console
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                String input = scanner.nextLine();
                hotel.executeCommand(input);
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
}