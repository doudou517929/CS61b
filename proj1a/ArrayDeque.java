public class ArrayDeque<T> {
    private T[] items;
    private int size;
    private int front;
    private int last;

    /*Creates an empty array deque*/
    public ArrayDeque() {
        items = (T []) new Object[8];
        size = 0;
        front = 0;
        last = 1;
    }

    /*Adds an item of type T to the front of the deque.*/
    public void addFirst(T item) {
        items[front] = item;
        if (size == 0) {
            last = front;
        }
        if (front > 0) {
            front -= 1;
        } else {
            front = items.length - 1;
        }
        size += 1;
        if (size >= items.length / 2) {
            resize(size * 2);
        }
    }

    /*Resize Array*/
    private void resize(int n) {
        if (n < 4) {
            n = 4;
        }
        T[] temp = (T []) new Object[n];
        if (last < front) {
            System.arraycopy(items, 0, temp, 0, last + 1);
            if (front != items.length - 1) {
                int i = items.length - front - 1;
                System.arraycopy(items, front + 1, temp, n - i, i);
                front = n - i - 1;
            } else {
                front = n - 1;
            }
        } else {
            System.arraycopy(items, front + 1, temp, 1, size);
            front = 0;
            last = size;
        }
        if (front == last) {
            last = (front + 1) % n;
        }
        items = temp;
    }

    /*Adds an item of type T to the back of the deque.*/
    public void addLast(T item) {
        if (!isEmpty()) {
            if (last != items.length - 1) {
                last += 1;
            } else {
                last = 0;
            }
        }
        items[last] = item;
        size += 1;
        if (size >= items.length / 2) {
            resize(size * 2);
        }
    }

    /*Returns true if deque is empty, false otherwise.*/
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    /*Returns the number of items in the deque.*/
    public int size() {
        return size;
    }

    /*Prints the items in the deque from first to last, separated by a space.*/
    public void printDeque() {
        int i = 0, p = front;
        while (i < size) {
            System.out.print(items[p]);
            i += 1;
            p = (p + 1) % items.length;
            if (i < size) {
                System.out.print(" ");
            }
        }
    }

    /**Removes and returns the item at the front of the deque.
     *If no such item exists, returns null.*/
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T temp;
        if (front != items.length - 1) {
            temp = items[front + 1];
            front += 1;
        } else {
            temp = items[0];
            front = 0;
        }
        items[front] = null;
        size -= 1;
        if (size < items.length / 4) {
            resize(items.length / 2);
        }
        return temp;
    }

    /*Removes and returns the item at the back of the deque.
     *If no such item exists, returns null.*/
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        T temp;
        if (items[last] == null) {
            if (last != 0) {
                last -= 1;
            } else {
                last = items.length - 1;
            }
        }
        temp = items[last];
        items[last] = null;
        size -= 1;
        if (size != 0) {
            if (last != 0) {
                last -= 1;
            } else {
                last = items.length - 1;
            }
        }
        if (size < items.length / 4) {
            resize(items.length / 2);
        }
        return temp;
    }

    /**Gets the item at the given index, where 0 is the front,
     * 1 is the next item, and so forth.
     * If no such item exists, returns null*/
    public T get(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        int p = (front + index + 1) % items.length;
        T temp = items[p];
        return temp;
    }
}
