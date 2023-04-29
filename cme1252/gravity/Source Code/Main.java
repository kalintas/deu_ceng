public class Main {

    public static void main(String[] args) {

        try {

            while (true) {

                Gravity gravity = new Gravity();

                gravity.mainMenu();

                gravity.initGame("./map.txt");

                if (!gravity.runGame()) {
                    System.exit(0);
                }
            }

        } catch (Exception exception) {

            System.err.printf("Error: %s", exception.getMessage());
            
        }
    }
}
