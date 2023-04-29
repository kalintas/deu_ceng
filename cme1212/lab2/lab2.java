import java.util.Scanner;

public class lab2 {

    private static class Point {
        private double x;
        private double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public void setX(double x) {
            this.x = x;
        }
        public void setY(double y) {
            this.y = y;
        }

        public double getX() {
            return this.x;
        }
        public double getY() {
            return this.y;
        }

        public void print() {
            System.out.printf("Point: (%f, %f)\n", this.x, this.y);
        }

        public static double getDistance(Point p1, Point p2) {
            return Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
        }

    }

    private static class Time {

        private int hour;
        private int minute;

        public Time(String time) {

            String[] values = time.split(":");
            hour = Integer.parseInt(values[0]);
            minute = Integer.parseInt(values[1]);
        }

        public void setHour(int hour) {
            this.hour = hour;
        }
        public void setMinute(int minute) {
            this.minute = minute;
        }

        public int getHour() {
            return this.hour;
        }
        public int getMinute() {
            return this.minute;
        }

        /**
         * Print in 12 hour format
         */
        public void convert() {
            System.out.printf("Time: %d:%d ", hour % 12, minute);

            if (hour >= 12) {
                System.out.println("p.m");
            } else {
                System.out.println("a.m");
            }
        }

    }

    private static class SportCenter {

        public static class Member {
            private String name;
            private String surname;
            private double height;
            private double weight;

            private void setName(String name) {
                this.name = name;
            }
            private void setSurname(String surname) {
                this.surname = surname;
            }
            private void setHeight(double height) {
                this.height = height;
            }
            private void setWeight(double weight) {
                this.weight = weight;
            }

            private String getName() {
                return this.name;
            }
            private String getSurname() {
                return this.surname;
            }
            private double getHeight() {
                return this.height;
            }
            private double getWeight() {
                return this.weight;
            }

            public void print() {
                System.out.printf("Member: %s %s %fm %fkg\n", this.name, this.surname, this.height, this.weight);
            }

            public double getBMI() {
                return this.weight / (this.height * this.height);
            }

            public String getWeightStatus() {

                double bmi = this.getBMI();

                if (bmi < 18.5) {
                    return "Thin";
                } else if (bmi < 25) {
                    return "Normal";
                } else if (bmi < 30) {
                    return "Fat";
                }

                return "Obese";
            }
        }

        String name;
        Member[] members;

        int memberCount = 0;
        int capacity;

        public SportCenter(int capacity) {
            this.capacity = capacity;
            members = new Member[capacity];
        }

        public void addMember(Member member) {

            this.members[this.memberCount] = member;
            this.memberCount += 1;
        }

        public void searchMember(String name, String surname) {

            for (int i = 0; i < this.memberCount; ++i) {

                if (this.members[i].getName() == name && this.members[i].getSurname() == surname) {
                    this.members[i].print();
                    return;
                }
            }

            System.out.println("Cannot found member");
        }

        public void printAllMembers() {

            for (int i = 0; i < this.memberCount; ++i) {
                this.members[i].print();
            }
        }
    }

    private static class Particle {

        private double px;
        private double py;
        private double pz;
        private double mass;

        public Particle() {}

        public void setPx(double px) {
            this.px = px;
        }
        public void setPy(double py) {
            this.py = py;
        }
        public void setPz(double pz) {
            this.pz = pz;
        }
        public void setMass(double mass) {
            this.mass = mass;
        }

        public double getPx() {
            return this.px;
        }
        public double getPy() {
            return this.py;
        }
        public double getPz() {
            return this.pz;
        }
        public double getMass() {
            return this.mass;
        }

        public double getKineticEnergy() {
            double vx = px / mass;
            double vy = py / mass;
            double vz = pz / mass;

            return 0.5 * mass * (vx * vx + vy * vy + vz * vz);
        }

    }

    private static void Example1() {
        Point p1 = new Point(2, 1);
        Point p2 = new Point(6, 4);

        p2.setX(8);
        p2.setY(5);

        p1.print();
        p2.print();

        double distance = Point.getDistance(p1, p2);

        System.out.printf("Distance = %f\n", distance);
    }

    private static void Example2() {

        Time time1 = new Time("14:50");
        time1.convert();
        Time time2 = new Time("01:40");
        time2.convert();

    }

    private static void Example3() {

        SportCenter sportCenter = new SportCenter(10);

        SportCenter.Member taner = new SportCenter.Member();
        taner.setName("Taner Tolga");
        taner.setSurname("Tarlacı");
        taner.setHeight(1.70);
        taner.setWeight(65);

        SportCenter.Member yasin = new SportCenter.Member();
        yasin.setName("Yasin");
        yasin.setSurname("Obuz");
        yasin.setHeight(1.83);
        yasin.setWeight(82);

        sportCenter.addMember(taner);
        sportCenter.addMember(yasin);
        sportCenter.printAllMembers();
        System.out.printf("Taner weight status: %s\n", taner.getWeightStatus());

        sportCenter.searchMember("Yasin", "Obuz");
    }

    private static void Example4() {

        Particle particle = new Particle();
        particle.setPx(12);
        particle.setPy(6);
        particle.setPz(3);

        particle.setMass(50);

        System.out.printf("Kinetic energy = %f", particle.getKineticEnergy());
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.println("Run an example[1,4]");
            int example = scanner.nextInt();

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
                default:
                    System.out.println("Invalid example");
                    break;
            }

            System.out.println();
        }
    }

}
