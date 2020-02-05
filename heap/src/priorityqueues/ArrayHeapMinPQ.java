package priorityqueues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class ArrayHeapMinPQ<T extends Comparable<T>> implements ExtrinsicMinPQ<T> {
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    static final int START_INDEX = 1;
    List<PriorityNode<T>> items;
    Map<T, Integer> map;
    private int size;

    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
        items.add(new PriorityNode<T>(null, 0.0));
        map = new HashMap<>();
        size = 0;
    }

    // Here's a method stub that may be useful. Feel free to change or remove it, if you wish.
    // You'll probably want to add more helper methods like this one to make your code easier to read.
    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) {
        PriorityNode<T> temp = items.get(a);
        map.put(items.get(b).getItem(), a);
        items.set(a, items.get(b));
        map.put(temp.getItem(), b);
        items.set(b, temp);
    }

    /**
     * Adds an item with the given priority value.
     * Runs in O(log N) time (except when resizing).
     * @throws IllegalArgumentException if item is null or is already present in the PQ
     */
    @Override
    public void add(T item, double priority) {
        if (item == null || contains(item)) {
            throw new IllegalArgumentException();
        }
        items.add(new PriorityNode<>(item, priority));
        size++;
        map.put(item, size);
        percolateUp(size);
    }

    private void percolateUp(int root) {
        while (root > 1 &&
            (items.get(root / 2).getPriority() > items.get(root).getPriority())) {
            swap(root, root / 2);
            root = root / 2;
        }
    }

    /**
     * Returns true if the PQ contains the given item; false otherwise.
     * Runs in O(log N) time.
     */
    @Override
    public boolean contains(T item) {
        return map.containsKey(item);
    }

    /**
     * Returns the item with the least-valued priority.
     * Runs in O(log N) time.
     * @throws NoSuchElementException if the PQ is empty
     */
    @Override
    public T peekMin() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return items.get(START_INDEX).getItem();
    }

    /**
     * Removes and returns the item with the least-valued priority.
     * Runs in O(log N) time (except when resizing).
     * @throws NoSuchElementException if the PQ is empty
     */
    @Override
    public T removeMin() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        T min = items.get(START_INDEX).getItem();
        swap(START_INDEX, size--);
        percolateDown(START_INDEX);
        items.set(size + 1, null);
        map.remove(min);
        return min;
    }

    private void percolateDown(int root) {
        while (2 * root <= size) {
            int j = 2 * root;
            if (j < size && (items.get(j).getPriority() > items.get(j + 1).getPriority())) {
                j++;
            }
            if (items.get(root).getPriority() <= items.get(j).getPriority()) {
                break;
            }
            swap(root, j);
            root = j;
        }
    }

    /**
     * Changes the priority of the given item.
     * Runs in O(log N) time.
     *
     * @throws NoSuchElementException if the item is not present in the PQ
     */
    @Override
    public void changePriority(T item, double priority) {
        if (!map.containsKey(item)) {
            throw new NoSuchElementException();
        }
        int index = map.get(item);
        items.get(index).setPriority(priority);
        swap(index, size);
        percolateUp(index);
    }

    /**
     * Returns the number of items in the PQ.
     * Runs in O(log N) time.
     */
    @Override
    public int size() {
        return size;
    }
}
