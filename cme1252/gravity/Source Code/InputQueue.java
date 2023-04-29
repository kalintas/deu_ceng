import java.util.Random;

public class InputQueue {

    private static final int SIZE = 15;

    private static Random random = new Random();

    private Queue queue;

    public InputQueue() throws Exception {

        this.queue = new Queue(InputQueue.SIZE);

        for (int i = 0; i < InputQueue.SIZE; ++i) {

            this.queue.enqueue(InputQueue.generateSquare());
        }
    }
    

    /**
     * Returns next square and place another square to the rear of the InputQueue.
     * @return next square to be placed on the map.
     */
    public char next() throws Exception {

        char nextSquare = (char)this.queue.dequeue();

        // Add one more square at the rear of the queue
        this.queue.enqueue(InputQueue.generateSquare());

        return nextSquare;
    }

    public void print() throws Exception {

         Queue tempQueue = new Queue(InputQueue.SIZE);

         while (!this.queue.isEmpty()) {

             char square = (char)this.queue.dequeue();

             System.out.print(square);

             tempQueue.enqueue(square);
         }

         this.queue = tempQueue;
    }

    private static char generateSquare() {

        int random = InputQueue.random.nextInt(40);

        if (random < 6) {
            // 6/40 possibility
            return '1';
        } else if (random < 11) {
            // 5/40 possibility
            return '2';
        } else if (random < 15) {
            // 4/40 possibility
            return '3';
        } else if (random < 16) {
            // 1/40 possibility
            return 'X';
        } else if (random < 26) {
            // 10/40 possibility
            return 'O';
        } else if (random < 35) {
            // 9/40 possibility
            return ':';
        }

        // 5/40 possibility
        return 'e';
    }


}
