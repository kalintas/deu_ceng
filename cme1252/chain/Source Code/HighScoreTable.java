import java.io.*;
import java.util.Scanner;

import enigma.console.Console;
import enigma.core.Enigma;

public class HighScoreTable {
    private Console console = Enigma.getConsole("CHAIN", 100, 30, 20);
    ;
    private HighScoreNode head;
    private HighScoreNode tail;

    public HighScoreTable() {
        head = null;
        tail = null;
        read();
    }

    public void addEnd(String name, int score) {
        HighScoreNode newNode = new HighScoreNode(name, score);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            if (head.getNext() == null) head.setNext(tail);
            newNode.setPrev(tail);
            tail.setNext(newNode);
            tail = newNode;
        }
    }

    public void add(String name, int score) {
        HighScoreNode newNode = new HighScoreNode(name, score);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            HighScoreNode temp = head;
            if (head.getNext() == null) {
                if (head.getScore() < newNode.getScore()) {
                    newNode.setNext(temp);
                    temp.setPrev(newNode);
                    head = newNode;
                    tail = temp;
                } else {
                    newNode.setPrev(head);
                    head.setNext(newNode);
                    tail = newNode;
                }
            } else {
                if (newNode.getScore() > temp.getScore()) {
                    newNode.setNext(temp);
                    temp.setPrev(newNode);
                    head = newNode;
                } else if (tail.getScore() > newNode.getScore()) {
                    addEnd(name, score);
                } else {
                    while (temp.getNext() != null && newNode.getScore() < temp.getScore()) {
                        temp = temp.getNext();
                    }
                    newNode.setNext(temp);
                    newNode.setPrev(temp.getPrev());
                    temp.getPrev().setNext(newNode);
                    if (newNode.getNext() != null) {
                        temp.setPrev(newNode);
                    } else {
                        tail = newNode;
                    }
                }
            }
        }
    }

    private void read() {
        try (BufferedReader br = new BufferedReader(new FileReader("highscore.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] temp = line.split(" ");
                String name = "";
                int score = Integer.parseInt(temp[temp.length - 1]);
                for (int i = 0; i < temp.length - 1; ++i) {
                    name += temp[i];
                }
                add(name, score);
            }
        } catch (IOException e) {
            System.out.println("File not found.");
        }
    }

    public void write(String name, int score) {
        add(name, score);
        HighScoreNode temp = head;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("highscore.txt", false))) {
            while (temp != null) {
                bw.write(temp.getName() + " " + temp.getScore());
                bw.newLine();
                temp = temp.getNext();
            }
        } catch (IOException e) {
            System.out.println("File not found.");
        }
    }

    public void display() throws IOException {
        HighScoreNode temp = head;
        int n = 1;
        console.getTextWindow().setCursorPosition(2, n);
        console.getTextWindow().output("Name\tScore\n");
        while (temp != null) {
            n++;
            console.getTextWindow().setCursorPosition(2, n);
            console.getTextWindow().output(temp.getName() + "\t" + temp.getScore() + "\n\n");
            temp = temp.getNext();
        }
        console.getTextWindow().setCursorPosition(2, n + 2);
        console.getTextWindow().output("Press enter to leave");
        console.getTextWindow().setCursorPosition(0, 0);
        System.in.read();
    }
}
