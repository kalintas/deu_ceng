public class HashFunctionSSF implements HashFunction {

    @Override
    public int getIndex(String key, int capacity) {
        int sum = 0;
        for (int i = 0; i < key.length(); ++i) {

            sum += key.charAt(i);
        }

        return sum % capacity;
    }
}
