
public class Robot {
    public int x;
    public int y;

    public int moveX;
    public int moveY;

    Robot(int x, int y, int moveX, int moveY) {
        this.x = x;
        this.y = y;

        this.moveX = moveX;
        this.moveY = moveY;
    }

    public Robot move(int moveX, int moveY) {

        Robot robot = new Robot(this.x + moveX, this.y + moveY, moveX, moveY);

        return robot;
    }

}


