
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileReader {

    /**
     * Reads the text file in the parameter filePath.
     * @throws FileNotFoundException if it does not exist
     * @return Lines in the text file in a String array
     */
    static String[] readFile(String filePath) throws FileNotFoundException {

        File file = new File(filePath);

        Scanner scanner = new Scanner(file);

        int lineCount = 0;
        while (scanner.hasNextLine()) {
            lineCount += 1;
            scanner.nextLine();
        }

        String[] lines = new String[lineCount];

        scanner.close();
        scanner = new Scanner(file);
        int index = 0;

        while (scanner.hasNextLine()) {

            lines[index] = scanner.nextLine();
            index += 1;
        }

        return lines;
    }
}
