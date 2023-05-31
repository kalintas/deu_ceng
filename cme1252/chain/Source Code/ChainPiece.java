
public class ChainPiece {


    private int x;
    private int y;

    public ChainPiece(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    /**
     * @return Returns the first linked numbers x position.
     */
    public int getFirstNumberX() {
        return this.x / 2;
    }

    /**
     * @return Returns the first linked numbers y position.
     */
    public int getFirstNumberY() {
        return this.y / 2;
    }

    /**
     * @return Returns the second linked numbers x position.
     */
    public int getSecondNumberX() {

        if (this.x % 2 == 0) {
            // Chain is linked vertically
            return this.x / 2;
        } else {
            // Chain is linked horizontally
            return this.x / 2 + 1;
        }
    }

    /**
     * @return Returns the second linked numbers y position.
     */
    public int getSecondNumberY() {

        if (this.x % 2 == 0) {
            // Chain is linked vertically
            return this.y / 2 + 1;
        } else {
            // Chain is linked horizontally
            return this.y / 2;
        }
    }

}
