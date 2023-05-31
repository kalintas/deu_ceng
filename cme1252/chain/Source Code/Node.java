
public class Node {

    private Object data;
    // Link to the next node.
    private Node link;

    /**
     * Constructs a new node from the parameter data with no link.
     */
    public Node(Object data) {

        this.data = data;
        this.link = null;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * @return link to the next node. Default value is null.
     */
    public Node getLink() {
        return this.link;
    }

    public void setLink(Node link) {
        this.link = link;
    }
}
