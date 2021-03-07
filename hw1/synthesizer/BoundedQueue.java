package synthesizer;
import java.util.Iterator;

/**items can only be enqueued at the back of the queue, and can only be dequeued
 * from the front of the queue. Unlike our Deque, the BoundedDeque has a fixed capacity,
 * and nothing is allowed to enqueue if the queue is full.
 */
public interface BoundedQueue<T> extends Iterable<T> {

    int capacity();     // return size of the buffer

    int fillCount();    // return number of items currently in the buffer

    void enqueue(T x);  // add item x to the end

    T dequeue();        // delete and return item from the front

    T peek();           // return (but do not delete) item from the front

    // is the buffer empty (fillCount equals zero)?
    default boolean isEmpty() {
        return fillCount() == 0;
    }

    // is the buffer full (fillCount is same as capacity)?
    default boolean isFull() {
        return fillCount() == capacity();
    }

    Iterator<T> iterator();
}
