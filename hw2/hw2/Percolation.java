package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int N;
    private boolean[] openGridIn1D;
    private int nOpen;
    private WeightedQuickUnionUF connectedGrids;
    private boolean percolated = false;

    // 2D -> 1D
    private int xyToIndexIn1D(int row, int col) {
        return N * row + col;
    }

    /** create N-by-N grid, with all sites initially blocked(0~N-1)
     * (0, 0) is the upper-left site
     * @param N
     */
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException(
                    "N should be greater than 0 but N = " + N + ".");
        }
        this.N = N;
        openGridIn1D = new boolean[N * N + 1]; // set virtual node on the bottom
        for (int i = 0; i <= N * N; i++) {
            openGridIn1D[i] = false;
        }
        connectedGrids = new WeightedQuickUnionUF(N * N + 2);
    }

    // check (row, col) is invalid or not
    private void isInvalidIndex(int row, int col) {
        if (row < 0 || row >= N || col < 0 || col >= N) {
            throw new IndexOutOfBoundsException(
                    "Invalid arguments given: row = " + row + "col = " + col + "."
            );
        }
    }

    // open the site (row, col) if it is not open already
    public void open (int row, int col) {
        isInvalidIndex(row, col);
        int indexIn1D = xyToIndexIn1D(row, col);
        if (openGridIn1D[indexIn1D]) {
            return;
        }
        openGridIn1D[indexIn1D] = true;
        update(indexIn1D, row, col);
        nOpen++;
    }

    private void update(int index, int row, int col) {
        if (row == 0) {
            connectedGrids.union(index + 1, 0);
        } else {
            if (isOpen(row - 1, col)) {
                connectedGrids.union(index + 1, xyToIndexIn1D(row - 1, col) + 1);
            }
        }
        /*
        if (row == N - 1) {
            connectedGrids.union(index + 1, N * N + 1);
        } else {
            if (isOpen(row + 1, col)) {
                connectedGrids.union(index + 1, xyToIndexIn1D(row + 1, col) + 1);
            }
        }
         */
        if (row < N - 1) {
            if (isOpen(row + 1, col)) {
                connectedGrids.union(index + 1, xyToIndexIn1D(row + 1, col) + 1);
            }
        }else {
            if (connectedGrids.connected(index + 1, 0)) {
                connectedGrids.union(index + 1, N * N + 1);
            }
        }
        if (col > 0) {
            if (isOpen(row, col - 1)) {
                connectedGrids.union(index + 1, xyToIndexIn1D(row, col - 1) + 1);
            }
        }
        if (col < N - 1) {
            if (isOpen(row, col + 1)) {
                connectedGrids.union(index + 1, xyToIndexIn1D(row, col + 1) + 1);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        isInvalidIndex(row, col);
        return openGridIn1D[xyToIndexIn1D(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        isInvalidIndex(row, col);
        return connectedGrids.connected(0, xyToIndexIn1D(row, col) + 1);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return nOpen;
    }

    // does the system percolate
    public boolean percolates() {

        for (int i = 0; i < N; i++) {
            if (connectedGrids.find(xyToIndexIn1D(N - 1, i) + 1) == connectedGrids.find(0)) {
                return true;
            }
        }
        return false;

        // return connectedGrids.connected(0, N * N + 1);
    }

    // use for unit testing
    public static void main(String[] args) {

    }
}
