import java.util.NoSuchElementException;

public class Queue {

    private final int CAPACITY = 30;

    private Object[] array = new Object[CAPACITY];

    private int count = 0;

    /**
     * Default constructor for Queue class
     */
    public Queue() {

    }

    /**
 * Copy constructor for Queue. Creates a new queue from the parameter queue.
     */
    public Queue(Queue queue) {

        for (int i = 0; i < queue.count; ++i) {

            this.array[i] = queue.array[i];
        }

        this.count = queue.count;
    }

    /**
     * Places a new element to the rear of the queue.
     * @return the newly enqueued object
     */
    public Object enqueue(Object object) throws Exception {

        if (this.isFull()) {
            throw new Exception("Cannot enqueue the queue. Queue is full.");
        }

        this.array[this.count] = object;

        this.count += 1;

        return object;
    }

    /**
     * Removes the element at the front of the queue.
     * @return the newly dequeued object
     */
    public Object dequeue() throws NoSuchElementException {

        if (this.isEmpty()) {
            throw new NoSuchElementException("Cannot dequeue the queue. Queue is empty.");
        }

        Object removedItem = this.array[0];

        // Move the items to the front of the queue
        for (int i = 0; i < this.count - 1; ++i) {

            this.array[i] = this.array[i + 1];
        }

        this.count -= 1;

        return removedItem;
    }

    /**
     * Peeks the queue without modifying.
     * @return element at the front of the queue
     */
    public Object peek() throws NoSuchElementException {

        if (this.isEmpty()) {
            throw new NoSuchElementException("Cannot peek the queue. Queue is empty.");
        }

        return this.array[0];
    }

    /**
     * @return true if the queue is full.
     */
    public boolean isFull() {

        return this.count == CAPACITY;
    }

    /**
     * @return true if the queue is empty.
     */
    public boolean isEmpty() {

        return this.count == 0;
    }

    /**
     * @return the current size of the queue.
     */
    public int size() {

        return this.count;
    }

}
