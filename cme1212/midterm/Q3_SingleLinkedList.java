
public class SingleLinkedList {

    // Head node variable.
    private Node head;

    /**
     * Default constructor
     */
    public SingleLinkedList() {

        // Defaulted to null
        this.head = null;
    }

    /**
     * Does an unsorted add to the linked lists back.
     * Insert at last if there is an element already.
     * Linked lists size is increased 1 after this operation.
     * @param data object to be added to the list
     */
    public void add(Object data) {

        Node newNode = new Node(data);

        if (this.head == null) {
            // List is empty.
            this.head = newNode;
        } else {
            // List has elements. Add newNode to the back of the list.
            Node lastNode = this.head;

            while (lastNode.getLink() != null) {
                lastNode = lastNode.getLink();
            }

            // lastNode.getLink() is null. Set it to newNode
            lastNode.setLink(newNode);
        }
    }


    /**
     * Returns the size of the list by iterating all the nodes.
     * @return size of the list.
     */
    public int size() {

        int size = 0;

        Node currentNode = this.head;

        while (currentNode != null) {

            size += 1;
            currentNode = currentNode.getLink();
        }

        return size;
    }

    /**
     * @return true if the list is empty.
     */
    public boolean isEmpty() {
        return this.head == null;
    }


    /**
     * Prints list to the console
     */
    public void display() {

        Node currentNode = this.head;

        while (currentNode != null) {

            System.out.printf("%s ", currentNode.getData());
            currentNode = currentNode.getLink();
        }

        System.out.println();
    }

    /**
     * Mutates the xth element in the list.
     * Mutation is the process of changing one bit in the list (from 1 to 0 or from 0 to 1).
     */
    public void mutation(int x) {

        if (this.head == null) {
            System.err.println("Cannot mutate list. List is empty");
            return;
        }

        if (x < 0) {
            System.err.println("Cannot mutate list. X cannot be negative");
            return;
        }

        Node current = this.head;

        for (int i = 0; i < x - 1; ++i) {

            current = current.getLink();

            if (current == null) {
                System.err.println("Cannot mutate list. X is out of bounds");
                return;
            }
        }

        int bit = (int)current.getData();

        current.setData(1 - bit);
    }

}
