
public class Hotel {

    // Constants
    private static final int MAX_ROOMS = 30;
    private static final int MAX_STAFFS = 50;

    // 10,000 TL per month constant expense
    private static final int CONSTANT_EXPENSE = 10000;

    // Variables
    private String name;
    private Date foundationDate;
    private Address address;
    private PhoneNumber phoneNumber;
    private int starCount;

    private Room[] rooms = new Room[MAX_ROOMS];
    private int roomCount = 0;

    private Staff[] staffs = new Staff[MAX_STAFFS];
    private int staffCount = 0;

    private Customer[] customers = new Customer[MAX_ROOMS];
    private int customerCount = 0;

    private Reservation[] reservations = new Reservation[MAX_ROOMS];
    private int reservationCount = 0;

    /**
     * Executes the parameter command.
     * @param command the format of a command is: command-name;command-parameters...
     *                For example: "addRoom;number-of-rooms;type;aircondition;balcony;price"
     *
     */
    public void executeCommand(String command) {

        // Check to see if the command string is all whitespace
        if (command.isBlank()) {
            // Skip the empty lines
            return;
        }

        String[] words = command.split(";");

        switch (words[0]) {
            case "addRoom":

                int count = Integer.parseInt(words[1]);

                Room[] newRooms = new Room[count];

                for (int i = 0; i < count; ++i) {
                    newRooms[i] = new Room(words);
                }

                this.addRoom(newRooms);

                break;
            case "addEmployee":

                Staff staff = new Staff(words);
                this.addEmployee(staff);

                break;
            case "addCustomer":

                Customer customer = new Customer(words);
                this.addCustomer(customer);

                break;
            case "addReservation":

                int customerId = Integer.parseInt(words[1]);

                String customerName = "";

                for (int i = 0; i < this.customerCount; ++i) {

                    if (this.customers[i].getId() == customerId) {
                        customerName = this.customers[i].getName();
                    }
                }

                // Cannot find customer with the customerId.
                // Ignore the reservation
                if (customerName.isEmpty()) {
                    break;
                }

                Reservation reservation = new Reservation(customerId, customerName, words);
                this.addReservation(reservation);

                break;
            case "deleteEmployee":

                int employeeId = Integer.parseInt(words[1]);
                this.deleteEmployee(employeeId);

                break;
            case "listRooms":
                this.listRooms();
                break;
            case "listEmployees":
                this.listEmployees();
                break;
            case "listCustomers":
                this.listCustomers();
                break;
            case "listReservations":
                this.listReservations();
                break;
            case "searchCustomer":

                String pattern = words[1];
                this.searchCustomer(pattern);

                break;
            case "statistics":

                int year;

                if (words.length == 1) {
                    // Default year is 2024
                    year = 2024;
                } else {
                    year = Integer.parseInt(words[1]);
                }

                this.statistics(year);
                break;
            case "simulation":
            {
                Date startDate = new Date(words[1]);
                Date endDate = new Date(words[2]);

                this.simulation(startDate, endDate);

                break;
            }
            case "searchRoom":
            {
                Date startDate = new Date(words[1]);
                Date endDate = new Date(words[2]);

                this.searchRoom(startDate, endDate);
                break;
            }
            default:

                System.out.printf("Incorrect input: %s\n", command);
                break;
        }

    }
    /**
     * Searches customer with the pattern parameter.
     * Prints the customer names that correspond to the pattern.
     * @param pattern can contain only one star (*) symbol,
     *                or one-or-more question mark (?) symbol,
     *                but not both of them. For example: Zey* or Zey???.
     */
    public void searchCustomer(String pattern) {

        Customer[] matchedCustomers = new Customer[this.customerCount];
        int matchedCustomerCount = 0;

        if (pattern.contains("*")) {

            if (pattern.charAt(pattern.length() - 1) == '*') {
                // Last character is '*'. Eg: Zey*
                if (pattern.charAt(pattern.length() - 1) == '*') {

                    // Get the name from the pattern: Zey
                    String name = pattern.substring(0, pattern.length() - 1);

                    // Search the name
                    for (int i = 0 ; i < this.customerCount ; ++i) {
                        if (this.customers[i].getName().startsWith(name)) {
                            matchedCustomers[matchedCustomerCount] = this.customers[i];
                            matchedCustomerCount += 1;
                        }
                    }
                }
            }
            else if (pattern.charAt(0) == '*'){
                for (int i = 0 ; i < this.customerCount ; ++i) {

                    // Get the name from pattern
                    String name  = pattern.substring(1);

                    String[] customerName = customers[i].getName().split(" ");
                    if (customerName[0].endsWith(name)) {
                        matchedCustomers[matchedCustomerCount] = this.customers[i];
                        matchedCustomerCount += 1;
                    }
                }
            }
            else {
                String[] patternParts = pattern.split("\\*");
                for (int i = 0 ; i < this.customerCount ; ++i) {

                    boolean allPartExist = false;

                    String[] customerName = customers[i].getName().split(" ");

                    if(customerName[0].startsWith(patternParts[0]) && customerName[0].endsWith(patternParts[1])){
                        allPartExist = true;
                    }

                    if (allPartExist) {
                        matchedCustomers[matchedCustomerCount] = this.customers[i];
                        matchedCustomerCount += 1;
                    }
                }
            }
        }
        else {

            for (int i = 0; i < this.customerCount; ++i) {

                String name = this.customers[i].getName();

                boolean matches = true;

                for (int t = 0; t < name.length() && t < pattern.length(); ++t) {

                    if (name.charAt(t) != pattern.charAt(t) && pattern.charAt(t) != '?') {
                        matches = false;
                        break;
                    }
                }

                if (matches) {
                    matchedCustomers[matchedCustomerCount] = this.customers[i];
                    matchedCustomerCount += 1;
                }
            }
        }

        Hotel.listCustomers(matchedCustomers, matchedCustomerCount);
    }

    /**
     * Deletes the employee with the id equal to parameter employeeId.
     * If employeeId does not exist function will print an error message.
     * @param employeeId id of an employee to be deleted
     */
    public void deleteEmployee(int employeeId){

        int index = -1;

        // Search for the employee with id
        for (int i = 0; i < this.staffCount; ++i) {
            if (this.staffs[i].getId() == employeeId) {
                index = i;
            }
        }

        // employeeId doesnt exists
        if (index == - 1) {
            System.out.printf("Employee id not found: %d\n", employeeId);
            return;
        }

        // Overwrite the deleted employee
        for (int i = index; i < this.staffCount - 1; ++i) {
            this.staffs[i] = this.staffs[i + 1];
        }

        this.staffCount -= 1;
    }

    /**
     * Adds new rooms to the hotel.
     */
    public void addRoom(Room[] newRooms) {

        for (int i = 0; i < newRooms.length; ++i) {
            this.rooms[this.roomCount] = newRooms[i];
            this.roomCount += 1;
        }
    }

    /**
     * Adds a new employee to the hotel.
     */
    public void addEmployee(Staff staff) {

        this.staffs[this.staffCount] = staff;
        this.staffCount += 1;
    }

    /**
     * Adds a new customer to the hotel
     */
    public void addCustomer(Customer customer) {

        this.customers[this.customerCount] = customer;
        this.customerCount += 1;
    }

    /**
     * Tries to add a new reservation.
     * If the reservation is not available it will ignore the reservation.
     */
    public void addReservation(Reservation reservation) {

        boolean available = true;

        for (int i = 0; i < this.reservationCount; ++i) {

            // Check if to see the rooms are same
            if (this.reservations[i].isBooked(reservation)) {
                available = false;
                break;
            }
        }

        // Add the reservation if the room is available
        if (available) {
            this.reservations[this.reservationCount] = reservation;
            this.reservationCount += 1;
        }
    }

    /**
     * Lists all the rooms in the hotel
     */
    public void listRooms() {

        for (int i = 0; i < this.roomCount; ++i) {
            this.rooms[i].print();
        }
    }

    /**
     * Lists all the employees in the hotel
     */
    public void listEmployees() {

        int maxNameLength = this.staffs[0].getName().length();

        for (int i = 1; i < this.staffCount; ++i) {

            int length = this.staffs[i].getName().length();

            if (length > maxNameLength) {
                maxNameLength = length;
            }
        }

        for (int i = 0; i < this.staffCount; ++i) {
            this.staffs[i].print(maxNameLength);
        }
    }

    /**
     * Lists all the customers from the given parameters
     */
    public static void listCustomers(Customer[] customers, int customerCount) {

        if (customerCount == 0) {
            return;
        }

        int maxNameLength = customers[0].getName().length();

        for (int i = 0; i < customerCount; ++i) {

            int length = customers[i].getName().length();

            if (length > maxNameLength) {
                maxNameLength = length;
            }
        }

        for (int i = 0; i < customerCount; ++i) {
            customers[i].print(maxNameLength);
        }
    }

    public void listCustomers() {
        Hotel.listCustomers(this.customers, this.customerCount);
    }

    /**
     * Lists all the reservations in the hotel
     */
    public void listReservations() {

        int maxNameLength = this.reservations[0].getCustomerName().length();

        for (int i = 0; i < this.reservationCount; ++i) {

            int length = this.reservations[i].getCustomerName().length();

            if (length > maxNameLength) {
                maxNameLength = length;
            }

        }

        for (int i = 0; i < this.reservationCount; ++i) {
            this.reservations[i].print(maxNameLength);
        }
    }

    /**
     * Searches and prints the empty rooms between the parameter startDate and endDate.
     * */
    public void searchRoom(Date startDate, Date endDate) {

        // Ids of the rooms that are booked within the specified dates
        int[] bookedRoomIds = new int[this.reservationCount];
        int bookedRoomCount = 0;

        // Go through all the reservations and store the booked rooms ids
        for (int i = 0; i < this.reservationCount; ++i) {

            if(this.reservations[i].isBooked(startDate, endDate)) {

                bookedRoomIds[bookedRoomCount] = this.reservations[i].getRoomId();
                bookedRoomCount += 1;
            }
        }

        // Print all the available rooms
        for (int i = 0; i < this.roomCount; ++i) {

            int roomId = this.rooms[i].getId();
            boolean booked = false;

            for (int t = 0; t < bookedRoomCount; ++t) {

                if (roomId == bookedRoomIds[t]) {
                    booked = true;
                    break;
                }
            }

            if (!booked) {
                this.rooms[i].print();
            }
        }

    }

    /**
     * Returns the room object with the specified id parameter
     * @throws IllegalArgumentException if there is no object with parameter id
     * @return Room object with Room.getId() == id
     */
    private Room getRoomById(int id) throws IllegalArgumentException {

        for (int i = 0; i < this.roomCount; ++i) {
            if (this.rooms[i].getId() == id) {
                return this.rooms[i];
            }
        }

        // Room id doesnt exist
        throw new IllegalArgumentException();
    }

    /**
     * Prints statistics of a year in the hotel
     */
    public void statistics(int year) {

        // Get the most reserved room
        Reservation mostReserved = this.reservations[0];
        int maxReserved = -1;

        int[] incomes = new int[this.reservationCount];
        int totalIncome = 0;

        for (int i = 0; i < this.reservationCount; ++i) {

            int reservedDays = this.reservations[i].getReservedDays(year);

            // Change the maximum if the current is higher
            if (reservedDays > maxReserved) {
                maxReserved = reservedDays;
                mostReserved = this.reservations[i];
            }

            try {
                Room room = this.getRoomById(this.reservations[i].getRoomId());

                incomes[i] = room.getPrice() * reservedDays;
                totalIncome += incomes[i];

            } catch (IllegalArgumentException e) {
                System.out.println("Faulty reservation");
            }
        }

        if (maxReserved > 0) {
            System.out.printf("   1.The most reserved room = Room #%d\n", mostReserved.getRoomId());
            System.out.printf("   2.The best customer = %s  %d days\n", mostReserved.getCustomerName(), maxReserved);
        } else {
            System.out.println("    1.None of the rooms are reserved");
            System.out.println("    2.There is no customer");
        }

        System.out.print("   3.Income = ");

        for (int i = 0; i < incomes.length; ++i) {
            if (incomes[i] > 0) {

                System.out.printf("%,d ", incomes[i]);

                if (i == incomes.length - 1) {
                    System.out.print("= ");
                } else {
                    System.out.print("+ ");
                }


            }
        }
        System.out.printf("%,d\n", totalIncome);

        // Salary of the all staff for one month
        int monthlySalary = 0;

        for (int i = 0; i < this.staffCount; ++i) {
            monthlySalary += this.staffs[i].getSalary();
        }

        // Constant expenses
        int constantExpenses = 12 * CONSTANT_EXPENSE;

        System.out.printf("     Salary = %,d\n", monthlySalary * 12);
        System.out.printf("     Constant expenses = %,d\n", constantExpenses);

        int profit = totalIncome - 12 * monthlySalary - constantExpenses;
        System.out.printf("     Profit = %,d - %,d - %,d = %,d\n", totalIncome, monthlySalary * 12, constantExpenses, profit);

        double[] occupancyRates = new double[12];
        double roomCount = this.roomCount;

        for (int i = 0; i < this.reservationCount; ++i) {

            int[] currentOccupancy = this.reservations[i].getOccupancy(year);

            for (int t = 0; t < occupancyRates.length; ++t) {

                occupancyRates[t] += currentOccupancy[t];
            }
        }

        for (int i = 0; i < occupancyRates.length; ++i) {

            double days = Date.getDaysInMonth(i + 1, year);

            occupancyRates[i] = (occupancyRates[i] / days) / roomCount;
        }

        System.out.print("   4.Monthly occupancy rate\n     ");

        for (int i = 1; i <= 12; ++i) {

            System.out.printf("%-7d", i);
        }

        System.out.print("\n     ");

        for (int i = 0; i < occupancyRates.length; ++i) {

            String value = (int)Math.round(occupancyRates[i] * 100.0) + "%";

            System.out.printf("%-7s", value);
        }

        System.out.println();
    }

    /**
     * Simulates the hotel and prints the results
     * @param startDate start date of the simulation
     * @param endDate end date of the simulation
     */
    public void simulation(Date startDate, Date endDate) {

        int dayCount = 0;

        System.out.print("Day         :");

        for (Date current = new Date(startDate); current.isBeforeOrEqual(endDate); current.addOneDay()) {

            System.out.printf("%9d", current.getDay());

            dayCount += 1;
        }

        double[] satisfactions = new double[dayCount];
        int currentDay = 0;

        int housekeeperCount = 0;

        for (int i = 0; i < this.staffCount; ++i) {

            if (this.staffs[i].getJob().equals("housekeeper")) {
                housekeeperCount += 1;
            }
        }

        System.out.print("\nCustomer    :");

        for (Date current = new Date(startDate); current.isBeforeOrEqual(endDate); current.addOneDay()) {

            int customerCount = 0;

            for (int i = 0; i < this.reservationCount; ++i) {

                if (this.reservations[i].isBooked(current)) {
                    customerCount += 1;
                }
            }

            double satisfaction = (double)(housekeeperCount * 3) / (double)customerCount;
            satisfactions[currentDay] = Double.min(satisfaction, 1.0);
            currentDay += 1;

            System.out.printf("%9d", customerCount);
        }

        double totalSatisfaction = 0.0;

        System.out.print("\nSatisfaction: ");

        for (int i = 0; i < satisfactions.length; ++i) {

            System.out.printf("%8d%%", (int)(satisfactions[i] * 100.0));

            totalSatisfaction += satisfactions[i];
        }

        double averageSatisfaction = totalSatisfaction / (double)satisfactions.length;

        System.out.printf("\nAverage Satisfaction = %.2f%%\n", averageSatisfaction * 100.0);

    }

}