public class MultiLinkedList {

    private ChainRowNode head = null;


    public void addChainRow(int row) {

        ChainRowNode newnode = new ChainRowNode(row);

        if (head == null) {

            head = newnode;
        } else {

            ChainRowNode temp = head;

            while (temp.getDown() != null) {

                temp = temp.getDown();
            }

            temp.setDown(newnode);
        }


    }

    public void addChainColumn(int row, char number) {

        ChainElementNode newnode = new ChainElementNode(number);

        ChainRowNode temp = head;

        while (temp != null && temp.getRow() != row) {

            temp = temp.getDown();
        }

        if (temp.getRight() == null) {

            temp.setRight(newnode);
        } else {

            ChainElementNode temp2 = temp.getRight();

            while (temp2.getNext() != null) {

                temp2 = temp2.getNext();
            }

            temp2.setNext(newnode);

        }

    }


    public void display(int row) {


        ChainRowNode temp = head;

        while (temp != null && temp.getRow() != row) {

            temp = temp.getDown();
        }

        ChainElementNode temp2 = temp.getRight();

        while (temp2 != null) {

            if (temp2.getNext() == null) {

                System.out.print(temp2.getNumber());
            } else {

                System.out.print(temp2.getNumber() + "+");
            }

            temp2 = temp2.getNext();
        }

        System.out.println();


    }

    public int getLastNumber(int row) {

        ChainRowNode temp = head;

        while (temp != null && temp.getRow() != row) {

            temp = temp.getDown();
        }

        ChainElementNode temp2 = temp.getRight();

        while (temp2.getNext() != null) {

            temp2 = temp2.getNext();
        }

        return temp2.getNumber();

    }


}
