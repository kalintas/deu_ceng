import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;

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
     * Copy constructor. Creates a new SLL from the parameter.
     */
    public SingleLinkedList(SingleLinkedList sll) {

        Node currentNode = sll.head;

        while (currentNode != null) {

            this.add(currentNode.getData());
            currentNode = currentNode.getLink();
        }
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

    public void addtoStart(Object data) {
    	
    	Node newnode=new Node(data);
    	
    	newnode.setLink(head);
    	
    	head=newnode;
    	
    }
    /**
     * Does a sorted add to the linked lists back.
     * Adds elements in most to the least order.
     * Linked lists size is increased 1 after this operation.
     * @return the index of the newly inserted object.
     * @param data object to be added to the list.
     */
   /* public int addSorted(Object data) {

        Node newNode = new Node(data);
        int index = 0;

        if (this.head == null) {
            // List is empty.
            this.head = newNode;
        } else if (Integer.pa >= (int)this.head.getData()) {
            // Add newNode to the front.
            newNode.setLink(this.head);
            this.head = newNode;
        } else {

            // Iterate elements of the list.
            for (Node currentNode = this.head; currentNode != null; currentNode = currentNode.getLink()) {

                Node nextNode = currentNode.getLink();
                index += 1;

                // Find the correct place.
                // Add to the back if all elements are bigger than parameter data.
                if (nextNode == null || (int)data >= (int)nextNode.getData()) {

                    currentNode.setLink(newNode);
                    newNode.setLink(nextNode);
                    break;
                }
            }
        }

        return index;
    }*/

    /**
     * Inserts parameter data at the location parameter index.
     * Size of the list is increased 1 after this call.
     * @throws IndexOutOfBoundsException if index is bigger or equal to the size of the list.
     * @param data object to be inserted to the list.
     * @param index location of the newly inserted object.
     */
    public void insert(Object data, int index) throws IndexOutOfBoundsException {

        Node newNode = new Node(data);

        // Insert to front
        if (index == 0) {

            // Set newNodes link to this.head.
            // Head node is null if the sll is empty.
            newNode.setLink(this.head);
            // Set head node to newNode
            this.head = newNode;
        } else {

            Node currentNode = this.head;

            for (int i = 1; i < index; ++i) {

                Node nextNode = currentNode.getLink();

                // Reached the end of the list without inserting. Throw an exception
                if (nextNode == null) {
                    throw new IndexOutOfBoundsException("Cannot insert to the list. Index is out of bounds");
                }

                currentNode = nextNode;
            }

            Node nextNode = currentNode.getLink();

            newNode.setLink(nextNode);
            currentNode.setLink(newNode);
        }
    }

    /**
     * Deletes all elements that match parameter data.
     * sll.find(data) is always null after calling sll.delete(data).
     * @param data object to be searched and removed from the list.
     * @throws NoSuchElementException if list is empty
     */
    public void delete(Object data) throws NoSuchElementException {

        if (this.isEmpty()) {
            throw new NoSuchElementException("Cannot delete element. List is empty");
        }

        Node previousNode = null;
        Node currentNode = this.head;

        do {

            Node nextNode = currentNode.getLink();

            if (currentNode.getData().equals(data)) {
                // Found a matching node

                if (previousNode == null) {
                    // This is the first node. Link head node to nextNode.
                    this.head = nextNode;
                } else {
                    // This is not the first node. Link previousNode to nextNode.
                    previousNode.setLink(nextNode);
                }
            } else {
                previousNode = currentNode;
            }

            currentNode = nextNode;

        } while (currentNode != null);
    }

    /**
     * Finds and returns the index of the node that equals to the parameter data from the list.
     * @param data object to be searched and returned.
     * @return index of the node from the list that Node.getData().equals(data).
     *         Returns -1 if there is no such element in the list.
     */
    public int find(Object data) {

        int index = 0;
        Node currentNode = this.head;

        while (currentNode != null && !currentNode.getData().equals(data)) {

            index += 1;
            currentNode = currentNode.getLink();
        }

        // No such element in the list. Return -1.
        if (currentNode == null) {
            return -1;
        }

        return index;
    }

    /**
     * Returns the head node.
     */
    public Node getHead() {
        return this.head;
    }

    /**
     * Returns the node at the index.
     * @throws NoSuchElementException if list is empty.
     * @throws IndexOutOfBoundsException if parameter index is larger than lists size
     * @return node at the parameter index.
     */
    public Node getNodeAt(int index) throws IndexOutOfBoundsException {

        if (this.isEmpty()) {
            throw new NoSuchElementException("Cannot get node. List is empty");
        }

        Node currentNode = this.head;

        for (int i = 0; i < index; ++i) {

            Node nextNode = currentNode.getLink();

            if (nextNode == null) {
                throw new IndexOutOfBoundsException("Cannot get the node. Index is out of bounds");
            }

            currentNode = nextNode;
        }

        return currentNode;
    }

    /**
     * @throws NoSuchElementException if list is empty.
     * @return a random node from the linked list. Returns null if the list is empty.
     */
    public Node getRandom() throws NoSuchElementException {

        if (this.isEmpty()) {
            throw new NoSuchElementException("Cannot get random element. List is empty.");
        }

        // Create a random index within the list
        int randomIndex = ThreadLocalRandom.current().nextInt(this.size());

        Node randomNode = this.head;

        for (int i = 0; i < randomIndex; ++i) {

            randomNode = randomNode.getLink();
        }

        return randomNode;
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
     * @param name name of the list.
     */
    public void print(String name) {

        System.out.println("s: "+name);

        Node currentNode = this.head;

        while (currentNode != null) {

            System.out.println("s "+currentNode.getData());
            currentNode = currentNode.getLink();
        }

        System.out.println();
    }
    
   
}
