import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class MemoryGame {

    private static final String ANIMALS_FILE_PATH = "D:\\animals.txt";

    private static final String HIGH_SCORE_TABLE_FILE_PATH = "D:\\highscoretable.txt";

    private static final Random random = new Random();

    private HighScoreTable highScoreTable;

    private SingleLinkedList animalSLL;

    private SingleLinkedList gameBoardSll1;
    private SingleLinkedList gameBoardSll2;

    private int score;
    private int steps;

    /**
     * Default constructor.
     */
    public MemoryGame() throws FileNotFoundException {

        this.highScoreTable = new HighScoreTable(HIGH_SCORE_TABLE_FILE_PATH);

        this.animalSLL = new SingleLinkedList();
        this.gameBoardSll1 = new SingleLinkedList();
        this.gameBoardSll2 = new SingleLinkedList();

        this.score = 0;
        this.steps = 0;

        // Fill the animalSll from the animals.txt file
        File animalsFile = new File(ANIMALS_FILE_PATH);

        Scanner animalsScanner = new Scanner(animalsFile);

        while (animalsScanner.hasNextLine()) {
            this.animalSLL.add(animalsScanner.nextLine());
        }
    }

    public void playGame() throws IOException {

        this.animalSLL.print("Animal SLL");

        Scanner consoleScanner = new Scanner(System.in);

        int n;
        int animalSllSize = this.animalSLL.size();

        // Get n value from user
        while (true) {
            System.out.print("Please enter n: ");

            try {
                n = Integer.parseInt(consoleScanner.nextLine());

                if (n > animalSllSize) {
                    System.out.printf("N(%d) cannot be larger than AnimalSll's size(%d)\r\n", n, animalSllSize);
                } else {
                    break;
                }
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid number.");
            }
        }

        this.fillGameBoards(n);

        this.gameBoardSll1.print("SLL1");
        this.gameBoardSll2.print("SLL2");

        int tileCount = n;

        // Play until there is no tile unmatched
        while (tileCount > 0) {

            int randomTile1 = MemoryGame.random.nextInt(tileCount);
            int randomTile2 = MemoryGame.random.nextInt(tileCount);

            System.out.printf("Randomly generated numbers: %d %d        Step=%d Score=%d\r\n",
                    randomTile1 + 1, randomTile2 + 1, ++this.steps, this.score);

            String tile1 = (String)this.gameBoardSll1.getNodeAt(randomTile1).getData();
            String tile2 = (String)this.gameBoardSll2.getNodeAt(randomTile2).getData();

            if (tile1.equals(tile2)) {

                // Delete the tile from the game boards
                this.gameBoardSll1.delete(tile1);
                this.gameBoardSll2.delete(tile2);

                this.score += 20;

                tileCount -= 1;
            } else {
                this.score -= 1;
            }

            this.gameBoardSll1.print("SLL1");
            this.gameBoardSll2.print("SLL2");
        }

        System.out.printf("The game is over. Step=%d Score=%d.\r\nPlease enter your name: ", this.steps, this.score);

        String playerName = consoleScanner.nextLine();

        // Replace whitespace characters with the '-' char.
        playerName = playerName.replaceAll("\\s+", "-");

        this.highScoreTable.addScore(playerName, this.score);

        System.out.println();
        this.highScoreTable.print();

        this.highScoreTable.writeToFile(HIGH_SCORE_TABLE_FILE_PATH);
    }


    /**
     * Fills the gameBoards with the values from the this.animalSll.
     */
    private void fillGameBoards(int n) {

        SingleLinkedList tempAnimalSll = new SingleLinkedList(this.animalSLL);

        // Add random nodes from animalSll to gameBoardSll
        for (int i = 0; i < n; ++i) {

            Object randomObject = tempAnimalSll.getRandom().getData();

            this.gameBoardSll1.add(randomObject);

            // Delete the object to make all elements in gameBoardSll1 unique
            tempAnimalSll.delete(randomObject);
        }

        SingleLinkedList tempSll1 = new SingleLinkedList(this.gameBoardSll1);

        // Add random nodes from gameBoardSll2 to gameBoardSll1
        for (int i = 0; i < n; ++i) {

            Object randomObject = tempSll1.getRandom().getData();

            this.gameBoardSll2.add(randomObject);

            // Delete the object to make all elements in gameBoardSll2 unique
            tempSll1.delete(randomObject);
        }
    }


}
