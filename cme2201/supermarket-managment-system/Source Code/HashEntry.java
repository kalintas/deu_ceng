public class HashEntry<KeyType, ValueType> {

    private KeyType key;
    private ValueType value;

    HashEntry(KeyType key, ValueType value) {
        this.key = key;
        this.value = value;
    }

    public KeyType getKey() {
        return key;
    }

    public void setKey(KeyType key) {
        this.key = key;
    }

    public ValueType getValue() {
        return value;
    }

    public void setValue(ValueType value) {
        this.value = value;
    }

    /*
        Since a HashEntry with null key is not allowed.
        We can safely use this to mark an entry as removed.
    */
    public void markAsRemoved() {
        this.key = null;
        this.value = null;
    }
    public boolean isRemoved() {
        return this.key == null;
    }

}
