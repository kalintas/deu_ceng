public class Room {

    private String type;
    private boolean hasAC;
    private boolean hasBalcony;
    private int price;

    private static int roomId = 1;
    private int id;

    public Room(String[] input) {

        this.type = input[2];
        this.hasAC = Boolean.parseBoolean(input[3]);
        this.hasBalcony = Boolean.parseBoolean(input[4]);
        this.price = Integer.parseInt(input[5]);

        this.id = roomId;
        roomId += 1;
    }

    public int getId() {
        return this.id;
    }
    public String getType() {
        return this.type;
    }
    public String getAC() {

        if (this.hasAC) {
            return "aircondition";
        }

        return "no-aircondition";
    }

    public String getBalcony() {

        if (this.hasBalcony) {
            return "balcony";
        }

        return "no-balcony";
    }

    public int getPrice() {
        return this.price;
    }

    public void print() {
        System.out.printf("   Room #%d  %-8s %-16s %-11s %dTL\n", this.id, this.type, this.getAC(), this.getBalcony(), this.price);
    }

}