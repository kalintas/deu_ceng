import enigma.console.Console;
import enigma.core.Enigma;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class ChainGame {

    private Console console;

    // Key event variables
    private boolean isKeyPressed = false;
    private int keyCode = 0;

    private static final int BOARD_WIDTH = 16;
    private static final int BOARD_HEIGHT = 10;

    // Game board variable
    public char[][] board = new char[BOARD_HEIGHT][BOARD_WIDTH];

    public SingleLinkedList chain = new SingleLinkedList();
    public MultiLinkedList table = new MultiLinkedList();

    HighScoreTable highScoreTable = new HighScoreTable();

    private Player player = new Player(this);

    // Game state variables
    private boolean gameOver = false;
    private String chainErrorReason = new String();

    private int boardSeed = 0;
    private int round = 0;

    /**
     * Default constructor.
     * Creates enigma console and setups event listeners.
     */
    public ChainGame() {

        console = Enigma.getConsole("CHAIN", 100, 30, 20);

        KeyListener keyListener = new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {

                isKeyPressed = true;
                keyCode = e.getKeyCode();
            }

            public void keyReleased(KeyEvent e) {
            }
        };
        console.getTextWindow().addKeyListener(keyListener);
    }

    /**
     * Initializes the game. Fills the game board with numbers.
     */
    public void initialize() {

        Random random = new Random(this.boardSeed);

        for (int i = 0; i < BOARD_HEIGHT; ++i) {
            for (int t = 0; t < BOARD_WIDTH; ++t) {

                int number = random.nextInt(4) + 1;

                this.board[i][t] = Character.forDigit(number, 10);
            }
        }
    }

    /**
     * Runs the chain game.
     *
     * @return true if the game should be restarted.
     */
    public boolean run() throws Exception {

        this.printScreen();

        while (!this.gameOver) {

            if (this.isKeyPressed) {

                switch (this.keyCode) {
                    case KeyEvent.VK_LEFT:
                        this.player.move(-1, 0);
                        break;
                    case KeyEvent.VK_RIGHT:
                        this.player.move(1, 0);
                        break;
                    case KeyEvent.VK_DOWN:
                        this.player.move(0, 1);
                        break;
                    case KeyEvent.VK_UP:
                        this.player.move(0, -1);
                        break;
                    case KeyEvent.VK_SPACE:

                        if (!this.player.removeChainPiece()) {

                            this.player.placeChainPiece();
                        }
                        break;
                    case KeyEvent.VK_E:
                        this.gameOver = true;
                        break;
                    case KeyEvent.VK_R:
                        return true;
                    case KeyEvent.VK_ENTER:
                        this.constructChain();
                        break;
                    default:
                        break;
                }

                this.isKeyPressed = false;
            }


            Thread.sleep(20);
        }

        this.gameOver();
        return false;
    }

    /**
     * Renders the game menu and gets the board seed from the user.
     */
    public void gameMenu() {

        this.clearScreen();

        String[] gameNameLines = {
                "   _____ _           _       \n",
                "  / ____| |         (_)      \n",
                " | |    | |__   __ _ _ _ __  \n",
                " | |    | '_ \\ / _` | | '_ \\ \n",
                " | |____| | | | (_| | | | | |\n",
                "  \\_____|_| |_|\\__,_|_|_| |_|\n",
        };

        for (int i = 0; i < gameNameLines.length; ++i) {

            this.setCursorPosition(30, i + 10);
            System.out.println(gameNameLines[i]);
        }

        while (true) {


            this.setCursorPosition(35, 17);
            System.out.print("Board Seed:                   ");
            this.setCursorPosition(46, 17);

            Scanner scanner = new Scanner(System.in);

            try {
                this.boardSeed = scanner.nextInt();
            } catch (Exception exception) {
                continue;
            }

            break;
        }
        this.clearScreen();
    }

    private void gameOver() throws IOException {

        if (!this.chainErrorReason.isEmpty()) {

            this.setCursorPosition(33, 14);
            System.out.println("- Game Over -");
            this.setCursorPosition(33, 15);
            System.out.println("Error in chain");
            this.setCursorPosition(33, 16);
            System.out.println(this.chainErrorReason);
        }

        this.setCursorPosition(33, 17);
        System.out.print("Name : ");
        String name = new Scanner(System.in).next();
        highScoreTable.write(name, player.getScore());
        clearScreen();
        highScoreTable.display();
    }

    private void clearScreen() {

        for (int i = 0; i < console.getTextWindow().getColumns(); ++i) {
            for (int j = 0; j < console.getTextWindow().getRows(); ++j) {
                console.getTextWindow().output(i, j, ' ');
            }
        }
    }

    private void setCursorPosition(int x, int y) {
        this.console.getTextWindow().setCursorPosition(x, y);
    }

    public void printSquare(char square, int x, int y) {

        this.console.getTextWindow().setCursorPosition(x, y);

        System.out.print(square);
    }

    private void printScreen() {

        for (int i = 0; i < BOARD_HEIGHT; ++i) {
            for (int t = 0; t < BOARD_WIDTH; ++t) {

                this.printSquare(this.board[i][t], t * 2, i * 2);
            }
        }

        this.player.print();

        this.setCursorPosition(33, 2);
        System.out.print("Score      :  " + this.player.getScore());
        this.setCursorPosition(33, 3);
        System.out.print("-----------------------------------------------------");
        this.setCursorPosition(33, 0);
        System.out.print("Board Seed :  " + this.boardSeed);
        this.setCursorPosition(33, 1);
        System.out.print("Round      :  " + this.round);
        this.setCursorPosition(33, 4);
        System.out.print("Table:");
    }

    /**
     * Chain rules:
     * There must be only one chain in each round. Chain with more than one part, broken chains, wrong positioned plus signs are prohibited.
     * Difference between neighbor squares in the chain must be 1 (+1 or -1).
     * The number of squares in the chain must be at least 4.
     * The score of the chain is n2 (n: The number of elements in the chain)
     */
    private void constructChain() {

        int chainSize = this.chain.size();

        // Don't end the game if there is no chain.
        if (chainSize == 0) {
            return;
        }

        // Check chain size rule.
        if (chainSize < 3) {
            this.chainErrorReason = "The number of squares must be at least 4";
            this.gameOver = true;
            return;
        }

        // Check neighbor difference rule
        for (Node current = this.chain.getHead(); current != null; current = current.getLink()) {

            ChainPiece piece = (ChainPiece) current.getData();

            char firstSquare = this.board[piece.getFirstNumberY()][piece.getFirstNumberX()];
            char secondSquare = this.board[piece.getSecondNumberY()][piece.getSecondNumberX()];

            int difference = Character.getNumericValue(firstSquare) - Character.getNumericValue(secondSquare);

            if (difference != 1 && difference != -1) {

                this.chainErrorReason = "Difference between neighbor squares must be 1";
                this.gameOver = true;
                return;
            }

        }

        // Calculate score
        int newScore = 0;

        if (this.player.getChainStartX() == this.player.getChainEndX() && this.player.getChainStartY() == this.player.getChainEndY()) {

            newScore = player.getScore() + (chainSize) * (chainSize);
        } else {

            newScore = player.getScore() + (chainSize + 1) * (chainSize + 1);
        }

        player.setScore(newScore);

        this.addToTable();

        // Chain is correct. Remove chained numbers.
        for (Node current = this.chain.getHead(); current != null; current = current.getLink()) {

            ChainPiece piece = (ChainPiece) current.getData();

            this.board[piece.getFirstNumberY()][piece.getFirstNumberX()] = '.';
            this.board[piece.getSecondNumberY()][piece.getSecondNumberX()] = '.';

            this.printSquare(' ', piece.getX(), piece.getY());
        }

        // Empty the chain SLL
        this.chain = new SingleLinkedList();

        this.round += 1;
        this.printScreen();
    }

    private void addToTable() {

        table.addChainRow(round + 1);

        for (Node current = this.chain.getHead(); current != null; current = current.getLink()) {

            ChainPiece piece = (ChainPiece) current.getData();

            char firstSquare = this.board[piece.getFirstNumberY()][piece.getFirstNumberX()];
            char secondSquare = this.board[piece.getSecondNumberY()][piece.getSecondNumberX()];

            if (current == this.chain.getHead()) {

                ChainPiece piece2 = (ChainPiece) current.getLink().getData();

                if (piece.getX() < piece2.getX() && piece.getY() < piece2.getY()) {

                    table.addChainColumn(round + 1, firstSquare);
                    table.addChainColumn(round + 1, secondSquare);
                } else if (piece.getX() > piece2.getX() && piece.getY() > piece2.getY()) {

                    table.addChainColumn(round + 1, secondSquare);
                    table.addChainColumn(round + 1, firstSquare);
                } else if (piece.getX() == piece2.getX() && piece.getY() > piece2.getY()) {

                    table.addChainColumn(round + 1, secondSquare);
                    table.addChainColumn(round + 1, firstSquare);
                } else if (piece.getX() == piece2.getX() && piece.getY() < piece2.getY()) {

                    table.addChainColumn(round + 1, firstSquare);
                    table.addChainColumn(round + 1, secondSquare);

                } else if (piece.getX() % 2 == 0 && piece.getY() % 2 == 1 && piece.getX() < piece2.getX() && piece.getY() > piece2.getY()) {

                    table.addChainColumn(round + 1, secondSquare);
                    table.addChainColumn(round + 1, firstSquare);
                } else if (piece.getX() % 2 == 0 && piece.getY() % 2 == 1 && piece.getX() > piece2.getX() && piece.getY() < piece2.getY()) {

                    table.addChainColumn(round + 1, firstSquare);
                    table.addChainColumn(round + 1, secondSquare);
                } else if (piece.getY() == piece2.getY() && piece.getX() < piece2.getX()) {

                    table.addChainColumn(round + 1, firstSquare);
                    table.addChainColumn(round + 1, secondSquare);
                } else if (piece.getY() == piece2.getY() && piece.getX() > piece2.getX()) {

                    table.addChainColumn(round + 1, secondSquare);
                    table.addChainColumn(round + 1, firstSquare);
                } else if (piece.getX() % 2 == 1 && piece.getY() % 2 == 0 && piece.getX() < piece2.getX() && piece.getY() > piece2.getY()) {

                    table.addChainColumn(round + 1, firstSquare);
                    table.addChainColumn(round + 1, secondSquare);
                } else if (piece.getX() % 2 == 1 && piece.getY() % 2 == 0 && piece.getX() > piece2.getX() && piece.getY() < piece2.getY()) {

                    table.addChainColumn(round + 1, secondSquare);
                    table.addChainColumn(round + 1, firstSquare);
                }
            } else {

                if (firstSquare == table.getLastNumber(round + 1) && !(piece.getSecondNumberX() == this.player.getChainStartX() && piece.getSecondNumberY() == this.player.getChainStartY()))
                    table.addChainColumn(round + 1, secondSquare);
                else if (secondSquare == table.getLastNumber(round + 1) && !(piece.getFirstNumberX() == this.player.getChainStartX() && piece.getFirstNumberY() == this.player.getChainStartY())) {

                    table.addChainColumn(round + 1, firstSquare);
                }
            }

        }

        if (round == 0) {

            setCursorPosition(33, 5);
            table.display(1);
        } else {

            setCursorPosition(33, 2 * round + 4);
            System.out.println("+");
            setCursorPosition(33, 2 * round + 5);
            table.display(round + 1);
        }
    }

}
