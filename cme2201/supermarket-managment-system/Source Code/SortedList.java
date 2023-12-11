import java.util.ArrayList;

public class SortedList<Type extends Comparable<? super Type>> {

    private ArrayList<Type> array;

    public SortedList() {
        this.array = new ArrayList<>();
    }

    public void add(Type value) {

        if (this.array.isEmpty()) {
            this.array.add(value);
            return;
        }

        int index = 0;

        for (; index < array.size(); ++index) {

            if (this.array.get(index).compareTo(value) > 0) {
                break;
            }
        }


        this.array.add(null);

        // Move the elements.
        for (int i = array.size() - 2; i >= index; --i) {
            this.array.set(i + 1, this.array.get(i));
        }

        this.array.set(index, value);
    }

    public ArrayList<Type> getArray() {
        return this.array;
    }
}
