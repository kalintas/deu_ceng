import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        Queue queue = new Queue(100);

        queue.enqueue(10);
        queue.enqueue(2);
        queue.enqueue(8);
        queue.enqueue(6);
        queue.enqueue(7);
        queue.enqueue(7);
        queue.enqueue(12);
        queue.enqueue(5);
        queue.enqueue(3);

        // Get the n from the user
        int n = scanner.nextInt();

        if (n > queue.size()) {
            System.err.print("Invalid N value");
            return;
        }

        if (queue.size() % n != 0) {
            System.out.println(false);
            return;
        }

        int firstSum = 0;

        if (!queue.isEmpty()) {

            for (int i = 0; i < n; ++i) {

                firstSum += (int)queue.dequeue();
            }
        }

        boolean sumsAreEqual = true;

        while (!queue.isEmpty()) {

            int sum = 0;

            for (int i = 0; i < n; ++i) {

                sum += (int)queue.dequeue();
            }

            if (firstSum != sum) {
                sumsAreEqual = false;
                break;
            }
        }

        System.out.println(sumsAreEqual);
    }
}
