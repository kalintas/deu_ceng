
public class HashTable<ValueType> implements DictionaryInterface<String, ValueType> {

    /* Constants */
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int INITIAL_CAPACITY = 10;

    /* Member variables */

    private int size;
    private double loadFactor;

    private int hashCollisionCount;

    private HashEntry<String, ValueType>[] entries;

    private HashFunction hashFunction;
    private CollisionResolver collisionResolver;

    /* Constructors */

    /**
        Default constructor of the HashTable class.
        Hash function and collision resolver are defaulted to SSF and LP.
    */
    public HashTable() {
        this(new HashFunctionSSF(), new CollisionResolverLP());
    }

    /**
    * Constructs a new HashTable with given hash function and collision resolver.
    * @param hashFunction hash function used by the hash table.
    * @param collisionResolver collision resolver used by the hash table.
    */
    @SuppressWarnings("unchecked")
    public HashTable(HashFunction hashFunction, CollisionResolver collisionResolver) {
        this.size = 0;
        this.hashCollisionCount = 0;
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.entries = (HashEntry<String, ValueType>[])new HashEntry[INITIAL_CAPACITY];
        this.hashFunction = hashFunction;
        this.collisionResolver = collisionResolver;
    }

    /* Member functions */

    /**
     * Inserts a new entry to HashTable. Size of the HashTable is increased by 1.
     * @param key a nonnull string key of the new hash entry.
     * @param value value corresponding to given key.
     * @throws IllegalArgumentException if parameter key is null.
     */
    public void put(String key, ValueType value) {

        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        // Resize the hash table if size of the hash table exceeds load factor after adding the entry.
        if (this.size + 1 >= (int)((double)this.entries.length * this.loadFactor)) {
            // Double the current table size.
            this.resize(getNextPrime(this.entries.length * 2));
        }

        this.putEntry(key, value);
    }

    /**
     * Returns the value that corresponds to the given key in the HashTable.
     * @return value that corresponds to the given key. Will return null if
     *      there is no such entry or corresponding value is null.
     */
    public ValueType get(String key) {

        int index = this.hashFunction.getIndex(key, this.entries.length);
        index = this.collisionResolver.locate(index, key, this.entries);

        ValueType value = null;

        if (index >= 0) {
            value = this.entries[index].getValue();
        }

        return value;
    }

    /**
     * Removes the entry with the given key. Size of the HashTable is decreased by 1.
     * @return value of the removed entry. Will return null if
     *      there is no such entry or corresponding value is null.
     */
    public ValueType remove(String key) {

        int index = this.hashFunction.getIndex(key, this.entries.length);
        index = this.collisionResolver.locate(index, key, this.entries);

        ValueType oldValue = null;

        if (index >= 0) {
            oldValue = this.entries[index].getValue();

            this.size -= 1;
            // Don't set entry to null.
            // Instead, make it a shallow entry by marking it as removed.
            // This is used later when locating other entries.
            this.entries[index].markAsRemoved();
        }

        return oldValue;
    }

    /**
     * Resizes the HashTable with given capacity. Rehashes all elements in their correct locations.
     * @param capacity new capacity of the HashTable. Must be smaller than size of the HashTable.
     * @throws IllegalArgumentException if parameter capacity is less than size of the HashTable.
     */
    @SuppressWarnings("unchecked")
    public void resize(int capacity) {

        if (capacity < this.size) {
            throw new IllegalArgumentException("Capacity cannot be less than size of the HashTable");
        }

        var oldEntries = this.entries;
        this.entries = (HashEntry<String, ValueType>[])new HashEntry[capacity];

        // Initialize the array with null values.
        for (int i = 0; i < this.entries.length; ++i) {
            this.entries[i] = null;
        }

        // Re-add all elements.
        for (int i = 0; i < oldEntries.length; ++i) {

            var entry = oldEntries[i];
            if (entry != null) {
                this.putEntry(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Private implementation of entry insertion.
     * nor check the key parameter for null values.
     */
    private void putEntry(String key, ValueType value) {

        while (true) {

            try {
                int index = this.hashFunction.getIndex(key, this.entries.length);

                var newEntry = new HashEntry<>(key, value);

                if (this.entries[index] == null) {
                    // There is no entry with this index.
                    this.entries[index] = newEntry;
                } else {
                    this.hashCollisionCount += 1;
                    // Hash collision occurred. Handle the collision with the collision resolver.
                    this.collisionResolver.handleCollision(index, newEntry, this.entries);
                }

                this.size += 1;
                return;
            } catch (IllegalStateException exception) {
                // Could not resolve the hash collision.
                // Increase the size of the hash table and try again.
                this.resize(getNextPrime(this.entries.length * 2));
            }
        }
    }

    public double getLoadFactor() {
        return this.loadFactor;
    }

    /**
     * Sets the load factor of the HashTable.
     * @throws IllegalArgumentException if given load factor is not in range of [0.0, 1.0].
     */
    public void setLoadFactor(double loadFactor) {

        if (loadFactor < 0.0 || loadFactor > 1.0) {
            throw new IllegalArgumentException("Load factor must be in range of [0.0, 1.0]");
        }

        this.loadFactor = loadFactor;
    }

    public int size() {
        return this.size;
    }

    public int getHashCollisionCount() {
        return this.hashCollisionCount;
    }

    /**
     * @return true if parameter value is a prime.
     */
    private static boolean isPrime(int value) {

        for (int i = 2; i < value; ++i) {
            if (value % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return first prime integer after parameter value.
     */
    private static int getNextPrime(int value) {

        while (!isPrime(value)) {
            value += 1;
        }
        return value;
    }
}