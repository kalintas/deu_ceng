
public class Main {

    public static void main(String[] args) {

        Stack s1 = new Stack(100);
        Stack s2 = new Stack(100);
        Queue q1 = new Queue(100);
        Queue q2 = new Queue(100);

        // Fill the stacks with the values.
        s1.push('0');
        s1.push('2');
        s1.push('9');
        s1.push('6');
        s1.push('1');
        s1.push('3');
        s1.push('8');
        s1.push('4');
        s1.push('5');
        s1.push('7');

        s2.push('d');
        s2.push('e');
        s2.push('r');
        s2.push('y');
        s2.push('o');
        s2.push('&');
        s2.push('u');
        s2.push('l');
        s2.push('a');
        s2.push('s');

        // Fill the q1 with values
        q1.enqueue('7');
        q1.enqueue('.');
        q1.enqueue('8');
        q1.enqueue('6');
        q1.enqueue('2');
        q1.enqueue(',');
        q1.enqueue('5');
        q1.enqueue('5');
        q1.enqueue(' ');
        q1.enqueue('T');
        q1.enqueue('L');

        while (!q1.isEmpty()) {

            char q1Value = (char)q1.dequeue();

            System.out.print(q1Value);

            Stack tempS1 = new Stack(100);
            int stackIndex = 0;

            // Search q1Value in s1.
            while (!s1.isEmpty()) {

                char s1Value = (char)s1.pop();
                tempS1.push(s1Value);

                if (q1Value == s1Value) {
                    // Found the value in s1. Search it in s2.

                    Stack tempS2 = new Stack(100);

                    char s2Value = 0;

                    for (int i = 0; i <= stackIndex; ++i) {

                        s2Value = (char)s2.pop();
                        tempS2.push(s2Value);
                    }

                    while (!tempS2.isEmpty()) {
                        s2.push(tempS2.pop());
                    }

                    q2.enqueue(s2Value);
                    break;
                }

                stackIndex += 1;
            }

            // Value is not digit. Add it directly.
            if (stackIndex == tempS1.size()) {

                q2.enqueue(q1Value);
            }

            while (!tempS1.isEmpty()) {
                s1.push(tempS1.pop());
            }

        }

        System.out.println();

        Queue tempQ2 = new Queue(100);

        while (!q2.isEmpty()) {

            char q2Value = (char)q2.dequeue();

            System.out.print(q2Value);

            tempQ2.enqueue(q2Value);
        }

        q2 = tempQ2;
    }
}
