public class Staff {

    private String name;
    private Date birthDate;
    private String gender;
    private Address address;
    private PhoneNumber phoneNumber;
    private String job;

    private int salary;

    private static int staffId = 1;
    private int id;

    public Staff(String[] input) {

        this.name = input[1] + " " + input[2];
        this.gender = input[3];
        this.birthDate = new Date(input[4]);
        this.address = new Address(input[5], input[6], input[7]);
        this.phoneNumber = new PhoneNumber(input[8]);
        this.job = input[9];
        this.salary = Integer.parseInt(input[10]);

        this.id = staffId;
        staffId += 1;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int inputID){
        this.id = inputID;
    }
    public String getName() {
        return this.name;
    }

    public String getGender() {
        return this.gender;
    }

    public String getJob() {
        return this.job;
    }

    public int getSalary() {
        return salary;
    }

    /**
     * Prints the Staff object
     * @param maxNameLength maximum length of the Staff.name to be printed
     */
    public void print(int maxNameLength) {

        String format = "   Employee #%d  %-" + maxNameLength + "s  %-7s %-11s %s\n";

        System.out.printf(format, this.id, this.name, this.gender, this.birthDate.getDate(), this.job);
    }

}