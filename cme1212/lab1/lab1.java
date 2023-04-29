
import java.util.Scanner;
public class lab1 {

    private static Scanner scanner;
    private static int GetInt(String name) {
        System.out.printf("%s = ", name);
        return scanner.nextInt();
    }

    private static boolean IsTriangle(int a, int b, int c) {
        return a + b > c && a + c > b && b + c > a &&
                Math.abs(a - b) < c && Math.abs(a - c) < b && Math.abs(b - c) < a;
    }
    private static void Example1() {
        int a = GetInt("a");
        int b = GetInt("b");
        int c = GetInt("c");

        if (IsTriangle(a, b, c)) {
            System.out.println("It is a triangle");
        } else {
            System.out.println("It is not a triangle");
        }
    }

    private static void Example2() {

        int space = 10;

        for (int i = 0; i < 6; ++i) {
            for (int t = 0; t < 11; ++t) {

                if (t >= space) {
                    System.out.print('*');
                } else {
                    System.out.print(' ');
                }
            }
            space -= 2;
            System.out.print('\n');
        }

    }

    private static void Example3() {

        double[] array = { 3, 12, 9, 4, 2 };

        double sum = 0;

        for (int i = 0; i < array.length; ++i) {
            sum += array[i];
        }

        double mean = sum / array.length;

        sum = 0;

        for (int i = 0; i < array.length; ++i) {

            sum += (array[i] - mean) * (array[i] - mean);
        }

        double deviation = Math.sqrt(sum / array.length);

        System.out.printf("result = %f\n", deviation);
    }

    private static boolean IsSignature(int[][] matrix) {

        for (int i = 0; i < matrix.length; ++i) {
            for (int t = 0; t < matrix[i].length; ++t) {

                if (matrix[i].length != matrix.length) {
                    return false;
                }

                if (i == t) {
                    if (matrix[i][t] != 1 && matrix[i][t] != -1) {
                        return false;
                    }
                } else {
                    if (matrix[i][t] != 0) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
    private static void Example4() {

        int[][] matrix = { { 1,0,0,0,0 } ,{ 0,-1,0,0,0 } ,{ 0,0,1,0,0 } ,{ 0,0,0,1,0 } ,{ 0,0,0,0,-1 } };

        System.out.println(IsSignature(matrix));
    }

    private static void Example5() {

        String str = "bus city country girl";

        String[] words = str.split(" ");

        for (int i = 0; i < words.length; ++i) {

            String word = words[i];
            int length = words[i].length();

            if (word.endsWith("y")) {
                words[i] = word.substring(0, length - 1) + "ies";
            } else if (word.endsWith("s") || word.endsWith("c")) {
                words[i] = word + "es";
            } else if (word.endsWith("ch") || word.endsWith("sh")) {
                words[i] = word.substring(0, length - 2) + "es";
            } else {
                words[i] = word + "s";
            }
        }

        String result = String.join(" ", words);
        System.out.println(result);
    }

    private static int Cube(int x) {
        return x * x * x;
    }

    private static int SumOfCubes(int n) {

        int sum = 0;

        for (int i = 1; i <= n; ++i) {

            sum += Cube(i);
        }

        return sum;
    }

    private static void Example6() {

        int input = GetInt("input");

        System.out.printf("result = %d\n", SumOfCubes(input));
    }

    public static void RunExample() {

        int example = GetInt("example[1-6]");

        switch (example) {
            case 1:
                Example1();
                break;
            case 2:
                Example2();
                break;
            case 3:
                Example3();
                break;
            case 4:
                Example4();
                break;
            case 5:
                Example5();
                break;
            case 6:
                Example6();
                break;
            default:
                System.out.println("invalid example");
        }

    }

    public static void main(String... args) {
        scanner = new Scanner(System.in);

        while (true) {

            RunExample();

            scanner.nextLine();
        }

    }

}
