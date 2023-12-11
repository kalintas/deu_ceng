public interface DictionaryInterface<KeyType, ValueType> {

    void put(KeyType key, ValueType value);

    ValueType get(KeyType key);

    ValueType remove(KeyType key);

    void resize(int capacity);
}
