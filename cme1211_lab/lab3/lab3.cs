using System;

class Lab3 {

    static T get_input<T>(string name) {
        Console.Write(name + " = ");
        return (T)Convert.ChangeType(Console.ReadLine().Replace('.', ','), typeof(T));
    }

    static void example1() {

        int a = get_input<int>("a");
        int b = get_input<int>("b");
            
        if (a == b) {
            Console.WriteLine("Yes");
        } else {
            Console.WriteLine("No");
        }
    }

    static void example2() {

        int input = get_input<int>("input");

        if (input % 5 == 0) {
            Console.WriteLine("True");
        } else {
            Console.WriteLine("False");
        }
    }

    static void example3() {
        int hour = get_input<int>("hour");
        int minutes = get_input<int>("minutes");

        Console.Write((hour % 12) + ":" + minutes);

        if (hour >= 12) {
            Console.WriteLine(" PM");
        } else {
            Console.WriteLine(" AM");
        }
    }

    static void example4() {
        double height = get_input<double>("height in meters");
        double weight = get_input<double>("weight in kilograms");

        double bmi = weight / (height * height);

        Console.Write("BMI = " + bmi + "\nYou are: ");

        if (bmi >= 30.0) {
            Console.WriteLine("OBESE");
        } else if (bmi >= 25.0) {
            Console.WriteLine("FAT");
        } else if (bmi >= 18.5) {
            Console.WriteLine("NORMAL");
        } else {
            Console.WriteLine("THIN");
        }
    }

    static void example5() {
        int m = get_input<int>("m");
        int n = get_input<int>("n");

        if ((m + n) % 2 == 0) {
            // m + n is even
            Console.WriteLine((double)(2 * m) / (double)(m * m - n * n));
        } else {
            // m + n is odd
            Console.WriteLine("0");
        }
    }

    static void example6() {
        int a = get_input<int>("a");
        int b = get_input<int>("b");
        int c = get_input<int>("c");
        
        if (a == b) {
            if (b == c) {
                Console.WriteLine("1 distinct value");
            } else {
                Console.WriteLine("2 distinct value");
            }
        } else {
            if (b == c || a == c) {
                Console.WriteLine("2 distinct value");
            } else {
                Console.WriteLine("3 distinct value");
            }
        }

    }

    static string get_season(int month, int day)
    {
        switch (month)
        {
            case 1:
            case 2:
                return "Winter";
            case 3:
                if (day > 20)
                {
                    return "Spring";
                } else {
                    return "Winter";
                }
            case 4:
            case 5:
                return "Spring";
            case 6:
                if (day > 20)
                {
                    return "Summer";
                } else {
                    return "Spring";
                }
            case 7:
            case 8:
                return "Summer";
            case 9:
                if (day > 22) {
                    return "Fall";
                } else {
                    return "Summer";
                }
            case 10:
            case 11:
                return "Fall";
            case 12:
                if (day > 21) {
                    return "Winter";
                } else {
                    return "Fall";
                }
            default:
                return "";
        }
    }

    static void example7() {
        int month = get_input<int>("month");
        int day = get_input<int>("day");

        Console.WriteLine("season = " + get_season(month, day));
    }

    static void example8() {
        double velocity = get_input<double>("velocity");
        double viscosity = get_input<double>("viscosity");
        double density = get_input<double>("density");
        double diameter = get_input<double>("diameter");

        double reynolds_number = (diameter * velocity * density) / viscosity;

        Console.WriteLine("Reynold's number = " + reynolds_number);

        if (reynolds_number < 2000.0) {
            Console.WriteLine("laminar flow");
        } else if (reynolds_number < 4000.0) {
            Console.WriteLine("transient flow");
        } else {
            Console.WriteLine("turbulent flow");
        }
    }

    static void run_example() {
        int example = get_input<int>("example [1, 8]");

        switch (example)
        {
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
            default:
                throw new FormatException();
        }
    }

    static void Main(string[] args) {

        while (true) {

            try {
                run_example();
            } catch (FormatException e) {
                Console.WriteLine("wrong input");
            }

            Console.Write('\n');
        }
    }
}

