public interface CollisionResolver {
    <KeyType, ValueType> int locate(int index, KeyType key, HashEntry<KeyType, ValueType>[] entries);
    <KeyType, ValueType> void handleCollision(int index, HashEntry<KeyType, ValueType> newEntry, HashEntry<KeyType, ValueType>[] entries) throws IllegalStateException;
}
