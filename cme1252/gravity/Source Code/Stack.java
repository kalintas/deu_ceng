import java.util.NoSuchElementException;

public class Stack {

    private Object[] array;
    private int count;

    public Stack(int capacity) {

        this.array = new Object[capacity];
        this.count = 0;
    }

    /**
     * Pushes the parameter object to the Stack.
     * @param object object to be pushed to stack.
     * @throws Exception if the Stack is full.
     * @return the newly pushed object.
     */
    public Object push(Object object) throws Exception {

        if (this.isFull()) {
            throw new Exception("Cannot push the stack. Stack is full");
        }

        this.array[this.count] = object;

        this.count += 1;

        return object;
    }

    /**
     * Pops the stack.
     * @throws NoSuchElementException if the stack is empty.
     * @return the newly popped object.
     */
    public Object pop() throws NoSuchElementException {

        if (this.isEmpty()) {
            throw new NoSuchElementException("Cannot pop the stack. Stack is empty.");
        }

        this.count -= 1;

        return this.array[this.count];
    }

    /**
     * Peeks the stack without modifying.
     * @throws NoSuchElementException if the stack is empty.
     * @return the object on top of the stack.
     */
    public Object peek() throws NoSuchElementException {

        if (this.count == 0) {
            throw new NoSuchElementException("Cannot peek the stack. Stack is empty.");
        }

        return this.array[this.count - 1];
    }

    /**
     * @return true if the stack is full.
     */
    public boolean isFull() {

        return this.count == this.array.length;
    }

    /**
     * @return true if the stack is empty.
     */
    public boolean isEmpty() {

        return this.count == 0;
    }

    /**
     * @return the current size of the stack.
     */
    public int size() {

        return this.count;
    }

}
