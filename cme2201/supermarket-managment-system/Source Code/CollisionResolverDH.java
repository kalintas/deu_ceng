public class CollisionResolverDH implements CollisionResolver {

    private static final int DH_CONSTANT = 7;

    private int hashFunction(int hashResult) {
        return DH_CONSTANT - (hashResult % DH_CONSTANT);
    }

    @Override
    public <KeyType, ValueType> int locate(int index, KeyType key, HashEntry<KeyType, ValueType>[] entries) {

        int secondHash = this.hashFunction(index);
        int startIndex = index;
        int lookupCount = 1;

        while (true) {

            if (entries[index] != null && entries[index].getKey().equals(key)) {
                return index;
            }

            if (startIndex == index && lookupCount > 1) {
                // Not found.
                return -1;
            }

            index = (startIndex + lookupCount * secondHash) % entries.length;
            lookupCount += 1;
        }
    }

    @Override
    public <KeyType, ValueType> void handleCollision(int index, HashEntry<KeyType, ValueType> newEntry, HashEntry<KeyType, ValueType>[] entries) throws IllegalStateException {

        int secondHash = this.hashFunction(index);
        int startIndex = index;
        int lookupCount = 1;

        while (true) {
            index = (startIndex + lookupCount * secondHash) % entries.length;
            lookupCount += 1;

            if (startIndex == index && lookupCount > 1) {
                throw new IllegalStateException("Cannot resolve the hash collision");
            }

            if (entries[index] == null || entries[index].isRemoved()) {
                // Insert the new item and handle the collision.
                entries[index] = newEntry;
                return;
            }
        }
    }
}
