import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class HighScoreTable {

    private static final int MAX_SIZE = 12;

    private SingleLinkedList nameSll;
    private SingleLinkedList scoreSll;

    /**
     * Create a new HighScoreTable object from highscoretable.txt file.
     * @param tableFilePath path to the highscoretable.txt file.
     * @throws FileNotFoundException if there is no file in the path tableFilePath
     */
    public HighScoreTable(String tableFilePath) throws FileNotFoundException {

        this.nameSll = new SingleLinkedList();
        this.scoreSll = new SingleLinkedList();

        File tableFile = new File(tableFilePath);

        Scanner tableScanner = new Scanner(tableFile);

        while (tableScanner.hasNextLine()) {

            String[] nameScore = tableScanner.nextLine().split(" ");

            String name = nameScore[0];
            int score = Integer.parseInt(nameScore[1]);

            this.addScore(name, score);
        }
    }

    /**
     * Add new score entry to the high score table.
     * If there is a score entry with the name, replaces the score with the bigger one.
     */
    public void addScore(String name, int score) {

        int nameIndex = this.nameSll.find(name);

        if (nameIndex >= 0) {
            // There is a node with a value name.

            // Get score value
            Node scoreNode = this.scoreSll.getNodeAt(nameIndex);

            if (score > (int)scoreNode.getData()) {

                // Delete the old entry to create a new one with new score value.
                // This is needed because the position of the entry may change with the new value.
                // So deleting and creating a new entry is the only way.
                this.nameSll.delete(name);
                this.scoreSll.delete(scoreNode.getData());
            } else {
                // No operation needed. Score is already bigger.
                return;
            }
        }

        // Create a new score entry
        int scoreIndex = this.scoreSll.addSorted(score);

        this.nameSll.insert(name, scoreIndex);
    }

    public void writeToFile(String tableFilePath) throws IOException {

        FileWriter tableFile = new FileWriter(tableFilePath);

        tableFile.write(this.toString());
        tableFile.close();
    }

    public void print() {

        System.out.println("High Score Table:");

        System.out.print(this);
    }

    /**
     * String converter function.
     */
    public String toString() {

        String result = new String();

        // High score cannot contain more than MAX_SIZE elements.
        int size = Math.min(this.scoreSll.size(), MAX_SIZE);

        for (int i = 0; i < size; ++i) {

            String name = (String)this.nameSll.getNodeAt(i).getData();
            int score = (int)this.scoreSll.getNodeAt(i).getData();

            result += name + " " + score + "\r\n";
        }

        return result;
    }

}
