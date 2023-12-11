public class HashFunctionPAF implements HashFunction {

    private static final int DEFAULT_PAF_CONSTANT = 33;

    int pafConstant;

    public HashFunctionPAF() {
        pafConstant = DEFAULT_PAF_CONSTANT;
    }

    public HashFunctionPAF(int pafConstant) {
        this.pafConstant = pafConstant;
    }

    @Override
    public int getIndex(String key, int capacity) {
        int sum = 0;
        int constant = 1;
        for (int i = 0; i < key.length(); ++i) {

            // Characters are represented as numbers in 1-26 (Case insensitive).
            int character = Character.toLowerCase(key.charAt(i)) - 'a' + 1;

            if (character < 1) {
                character = 1;
            } else if (character > 26) {
                character = 26;
            }

            sum = (sum + (constant * character)) % capacity;
            constant = (constant * this.pafConstant) % capacity;
        }

        return sum;
    }
}
