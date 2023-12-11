public class CollisionResolverLP implements CollisionResolver {
    @Override
    public <KeyType, ValueType> int locate(int index, KeyType key, HashEntry<KeyType, ValueType>[] entries) {
        int startIndex = index;

        do {

            if (entries[index] != null && entries[index].getKey().equals(key)) {
                return index;
            }

            index = (index + 1) % entries.length;

        } while (startIndex != index);

        // Not found.
        return -1;
    }

    @Override
    public <KeyType, ValueType> void handleCollision(int index, HashEntry<KeyType, ValueType> newEntry, HashEntry<KeyType, ValueType>[] entries) throws IllegalStateException {
        int startIndex = index;

        while (true) {
            index = (index + 1) % entries.length;

            if (startIndex == index) {
                // Could not fit the entry.
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
