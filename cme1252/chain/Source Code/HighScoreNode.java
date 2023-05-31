public class HighScoreNode {
    private HighScoreNode prev;
    private HighScoreNode next;
    private String name;
    private int score;

    public HighScoreNode(String name, int score) {
        next = null;
        prev = null;
        this.name = name;
        this.score = score;
    }

    public HighScoreNode getPrev() {
        return prev;
    }

    public void setPrev(HighScoreNode prev) {
        this.prev = prev;
    }

    public HighScoreNode getNext() {
        return next;
    }

    public void setNext(HighScoreNode next) {
        this.next = next;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

}
