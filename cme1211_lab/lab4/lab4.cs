using System;

class Lab4 {

    static int get_input(string name) {
        Console.Write(name + " = ");
        return Convert.ToInt32(Console.ReadLine());
    }
    
    static int get_input() {
        return Convert.ToInt32(Console.ReadLine());
    }

    static string get_polygon_name(int sides) {
        switch (sides) {
            case 5:
                return "pentagon";
            case 6:
                return "hexagon";
            case 7:
                return "heptagon";
            case 8:
                return "octagon";
            default:
                return "not defined";
        }
    }

    static void example1() {
        int sides = get_input("number of sides");

        Console.WriteLine(get_polygon_name(sides));
    }

    static void example2() {
        for (int i = 3; i <= 87; i += 7) {
            Console.Write(i + " ");
        }
    }

    static void example3() {
        int i = 3;
        while (i <= 87) {
            Console.Write(i + " ");
            i += 7;
        }
    }

    static void example4() {
        
        for (int i = 0; i < 24; ++i) {

            string str;
            int hour = i % 12;

            if (hour == 0) {
                hour = 12;

                if (i == 0) {
                    str = " midnight";
                } else {
                    str = " noon";
                }
            } else {
                if (i < 12) {
                    str = "am";
                }
                else {
                    str = "pm";
                }
            }

            Console.WriteLine(hour + str);
        }

    }

    static void example5() {
        int n = get_input("n");
        double sum = 1.0;
        int i = 1;

        while (i < n) {
            sum += 1.0 / ((double)n);
            i += 1;
        }

        Console.WriteLine("result = " + sum);
    }

    static void example6() {

        Random rng = new Random();
        int tries = 0;
        int number;

        do
        {
            number = rng.Next(1, 7);
            tries += 1;

            Console.WriteLine(tries + ". try    " + number);

        } while (number != 6);

        Console.WriteLine("It came up after " + tries + " tries");
    }

    static void example7() {
        int n = get_input("n");

        if (n == 0) {
            return;
        }

        int smallest_odd = get_input();

        for (int i = 1; i < n; ++i) {

            int input = get_input();
            
            if (smallest_odd % 2 == 0 || (input % 2 == 1 && input < smallest_odd)) {
                smallest_odd = input;
            }
        }

        if (smallest_odd % 2 == 0) {
            Console.WriteLine("There were no odd number");
        } else {
            Console.WriteLine("smallest odd = " + smallest_odd);
        }
    }

    static string get_type_of_digit(int digit) {
        switch(digit) {
            case 1:
            case 4:
            case 9:
                return "Perfect Square";
            case 2:
            case 6:
            case 8:
                return "Even number";
            case 3:
            case 5:
            case 7:
                return "Prime number";
            default:
                return "Not defined";
        }
    }

    static void example8() {
        int input = get_input("input");

        Console.WriteLine(get_type_of_digit(input));
    }

    static void example9() {

        for (int t = 1; t <= 10; ++t) {

            double result = 0.12 * (double)(t * t * t * t) + 12.0 * (double)(t * t * t) - 380.0 * (double)(t * t) + 4100.0 * (double)(t) + 220.0;

            Console.WriteLine("Height at the " + t + "th hour = " + result);

        }
    }

    static void example10() {

        Random rng = new Random();

        int random_number = rng.Next(1, 100);

        int guess = get_input("guess");

        int attempt = 1;

        while (guess != random_number) {

            if (guess > random_number) {
                Console.WriteLine("Please enter a lower number");
            } else {
                Console.WriteLine("Please enter a higher number");
            }

            guess = get_input("guess");
            attempt += 1;
        }

        Console.WriteLine("You guessed the number after " + attempt + " attempts");
    }

    static void example11() {

        int m = get_input("m");

        double result = 1.0;

        for (int i = 2; i <= 2 * m; ++i) {
            if (i % 2 == 0) {
                // even
                result *= 1.0 / (double)i;
            } else {
                // odd
                result *= (double)i;
            }
        }

        result *= Math.PI / 2.0;

        Console.WriteLine("result = " + result);
    }

    static void run_example(int example) {

        switch (example) {
            case 1:
                example1();
                break;
            case 2:
                example2();
                break;
            case 3:
                example3();
                break;
            case 4:
                example4();
                break;
            case 5:
                example5();
                break;
            case 6:
                example6();
                break;
            case 7:
                example7();
                break;
            case 8:
                example8();
                break;
            case 9:
                example9();
                break;
            case 10:
                example10();
                break;
            case 11:
                example11();
                break;
            default:
                break;
        }

    }

    static void Main(string[] args) {

        while (true) {

            try {
                int example = get_input("example");

                run_example(example);
            } catch (FormatException e) {
                Console.WriteLine("Wrong input");
            }

            Console.Write('\n');
        }
    }
}

