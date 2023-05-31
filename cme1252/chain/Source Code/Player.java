

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
     *
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
        if (this.chainGame.chain.isEmpty()) {
            // This is the first chain piece.
            this.chainStartX = firstX;
            this.chainStartY = firstY;

            this.chainEndX = secondX;
            this.chainEndY = secondY;


            this.chainGame.chain.add(chainPiece);
        } else {

            // Iterate through chain pieces to check if chainPiece is placable
            for (Node current = this.chainGame.chain.getHead(); current != null; current = current.getLink()) {

                ChainPiece piece = (ChainPiece) current.getData();

                // Cannot be in the same position with another chain piece.
                if (piece.getX() == chainPiece.getX() && piece.getY() == chainPiece.getY()) {
                    return;
                }

                // Check if this chain piece connects the start or end of the chain with a middle chain piece.
                if (current != this.chainGame.chain.getHead() && current.getLink() != null) {

                    if (
                        (piece.getFirstNumberX() == firstX && piece.getFirstNumberY() == firstY) ||
                        (piece.getSecondNumberX() == secondX && piece.getSecondNumberY() == secondY) ||
                        (piece.getFirstNumberX() == secondX && piece.getFirstNumberY() == secondY) ||
                        (piece.getSecondNumberX() == firstX && piece.getSecondNumberY() == firstY)) {
                        return;
                    }
                }
            }

            // Chain is linked to itself. Cannot add more nodes.
            if (this.chainStartX == this.chainEndX && this.chainStartY == this.chainEndY) {
                return;
            }
            // Check chain start and end variables.
            // The new chainPiece must at least have one common number with the start or the end of the chain.
            if (this.chainStartX == firstX && this.chainStartY == firstY) {
                this.chainStartX = secondX;
                this.chainStartY = secondY;

                this.chainGame.chain.addFirst(chainPiece);

            } else if (this.chainStartX == secondX && this.chainStartY == secondY) {
                this.chainStartX = firstX;
                this.chainStartY = firstY;

                this.chainGame.chain.addFirst(chainPiece);

            } else if (this.chainEndX == firstX && this.chainEndY == firstY) {
                this.chainEndX = secondX;
                this.chainEndY = secondY;

                this.chainGame.chain.add(chainPiece);

            } else if (this.chainEndX == secondX && this.chainEndY == secondY) {

                this.chainEndX = firstX;
                this.chainEndY = firstY;

                this.chainGame.chain.add(chainPiece);

            } else {
                // No common numbers. Chain piece is cannot be linked to the chain.
                return;
            }
        }


        this.chainGame.printSquare('+', chainPiece.getX(), chainPiece.getY());
    }

    /**
     * Removes one chain piece from the end or the start of the chain.
     * It will remove a chain piece if the distance between the player is 1.
     *
     * @return true when a chain piece is removed.
     */
    public boolean removeChainPiece() {

        if (this.chainGame.chain.isEmpty()) {
            return false;
        }

        int distanceX = this.x - this.oldX;
        int distanceY = this.y - this.oldY;

        int frontX = this.x + distanceX;
        int frontY = this.y + distanceY;

        ChainPiece lastChainPiece = (ChainPiece) this.chainGame.chain.getLast().getData();
        ChainPiece firstChainPiece = (ChainPiece) this.chainGame.chain.getHead().getData();

        if (firstChainPiece.getX() == frontX && firstChainPiece.getY() == frontY) {
            // Remove start chain piece
            ChainPiece start = (ChainPiece) this.chainGame.chain.deleteFirst().getData();

            this.chainGame.printSquare(' ', start.getX(), start.getY());

            int firstX = start.getFirstNumberX();
            int firstY = start.getFirstNumberY();

            int secondX = start.getSecondNumberX();
            int secondY = start.getSecondNumberY();

            if (this.chainStartX == firstX && this.chainStartY == firstY) {

                this.chainStartX = secondX;
                this.chainStartY = secondY;
            } else if (this.chainStartX == secondX && this.chainStartY == secondY) {

                this.chainStartX = firstX;
                this.chainStartY = firstY;
            }

        } else if (lastChainPiece.getX() == frontX && lastChainPiece.getY() == frontY) {
            // Remove start chain piece
            ChainPiece start = (ChainPiece) this.chainGame.chain.deleteLast().getData();

            this.chainGame.printSquare(' ', start.getX(), start.getY());

            int firstX = start.getFirstNumberX();
            int firstY = start.getFirstNumberY();

            int secondX = start.getSecondNumberX();
            int secondY = start.getSecondNumberY();

            if (this.chainEndX == firstX && this.chainEndY == firstY) {

                this.chainEndX = secondX;
                this.chainEndY = secondY;
            } else if (this.chainEndX == secondX && this.chainEndY == secondY) {

                this.chainEndX = firstX;
                this.chainEndY = firstY;
            }
        } else {
            // Cannot remove a chain piece.
            return false;
        }

        return true;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getChainStartX() {
        return chainStartX;
    }

    public int getChainStartY() {
        return chainStartY;
    }

    public int getChainEndX() {
        return chainEndX;
    }

    public int getChainEndY() {
        return chainEndY;
    }
}
