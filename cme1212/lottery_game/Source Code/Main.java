
public class Main {

    public static void main(String[] args) {

        // Create an instance of LotteryGame
        LotteryGame lotteryGame = new LotteryGame();

        while (true) {
            try {

                // Initialize the game
                lotteryGame.initGame();

                // Exit the game if the user doesn't want to play another game
                if (!lotteryGame.playGame()) {
                    break;
                }

            } catch (Exception exception) {

                // Print the exception and continue executing.
                System.out.println("Exception: " + exception.getMessage());
            }
        }
    }
}
