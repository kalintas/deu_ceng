using System;

class Lab5
{
    static T get_input<T>(string name) {
        Console.Write("{0} = ", name);

        return (T)Convert.ChangeType(Console.ReadLine(), typeof(T));
    }

    static double pow(double x, int power) {

        double result = 1;

        for (int i = 1; i <= power; ++i) {
            result *= x;
        }

        return result;
    }

    static void example1() {

        for (int i = 5; i <= 14; i += 3) {
            for (int t = 3; t < 8; ++t) {

                Console.WriteLine("{0} x {1} = {2}", i, t, i * t);
            }
        }
    }

    static void example2()
    {

        int s = get_input<int>("s");
        int n = get_input<int>("n");

        for (int i = 0; i < n; ++i)
        {

            for (int t = 0; t < n; ++t)
            {

                if (i == t)
                {
                    Console.Write("{0} ", s);
                }
                else
                {
                    Console.Write("0 ");
                }
            }

            Console.WriteLine();
        }
    }

    static void example3() {
        double x = get_input<double>("x");

        double result = x;
        Console.WriteLine("1th = {0}", x);

        for (int i = 2; i <= 20; ++i) {
            result += pow(x, i * 2 - 1) / (double)(i * 2 - 1);
            Console.WriteLine("{0}th = {1}", i, result);
        }
    }

    static void example4() {

        for (int i = 0; i < 5; ++i) {
            for (int t = 9 - i * 2; t > 0; --t) {

                Console.Write(t);
            }

            Console.WriteLine();
        }
    }

    static void example5() {

        double result = 1.0;
        double val = 1.0;

        for (int i = 1; i < 10; ++i) {

            val *= pow((double)(i * 2 - 1), 3) / pow((double)(i * 2), 3);

            double value = (double)(i * 4 + 1) * val;

            if (i % 2 == 0) {
                result += value;
            } else {
                result -= value;
            }
        }

        Console.WriteLine(result);
    }

    static void example6() {

        for (int i = 1; i <= 2; ++i) {

            Console.WriteLine(i);

            for (int t = 1; t <= 5; ++t) {

                Console.WriteLine(" {0}.{1}", i, t);

                for (int j = 1; j <= 4; ++j) {

                    Console.WriteLine("  {0}.{1}.{2}", i, t, j);
                }
            }
        }
    }

    static void example7() {

        string shapes = "ABCDEFGHIJKLMNOPQRS";

        for (int i = 0; i < 5; ++i) {

            for (int t = 0; t < 8 - i * 2; ++t) {
                Console.Write(' ');
            }

            for (int t = 0; t < 3 + i * 4; ++t) {
                Console.Write(shapes[t]);
            }

            for (int t = 0; t < 8 - i * 2; ++t)
            {
                Console.Write(' ');
            }

            Console.WriteLine();
        }

    }

    static void example8() {

        int input = get_input<int>("input (1, 5, 9, 13, 17, ...)");

        double result = Math.Sqrt((double)input);

        for (int i = input - 4; i >= 1; i -= 4) {

            result += i;
            result = Math.Sqrt(result);
        }

        Console.WriteLine(result);
    }

    static bool run_example() {

        int example = get_input<int>("example (1, 8) 0 for exiting");

        switch(example) {
            case 0:
                return true; 
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
                break;
        }

        return false;
    }

    static void Main(string[] args) {

        while (true) {

            try {
                if (run_example()) { break; }
            } catch (FormatException e) {
                Console.WriteLine("wrong input");
            }

            Console.WriteLine();
        }
    }
}

