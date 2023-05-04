import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Scanner for getting inputs from the user
        Scanner scanner = new Scanner(System.in);

        // Create the stack with 100 capacity
        Stack stack = new Stack(100);

        // Fill the stack with the sample values
        stack.push(18);
        stack.push(5);
        stack.push(3);
        stack.push(14);
        stack.push(5);
        stack.push(5);
        stack.push(5);
        stack.push(5);
        stack.push(3);
        stack.push(5);
        stack.push(3);
        stack.push(3);

        for (int i = 0; i < 8; ++i) {

            int input = scanner.nextInt();

            // Create a temporary stack
            Stack tempStack = new Stack(100);

            int repeatCount = 0;

            while (!stack.isEmpty()) {

                int element = (int)stack.pop();

                if (input == element) {
                    repeatCount += 1;
                }

                tempStack.push(element);
            }

            while (!tempStack.isEmpty()) {
                stack.push(tempStack.pop());
            }

            double repeatPercentage = ((double)repeatCount / (double)stack.size()) * 100.0;

            if (repeatPercentage > 30.0) {
                System.out.println("active");
            } else {
                System.out.println("non-active");
            }
        }
    }

}