public class ChainElementNode {

    private char number;
    private ChainElementNode next;

    public ChainElementNode(char number) {

        this.number = number;
        next = null;

    }

    public char getNumber() {
        return number;
    }

    public void setNumber(char number) {
        this.number = number;
    }

    public ChainElementNode getNext() {
        return next;
    }

    public void setNext(ChainElementNode next) {
        this.next = next;
    }


}
