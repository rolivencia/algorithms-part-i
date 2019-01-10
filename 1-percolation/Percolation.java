import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */
public class Percolation {

    // A value of 0 for a given cell means that the site is closed
    // A value different than zero for a given cell means that the site is open
    private final WeightedQuickUnionUF uf;
    private final int[][] grid;
    private final int size;
    private int openSitesCount;

    private final int virtualTopElementPosition;
    private final int virtualBottomElementPosition;

    // create n-by-n grid, with all sites blocked

    /**
     * Creates a new instance of the Percolation class
     *
     * @param n
     */
    public Percolation(final int n) {

        if (!this.numberOfElementsIsLegal(n)) {
            throw new IllegalArgumentException("Number of elements: " + n);
        }

        this.openSitesCount = 0;
        this.size = n;
        this.virtualBottomElementPosition = this.size * this.size + 1;
        this.virtualTopElementPosition = this.size * this.size;

        this.uf = new WeightedQuickUnionUF(n * n + 2); // we add two to have the virtual top and bottom
        this.grid = new int[n][n]; // initializes by default to zero
    }

    private int matrixToUfIndex(int i, int j) {
        return this.size * i + j;
    }

    /**
     * Validates if row and col numbers are within the bounds of the defined grid
     *
     * @param row
     * @param col
     * @return
     */
    private boolean cellCoordinatesAreLegal(final int row, final int col) {
        return row > 0 || col > 0 || row <= size || col <= size;
    }

    /**
     * Validates if the number of elements for the grid is legal
     *
     * @param n
     * @return
     */
    private boolean numberOfElementsIsLegal(final int n) {
        return n > 0;
    }

    // open site (row, col) if it is not open already
    public void open(final int row, final int col) {

        if (!this.cellCoordinatesAreLegal(row, col)) {
            throw new IllegalArgumentException("Row: " + row + "; Column: " + col);
        }

        // We translate the indexes to deal with the convention of starting with 1 as initial index
        int i = row - 1;
        int j = col - 1;
        int currentElementPosition = this.matrixToUfIndex(i, j);

        if (!this.isOpen(row, col)) {
            this.openSitesCount++;
        }
        this.grid[i][j] = 1;

        if (i == 0) { // virtual top
            this.uf.union(currentElementPosition, this.virtualTopElementPosition);
        } else if (i == this.size - 1) { // virtual bottom
            this.uf.union(currentElementPosition, this.virtualBottomElementPosition);
        }

        // If neighbors are open, we do the union of the current element with them
        if (i > 0 && this.grid[i - 1][j] != 0) {
            this.uf.union(this.matrixToUfIndex(i - 1, j), currentElementPosition);
        }
        if (i < this.size - 1 && this.grid[i + 1][j] != 0) {
            this.uf.union(this.matrixToUfIndex(i + 1, j), currentElementPosition);
        }
        if (j > 0 && this.grid[i][j - 1] != 0) {
            this.uf.union(this.matrixToUfIndex(i, j - 1), currentElementPosition);
        }
        if (j < this.size - 1 && this.grid[i][j + 1] != 0) {
            this.uf.union(this.matrixToUfIndex(i, j + 1), currentElementPosition);
        }

    }

    // is site (row, col) open?
    public boolean isOpen(final int row, final int col) {

        if (!this.cellCoordinatesAreLegal(row, col)) {
            throw new IllegalArgumentException("Row: " + row + "; Column: " + col);
        }

        return this.grid[row - 1][col - 1] != 0;
    }

    // number of open sites
    public int numberOfOpenSites() {
        return this.openSitesCount;
    }

    // is site (row, col) full?
    public boolean isFull(final int row, final int col) {

        if (!this.cellCoordinatesAreLegal(row, col)) {
            throw new IllegalArgumentException("Row: " + row + "; Column: " + col);
        }
        final int i = row - 1;
        final int j = col - 1;

        int currentElementPosition = this.matrixToUfIndex(i, j);
        return this.isOpen(row, col) && this.uf.connected(this.virtualTopElementPosition, currentElementPosition);
    }

    // does the system percolate?
    public boolean percolates() {
        return this.uf.connected(this.virtualTopElementPosition, this.virtualBottomElementPosition);
    }

    /**
     * Main method
     */
    public static void main(String[] args) {
//        int n = StdIn.readInt();
//        while (StdIn.isEmpty()) {
//
//        }
    }

}
