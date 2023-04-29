
public class Main {

    public static void main(String[] args) {

        try {
            MemoryGame memoryGame = new MemoryGame();

            memoryGame.playGame();
        } catch (Exception exception) {

            System.err.printf("Exception: (%s) at: %s\r\n", exception.getMessage(), exception.getStackTrace());
        }
    }
}
