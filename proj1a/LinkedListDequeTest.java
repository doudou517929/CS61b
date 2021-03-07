import org.junit.Test;
import static org.junit.Assert.*;

/** Performs some basic linked list tests. */
public class LinkedListDequeTest {
	
	/* Utility method for printing out empty checks. */
	public static boolean checkEmpty(boolean expected, boolean actual) {
		if (expected != actual) {
			System.out.println("isEmpty() returned " + actual + ", but expected: " + expected);
			return false;
		}
		return true;
	}

	/* Utility method for printing out empty checks. */
	public static boolean checkSize(int expected, int actual) {
		if (expected != actual) {
			System.out.println("size() returned " + actual + ", but expected: " + expected);
			return false;
		}
		return true;
	}

	/* Prints a nice message based on whether a test passed. 
	 * The \n means newline. */
	public static void printTestStatus(boolean passed) {
		if (passed) {
			System.out.println("Test passed!\n");
		} else {
			System.out.println("Test failed!\n");
		}
	}

	/** Adds a few things to the list, checking isEmpty() and size() are correct, 
	  * finally printing the results. 
	  *
	  * && is the "and" operation. */
	public static void addIsEmptySizeTest() {
		System.out.println("Running add/isEmpty/Size test.");
		System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

		LinkedListDeque<String> lld1 = new LinkedListDeque<String>();

		boolean passed = checkEmpty(true, lld1.isEmpty());

		lld1.addFirst("front");
		
		// The && operator is the same as "and" in Python.
		// It's a binary operator that returns true if both arguments true, and false otherwise.
		passed = checkSize(1, lld1.size()) && passed;
		passed = checkEmpty(false, lld1.isEmpty()) && passed;

		lld1.addLast("middle");
		passed = checkSize(2, lld1.size()) && passed;

		lld1.addLast("back");
		passed = checkSize(3, lld1.size()) && passed;

		System.out.println("Printing out deque: ");
		lld1.printDeque();

		printTestStatus(passed);
	}

	/** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
	public static void addRemoveTest() {

		System.out.println("Running add/remove test.");

		System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

		LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
		// should be empty 
		boolean passed = checkEmpty(true, lld1.isEmpty());

		lld1.addFirst(10);
		// should not be empty 
		passed = checkEmpty(false, lld1.isEmpty()) && passed;

		lld1.removeFirst();
		// should be empty 
		passed = checkEmpty(true, lld1.isEmpty()) && passed;

		printTestStatus(passed);
	}

	@Test
	public void testAddGet(){
		ArrayDeque<String> arr1 = new ArrayDeque<>();
		arr1.addFirst("a");
		String at2 = arr1.removeFirst();
		arr1.addFirst("b");
		String at3 = arr1.removeFirst();
		arr1.addFirst("c");
		String at4 = arr1.removeFirst();
		arr1.addFirst("d");
		String at5 = arr1.removeFirst();
		System.out.print(at2+at3+at4+at5);
	}

	@Test
	public void testget1() {
		ArrayDeque<Integer> arr = new ArrayDeque<>();
		arr.addLast(0);
		arr.addFirst(1);
		arr.addLast(2);
		int i = arr.get(2);
		assertEquals(2, i);
	}

	@Test
	public  void testget2() {
		ArrayDeque<Integer> arr = new ArrayDeque<>();
		arr.addLast(0);
		arr.removeFirst();
		arr.addFirst(2);
		arr.removeLast();
		arr.addLast(4);
		int i = arr.get(0);
		assertEquals(4, i);
	}

	@Test
	public void testget3() {
		ArrayDeque<Integer> arr = new ArrayDeque<>();
		arr.addFirst(0);
		arr.removeFirst();
		arr.addLast(2);
		arr.addFirst(3);
		arr.removeFirst();
		arr.addLast(5);
		arr.addLast(6);
		int i = arr.removeFirst();
		assertEquals(2, i);
	}

	@Test
	public void testaddfirst() {
		ArrayDeque<Integer> arr = new ArrayDeque<>();
		arr.addFirst(0);
		arr.removeFirst();
		arr.addFirst(2);
		arr.removeFirst();
		arr.addFirst(4);
		arr.addFirst(5);
	}

	@Test
	public void testaddlast() {
		ArrayDeque<Integer> arr = new ArrayDeque<>();
		arr.addLast(0);
		arr.removeFirst();
		arr.addLast(2);
		arr.isEmpty();
		arr.addLast(4);
		int i = arr.removeFirst();
		assertEquals(2, i);
	}

	@Test
	public void testadd() {
		ArrayDeque<Integer> arr = new ArrayDeque<>();
		arr.addLast(0);
		arr.addFirst(1);
		int i = arr.removeLast();
		assertEquals(0, i);
	}

	@Test
	public void testremovelast() {
		ArrayDeque<Integer> arr = new ArrayDeque<>();
		arr.addFirst(0);
		int i = arr.removeLast();
		assertEquals(0, i);
	}

	public static void main(String[] args) {
		System.out.println("Running tests.\n");
		addIsEmptySizeTest();
		addRemoveTest();
	}
} 