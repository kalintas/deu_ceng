public class Reservation {

    private int customerId;
    private String customerName;

    private int roomId;

    private Date arrivalDate;
    private Date departureDate;

    public Reservation(int customerId, String customerName, String[] words) {

        this.customerId = customerId;
        this.customerName = customerName;

        this.roomId = Integer.parseInt(words[2]);

        this.arrivalDate = new Date(words[3]);
        this.departureDate = new Date(words[4]);
    }

    public int getCustomerId() {
        return this.customerId;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public int getRoomId() {
        return this.roomId;
    }
    public Date getDepartureDate() {return departureDate;}
    public Date getArrivalDate() {return arrivalDate;}

    /**
     * Prints the Reservation object
     * @param maxNameLength maximum length of the Reservation.customerName to be printed
     */
    public void print(int maxNameLength) {

        String format = "   Room #%d  %-" + maxNameLength + "s  %-11s %-11s\n";

        System.out.printf(format, this.roomId, this.customerName, this.arrivalDate.getDate(), this.departureDate.getDate());
    }

    /**
     * Returns true if this reservation is booked in the parameter reservation durations
     * @return returns true if the reservations do collide
     */
    public boolean isBooked(Reservation reservation) {

        // Return false if the rooms are different
        if (this.roomId != reservation.roomId) {
            return false;
        }

        return this.isBooked(reservation.arrivalDate, reservation.departureDate);
    }

    /**
     * Returns true if this reservation is booked between specified dates
     * @return returns true if the reservation is booked
     */
    public boolean isBooked(Date arrival, Date departure) {

        if (arrival.isBefore(this.arrivalDate)) {
            // arrival is before this.arrivalData
            // So in order to be booked departure date must be after than this.arrivalData
            return departure.isAfter(this.arrivalDate);
        } else if (arrival.isAfterOrEqual(this.departureDate)) {
            // arrival is after or equal to this.departureDate
            // This room is available
            return false;
        }

        // arrival is between this.arrivalData and this.departureDate
        // This room is booked
        return true;
    }

    /**
     * Returns true if this reservation is booked in the specified date
     * @return returns true if the reservation is booked
     */
    public boolean isBooked(Date date) {
        // True if date is between [this.arrivalDate, this.departureDate)

        return date.isAfterOrEqual(this.arrivalDate) && date.isBefore(this.departureDate);
    }

    /**
     * Returns the amount of days this reservation takes
     */
    public int getReservedDays(int year) {

        Date arrival = Date.max(this.arrivalDate, new Date(1, 1, year));
        Date departure = Date.min(this.departureDate, new Date(31, 12, year));

        return Date.getDifference(arrival, departure);
    }

    /**
     * Returns specified year's occupancies
     * @return Returns an int array with the length of 12. Every element of the array
     *          contains how much day the room is booked in the month.
     */
    public int[] getOccupancy(int year) {
        //addReservation;2;3;2.2.2022;27.4.2024
        int[] occupancy = new int[12];

        Date arrival = Date.max(this.arrivalDate, new Date(1, 1, year));
        Date departure = Date.min(this.departureDate, new Date(31, 12, year));

        if (arrival.isAfter(departure)) {
            return occupancy;
        }


        if (arrival.getMonth() == departure.getMonth()) {
            // Arrival and the departure is in the same month
            // Don't count the day departure

            occupancy[arrival.getMonth() - 1] = departure.getDay() - arrival.getDay();

        } else {

            occupancy[arrival.getMonth() - 1] = arrival.getDaysInMonth() - arrival.getDay() + 1; // Count the departure day
            occupancy[departure.getMonth() - 1] = departure.getDay() - 1;                        // Don't count the departure day

            // The months between arrival and departure has full occupancy
            for (int i = arrival.getMonth() + 1; i < departure.getMonth(); ++i) {

                occupancy[i - 1] = Date.getDaysInMonth(i, year);
            }
        }

        // Add one if there is more booked days after this year
        if (this.departureDate.isNotEqual(departure)) {
            occupancy[departure.getMonth() - 1] += 1;
        }

        return occupancy;
    }

}

