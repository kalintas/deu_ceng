

public class Player {

    private int score = 0;
    private int x = 15;
    private int y = 9;

    private int oldX;
    private int oldY;

    // Start and the end of the chain.
    private int chainStartX = 0;
    private int chainStartY = 0;

    private int chainEndX = 0;
    private int chainEndY = 0;

    private ChainGame chainGame;

    public Player(ChainGame chainGame) {
        this.chainGame = chainGame;
    }

    public void print() {
        this.chainGame.printSquare('P', this.x, this.y);
    }

    /**
     * Moves the player in direction of parameter moveX and moveY.
     * @param moveX movement to be done in x dimension
     * @param moveX movement to be done in y dimension
     */
    public void move(int moveX, int moveY) {

        int newX = this.x + moveX;
        int newY = this.y + moveY;

        // Check map boundaries
        if (newX >= 0 && newX < 31 && newY >= 0 && newY < 19) {

            // Player cannot move through chains.
            // Check if newX and newY does collide with the chain.
            Node current = this.chainGame.chain.getHead();

            while (current != null) {

                ChainPiece piece = (ChainPiece) current.getData();

                if (piece.getX() == newX && piece.getY() == newY) {
                    return;
                }

                current = current.getLink();
            }

            // Player cannot move through numbers.
            // Player cannot be in both even coordinates.
            if (newX % 2 == 1 || newY % 2 == 1) {

                // Move player
                this.chainGame.printSquare(' ', this.x, this.y);
                this.chainGame.printSquare('P', newX, newY);

                this.oldX = this.x;
                this.oldY = this.y;

                this.x = newX;
                this.y = newY;
            }
        }

    }

    /**
     * Places a chain piece at the players old position.
     */
    public void placeChainPiece() {

        ChainPiece chainPiece = new ChainPiece(this.oldX, this.oldY);

        // Chain piece cannot be in odd coordinates.
        if (chainPiece.getX() % 2 == 1 && chainPiece.getY() % 2 == 1) {
            return;
        }

        // Coordinates of the linked two numbers.
        int firstX = chainPiece.getFirstNumberX();
        int firstY = chainPiece.getFirstNumberY();

        int secondX = chainPiece.getSecondNumberX();
        int secondY = chainPiece.getSecondNumberY();

        // Chain piece is placeable, but we still need to check if it's connected to the chain.
        Node head = this.chainGame.chain.getHead();

        if (head == null) {
            // This is the first chain piece.
            this.chainStartX = firstX;
            this.chainStartY = firstY;

            this.chainEndX = secondX;
            this.chainEndY = secondY;
           
        } else {

            // Check chain start and end variables.
            // The new chainPiece must at least have one common number with the start or the end of the chain.
            if (this.chainStartX == firstX && this.chainStartY == firstY) {
                this.chainStartX = secondX;
                this.chainStartY = secondY;
               
                
            } else if (this.chainStartX == secondX && this.chainStartY == secondY) {
                this.chainStartX = firstX;
                this.chainStartY = firstY;
               
            } else if (this.chainEndX == firstX && this.chainEndY == firstY) {
                this.chainEndX = secondX;
                this.chainEndY = secondY;
               
            } else if (this.chainEndX == secondX && this.chainEndY == secondY) {
                this.chainEndX = firstX;
                this.chainEndY = firstY;
             
            } else {
                // No common numbers. Chain piece is cannot be linked to the chain.
                return;
            }
        }

        this.chainGame.printSquare('+', chainPiece.getX(), chainPiece.getY());

        this.chainGame.chain.add(chainPiece);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
