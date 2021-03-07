import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {

    @Test
    public void mytest() {
        StudentArrayDeque<Integer> stu = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> arr = new ArrayDequeSolution<>();
        int f = 0;
        while (f < 10000) {
            int i = StdRandom.uniform(1, 3);
            if (i == 1) {
                stu.addFirst(i);
            } else if (i == 2) {
                if (!stu.isEmpty()) {
                    Integer n = stu.removeFirst();
                }
            }/* else if (i == 3) {
                stu.addLast(i);
           } else {
                 if (!stu.isEmpty()) {
                    Integer n = stu.removeLast();
                }
            } */
            f += 1;
        }
    }
}
