
public class Queue {

    private int rear, front;
    private Object[] array;

    public Queue(int capacity) {

        this.array = new Object[capacity];
        this.rear = -1;
        this.front = 0;
    }

    /**
     * Places a new element to the rear of the queue.
     * @return the newly enqueued object
     */
    public void enqueue(Object object) throws Exception {

        if (this.isFull()) {
            throw new Exception("Queue overflow");
        }

        this.rear = (this.rear + 1) % this.array.length;
        this.array[this.rear] = object;
    }

    /**
     * Removes the element at the front of the queue.
     * @return the newly dequeued object
     */
    public Object dequeue() throws Exception {

        if (this.isEmpty()) {
            throw new Exception("Queue is empty");
        }

        Object object = this.array[this.front];
        this.array[this.front] = null;
        this.front = (this.front + 1) % this.array.length;

        return object;
    }

    /**
     * Peeks the queue without modifying.
     * @return element at the front of the queue
     */
    public Object peek() throws Exception {

        if (this.isEmpty()) {
            throw new Exception("Queue is empty");
        }

        return this.array[this.front];
    }

    /**
     * @return true if the queue is empty.
     */
    public boolean isEmpty() {
        return this.array[this.front] == null;
    }

    /**
     * @return true if the queue is full.
     */
    public boolean isFull() {

        return this.front == (this.rear + 1) % this.array.length &&
                this.array[front] != null && this.array[rear] != null;
    }

    /**
     * @return the current size of the queue.
     */
    public int size() {

        if (this.array[this.front] == null) {
            return 0;
        }

        if (this.rear >= this.front) {

            return this.rear - this.front + 1;
        }

        return this.array.length - (this.front - this.rear) + 1;
    }
}
