using System;

class Lab10 {

    // procedure
    static void PrintShape() {

        for (int i = 0; i < 5; ++i) {

            for (int t = 1; t <= 6; ++t) {
                Console.Write(t);
            }
            Console.WriteLine();
        }
    }

    static int GetDigitSum(int number) {

        int sum = 0;

        for (int i = 1; number / i != 0; i *= 10) {
            sum += (number / i) % 10;
        }

        return sum;
    }

    // function
    static bool IsMagicNumber(int number) {

        return GetDigitSum(number) == 10;
    }

    static char GetComplement(char nucleotide) {

        switch (nucleotide) {
            case 'A':
                return 'T';
            case 'T':
                return 'A';
            case 'G':
                return 'C';
            case 'C':
                return 'G';
            default:
                throw new Exception();
        }
    }

    // function
    static char[] GetDNAComplement(char[] dna) {

        char[] complement = new char[dna.Length];

        for (int i = 0; i < dna.Length; ++i) {

            complement[i] = GetComplement(dna[i]);
        }

        return complement;
    }

    // procedure 
    static void PrintArrayToInside(int[] array) {

        for (int i = 0; i < array.Length / 2; ++i) {

            Console.Write("{0} {1} ", array[i], array[array.Length - i - 1]);
        }

        if (array.Length % 2 == 1) {
            Console.Write(array[array.Length / 2]);
        }

        Console.WriteLine();
    }

    // function
    static bool IsHalfSumsEqual(int[] array) {

        int sum1 = 0;
        int sum2 = 0;

        for (int i = 0; i < array.Length / 2; ++i) {
            sum1 += array[i];
            sum2 += array[array.Length - i - 1];
        }

        return sum1 == sum2;
    }

    // function
    static double Power(double value, double power) {

        double result = 1.0;

        for (double i = 1; i <= power; ++i) {
            result *= value;
        }

        return result;
    }

    // function
    static double Factorial(double value) {

        double result = 1.0;

        for (double i = 2.0; i <= value; i += 1.0) {
            result *= i;
        }

        return result;
    }

    // function
    static double CalculateFormula(double x, int power, double factorial) {

        return Power(x, power) / Factorial(factorial);
    }

    // function
    static double CalculateSin(double x) {

        double result = 0.0;

        for (int i = 0; i <= 4; ++i) {

            double val = CalculateFormula(x, i * 2 + 1, i * 2 + 1);

            if (i % 2 == 0) {
                result += val;
            } else {
                result -= val;
            }
        }

        return result;
    }

    static double GetAverage(int[] array) {
        int sum = 0;

        for (int i = 0; i < array.Length; ++i) {
            sum += array[i];
        }

        return (double)sum / (double)array.Length;
    }

    // function
    static double GetStandardDeviation(int[] array) {

        double average = GetAverage(array);

        double sum = 0.0;

        for (int i = 0; i < array.Length; ++i) {

            double val = array[i] - average;

            sum += val * val;
        }

        return Math.Sqrt(sum / (double)array.Length);
    }

    static void Main(string[] args) {

        Console.WriteLine("Example 1:");
        PrintShape();
        Console.WriteLine();

        Console.WriteLine("Example 2:");
        Console.WriteLine(IsMagicNumber(64));
        Console.WriteLine(IsMagicNumber(28));
        Console.WriteLine(IsMagicNumber(17));
        Console.WriteLine();

        Console.WriteLine("Example 3:");
        Console.WriteLine(new string(GetDNAComplement("AGTAAAC".ToCharArray())));
        Console.WriteLine();

        Console.WriteLine("Example 4:");
        PrintArrayToInside(new int[] { 1, 2, 3, 4, 5, 6 } );
        Console.WriteLine();

        Console.WriteLine("Example 5:");
        Console.WriteLine(IsHalfSumsEqual(new int[] { 2, 4, 16, 3, 7, 12 }));
        Console.WriteLine(IsHalfSumsEqual(new int[] { 2, 4, 6, 8, 2, 7, 9, 3, 7, 12 }));
        Console.WriteLine();

        Console.WriteLine("Example 6:");
        Console.WriteLine(CalculateSin(Math.PI / 2));
        Console.WriteLine();

        Console.WriteLine("Example 7:");
        Console.WriteLine(GetStandardDeviation(new int[] { 3, 12, 9, 4, 2 }));
        Console.WriteLine();

        Console.ReadLine();
    }
}
