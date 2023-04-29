import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class HighScoreTable {

    // File path of the high score table
    private String filePath;

    // Maximum count of high score table entries
    private int maxCount;

    // Q1 and Q2 for storing name and score information of users
    private Queue nameQueue;
    private Queue scoreQueue;

    /**
     * Creates a new HighScoreTable instance with the parameter values.
     * @param filePath file path of the high score table to read and write.
     * @param maxCount max count of entries in the high score table.
     */
    public HighScoreTable(String filePath, int maxCount) {

        this.filePath = filePath;
        this.maxCount = maxCount;
    }

    /**
     * Reads the high score table from this.filePath
     * @throws Exception if there is no file in the path this.filePath. Or the format is not correct
     */
    public void readFile() throws Exception {

        File file = new File(this.filePath);
        Scanner scanner = new Scanner(file);

        this.nameQueue = new Queue();
        this.scoreQueue = new Queue();

        while (scanner.hasNextLine()) {

            String line = scanner.nextLine();

            String[] values = line.split(" ");

            if (values.length != 2) {
                throw new Exception("Invalid highscoretable.txt format. Must be two arguments in each line");
            }

            this.nameQueue.enqueue(values[0]);
            this.scoreQueue.enqueue(Integer.parseInt(values[1]));

            // Stop reading the file after reaching the max count.
            if (this.nameQueue.size() >= this.maxCount) {
                break;
            }
        }
    }

    /**
     * Writes the high score table from this.filePath without modifying this instance.
     * @throws Exception if there is no file in the path this.filePath
     */
    public void writeFile() throws Exception {

        FileWriter file = new FileWriter(this.filePath);

        Queue tempNameQueue = new Queue(this.nameQueue);
        Queue tempScoreQueue = new Queue(this.scoreQueue);

        while (!tempNameQueue.isEmpty() && !tempScoreQueue.isEmpty()) {

            String name = (String)tempNameQueue.dequeue();
            int score = (int)tempScoreQueue.dequeue();

            file.write(name + " " + score + "\r\n");
        }

        file.close();
    }

    /**
     * Sorts the players based on their score.
     */
    public void sortPlayers() throws Exception {

        Queue tempNameQueue = this.nameQueue;
        Queue tempScoreQueue = this.scoreQueue;

        this.nameQueue = new Queue();
        this.scoreQueue = new Queue();

        // Add players one by one
        while (!tempNameQueue.isEmpty()) {

            String name = (String)tempNameQueue.dequeue();
            int score = (int)tempScoreQueue.dequeue();

            // This function will handle the sorting logic
            this.addPlayer(name, score);
        }
    }

    /**
     * Adds a new player to the high score table. The player will be added according to the parameter newScore.
     * If there is a player with the score parameter newScore, new player will be added after that player.
     * @param newName name of the player to be added
     * @param newScore score of the player to be added
     */
    public void addPlayer(String newName, int newScore) throws Exception {

        Queue tempNameQueue = new Queue();
        Queue tempScoreQueue = new Queue();

        boolean addedElement = false;

        while (!this.scoreQueue.isEmpty() && tempScoreQueue.size() < this.maxCount) {

            int score = (int)this.scoreQueue.peek();

            if (!addedElement && score < newScore) {

                tempScoreQueue.enqueue(newScore);
                tempNameQueue.enqueue(newName);

                addedElement = true;

            } else {
                tempScoreQueue.enqueue(this.scoreQueue.dequeue());
                tempNameQueue.enqueue(this.nameQueue.dequeue());
            }
        }

        if (!addedElement && tempScoreQueue.size() < this.maxCount) {
            tempScoreQueue.enqueue(newScore);
            tempNameQueue.enqueue(newName);
        }

        this.scoreQueue = tempScoreQueue;
        this.nameQueue = tempNameQueue;
    }

    /**
     * Changes already existing score with the newScore if the current score is lower than parameter newScore.
     * @return true if there is a score entry with the parameter name, false if there is not.
     */
    public boolean changeExistingUser(String newName, int newScore) throws Exception {

        Queue tempNameQueue = new Queue();
        Queue tempScoreQueue = new Queue();

        boolean foundElement = false;

        while (!this.nameQueue.isEmpty()) {

            String name = (String)this.nameQueue.dequeue();
            int score = (int)this.scoreQueue.dequeue();

            if (!foundElement && name.equals(newName) && score < newScore) {

                foundElement = true;
            } else {
                tempScoreQueue.enqueue(score);
                tempNameQueue.enqueue(name);
            }
        }

        this.scoreQueue = tempScoreQueue;
        this.nameQueue = tempNameQueue;

        if (foundElement) {
            this.addPlayer(newName, newScore);
        }

        return foundElement;
    }

    /**
     * Prints the high score table to the console.
     */
    public void print() {

        Queue tempNameQueue = new Queue(this.nameQueue);
        Queue tempScoreQueue = new Queue(this.scoreQueue);

        while (!tempNameQueue.isEmpty() && !tempScoreQueue.isEmpty()) {

            System.out.printf("%s %d\r\n", tempNameQueue.dequeue(), (int)tempScoreQueue.dequeue());
        }
    }

}
