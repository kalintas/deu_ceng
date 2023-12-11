
public class Customer {

    private String name;

    private SortedList<MarketTransaction> transactions;

    public Customer(String name, MarketTransaction marketTransaction) {
        this(name);
        this.transactions.add(marketTransaction);
    }

    public Customer(String name) {
        this.name = name;
        this.transactions = new SortedList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SortedList<MarketTransaction> getTransactions() {
        return transactions;
    }

    public static class MarketTransaction implements Comparable<MarketTransaction> {
        private String date;
        private String name;

        public MarketTransaction(String date, String name) {
            this.date = date;
            this.name = name;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public int compareTo(MarketTransaction rhs) {
            return this.date.compareTo(rhs.date);
        }
    }
}
