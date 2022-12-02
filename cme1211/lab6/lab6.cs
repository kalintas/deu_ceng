using System;
using System.Collections.Generic;
class Lab6 {

    static T GetInput<T>(string name) {
        Console.Write("{0} = ", name);

        return (T)Convert.ChangeType(Console.ReadLine(), typeof(T));
    }
    static void PrintArr<T>(string name, T[] arr, int length) {

        Console.Write("{0} = ( ", name);
        for (int i = 0; i < length; ++i)
        {
            Console.Write("{0}, ", arr[i]);
        }
        Console.WriteLine(")");
    }

    static void PrintArr<T>(string name, T[] arr) {
        PrintArr<T>(name, arr, arr.Length);
    }

    static bool Contains<T>(T[] arr, T value) {

        for (int i = 0; i < arr.Length; ++i) {
            if (EqualityComparer<T>.Default.Equals(arr[i], value)) {
                return true;
            }
        }

        return false;
    }

    static void example1() {

        string[] names = { "ali", "ayşe", "ayşe", "sibel", "zeynep", "ayşe", "can", "zeynep" };

        PrintArr("names", names);

        string name = GetInput<string>("name");

        int count = 0;

        for (int i = 0; i < names.Length; ++i) {
            if (name == names[i]) {
                count += 1;
            }
        }

        Console.WriteLine("count = {0}", count);
    }

    static void example2() {
        string[] english = { "Apple", "Pear", "Cherry", "Banana", "Melon", "Apricot" };
        string[] turkish = { "Elma", "Armut", "Kiraz", "Muz", "Kavun", "Kayısı" };

        PrintArr("english", english);
        PrintArr("turkish", turkish);

        string input = GetInput<string>("input");

        for (int i = 0; i < english.Length; ++i) {
            if (input.ToUpper() == english[i].ToUpper()) {

                Console.WriteLine(turkish[i]);
                return;
            }
        }

        Console.WriteLine("Not defined.");
    }

    static void example3() {

        int[] numbers = { 4, 17, 3, 12, 1, 14, 15, 55, 9, 61 };

        PrintArr("numbers", numbers);

        for (int i = 0; i < numbers.Length; ++i) {
            
            if (numbers[i] % 3 == 0) {
                Console.Write("{0} ", numbers[i]);
            }
        }

        Console.WriteLine();
    }

    static void example4() {
        int[] numbers = { -1, 5, 6, 2, 9, -6, 4, 3, 8, 0 };

        PrintArr("numbers", numbers);

        int sum = 0;

        for (int i = 0; i < numbers.Length; ++i) {
            sum += numbers[i];
        }

        int average = sum / numbers.Length;

        int greater = 0;

        for (int i = 0; i < numbers.Length; ++i) {

            if (numbers[i] > average) {
                greater += 1;
            }
        }

        double percentage = ((double)greater / (double)numbers.Length) * 100.0;

        Console.WriteLine("percentage = %{0}", percentage);
    }

    static void example5() {
        string[] a = { "derya", "deniz", "nehir", "ırmak", "su", "yağmur" };

        PrintArr("a", a);

        for (int i = 0; i < a.Length; i += 2) {

            string buffer = a[i];
            a[i] = a[i + 1];
            a[i + 1] = buffer;
        }

        PrintArr("result", a);
    }

    static void example6() {
        int[] numbers = { 1, 7, 7, 12, 1, 5, 1, 1, 12, 12, 12, 7, 1, 1 };

        PrintArr("numbers", numbers);

        int[] unique = new int[numbers.Length];
        int unique_count = 0;

        for (int i = 0; i < numbers.Length; ++i) {

            if (!Contains(unique, numbers[i])) {
                unique_count += 1;
                unique[unique_count - 1] = numbers[i];
            }
        }

        PrintArr("unique values", unique, unique_count);
    }

    static bool IsSubArray<T>(T[] arr1, T[] arr2) {

        for (int i = 0; i < arr2.Length; ++i) {

            if (!Contains(arr1, arr2[i])) {
                return false;
            }
        }

        return true;
    }

    static void example7() {

        char[] arr1 = { 'a', 'b', 'c', 'd', 'e', 'f', 'g' };
        char[] arr2 = { 'b', 'e', 'g' };

        PrintArr("arr1", arr1);
        PrintArr("arr2", arr2);

        Console.WriteLine("result = {0}", IsSubArray(arr1, arr2));

        char[] arr3 = { 'a', 'b', 'c', 'd', 'e', 'f', 'g' };
        char[] arr4 = { 'b', 'e', 'g', 'k' };


        PrintArr("arr3", arr3);
        PrintArr("arr4", arr4);

        Console.WriteLine("result = {0}", IsSubArray(arr3, arr4));
    }

    static void example8() {
        int[] numbers = { 50, 51, 56, 53 };

        PrintArr("numbers", numbers);

        int sum = 0;

        for (int i = 0; i < numbers.Length; ++i) {
            sum += numbers[i];
        }

        double one_over_length = 1.0 / (double)numbers.Length;

        double average = (double)sum * one_over_length;

        double deviation_sum = 0.0;

        for (int i = 0; i < numbers.Length; ++i) {

            double difference = (double)numbers[i] - average;
            deviation_sum += difference * difference;
        }

        double standard_deviation = Math.Sqrt(deviation_sum * one_over_length);

        Console.WriteLine("result = {0}", standard_deviation);
    }

    static void example9() {
        int[] number1 = { 179, 534, 672, 898 };
        int[] number2 = { 211, 145, 070, 230 };
        int[] sum = { 0, 0, 0, 0 };

        PrintArr("number1", number1);
        PrintArr("number2", number2);

        for (int i = sum.Length - 1; i >= 0; --i) {

            int val = number1[i] + number2[i];

            sum[i] += val % 1000;

            if (i > 0) {
                sum[i - 1] += val / 1000;
            }
        }

        PrintArr("sum", sum);
    }

    static void RunExample(int example)
    {
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
            case 9:
                example9();
                break;
            default:
                throw new Exception();
        }
    }
    static void Main(string[] args) {


        while (true) {

            try {
                int example = GetInput<int>("example[1, 9] press 0 for exiting");

                if (example == 0) {
                    break;
                }

                RunExample(example);

            } catch (Exception e) {
                Console.WriteLine("Wrong input.");
            }

            Console.WriteLine();
        }
    }
}

