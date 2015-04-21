// HW1 2-d array Problems
// CharGrid encapsulates a 2-d grid of chars and supports
// a few operations on the grid.

package assign1;

public class CharGrid {
	static final int[][] DELTA = {
			{-1, 0},
			{0, +1},
			{+1, 0},
			{0, -1}
	};

	/**
	 * Constructs a new CharGrid with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public CharGrid(char[][] grid) {
		this.grid = grid;
	}
	
	/**
	 * Returns the area for the given char in the grid. (see handout).
	 * @param ch char to look for
	 * @return area for given char
	 */
	public int charArea(char ch) {
		int count = countOcurrence(0, 0, grid.length - 1, grid[0].length - 1, ch);
		if (count == 0)
			return 0;
		int minArea = grid.length * grid[0].length;
		for (int uli = 0; uli < grid.length; uli++)
			for (int ulj = 0; ulj < grid[0].length; ulj++)
				for (int lri = uli; lri < grid.length; lri++)
					for (int lrj = ulj; lrj < grid[0].length; lrj++)
						if (count == countOcurrence(uli, ulj, lri, lrj, ch)) {
							minArea = Math.min(minArea, (lri - uli + 1) * (lrj - ulj + 1));
						}
		return minArea;
	}
	
	/**
	 * count the ocurrence of ch within a given rectangle
	 */
	private int countOcurrence(int uli, int ulj, int lri, int lrj, char ch) {
		int count = 0;
		for (int i = uli; i <= lri; i++)
			for (int j = ulj; j <= lrj; j++)
				count = (grid[i][j] == ch) ? count + 1 : count;
		return count;
	}

	/**
	 * Returns the count of '+' figures in the grid (see handout).
	 * @return number of + in grid
	 */
	public int countPlus() {
		int count = 0;
		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid[0].length; j++)
				if (allArmsHaveSameLen(i, j))
					count++;
		return count;
	}
	
	private boolean allArmsHaveSameLen(int centerX, int centerY) {
		char ch = grid[centerX][centerY];
		int[] armLens = new int[4];
		for (int i = 0; i < DELTA.length; i++)
			armLens[i] = getArmLen(i, centerX + DELTA[i][0], centerY + DELTA[i][1], ch);
		if (armLens[0] == 0)
			return false;
		for (int i = 0; i < DELTA.length - 1; i++)
			if (armLens[i] != armLens[i + 1])
				return false;
		return true;
	}
	
	private int getArmLen(int dir, int x, int y, char ch) {
		int len = 0;
		while (true) {
			if (x < 0 || x >= grid.length || y < 0 || y >= grid[0].length)
				break;
			if (grid[x][y] == ch)
				len++;
			else
				break;
			x += DELTA[dir][0];
			y += DELTA[dir][1];
		}
		return len;
	}
	
	// ivars
	private char[][] grid;
}
