
using System;

class Lab2 {

    static T get_input<T>(string name) {
        Console.Write(name + " = ");

        return (T)Convert.ChangeType(Console.ReadLine(), typeof(T));
    }
    
    static void example1() {
        double x = get_input<double>("x");
        Console.WriteLine("|x| = " + Math.Abs(x) + "  x^1/4 = " + Math.Pow(x, 1.0 / 4.0) + "  e^x = " + Math.Exp(x));
    }

    static void example2() {

        double x1 = get_input<double>("x1");
        double y1 = get_input<double>("y1");

        double x2 = get_input<double>("x2");
        double y2 = get_input<double>("y2");

        double distance = (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
        distance = Math.Sqrt(distance);

        Console.WriteLine("distance = " + distance);
    }

    static void example3() {
        Random rand = new Random();

        Console.WriteLine(rand.Next(1, 50) + " " + rand.Next(1, 50) + " " + rand.Next(1, 50) + " " + rand.Next(1, 50) + " " + rand.Next(1, 50) + " " + rand.Next(1, 50));
    }

    static void example4() {
        double r = 3.0;
        double ratio = ((360.0 - 40.0) / 360.0);
        double area = ratio * Math.PI * r * r;
        double perimeter = (2.0 * Math.PI * r * r * ratio) + r * 2.0;

        Console.WriteLine("area = " + area + " perimeter = " + perimeter);
    }

    static void example5() {
        double degree = get_input<double>("degree");
        double radian = (degree / 360.0) * Math.PI * 2.0;
        double revolution = degree / 360.0;
        double sign = degree / 30.0;

        Console.WriteLine("radian = " + radian + " revolution = " + revolution + " sign = " + sign);
    }

    static void example6() {
        int integer = get_input<int>("integer");
        if (integer >= 100 || integer < 0) {
            throw new FormatException();
        }

        int sum_of_digits = (integer / 10) + (integer % 10);
        Console.WriteLine("sum of digits = " + sum_of_digits);
    }

    static void example7() {
        double a = get_input<double>("a");
        double b = get_input<double>("b");
        double x = get_input<double>("x");

        double result = 2.0 * Math.Pow(a * x + b, 3.0 / 2.0) / (3.0 * a);

        Console.WriteLine("result = " + result);
    }

    static void run_example() {
        Console.WriteLine("Select example 1-7");
        int example = get_input<int>("example");

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
            default:
                throw new FormatException();
        }
    }

    static void Main(string[] args) {

        while (true) {
            try {

                run_example();
            }
            catch (FormatException e) {
                Console.WriteLine("wrong input");
            }

        }
    }
}
