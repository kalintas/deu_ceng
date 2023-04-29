import enigma.console.Console;
import enigma.console.TextAttributes;
import enigma.core.Enigma;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.util.Scanner;

public class Gravity implements KeyListener {

    // Game constants

    // Width and height of the game field.
    private static final int WIDTH = 55;
    private static final int HEIGHT = 25;

    // Width and height of the console window.
    private static final int WINDOW_WIDTH = 90;
    private static final int WINDOW_HEIGHT = 26;

    // Game speed is 10 frames per second.
    private static final int FPS = 10;

    private static final int BACKPACK_SIZE = 8;

    private static final Random random = new Random();

    private static class SquareType {
        public static final char PLAYER = 'P';
        public static final char ROBOT = 'X';
        public static final char EARTH = ':';
        public static final char BOULDER = 'O';
        public static final char WALL = '#';
        public static final char EMPTY = ' ';
    }

    public char[][] map = new char[HEIGHT][WIDTH];

    private boolean gameOver = false;
    private String deathReason = new String();
    private boolean keyPressed = false;
    private int keyCode = 0;

    private InputQueue inputQueue;

    private Stack backpackStack;

    private Robot[] robots = new Robot[1000];
    private int robotCount = 0;

    private Boulder[] boulders = new Boulder[1000];
    private int boulderCount = 0;

    private Player player;

    public Console console;

    /**
     * Default constructor for Gravity class
     */
    public Gravity() throws Exception {

        this.player = new Player();
        this.inputQueue = new InputQueue();
        this.backpackStack = new Stack(BACKPACK_SIZE);

        // Create the enigma console
        this.console = Enigma.getConsole("Gravity", Gravity.WINDOW_WIDTH + 1, Gravity.WINDOW_HEIGHT + 1, 20);

        this.console.getTextWindow().addKeyListener(this);
    }

    /**
     * Renders main menu for the gravity game.
     * Waits until user pressed enter.
     */
    public void mainMenu() throws InterruptedException {

        this.printStars(Gravity.WINDOW_WIDTH, Gravity.WINDOW_HEIGHT);

        this.setConsoleColor(Color.CYAN, Color.BLACK);

        String[] gravityText = new String[]{
                "                        _ _         ",
                "                       (_) |        ",
                "   __ _ _ __ __ ___   ___| |_ _   _ ",
                "  / _` | '__/ _` \\ \\ / / | __| | | |",
                " | (_| | | | (_| |\\ V /| | |_| |_| |",
                "  \\__, |_|  \\__,_| \\_/ |_|\\__|\\__, |",
                "   __/ |                       __/ |",
                "  |___/                       |___/ "};


        for (int i = 0; i < gravityText.length; ++i) {
            this.setCursorPos(18, 7 + i);
            System.out.print(gravityText[i]);
        }

        this.setCursorPos(28, 7 + gravityText.length);
        System.out.println("Please press enter:");

        while (!this.keyPressed || this.keyCode != KeyEvent.VK_ENTER) {

            Thread.sleep(20);
        }
    }

    /**
     * Initialize the game with these steps:
     * 1.	Walls are placed.
     * 2.	All empty squares are converted into earth squares.
     * 3.	Random 180 earth squares are converted into boulders.
     * 4.	Random 30 earth squares are converted into treasures (Random 1, 2 or 3 with equal probability).
     * 5.	Random 200 earth squares are converted into empty squares.
     * 6.	Random 7 earth squares are converted into robots.
     * 7.	Player P is placed on a random earth square.
     */
    public void initGame(String mapFilePath) throws Exception {

        String[] lines = FileReader.readFile(mapFilePath);

        // Check if the text file is correct
        if (lines.length != HEIGHT) {
            throw new Exception("Text file format is incorrect");
        }

        // 1. Walls are placed.
        // Fill this.map with the text file content
        for (int i = 0; i < Gravity.HEIGHT; ++i) {
            for (int t = 0; t < Gravity.WIDTH; ++t) {

                char square = lines[i].charAt(t);

                // 2. All empty squares are converted into earth squares.
                // Convert empty squares to earth squares
                if (square == SquareType.EMPTY) {
                    square = SquareType.EARTH;
                }

                this.setSquare(t, i, square);
            }
        }

        // 3. Random 180 earth squares are converted into boulders.
        for (int i = 0; i < 180; ++i) {
            this.setRandomEarth(SquareType.BOULDER);
        }

        // 4. Random 30 earth squares are converted into treasures (Random 1, 2 or 3 with equal probability).
        for (int i = 0; i < 30; ++i) {

            int treasure = Gravity.random.nextInt(3) + 1;

            this.setRandomEarth(Character.forDigit(treasure, 10));
        }

        // 5. Random 200 earth squares are converted into empty squares.
        for (int i = 0; i < 200; ++i) {
            this.setRandomEarth(SquareType.EMPTY);
        }

        // 6. Random 7 earth squares are converted into robots.
        for (int i = 0; i < 7; ++i) {
            this.setRandomEarth(SquareType.ROBOT);
        }

        // 7. Player P is placed on a random earth square.
        this.setRandomEarth(SquareType.PLAYER);
    }

    /**
     * Run the game
     */
    public boolean runGame() throws Exception {

        this.printPlayerStatus();

        // Duration of a frame based on FPS constant.
        long frameTime = 1000 / Gravity.FPS;

        // Timer for the input queue
        long inputQueueTimer = 0;
        long timer = 0;
        long robotTimer = 0;
        long secondsPassed = 0;

        // Game loop
        while (!this.gameOver) {

            // Frame start time
            long frameStart = System.currentTimeMillis();

            // Change the map with the value from the input queue
            if (inputQueueTimer > 3000) {

                char nextSquare = this.inputQueue.next();
                char[] searchSquares;

                switch (nextSquare) {
                    case '1':
                    case '2':
                    case '3':
                    case SquareType.ROBOT:
                    case SquareType.BOULDER:

                        searchSquares = new char[]{SquareType.EARTH, SquareType.EMPTY};
                        break;
                    case SquareType.EARTH:

                        searchSquares = new char[]{SquareType.EMPTY};
                        break;
                    case 'e':

                        searchSquares = new char[]{SquareType.EARTH};
                        nextSquare = SquareType.EMPTY; // Dont place 'e' char onto screen
                        break;
                    default:
                        throw new Exception("Faulty input queue");
                }

                // Before replacing the square, check if the nextSquare is a boulder
                if (nextSquare == SquareType.BOULDER) {

                    // Set a random boulder square to earth square
                    this.replaceRandomSquare(new char[]{SquareType.BOULDER}, SquareType.EARTH);
                }

                this.replaceRandomSquare(searchSquares, nextSquare);

                // Update input queue
                this.setConsoleColor(Color.WHITE, Color.BLACK);
                this.setCursorPos(WIDTH + 1, 2);
                this.inputQueue.print();

                inputQueueTimer = 0;
            }

            // Handle player inputs
            if (this.keyPressed) {

                // Handle player inputs
                switch (this.keyCode) {
                    case KeyEvent.VK_RIGHT:
                        this.movePlayer(1, 0);
                        break;
                    case KeyEvent.VK_LEFT:
                        this.movePlayer(-1, 0);
                        break;
                    case KeyEvent.VK_DOWN:
                        this.movePlayer(0, 1);
                        break;
                    case KeyEvent.VK_UP:
                        this.movePlayer(0, -1);
                        break;
                    case KeyEvent.VK_SPACE:
                        this.teleportPlayer();
                        break;
                    default:
                        break;
                }

                this.keyPressed = false;
            }

            // 1 second
            if (timer > 1000) {
                // Update timer
                this.setConsoleColor(Color.WHITE, Color.BLACK);
                this.setCursorPos(Gravity.WIDTH + 10, 20);
                System.out.print(++secondsPassed);

                timer = 0;
            }

            if (robotTimer > 200) {

                // Move robots
                for (int i = 0; i < this.robotCount; i++) {

                    Robot newRobot = this.moveRobot(this.robots[i]);

                    // Robot died
                    if (newRobot == null) {

                        // Overwrite this robot. This way there is no null elements in the this.robots
                        for (int t = i; t < this.robotCount - 1; ++t) {

                            this.robots[t] = this.robots[t + 1];

                        }

                        this.robotCount -= 1;
                        this.player.score += 900;

                        // Update the score value
                        this.setConsoleColor(Color.WHITE, Color.BLACK);
                        this.setCursorPos(WIDTH + 4, 18);
                        System.out.print("Score:    ");
                        this.setCursorPos(WIDTH + 11, 18);
                        System.out.print(this.player.score);
                    } else {

                        this.robots[i] = newRobot;
                    }
                }

                robotTimer = 0;
            }

            // Fall boulders
            for (int i = 0; i < this.boulderCount; ++i) {

                this.fallBoulder(this.boulders[i]);
            }

            // Frame end time
            long frameEnd = System.currentTimeMillis();

            // Elapsed time in milliseconds
            long elapsedTime = frameEnd - frameStart;

            // Sleep the rest of the frameTime
            long sleepTime = frameTime - elapsedTime;

            if (sleepTime > 0) {
                Thread.sleep(sleepTime);
            }

            inputQueueTimer += frameTime;
            timer += frameTime;
            robotTimer += frameTime;
        }

        // Print game over screen

        this.printStars(Gravity.WIDTH, Gravity.HEIGHT);

        this.setConsoleColor(Color.CYAN, Color.BLACK);
        this.setCursorPos(7, 8);

        String gameOverText =
                "\n" +
                        "   __ _  __ _ _ __ ___   ___    _____   _____ _ __  \n" +
                        "  / _` |/ _` | '_ ` _ \\ / _ \\  / _ \\ \\ / / _ \\ '__| \n" +
                        " | (_| | (_| | | | | | |  __/ | (_) \\ V /  __/ |    \n" +
                        "  \\__, |\\__,_|_| |_| |_|\\___|  \\___/ \\_/ \\___|_|    \n" +
                        "   __/ |                                            \n" +
                        "  |___/                                             \n" +
                        "\n";

        System.out.println(gameOverText);

        this.setCursorPos((this.WIDTH - this.deathReason.length()) / 2, 14);
        System.out.print(this.deathReason);

        // Wait 2 seconds
        Thread.sleep(2000);


        // Clear the screen
        this.printStars(this.WIDTH, this.HEIGHT);

        this.setCursorPos(0, 0);
        this.setConsoleColor(Color.WHITE, Color.BLACK);
        System.out.println("Play Again (Y/N)?");

        Scanner scanner = new Scanner(System.in);

        String input = new String();

        do {

            for (int i = 0; i < input.length(); ++i) {

                this.setCursorPos(i, 1);
                System.out.print(' ');
            }
            this.setCursorPos(0, 1);

            input = scanner.nextLine().toUpperCase();

        } while (!input.equals("Y") && !input.equals("N"));

        return input.equals("Y");
    }

    // Key event callbacks
    public void keyTyped(KeyEvent event) {

    }

    public void keyPressed(KeyEvent event) {

        this.keyPressed = true;
        this.keyCode = event.getKeyCode();
    }

    public void keyReleased(KeyEvent event) {

    }


    /**
     * Makes the boulder fall at the given location. Does nothing if the square at the
     * location is not a boulder
     */
    private void fallBoulder(Boulder boulder) {

        Boulder newBoulder;

        switch (this.map[boulder.y + 1][boulder.x]) {

            case SquareType.EMPTY:

                newBoulder = new Boulder(boulder.x, boulder.y + 1);

                break;
            case SquareType.BOULDER:

                int[] sidesX = new int[2];
                int sidesCount = 0;

                if (this.map[boulder.y + 1][boulder.x - 1] == SquareType.EMPTY) {

                    sidesX[sidesCount] = boulder.x - 1;
                    sidesCount += 1;
                }

                if (this.map[boulder.y + 1][boulder.x + 1] == SquareType.EMPTY) {

                    sidesX[sidesCount] = boulder.x + 1;
                    sidesCount += 1;
                }

                if (sidesCount == 0) {
                    // Sides of the boulder are full. Cannot move
                    return;
                }

                newBoulder = new Boulder(sidesX[Gravity.random.nextInt(sidesCount)], boulder.y + 1);

                break;
            default:
                return;
        }

        this.setSquare(boulder.x, boulder.y, SquareType.EMPTY);
        this.setSquare(newBoulder.x, newBoulder.y, SquareType.BOULDER);

        boulder.x = newBoulder.x;
        boulder.y = newBoulder.y;
    }

    /**
     * Moves robot to a random direction with no diagonal movement.
     *
     * @return robot with the new random position.
     * Returns null if robot is dead.
     */
    private Robot moveRobot(Robot robot) {

        Robot[] newRobots = new Robot[4];
        int newRobotCount = 0;

        char square = this.map[robot.y + robot.moveY][robot.x + robot.moveX];

        if (square == SquareType.EMPTY || square == SquareType.EARTH || square == SquareType.PLAYER) {

            newRobots[newRobotCount] = robot.move(robot.moveX, robot.moveY);
            newRobotCount += 1;
        } else {

            for (int y = -1; y <= 1; ++y) {
                for (int x = -1; x <= 1; ++x) {

                    if (Math.abs(y) != Math.abs(x)) {

                        // Possible (x, y) values:
                        // (0, -1), (-1, 0), (1, 0), (0, 1)

                        square = this.map[robot.y + y][robot.x + x];

                        if (square == SquareType.EMPTY || square == SquareType.EARTH || square == SquareType.PLAYER) {

                            newRobots[newRobotCount] = robot.move(x, y);
                            newRobotCount += 1;
                        }
                    }

                }
            }
        }

        // Robot cannot move anywhere
        if (newRobotCount == 0) {
            return robot;
        }

        // Set old square empty
        setSquare(robot.x, robot.y, SquareType.EMPTY);

        int randomRobot = Gravity.random.nextInt(newRobotCount);

        Robot oldRobot = robot;
        robot = newRobots[randomRobot];

        if (this.map[robot.y][robot.x] == SquareType.PLAYER) {
            this.gameOver = true;
            this.deathReason = "You were killed by a enemy robot.";
        }

        // Robot moved down
        if (oldRobot.x == robot.x && oldRobot.y + 1 == robot.y) {

            // There is a boulder on top of the robot in the old position
            if (this.map[oldRobot.y - 1][oldRobot.x] == SquareType.BOULDER) {

                // Kill the robot by returning null
                return null;
            }
        }

        // Set new square to robot
        setSquare(robot.x, robot.y, SquareType.ROBOT);

        return robot;
    }

    /**
     * Teleport player to random earth square
     */
    private void teleportPlayer() {

        if (this.player.teleportRight <= 0) {
            return;
        }
        this.player.teleportRight -= 1;

        int oldPlayerX = this.player.x;
        int oldPlayerY = this.player.y;

        // Set random earth square to player
        this.setRandomEarth(SquareType.PLAYER);

        // Update teleport count
        this.setConsoleColor(Color.WHITE, Color.BLACK);
        this.setCursorPos(WIDTH + 4, 16);
        System.out.print("Teleport:");
        this.setCursorPos(WIDTH + 14, 16);
        System.out.print(this.player.teleportRight);

        // Set previous square empty
        this.setSquare(oldPlayerX, oldPlayerY, SquareType.EMPTY);

        // Set new square player
        this.setSquare(this.player.x, this.player.y, SquareType.PLAYER);
    }

    /**
     * Tries to move the player across the map.
     *
     * @param moveX x dimension of the movement
     * @param moveY y dimension of the movement
     */
    private void movePlayer(int moveX, int moveY) throws Exception {

        int newPlayerX = this.player.x + moveX;
        int newPlayerY = this.player.y + moveY;

        char square = this.map[newPlayerY][newPlayerX];

        switch (square) {
            case SquareType.EMPTY:
            case SquareType.EARTH:
                break;
            case '1':
            case '2':
            case '3':
                this.addToBackpack(square);
                break;
            case SquareType.BOULDER:

                if (moveY == 0 && this.map[this.player.y][newPlayerX + moveX] == SquareType.EMPTY) {

                    for (int i = 0; i < this.boulderCount; ++i) {

                        if (this.boulders[i].x == newPlayerX && this.boulders[i].y == newPlayerY) {
                            this.boulders[i].x += moveX;
                            break;
                        }
                    }

                    this.setSquare(newPlayerX + moveX, this.player.y, SquareType.BOULDER);
                    break;

                } else {
                    return;
                }
            default:
                return;
        }

        if (moveX == 0 && moveY == 1 && this.map[this.player.y - 1][this.player.x] == SquareType.BOULDER) {

            this.gameOver = true;
            this.deathReason = "You were killed by a falling boulder.";
        }

        // Set previous square empty
        this.setSquare(this.player.x, this.player.y, SquareType.EMPTY);

        // Set new square player
        this.setSquare(newPlayerX, newPlayerY, SquareType.PLAYER);

        this.player.x = newPlayerX;
        this.player.y = newPlayerY;
    }

    private void addToBackpack(char newNumber) throws Exception {

        if (this.backpackStack.isEmpty()) {
            this.backpackStack.push(newNumber);
        } else if (this.backpackStack.peek().equals(newNumber)) {

            switch (newNumber) {
                case '1':
                    this.player.score += 10;
                    break;
                case '2':
                    this.player.score += 40;
                    break;
                case '3':
                    this.player.score += 90;
                    this.player.teleportRight += 1;
                    break;
            }

            this.backpackStack.pop();
        } else if (this.backpackStack.isFull()) {

            this.backpackStack.pop();
            this.addToBackpack(newNumber);
            return;

        } else {
            this.backpackStack.push(newNumber);
        }

        setConsoleColor(Color.WHITE, Color.BLACK);

        for (int i = 0; i < BACKPACK_SIZE; i++) {
            this.setCursorPos(WIDTH + 8, 12 - i);
            System.out.print(" ");
        }

        //  Update teleport right
        this.setCursorPos(WIDTH + 14, 16);
        System.out.print("               ");
        this.setCursorPos(WIDTH + 14, 16);
        System.out.print(this.player.teleportRight);

        Stack tempStack = new Stack(backpackStack.size());
        int size = backpackStack.size();

        setConsoleColor(Color.RED, Color.BLACK);

        for (int i = 0; i < size; i++) {
            tempStack.push(backpackStack.pop());
        }

        for (int i = 0; i < size; i++) {
            this.setCursorPos(WIDTH + 8, 12 - i);
            System.out.print(tempStack.peek());
            backpackStack.push(tempStack.pop());
        }

        //  Updating score
        setConsoleColor(Color.WHITE, Color.BLACK);
        this.setCursorPos(WIDTH + 11, 18);
        System.out.print(this.player.score);

    }

    private void setCursorPos(int x, int y) {

        this.console.getTextWindow().setCursorPosition(x, y);
    }

    private void setConsoleColor(Color foreground, Color background) {

        this.console.setTextAttributes(new TextAttributes(foreground, background));
    }


    /**
     * Sets the console color based on parameter square type.
     */
    private void setSquareColor(char square) {

        Color foregroundColor;
        Color backgroundColor = Color.BLACK;

        switch (square) {
            case SquareType.PLAYER:
                foregroundColor = Color.GREEN;
                break;
            case SquareType.ROBOT:
                foregroundColor = Color.YELLOW;
                break;
            case '1':
            case '2':
            case '3':
                foregroundColor = Color.RED;
                break;
            case SquareType.WALL:
                foregroundColor = Color.BLACK;
                backgroundColor = Color.GRAY;
                break;
            default:
                foregroundColor = Color.WHITE;
                break;
        }

        this.setConsoleColor(foregroundColor, backgroundColor);
    }

    private void setSquare(int squareX, int squareY, char square) {

        this.map[squareY][squareX] = square;

        this.setSquareColor(square);

        this.setCursorPos(squareX, squareY);

        System.out.print(square);
    }

    /**
     * Print the whole screen to the console.
     */
    private void printPlayerStatus() throws Exception {

        this.setConsoleColor(Color.WHITE, Color.BLACK);
        this.setCursorPos(WIDTH + 1, 0);
        System.out.print("Input");

        this.setConsoleColor(Color.BLACK, Color.WHITE);
        this.setCursorPos(WIDTH + 1, 1);
        System.out.print("<<<<<<<<<<<<<<<");

        this.setConsoleColor(Color.WHITE, Color.BLACK);
        this.setCursorPos(WIDTH + 1, 2);
        this.inputQueue.print();

        this.setConsoleColor(Color.BLACK, Color.WHITE);
        this.setCursorPos(WIDTH + 1, 3);
        System.out.print("<<<<<<<<<<<<<<<");

        this.setConsoleColor(Color.WHITE, Color.BLACK);


        for (int i = 0; i < BACKPACK_SIZE; ++i) {
            this.setCursorPos(WIDTH + 6, i + 5);
            System.out.print("|   |");
        }

        this.setCursorPos(WIDTH + 6, 5 + BACKPACK_SIZE);
        System.out.print("+---+");
        this.setCursorPos(WIDTH + 5, 6 + BACKPACK_SIZE);
        System.out.print("Backpack");

        this.setCursorPos(WIDTH + 4, 16);
        System.out.print("Teleport:");
        this.setCursorPos(WIDTH + 14, 16);
        System.out.print(this.player.teleportRight);

        this.setCursorPos(WIDTH + 4, 18);
        System.out.print("Score:    ");
        this.setCursorPos(WIDTH + 11, 18);
        System.out.print(this.player.score);

        this.setCursorPos(WIDTH + 4, 20);
        System.out.print("Time: 0");

    }

    /**
     * Sets a random earth square with the parameter square value.
     *
     * @param square value to be replaced with a random earth square.
     */
    private void setRandomEarth(char square) {
        this.replaceRandomSquare(new char[]{SquareType.EARTH}, square);
    }

    /**
     * Replaces a random instance of a parameter oldSquare with the parameter newSquare from the map.
     *
     * @param searchSquares square to be replaced
     * @param newSquare     new value of the
     */
    private void replaceRandomSquare(char[] searchSquares, char newSquare) {

        while (true) {

            // We assume the sides of the game map is filled with walls.
            // Generate random indices that are in the game map.
            // x -> [1, WIDTH - 2]
            // y -> [1, HEIGHT - 2]
            int x = Gravity.random.nextInt(Gravity.WIDTH - 2) + 1;
            int y = Gravity.random.nextInt(Gravity.HEIGHT - 2) + 1;

            for (int i = 0; i < searchSquares.length; ++i) {

                if (this.map[y][x] == searchSquares[i]) {

                    this.setSquare(x, y, newSquare);

                    switch (newSquare) {
                        case SquareType.PLAYER:
                            this.player.x = x;
                            this.player.y = y;
                            break;
                        case SquareType.ROBOT:
                            this.robots[this.robotCount] = new Robot(x, y, 0, 0);
                            this.robotCount++;
                            break;
                        case SquareType.BOULDER:
                            this.boulders[this.boulderCount] = new Boulder(x, y);
                            this.boulderCount += 1;
                            break;
                        default:
                            break;
                    }

                    return;
                }
            }
        }
    }

    /**
     * Prints stars to the console.
     */
    private void printStars(int width, int height) throws InterruptedException {

        // Stars for visual effect
        char[] stars = new char[]{'.', '*', 'o', '\u2B50'};
        Color[] starColors = new Color[]{Color.WHITE, Color.YELLOW, Color.ORANGE};

        // Print background with stars
        for (int i = 0; i < height; ++i) {
            for (int t = 0; t < width; ++t) {

                this.setCursorPos(t, i);

                char character;

                if (Gravity.random.nextFloat() < 0.05f) {
                    int randomStar = Gravity.random.nextInt(stars.length);
                    character = stars[randomStar];

                    int randomColor = Gravity.random.nextInt(starColors.length);
                    this.setConsoleColor(starColors[randomColor], Color.BLACK);

                    Thread.sleep(10);
                } else {
                    character = ' ';
                }

                System.out.print(character);
            }
        }
    }

}
