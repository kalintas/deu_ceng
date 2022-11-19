
using System;

// returns the minimum from parameters
static double Min(double lhs, double rhs) {

    if (lhs < rhs) {
        return lhs;
    }
    return rhs;
}

// returns !val
static double Factorial(double val) {

    double result = 1;

    for (double i = 2; i <= val; ++i) {
        result *= i;
    }

    return result;
}

// returns val^power
static double Pow(double val, int power) {

    double result = val;

    for (int i = 1; i < power; ++i) {
        result *= val;
    }

    return result;
}

static double GetNumber(string name, int min_value, int max_value) {
    Console.Write("{0} = ", name);

    int result = 0;
    
    // get input from user until it is in correct form
    while (!int.TryParse(Console.ReadLine(), out result) ||
        result < min_value || result > max_value) {
        Console.Write("Wrong input. Enter again = ");
    }

    return (double)result;
}


double x = GetNumber("x [2, 50]", 2, 50);
double y = GetNumber("y [25, 30]", 25, 30);

Console.Write("Enter the operator (* or +) = ");

// string? because Console.Readline() could return null
string? operator_type;

while ((operator_type = Console.ReadLine()) == null ||
    (operator_type != "*" && operator_type != "+")) {

    Console.Write("Wrong Input. Enter again = ");
}

// true if operator is '+'
bool operator_sum = operator_type == "+";

double result = 0.0;

for (int i = 0; i < 25; ++i) {

    double lhs_coefficient = (double)i * 3.0 + 2.0; // 2-5-8-11-14-...
    double rhs_coefficient = lhs_coefficient + 3.0; // 5-8-11-14-17-...

    double operation_result;

    if (operator_sum) {
        // operator is +
        operation_result = (lhs_coefficient * x) + (rhs_coefficient * x);
    } else {
        // operator is *
        operation_result = (lhs_coefficient * x) * (rhs_coefficient * x);
    }

    double numerator = Min(operation_result, Factorial(y - i));

    double denominator = 0.0;

    for (int t = i * 2 + 1; t <= i * 4 + 3; t += 2) {
        // t is 1,3 - 3,5,7 - 5,7,9,11 - 7,9,11,13,15 - ...
        denominator += Pow(t, i + 1);
    }

    double fraction = numerator / denominator;

    if (i % 2 == 0 && i > 0) {
        // when i is 2,4,6,8,10,...
        result -= fraction;
    } else {
        // when i is 0,1,3,5,7,...
        result += fraction;
    }
}

Console.WriteLine("result = {0}", result);
Console.ReadLine();
