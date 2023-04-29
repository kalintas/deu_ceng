public class Customer {

    private String name;
    private String gender;
    private Date birthDate;
    private Address contactAddress;
    private PhoneNumber phoneNumber;

    private static int customerId = 1;
    private int id;

    public Customer(String[] input) {

        this.name = input[1] + " " + input[2];
        this.gender = input[3];
        this.birthDate = new Date(input[4]);
        this.contactAddress = new Address(input[5], input[6], input[7]);
        this.phoneNumber = new PhoneNumber(input[8]);

        this.id = customerId;
        customerId += 1;
    }

    public int getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public String getGender() {
        return this.gender;
    }

    public Address getContactAddress() {
        return this.contactAddress;
    }

    /**
     * Prints the Customer object
     * @param maxNameLength maximum length of the Customer.name to be printed
     */
    public void print(int maxNameLength) {

        String format = "   Customer #%d  %-" + maxNameLength + "s  %-7s %-11s %-10s %s\n";

        System.out.printf(format, this.id, this.name, this.gender, this.birthDate.getDate(), this.contactAddress.city, this.phoneNumber);
    }

}