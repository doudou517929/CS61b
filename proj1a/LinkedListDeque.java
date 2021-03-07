public class LinkedListDeque<T> {

    private IntNode sentinel;
    private int size;

    private class IntNode {
        private T item;
        private IntNode next;
        private IntNode prev;
        private IntNode(T i, IntNode m, IntNode n) {
            item = i;
            next = n;
            prev = m;
        }
    }

    /*Creates an empty linked list deque*/
    public LinkedListDeque() {
        sentinel = new IntNode(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    /*Adds an item of type T to the front of deque*/
    public void addFirst(T item) {
        IntNode tempn = sentinel.next;
        sentinel.next = new IntNode(item, sentinel, tempn);
        tempn.prev = sentinel.next;
        size += 1;
    }

    /*Adds an item of type T to the back of the deque.*/
    public void addLast(T item) {
        IntNode tempn = sentinel.prev;
        sentinel.prev = new IntNode(item, tempn, sentinel);
        tempn.next = sentinel.prev;
        size += 1;
    }

    /*Returns true if deque is empty, false otherwise.*/
    public boolean isEmpty() {
        if (size == 0) {  //sentinel.next == sentinel && sentinel.prev == sentinel
            return true;
        }
        return false;
    }

    /*Returns the number of items in the deque.*/
    public int size() {
        return size;
    }

    /*Prints the items in the deque from first to last, separated by a space*/
    public void printDeque() {
        IntNode p = sentinel.next;
        while (p != sentinel) {
            System.out.print(p.item);
            if (p.next != sentinel) {
                System.out.print(" ");
            }
            p = p.next;
        }
    }

    /*Removes and returns the item at the front of the deque.
     *If no such item exists, returns null*/
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        IntNode tempn = sentinel.next.next;
        T temp = sentinel.next.item;
        sentinel.next = tempn;
        tempn.prev = sentinel;
        size -= 1;
        return temp;
    }

    /*Removes and returns the item at the back of the deque.
     *If no such item exists, returns null.*/
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        IntNode tempn = sentinel.prev.prev;
        T temp = sentinel.prev.item;
        sentinel.prev = tempn;
        tempn.next = sentinel;
        size -= 1;
        return temp;
    }

    /*Gets the item at the given index, where 0 is the front, 1 is the next item,
     *and so forth. If no such item exists, returns null.*/
    public T get(int index) {
        if (size < index) {
            return null;
        }
        int i = 0;
        IntNode p = sentinel.next;
        while (i < index) {
            p = p.next;
            i += 1;
        }
        T temp = p.item;
        return temp;
    }

    /*Same as get, but uses recursion.*/
    public T getRecursive(int index) {
        if (index > size) {
            return null;
        }
        T temp = helper(index, sentinel.next);
        return temp;
    }
    private T helper(int index, IntNode p) {
        if (index == 0) {
            return p.item;
        }
        return helper(index - 1, p.next);
    }
}
