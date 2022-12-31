using System;

class Lab11 {
    struct Clock {

        public int hour;
        public int minute;
    }

    struct Triangle {

        public double a;
        public double b;
        public double c;
    }

    struct Point {

        public double x;
        public double y;
    }

    struct Vec3 {

        public double a;
        public double b;
        public double c;
    }


    struct Address {
        public string text;
        public string city;
    }

    struct Date {
        public int day;
        public int month;
        public int year;
    }

    struct Doctor {

        public string name;
        public Address address;
        public Date birthdate;
    }

    static T GetInput<T>(string name) {

        Console.Write("{0}: ", name);
    
        return (T)Convert.ChangeType(Console.ReadLine(), typeof(T));
    }

    static Clock CalculateClockDifference(Clock lhs, Clock rhs)
    {

        int hour_diff = lhs.hour - rhs.hour;
        int minute_diff = lhs.minute - rhs.minute;

        int difference = Math.Abs(hour_diff) * 60 + Math.Abs(minute_diff);

        Clock result;

        result.hour = difference / 60;
        result.minute = difference % 60;

        return result;
    }

    static void PrintClock(Clock Clock) {

        if (Clock.hour > 0) {
            Console.Write("{0} hours and ", Clock.hour);
        }

        Console.WriteLine("{0} minutes", Clock.minute);
    }

    static Clock GetClock(string name) {
        Clock result;

        Console.Write("{0}: ", name);
        string line = Console.ReadLine();
        string[] numbers = line.Split(':');

        result.hour = Convert.ToInt32(numbers[0]);
        result.minute = Convert.ToInt32(numbers[1]);

        return result;
    }

    static void Example1() {
        Clock clock1 = GetClock("clock1");
        Clock clock2 = GetClock("clock2");

        Clock difference = CalculateClockDifference(clock1, clock2);

        PrintClock(difference);
    }

    static Triangle GetTriangle(string name) {

        Triangle result;

        result.a = GetInput<double>($"{name}.a");
        result.b = GetInput<double>($"{name}.b");
        result.c = GetInput<double>($"{name}.c");

        return result;
    }

    static bool IsTriangle(Triangle t) {
        // a + b > c      a + c > b     b + c > a   
        // a - b < c      a - c < b     b - c < a

        return t.a + t.b > t.c && t.a + t.c > t.b && t.b + t.c > t.a &&
            t.a - t.b < t.c && t.a - t.c < t.b && t.b - t.c < t.a;
    }

    static void Example2() {

        Triangle triangle = GetTriangle("triangle");

        if (IsTriangle(triangle)) {
            Console.WriteLine("It is a triangle");
        } else {
            Console.WriteLine("It is not a triangle");
        }
    }

    static Point GetPoint(string name) {

        Point result;

        result.x = GetInput<double>($"{name}.x");
        result.y = GetInput<double>($"{name}.y");

        return result;
    }

    static void Example3() {

        Point p1 = GetPoint("point1");
        Point p2 = GetPoint("point2");

        double m = (p2.y - p1.y) / (p2.x - p1.x);

        double c = p1.y - m * p1.x;

        Console.WriteLine("y = {0}x + {1}", m, c);
    }

    static Vec3 GetVec3(string name) {

        Vec3 result;

        result.a = GetInput<double>($"{name}.a");
        result.b = GetInput<double>($"{name}.b");
        result.c = GetInput<double>($"{name}.c");

        return result;
    }

    static Vec3 GetVecSquared(Vec3 vec) {

        Vec3 result;

        result.a = vec.a * vec.a;
        result.b = vec.b * vec.b;
        result.c = vec.c * vec.c;

        return result;
    }

    static Vec3 GetCrossProduct(Vec3 lhs, Vec3 rhs) {

        Vec3 result;

        result.a = lhs.b * rhs.c - lhs.c * rhs.b;
        result.b = lhs.c * rhs.a - lhs.a * rhs.c;
        result.c = lhs.a * rhs.b - lhs.b * rhs.a;

        return result;
    }

    static void Example4() {

        Vec3 vec1 = GetVec3("vec1");
        Vec3 vec2 = GetVec3("vec2");

        Vec3 result = GetCrossProduct(vec1, vec2);

        Console.WriteLine("result = ({0}, {1}, {2})", result.a, result.b, result.c);
    }

    static void Example5() {

        Vec3 velocity = GetVec3("velocity");
        double mass = GetInput<double>("mass");

        Vec3 vel2 = GetVecSquared(velocity);

        double energy = 0.5 * mass * (vel2.a + vel2.b + vel2.c);

        Console.WriteLine("kinetic energy = {0}", energy);
    }

    static Address GetAddress(string name) {

        Address result;
        result.text = GetInput<string>($"{name}.text");
        result.city = GetInput<string>($"{name}.city");

        return result;
    }

    static Date GetDate(string name) {

        Date result;
        result.day = GetInput<int>($"{name}.day");
        result.month = GetInput<int>($"{name}.month");
        result.year = GetInput<int>($"{name}.yar");

        return result;
    }

    static Doctor GetDoctor(string name) {

        Doctor result;

        result.name = GetInput<string>($"{name}.name");
        result.address = GetAddress($"{name}.address");
        result.birthdate = GetDate($"{name}.birthdate");

        return result;
    }

    static void Example6() {
        Doctor doctor = GetDoctor("doctor");
    }

    static void RunExample() {

        int example = GetInput<int>("example [1, 6]");

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
                throw new Exception("invalid example");
        }
    }

    static void Main(string[] args) {

        while (true) {

            try {
                RunExample();

            } catch (Exception e) {
                Console.WriteLine("Exception: {0}", e.Message);
            }

            Console.WriteLine();
        }
    }
}

