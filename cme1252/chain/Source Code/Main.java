import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

    	Scanner sc=new Scanner(System.in);
    	
    	System.out.println("SEED:");
        
        int seed=sc.nextInt();
    	
        ChainGame chainGame = new ChainGame();
        
        chainGame.initialize(seed);
        chainGame.run();

        System.exit(0);
    }
}
