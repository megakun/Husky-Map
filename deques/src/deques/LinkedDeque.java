package deques;

public class LinkedDeque<T> extends AbstractDeque<T> {
    private int size;
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    Node<T> front;
    Node<T> back; // may be the same as front, if you're using circular sentinel nodes


    public LinkedDeque() {
        size = 0;
        front = new Node(null);
        back = new Node(null);
        front.next = back;
        back.prev = front;

    }

    static class Node<T> {
        // IMPORTANT: Do not rename these fields or change their visibility.
        // We access these during grading to test your code.
        T value;
        Node<T> next;
        Node<T> prev;

        Node(T value) {
            this.value = value;
            this.next = null;
            this.prev = null;
        }

        Node(Node<T> prev, T value, Node<T> next) {
            this.value = value;
            this.next = next;
            this.prev = prev;
        }
    }

    public void addFirst(T item) {
        front.next = new Node(front, item, front.next);
        if (size == 0) {
            back.prev = front.next;
        } else {
            Node<T> p = front.next.next;
            p.prev = front.next;
        }
        size += 1;

    }

    public void addLast(T item) {
        back.prev = new Node(back.prev, item, back);
        if (size == 0) {
            front.next = back.prev;
        } else {
            Node<T> p = back.prev.prev;
            p.next = back.prev;
        }

        size += 1;
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        Node<T> p = front.next;
        front.next = front.next.next;
        Node<T> n = front.next;
        n.prev = front;
        size -= 1;
        return p.value;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        Node<T> p = back.prev;
        back.prev = back.prev.prev;
        Node<T> n = back.prev;
        n.next = back;
        size -= 1;
        return p.value;
    }

    public T get(int index) {
        if ((index >= size) || (index < 0)) {
            return null;
        }
        Node<T> p = front.next;
        for (int i = 0; i < index; i++) {
            p = p.next;
        }
        return p.value;
    }

    public int size() {
        return size;
    }
}
