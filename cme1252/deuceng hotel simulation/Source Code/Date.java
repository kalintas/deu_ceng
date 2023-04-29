import java.util.regex.Pattern;

public class Date {

    private int day;
    private int month;
    private int year;

    /**
     * Constructor to create Date class
     * @param date must be in the dd.mm.yyyy format
     */
    public Date(String date) {

        String[] splitDate = date.split(Pattern.quote("."));
        this.day = Integer.parseInt(splitDate[0]);
        this.month = Integer.parseInt(splitDate[1]);
        this.year = Integer.parseInt(splitDate[2]);
    }

    /**
     * Constructor to create Date class
     */
    public Date(int day, int month, int year) {

        this.day = day;
        this.month = month;
        this.year = year;
    }

    /**
     * Copy Constructor to create Date class
     */
    public Date(Date date) {

        this.day = date.day;
        this.month = date.month;
        this.year = date.year;
    }

    // Getters

    public int getDay() {
        return this.day;
    }

    public int getMonth() {
        return this.month;
    }

    public int getYear() {
        return this.year;
    }

    public String getDate() {
        return String.format("%d.%d.%d", this.day, this.month, this.year);
    }

    /**
     * Returns true if this instance of date is larger than parameter date
     * @param date date to be compared
     */
    public boolean isAfter(Date date) {

        if (this.year > date.year) {
            return true;
        } else if (this.year == date.year) {

            if (this.month > date.month) {
                return true;
            } else if (this.month == date.month) {

                return this.day > date.day;
            }
        }

        return false;
    }

    /**
     * Returns true if this instance of date is equal to the parameter date
     * @param date date to be compared
     */
    public boolean isEqual(Date date) {
        return this.year == date.year && this.month == date.month && this.day == date.day;
    }

    /**
     * Returns true if this instance of date is NOT equal to the parameter date
     * @param date date to be compared
     */
    public boolean isNotEqual(Date date) {
        return !this.isEqual(date);
    }

    /**
     * Returns true if this instance of date is less than the parameter date
     * @param date date to be compared
     */
    public boolean isBefore(Date date) {
        return date.isAfter(this);
    }

    /**
     * Returns true if this instance of date is less than or equal to the parameter date
     * @param date date to be compared
     */
    public boolean isBeforeOrEqual(Date date) {
        return this.isBefore(date) || this.isEqual(date);
    }

    /**
     * Returns true if this instance of date is larger than or equal to the parameter date
     * @param date date to be compared
     */
    public boolean isAfterOrEqual(Date date) {
        return this.isAfter(date) || this.isEqual(date);
    }

    /**
     * Adds one day to current date
     * Example: 10.4.2024 -> 11.4.2024
     */
    public void addOneDay() {

        int daysInMonth = this.getDaysInMonth();

        if (this.day >= daysInMonth) {

            this.day = 1;

            if (this.month == 12) {

                this.month = 1;
                this.year += 1;
            } else {
                this.month += 1;
            }
        } else {
            this.day += 1;
        }
    }

    /**
     * Returns true if parameter year is leap year
     */
    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }

    /**
     * Returns the month length of the parameter month
     *
     * @param year is required to get february months length
     */
    public static int getDaysInMonth(int month, int year) {

        switch (month) {
            case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                return 31;
            case 4: case 6: case 9: case 11:
                return 30;
            case 2:
                if (Date.isLeapYear(year)) {
                    return 29;
                }
                return 28;
            default:
                break;
        }

        System.err.printf("Wrong parameter month: %d\n", month);
        return -1;
    }

    /**
     * Returns the length of the month at this.month
     */
    public int getDaysInMonth() {
        return Date.getDaysInMonth(this.month, this.year);
    }

    /**
     * Returns the difference of Date start and Date end parameters in day time unit
     * @return Difference between start and end in days
     */
    public static int getDifference(Date start, Date end) {

        if (start.isAfter(end)) {
            return 0;
        }

        int days = 0;
        int currentYear = start.year;

        for (int month = start.month; end.year > currentYear || (end.year == currentYear && month < end.month); ++month) {

            int day = getDaysInMonth(month, currentYear);

            days += day;

            if (month == 12) {

                currentYear += 1;
                month = 0;
            }
        }

        days += end.day - start.day;

        return days;
    }

    /**
     * Returns the minimum date between given dates.
     * Date.min(lhs, rhs).isOrEqualBefore(lhs) == true
     * Date.min(lhs, rhs).isOrEqualBefore(rhs) == true
     */
    public static Date min(Date lhs, Date rhs) {

        if (lhs.isBefore(rhs)) {
            return lhs;
        }

        return rhs;
    }

    /**
     * Returns the maximum date between given dates.
     * Date.max(lhs, rhs).isOrEqualAfter(lhs) == true
     * Date.max(lhs, rhs).isOrEqualAfter(rhs) == true
     */
    public static Date max(Date lhs, Date rhs) {

        if (lhs.isAfter(rhs)) {
            return lhs;
        }

        return rhs;
    }

}
