package hw2;


import org.junit.Test;

import static org.junit.Assert.*;

public class TestPercolation {
    private Percolation t = new Percolation(3);

    @Test
    public void testIsOpen() {
        t.open(1, 0);
        assertFalse(t.isOpen(0, 0));
        assertTrue(t.isOpen(1, 0));
    }

    @Test
    public void testIsFull() {
        t.open(1, 0);
        assertFalse(t.isFull(1, 0));
        t.open(0, 0);
        assertTrue(t.isFull(1, 0));
    }

    @Test
    public void testNumberOfOpenSites() {
        assertEquals(0, t.numberOfOpenSites());
        t.open(1, 0);
        assertEquals(1, t.numberOfOpenSites());
        t.open(0, 0);
        assertEquals(2, t.numberOfOpenSites());
    }

    @Test
    public void testPercolates() {
        t.open(0, 0);
        t.open(1, 0);
        t.open(1, 1);
        assertFalse(t.percolates());
        t.open(2, 1);
        assertTrue(t.percolates());
    }
}
