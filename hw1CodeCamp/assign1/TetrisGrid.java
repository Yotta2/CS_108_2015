//
// TetrisGrid encapsulates a tetris board and has
// a clearRows() capability.
package assign1;

public class TetrisGrid {
	
	/**
	 * Constructs a new instance with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public TetrisGrid(boolean[][] grid) {
		this.grid = grid;
	}
	
	
	/**
	 * Does row-clearing on the grid (see handout).
	 */
	public void clearRows() {
		for (int i = grid[0].length - 1; i >= 0 ; i--)
			if (isFullRow(i))
				shiftRow(i);
	}

	/**
	 * Returns the internal 2d grid array.
	 * @return 2d grid array
	 */
	boolean[][] getGrid() {
		return grid;
	}
	
	boolean isFullRow(int y) {
		for (int i = 0; i < grid.length; i++)
			if (grid[i][y] != true)
				return false;
		return true;
	}

	private void shiftRow(int y) {
		for (int i = y; i < grid[0].length; i++)
			for (int j = 0; j < grid.length; j++) {
				if (i == grid[0].length - 1)
					grid[j][i] = false;
				else
					grid[j][i] = grid[j][i + 1];
			}
	}

	private boolean[][] grid;
}