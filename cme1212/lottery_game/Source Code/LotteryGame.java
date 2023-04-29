import java.util.Random;
import java.util.Scanner;

public class LotteryGame {

    // Create a static random number generator
    private static Random random = new Random();

    // Create an instance of HighScoreTable with the file path and maximum count of entries
    private HighScoreTable highScoreTable = new HighScoreTable("D:\\highscoretable.txt", 12);

    // Game variables
    private Queue bag1;
    private Queue bag2;

    private Stack cardStack1;
    private Stack cardStack2;

    private int playerScore1;
    private int playerScore2;

    /**
     * Initializes the game. Reads the high score table and sets variables to default values.
     */
    public void initGame() throws Exception {

        // Read file and sort players
        this.highScoreTable.readFile();
        this.highScoreTable.sortPlayers();

        this.bag1 = new Queue();
        this.bag2 = new Queue();

        // Fill the first bag with
        for (int i = 1; i <= 13; ++i) {

            this.bag1.enqueue(i);
        }

        this.playerScore1 = 0;
        this.playerScore2 = 0;
    }

    /**
     * Plays the game until game is over.
     * At the end of the game, asks the user if he/she wants to play again
     * @return Returns true if user wants to play again.
     */
    public boolean playGame() throws Exception {

        Scanner scanner = new Scanner(System.in);

        // Take the value of n from the user
        System.out.print("Please enter n: ");

        int n = Integer.parseInt(scanner.nextLine());

        System.out.println();

        // Create unique random card stacks
        this.cardStack1 = LotteryGame.createCardStack(n);
        this.cardStack2 = LotteryGame.createCardStack(n);

        // Print the round firstly
        this.printGame(n);

        // Game state variables
        boolean gameOver = false;
        boolean firstTournamentCompleted = false;

        int round = 1;

        // Game loop
        while (!gameOver) {

            // Select next ball from the this.bag1
            int nextBall = LotteryGame.random.nextInt(this.bag1.size());

            Queue tempQueue = new Queue();

            for (int i = 0; i < nextBall; ++i) {

                tempQueue.enqueue(this.bag1.dequeue());
            }

            // Get the selected ball
            int selectedBall = (int)this.bag1.dequeue();

            // Put the balls except the selected one
            while (!this.bag1.isEmpty()) {

                tempQueue.enqueue(this.bag1.dequeue());
            }

            // this.bag1.size() is decreased 1 after these operations
            this.bag1 = tempQueue;

            // Add selected ball to the this.bag2
            this.bag2.enqueue(selectedBall);


            this.playerScore1 += LotteryGame.deletePlayerCard(selectedBall, this.cardStack1);
            this.playerScore2 += LotteryGame.deletePlayerCard(selectedBall, this.cardStack2);

            System.out.printf("%d. selected value:", round);
            LotteryGame.printCard(selectedBall);
            System.out.println();

            this.printGame(n);

            round += 1;

            // Check if the tournament is complete
            if (!firstTournamentCompleted) {

                if (n - this.cardStack1.size() == 4) {

                    this.playerScore1 += 30;
                    firstTournamentCompleted = true;
                }

                if (n - this.cardStack2.size() == 4) {

                    this.playerScore2 += 30;
                    firstTournamentCompleted = true;
                }

                if (firstTournamentCompleted) {
                    System.out.println("First tournament is completed");
                    this.printGame(n);
                }
            }

            // Check if the game is over
            if (this.cardStack1.isEmpty()) {
                this.playerScore1 += 50;
                gameOver = true;
            }

            if (this.cardStack2.isEmpty()) {
                this.playerScore2 += 50;
                gameOver = true;
            }
        }

        System.out.println("Game over!");

        int maxScore;

        boolean tie = false;

        if (this.playerScore1 > this.playerScore2) {

            maxScore = this.playerScore1;
            System.out.print("Winner: Player1");

        } else if (this.playerScore2 > this.playerScore1) {

            maxScore = this.playerScore2;

            System.out.print("Winner: Player2");
        } else {
            tie = true;
            maxScore = this.playerScore1;
            System.out.print("Tie");
        }

        System.out.printf(" with %d points\r\n", maxScore);

        if (!tie) {

            System.out.print("What is your name: ");
            String name = scanner.nextLine();

            if (!this.highScoreTable.changeExistingUser(name, maxScore)) {

                this.highScoreTable.addPlayer(name, maxScore);
            }

            this.highScoreTable.print();

            this.highScoreTable.writeFile();
        }

        // Ask user if he/she wants to play again
        boolean playAgain;

        while (true) {

            System.out.print("Play again? (Y/N): ");

            char input = scanner.next(".").charAt(0);

            input = Character.toUpperCase(input);

            if (input == 'Y') {
                playAgain = true;
                break;
            } else if (input == 'N') {
                playAgain = false;
                break;
            }
        }

        return playAgain;
    }

    /**
     * Create a card stack filled with unique random values in the small to big order.
     */
    private static Stack createCardStack(int n) throws Exception {

        if (n < 7 || n > 10) {
            throw new Exception("Invalid value for n. Valid range: [7, 10]");
        }

        Stack stack = new Stack();

        while (stack.size() != n) {

            // Create a random card
            int card = LotteryGame.random.nextInt(13) + 1;

            // Try to add card to the stack
            stack = addUniqueCardToStack(card, stack);
        }

        return stack;
    }

    /**
     * Add a unique card to the parameter stack. If the parameter newCard is not unique,
     * function will return the original stack.
     * @param newCard Card to be added to the parameter stack.
     * @param stack Original stack that will be pushed on to.
     * @return If the newCard in not unique equal to the parameter stack. Otherwise, it's a stack containing the
     * parameter newCard in the small to big order.
     */
    private static Stack addUniqueCardToStack(int newCard, Stack stack) throws Exception {

        // Resulting stack. This stack contains items small to big
        Stack resultStack = new Stack(stack);

        // Temporary stack to store items. This stack contains items big to small
        Stack tempStack = new Stack();
        boolean addedCard = false;

        while (!resultStack.isEmpty() && !addedCard) {

            int card = (int)resultStack.pop();

            if (newCard == card) {

                // Card is not unique. Return the original stack
                return stack;
            } else if (newCard < card) {

                tempStack.push(newCard);
                addedCard = true;
            }

            tempStack.push(card);
        }

        // Either stack is empty or newCard is the smallest element
        if (!addedCard) {
            // So add the newCard on top of the tempStack
            tempStack.push(newCard);
        }

        // Re-push the elements to the resultStack
        while (!tempStack.isEmpty()) {
            resultStack.push(tempStack.pop());
        }

        return resultStack;
    }

    /**
     * Deletes the parameter ball from the parameter stack.
     * @param ball The value to be searched and removed from the stack
     * @param stack Stack to be searched.
     * @return 10 if the parameter ball exists in the stack,
     *         -5 if the parameter ball does not exist in the stack.
     */
    private static int deletePlayerCard(int ball, Stack stack) throws Exception {

        Stack tempStack = new Stack();

        boolean cardExists = false;

        while (!stack.isEmpty()) {

            int card = (int)stack.pop();

            if (card == ball) {

                cardExists = true;
                break;
            }

            tempStack.push(card);
        }

        while (!tempStack.isEmpty()) {

            stack.push(tempStack.pop());
        }

        if (cardExists) {
            return 10;
        }

        return -5;
    }

    /**
     * Prints the parameter card to the console.
     */
    private static void printCard(int card) {

        switch (card)  {
            case 1:
                System.out.print("  A");
                return;
            case 11:
                System.out.print("  J");
                return;
            case 12:
                System.out.print("  Q");
                return;
            case 13:
                System.out.print("  K");
                return;
            default:
                System.out.printf(" %2d", card);
        }
    }

    /**
     * Prints parameter stack to console
     */
    private static void printStack(Stack stack, int n) throws Exception {

        Stack tempStack = new Stack(stack);

        while (!tempStack.isEmpty()) {

            int card = (int)tempStack.pop();

            LotteryGame.printCard(card);
        }

        for (int i = stack.size(); i < n; ++i) {

            System.out.print("   ");
        }
    }

    /**
     * Prints parameter queue to console
     */
    private static void printQueue(Queue queue) throws Exception {

        Queue tempQueue = new Queue(queue);

        while (!tempQueue.isEmpty()) {

            int card = (int)tempQueue.dequeue();

            LotteryGame.printCard(card);
        }
    }

    /**
     * Print the current status of the game. Including cardStacks, playerScores and bags.
     */
    private void printGame(int n) throws Exception {

        System.out.print("Player1:");

        LotteryGame.printStack(this.cardStack1, n);

        System.out.printf("   Score: %3d    Bag1:", this.playerScore1);

        LotteryGame.printQueue(this.bag1);

        System.out.print("\r\nPlayer2:");

        LotteryGame.printStack(this.cardStack2, n);

        System.out.printf("   Score: %3d    Bag2:", this.playerScore2);

        LotteryGame.printQueue(this.bag2);

        System.out.println();
    }

}
