import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {

    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();

    @Test
    public void testequalChars(){
        assertTrue(offByOne.equalChars('a', 'b'));
    }

    static CharacterComparator offByN = new OffByN(5);

    @Test
    public void testequalNChars(){
        assertTrue(offByN.equalChars('a', 'f'));
    }

}
