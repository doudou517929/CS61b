package hw2;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/** estimate the percolation threshold.
 * Initialize all sites to be blocked.
 * repeat: Choose a site uniformly at random, open the site,
 * the fraction of sites that are opened when the system percolates provides an istimate of
 * percolation threshold.
 */
public class PercolationStats {

    private int N;
    private int T;
    private double[] thresholds;
    private PercolationFactory pf;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0) {
            throw new IllegalArgumentException(
                    "N should be greater than 0 but given N = " + N + "."
            );
        }
        if (T <= 0) {
            throw new IllegalArgumentException(
                    "N should be greater than 0 but given T = " + T + "."
            );
        }
        this.N = N;
        this.T = T;
        thresholds = new double[T];
        this.pf = pf;
        simulate();
    }

    // runs T simulations and record the results
    private void simulate() {
        int row, col;
        double threshold;
        for (int t = 0; t < T; t++) {
            Percolation s = pf.make(N);
            while (!s.percolates()) {
                row = StdRandom.uniform(N);
                col = StdRandom.uniform(N);
                if (!s.isOpen(row, col)) {
                    s.open(row, col);
                }
            }
            threshold = (double) s.numberOfOpenSites() / (N * N);
            thresholds[t] = threshold;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    // low endpoing of 95% confidence interval
    public double confidenceLow() {
        return mean() - (1.96 * stddev() / Math.sqrt(T));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return mean() + (1.96 * stddev() / Math.sqrt(T));
    }

}
