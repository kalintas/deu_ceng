
public class Main {

    public static void main(String[] args) throws Exception {

        while (true) {

            ChainGame chainGame = new ChainGame();

            chainGame.gameMenu();
            chainGame.initialize();

            if (!chainGame.run()) {
                System.exit(0);
            }
        }
    }
}
