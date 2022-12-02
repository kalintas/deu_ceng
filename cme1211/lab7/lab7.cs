using System;

class Lab7 {

    static T GetInput<T>(string name) {

        Console.Write("{0}: ", name);

        return (T)Convert.ChangeType(Console.ReadLine(), typeof(T));
    }

    static bool ForEach<T>(T[,] arr, Func<T, int, int, bool> func) {

        for (int i = 0; i < arr.GetLength(0); ++i) {
            for (int t = 0; t < arr.GetLength(1); ++t) {

                if (!func(arr[i, t], i, t)) {
                    return false;
                }
            }
        }

        return true;
    }

    static void Example1() {


        int[,] arr = {
            {5, 4, 0, 2, 8, 0},
            {0, 5, 7, 1, 3, 5},
            {12, 3, 6, 0, 2, 0}
        };

        int count = 0;

        ForEach<int>(arr, (int val, int i, int t) => {
            if (val == 0) {
                count += 1;
            }

            return true;
        });

        Console.WriteLine("count: {0}", count);
    }

    
    static void Example2() {

        char[,] arr = { 
            { 'S', 'O', 'S' }, 
            { 'O', 'S', 'O' }, 
            { 'S', 'O', 'S' }
        };

        bool result = ForEach<char>(arr, (char c, int i, int t) => {

            c = char.ToLower(c);

            return c == 's' || c == 'o';
        });


        Console.WriteLine(result);
    }

    static void Example3() {

        int[,] arr = { 
            { 1, 0, 0, 0, 0 }, 
            { 0, 1, 0, 0, 0 }, 
            { 0, 0, -1, 0, 0 }, 
            { 0, 0, 0, -1, 0 }, 
            { 0, 0, 0, 0, 1 }
        };

        bool result = ForEach<int>(arr, (int val, int i, int t) => {

            if (i == t) {
                return val == 1 || val == -1;
            }

            return val == 0;
        });

        Console.WriteLine(result);
    }

    static void Example4() {

        int[,] arr = { 
            { 4, 0, 0, 0, 0 }, 
            { 0, 4, 0, 0, 0 }, 
            { 0, 0, 4, 0, 0 }, 
            { 0, 0, 0, 4, 0 }, 
            { 0, 0, 0, 0, 4 }
        };

        int scalar = arr[0, 0];

        bool result = ForEach<int>(arr, (int val, int i, int t) => {

            if (i == t) {

                bool is_equal = scalar == val;
                scalar = val;

                return is_equal;
            }

            return true;
        });

        Console.WriteLine(result);
    }

    static void Example5() {

        int[,] arr = { 
            { 0, 0, 0, 0, 1 }, 
            { 0, 0, 0, 1, 0 }, 
            { 0, 0, 1, 0, 0 }, 
            { 0, 1, 0, 0, 0 }, 
            { 1, 0, 0, 0, 0 }
        };

        int size = arr.GetLength(0);

        bool result = ForEach<int>(arr, (int val, int i, int t) => {

            if (i + t == size - 1) {
                return val == 1;
            }

            return val == 0;
        });

        Console.WriteLine(result);
    }
    
    static void Example6() {
        int[,] arr = {
            { 0, 2, -1},
            {-2, 0, -4},
            { 1, 4,  0}
        };

        bool result = ForEach<int>(arr, (int val, int i, int t) => {

            return val == -arr[t, i];
        });

        Console.WriteLine(result);
    }

    static void Example7() {

        int[,] arr = {
            {1, 0, 0, 0, 0},
            {7, 8, 0, 0, 0},
            {9, 1, 1, 0, 0},
            {9, 3, 9, 2, 0},
            {5, 6, 8, 1, 5}
        };

        bool result = ForEach<int>(arr, (int val, int i, int t) => {

            if (t > i) {
                return val == 0;
            }

            return val != 0;
        });

        Console.WriteLine(result);
    }

    static void Example8() {

        bool[,] arr = {
            {true, false, false, true, true},
            {false, false, false, true, false},
            {true, true, true, false, true},
            {false, true, false, false, true}
        };

        Console.Write("Output:");

        ForEach<bool>(arr, (bool val, int i, int t) => {

            if (t == 0) {
                Console.WriteLine();
            }

            if (val) {
                Console.Write('*');
            } else {
                Console.Write('-');
            }

            return true;
        });

        Console.WriteLine();
    }


    static void Main(string[] args) {

        while (true) {
            try {

                int example = GetInput<int>("example");

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
                    case 7:
                        Example7();
                        break;
                    case 8:
                        Example8();
                        break;
                    default:
                        throw new Exception("Enter in range: [1, 9]");
                }


            }
            catch (Exception e) {
                Console.WriteLine("Error: {0}", e.Message);
            };
        }

    }

}

