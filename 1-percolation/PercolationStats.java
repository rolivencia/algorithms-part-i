/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final double[] trialResult;
    private final int numberOfTrials;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(final int n, final int trials) {
        this.numberOfTrials = trials;
        this.trialResult = new double[this.numberOfTrials];

        for (int i = 0; i < this.numberOfTrials; i++) {

            Percolation percolation = new Percolation(n);
            int randRow = 0;
            int randCol = 0;
            // We open sites until the system percolates
            while (!percolation.percolates()) {
                // Closed sites are randomly picked to be opened
                do {
                    randRow = 1 + StdRandom.uniform(n);
                    randCol = 1 + StdRandom.uniform(n);
                }
                while (percolation.isOpen(randRow, randCol));
                percolation.open(randRow, randCol);
            }

            this.trialResult[i] = (percolation.numberOfOpenSites()) / ((double) n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(this.trialResult);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(this.trialResult);
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return this.mean() - this.confidenceInterval();
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return this.mean() + this.confidenceInterval();
    }

    private double confidenceInterval() {
        double numerator = 1.96 * stddev();
        double denominator = Math.sqrt((double) this.numberOfTrials);
        return numerator / denominator;
    }

    /**
     * Main method
     */
    public static void main(String[] args) {
        int n, t;

        if (args.length == 2) {
            n = Integer.parseInt(args[0]);
            t = Integer.parseInt(args[1]);

            PercolationStats percolation = new PercolationStats(n, t);
            StdOut.println("mean                    = " + percolation.mean());
            StdOut.println("stddev                  = " + percolation.stddev());
            StdOut.println("95% confidence interval = [" + percolation.confidenceLo() + ", " + percolation.confidenceHi() + "]");
        } else {
            throw new IllegalArgumentException("Number of arguments different than expected (2)");
        }
    }
}
