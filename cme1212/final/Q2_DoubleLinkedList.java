
import java.util.Scanner;

public class DoubleLinkedList {
    public DLLNode head;
    public DLLNode tail;

    public DoubleLinkedList() {
        head = null;
        tail = null;
    }

    public void add(Object dataToAdd) {

        DLLNode newNode = new DLLNode(dataToAdd);

        if (head == null && tail == null) {
            head = newNode;
            tail = newNode;
        } else {

            DLLNode currentNode = head;

            while (currentNode.getNext() != null) {
                currentNode = currentNode.getNext();
            }

            currentNode.setNext(newNode);
            newNode.setPrev(currentNode);
            tail = newNode;
        }

    }

    public void display() {
        if (head == null) {
            System.out.println("linked list is empty");
        } else {
            DLLNode temp = head;
            while (temp != null) {
                System.out.print(temp.getData() + " ");
                temp = temp.getNext();
            }
        }
    }

    public boolean search(Object searchItem) {
        boolean flag = false;
        if (head == null) {
            System.out.println("linked list is empty");
        } else {
            DLLNode temp = head;
            while (temp != null) {
                if (searchItem == temp.getData()) {
                    flag = true;
                }
                temp = temp.getNext();
            }
        }

        return flag;
    }

    public int size() {
        int count = 0;
        if (head == null) {
            System.out.println("linked list is empty");
        } else {
            DLLNode temp = head;
            while (temp != null) {
                count++;
                temp = temp.getNext();
            }
        }
        return count;
    }


    public void delete(Object dataToDelete) {
        if (head == null) {
            System.out.println("linked list is empty");
        } else {
            if (head.getData() == dataToDelete) {
                head = head.getNext();
            }
            DLLNode temp = head;
            DLLNode previous = null;
            while (temp != null) {
                if (temp.getData() == dataToDelete) {
                    previous.setNext(temp.getNext());
                    temp.getNext().setPrev(previous);
                    temp = previous;
                }
                previous = temp;
                temp = temp.getNext();
            }
        }
    }

    public void game() {

        // Create the scanner for getting inputs.
        Scanner scanner = new Scanner(System.in);

        // Get the middle node.
        DLLNode currentNode = this.head;

        int middlePoint = this.size() / 2 - 1;

        // Get the left word if word count is even.
        if (this.size() % 2 == 0) {
            middlePoint -= 1;
        }
        for (int i = 0; i <= middlePoint; ++i) {
            currentNode = currentNode.getNext();
        }

        int stepCount = 1;

        while (true) {

            int input = 0;

            // Check if input is in correct format.
            try {
                System.out.print("Enter a number [-3, +3]: ");
                input = Integer.parseInt(scanner.nextLine());

            } catch (Exception exception) {
                System.err.println("Wrong input. Enter an integer.");
                continue;
            }

            if (input > 3 || input < -3) {
                System.err.println("Input number must be in range of [-3, +3]");
                continue;
            }

            for (int i = input; i != 0; ) {

                DLLNode nextNode;

                if (i > 0) {

                    nextNode = currentNode.getNext();
                    i -= 1;
                } else {
                    nextNode = currentNode.getPrev();
                    i += 1;
                }

                if (nextNode == null) {
                    System.out.printf("The game is over after %d steps", stepCount);
                    return;
                }

                currentNode = nextNode;
            }

            System.out.println((String) currentNode.getData());

            stepCount += 1;
        }
    }

}
