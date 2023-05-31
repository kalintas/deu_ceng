import enigma.console.Console;
import enigma.core.Enigma;
import enigma.event.TextMouseEvent;
import enigma.event.TextMouseListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

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
    public MultiLinkedList table=new MultiLinkedList();

    private Player player = new Player(this);

    // Game state variables
    private boolean gameOver = false;
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
    public void initialize(int seed) {

        this.boardSeed = seed;

        Random random = new Random(seed);

        for (int i = 0; i < BOARD_HEIGHT; ++i) {
            for (int t = 0; t < BOARD_WIDTH; ++t) {

                int number = random.nextInt(4) + 1;

                this.board[i][t] = Character.forDigit(number, 10);
            }
        }
    }

    public void run() throws Exception {

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
                        this.player.placeChainPiece();
                        break;
                    case KeyEvent.VK_E:
                        this.gameOver = true;
                        break;
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

        this.setCursorPosition(33, 15);
        System.out.println("Error in chain. Press enter to leave");
        this.setCursorPosition(33, 16);
        System.out.println("- Game Over -");

        this.setCursorPosition(0, 0);
        // Wait user to press enter
        System.in.read();
    }

    public void setCursorPosition(int x, int y) {
        this.console.getTextWindow().setCursorPosition(x,y);
    }

    public void printSquare(char square, int x, int y) {

        this.console.getTextWindow().setCursorPosition(x, y);

        System.out.print(square);
    }

    public void printScreen() {

        for (int i = 0; i < BOARD_HEIGHT; ++i) {
            for (int t = 0; t < BOARD_WIDTH; ++t) {

                this.printSquare(this.board[i][t], t * 2, i * 2);
            }
        }

        this.player.print();

        this.setCursorPosition(33, 2);
        System.out.print("Score      :  " + this.player.getScore());
        this.setCursorPosition(33,3);
        System.out.print("-----------------------------------------------------");
        this.setCursorPosition(33,0);
        System.out.print("Board Seed :  " + this.boardSeed);
        this.setCursorPosition(33,1);
        System.out.print("Round      :  " + this.round);
        this.setCursorPosition(33,4);
        System.out.print("Table:");
    }

    /**
     * Chain rules:
     * There must be only one chain in each round. Chain with more than one part, broken chains, wrong positioned plus signs are prohibited.
     * Difference between neighbor squares in the chain must be 1 (+1 or -1).
     * The number of squares in the chain must be at least 4.
     * The score of the chain is n2 (n: The number of elements in the chain)
     */
    public void constructChain() {

        int chainSize = this.chain.size();

        // Check chain size rule.
        if (chainSize < 3) {
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
                this.gameOver = true;
                return;
            }

        }

        int newScore = player.getScore() + chainSize * chainSize;

        player.setScore(newScore);

        table.addChainRow(round+1);
        
        for (Node current = this.chain.getHead(); current != null; current = current.getLink()) {

            ChainPiece piece = (ChainPiece) current.getData();
            
            char firstSquare=this.board[piece.getFirstNumberY()][piece.getFirstNumberX()];
            char secondSquare=this.board[piece.getSecondNumberY()][piece.getSecondNumberX()];

            if(current==this.chain.getHead()) {
            	
            	ChainPiece piece2=(ChainPiece) current.getLink().getData();
            	
            if(piece.getX()<piece2.getX() && piece.getY()<piece2.getY()) {
            	
            	table.addChainColumn(round+1, firstSquare);
            	table.addChainColumn(round+1, secondSquare);
            }
            else if(piece.getX()>piece2.getX() && piece.getY()>piece2.getY()) {
            	
            	table.addChainColumn(round+1, secondSquare);
            	table.addChainColumn(round+1, firstSquare);
            }
            else if(piece.getX()==piece2.getX() && piece.getY()>piece2.getY()) {
            	
            	table.addChainColumn(round+1, secondSquare);
            	table.addChainColumn(round+1, firstSquare);
            }
            else if(piece.getX()==piece2.getX() && piece.getY()<piece2.getY()) {
            	
            	table.addChainColumn(round+1, firstSquare);
            	table.addChainColumn(round+1, secondSquare);
            	
            }
            else if(piece.getX()%2==0 && piece.getY()%2==1 && piece.getX()<piece2.getX() && piece.getY()>piece2.getY()) {
            	
            	table.addChainColumn(round+1, secondSquare);
            	table.addChainColumn(round+1, firstSquare);
            }
            else if(piece.getX()%2==0 && piece.getY()%2==1 && piece.getX()>piece2.getX() && piece.getY()<piece2.getY()) {
            	
            	table.addChainColumn(round+1, firstSquare);
            	table.addChainColumn(round+1, secondSquare);
            }
            
            else if(piece.getY()==piece2.getY() && piece.getX()<piece2.getX()) {
            	
            	table.addChainColumn(round+1, firstSquare);
            	table.addChainColumn(round+1, secondSquare);
            }
            
            else if(piece.getY()==piece2.getY() && piece.getX()>piece2.getX()) {
            	
            	table.addChainColumn(round+1, secondSquare);
            	table.addChainColumn(round+1, firstSquare);
            }
            
            else if(piece.getX()%2==1 && piece.getY()%2==0 && piece.getX()<piece2.getX() && piece.getY()>piece2.getY() ) {
            	
            	table.addChainColumn(round+1, firstSquare);
            	table.addChainColumn(round+1, secondSquare);
            }
            else if(piece.getX()%2==1 && piece.getY()%2==0 && piece.getX()>piece2.getX() && piece.getY()<piece2.getY() ) {
            	
            	table.addChainColumn(round+1, secondSquare);
            	table.addChainColumn(round+1, firstSquare);
            }
            }
            else {
            	
            	if(firstSquare==table.getLastNumber(round+1))
            	table.addChainColumn(round+1, secondSquare);
            	else if(secondSquare==table.getLastNumber(round+1)){
            		
            		table.addChainColumn(round+1, firstSquare);
            	}
            }

        }

        // Chain is correct. Remove chained numbers.
        for (Node current = this.chain.getHead(); current != null; current = current.getLink()) {

            ChainPiece piece = (ChainPiece) current.getData();

            this.board[piece.getFirstNumberY()][piece.getFirstNumberX()] = '.';
            this.board[piece.getSecondNumberY()][piece.getSecondNumberX()] = '.';

            this.printSquare(' ', piece.getX(), piece.getY());
        }

        if(round==0) {
        	
        	setCursorPosition(33,5);
        	table.display(1);
        }
        else {
        	
        	setCursorPosition(33,2*round+4);
        	System.out.println("+");
        	setCursorPosition(33,2*round+5);
        	table.display(round+1);
        }

        // Empty the chain SLL
        this.chain = new SingleLinkedList();

        this.round += 1;
        this.printScreen();
        
    }

}
