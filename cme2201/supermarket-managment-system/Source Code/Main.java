import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    static void indexAllCustomers(HashTable<Customer> hashTable) throws FileNotFoundException {
        Scanner fileScanner = new Scanner(new File("supermarket_dataset_50K.csv"));

        fileScanner.nextLine();

        var startTime = System.nanoTime();

        int currentLine = 0;

        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] splitLine = line.split(",");

            String customerUuid = splitLine[0];
            String customerName = splitLine[1];
            String transactionDate = splitLine[2];
            String productName = splitLine[3];

            var transaction = new Customer.MarketTransaction(transactionDate, productName);
            var customer = hashTable.get(customerUuid);

            if (customer == null) {
                hashTable.put(customerUuid, new Customer(customerName, transaction));
            } else {
                customer.getTransactions().add(transaction);
            }

            currentLine += 1;
            if (currentLine % 1000 == 0) {
                System.out.println(currentLine);
            }
        }

        var endTime = System.nanoTime();
        var indexingTime = (endTime - startTime) / 1000000; // Convert to milliseconds.

        System.out.printf("Collision count = %d, indexing time = %d ms\n", hashTable.getHashCollisionCount(), indexingTime);
    }

    public static void main(String[] args) throws FileNotFoundException {

        HashTable<Customer> hashTable = new HashTable<>(new HashFunctionPAF(), new CollisionResolverDH());

        hashTable.setLoadFactor(0.80);

        indexAllCustomers(hashTable);

        Scanner fileScanner = new Scanner(new File("customer_1K.txt"));

        long searchTimeSum = 0;
        long minSearchTime = Long.MAX_VALUE;
        long maxSearchTime = -1;

        while (fileScanner.hasNextLine()) {

            String customerUuid = fileScanner.nextLine();

            var startTime = System.nanoTime();
            hashTable.get(customerUuid);
            var endTime = System.nanoTime();

            var searchTime = endTime - startTime;

            searchTimeSum += searchTime;

            if (searchTime < minSearchTime) {
                minSearchTime = searchTime;
            }
            if (searchTime > maxSearchTime) {
                maxSearchTime = searchTime;
            }
        }

        double averageSearchTime = (double)searchTimeSum / 1000.0;
        System.out.printf("Average search time = %f ns, min search time = %d ns, max search time = %d ns", averageSearchTime, minSearchTime, maxSearchTime);
    }
}