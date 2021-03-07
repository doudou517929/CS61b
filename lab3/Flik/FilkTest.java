import static org.junit.Assert.*;
import org.junit.Test;

public class FilkTest {
    @Test
    public void testFilk() {
        int i = 128;
        int j = 128;
        assertTrue(Flik.isSameNumber(i, j));
    }
}
