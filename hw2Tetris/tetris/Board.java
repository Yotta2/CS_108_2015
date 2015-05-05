// Board.java
package tetris;

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{

	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;

	// Here a few trivial methods are provided:
	
	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		committed = true;
		
		maxHeight = 0;
		grid = new boolean[width][height];
		xGrid = new boolean[width][height];
		widths = new int[height];
		xWidths = new int[height];
		heights = new int[width];
		xHeights = new int[width];
	}
	
	
	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}
	
	
	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}
	
	
	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {	 
		return maxHeight;
	}
	
	
	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
		if (DEBUG) {
			for (int x = 0; x < width; x++) {
				int h = height - 1;
				while (h >= 0 && grid[x][h] != true)
					h--;
				int rightHeight = h + 1;
				if (heights[x] != rightHeight) {
					System.out.println(toString());
					throw new RuntimeException("Internal Error: column " + x + " height incorrect! Was supposed to be " + rightHeight + ", but internally stored value is " + heights[x]);
				}
			}
			for (int y = 0; y < height; y++) {
				int count = 0;
				for (int x = 0; x < width; x++)
					if (grid[x][y])
						count++;
				if (widths[y] != count)
					throw new RuntimeException("Internal Error: row " + y + " width incorrect! Was supposed to be " + count + ", but internally stored value is " + widths[y]);
			}
			int tmpMaxHeight = 0;
			for (int x = 0; x < width; x++)
				tmpMaxHeight = Math.max(tmpMaxHeight, heights[x]);
			if (tmpMaxHeight != maxHeight)
				throw new RuntimeException("Internal Error: maxHeight incorrect! Was supposed to be " + tmpMaxHeight + ", but internally stored value is " + maxHeight);
		}
	}
	
	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.
	 
	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public int dropHeight(Piece piece, int x) {
		int y = 0;
		for (int pieceX = 0; pieceX < piece.getWidth(); pieceX++)
			y = Math.max(heights[x + pieceX] - piece.getSkirt()[pieceX], y);
		return y;
	}
	
	
	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		return heights[x];
	}
	
	
	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		 return widths[y];
	}
	
	
	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			return true;
		return grid[x][y];
	}

	
	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.
	 
	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		if (!committed) throw new RuntimeException("place commit problem");
		committed = false;

		int result = PLACE_OK;
		
		for (TPoint point : piece.getBody()) {
			int gridX = x + point.x;
			int gridY = y + point.y;
			if (gridX < 0 || gridX >= width || gridY < 0 || gridY >= height) {
				result = PLACE_OUT_BOUNDS;
				break;
			}
			if (grid[gridX][gridY]) {
				result = PLACE_BAD;
				break;
			}
			grid[gridX][gridY] = true;
			widths[gridY]++;
			heights[gridX] = Math.max(heights[gridX], gridY + 1);
			maxHeight = Math.max(maxHeight, heights[gridX]);
			if (widths[gridY] == width)
				result = PLACE_ROW_FILLED;
		}
		
		if (result == PLACE_OK || result == PLACE_ROW_FILLED)
			sanityCheck();

		return result;
	}
	
	
	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		committed = false;
		int rowsCleared = 0;
		int to = -1;
		int from = 0;
		for (int i = 0; i < height; i++)
			if (widths[i] == width) {
				if (to == -1)
					to = i;
				rowsCleared++;
			}
		if (to != -1) {
			from = to + 1;
			while (to < height) {
				while (from < height && widths[from] == width)
					from++;
				if (from >= height) {
					for (int x = 0; x < width; x++)
						grid[x][to] = false;
					widths[to] = 0;
				} else {
					for (int x = 0; x < width; x++)
						grid[x][to] = grid[x][from];
					widths[to] = widths[from];
				}
				from++;
				to++;
				if (to >= maxHeight)
					break;
			}
		}
		updateHeights();
		maxHeight -= rowsCleared;
		sanityCheck();
		return rowsCleared;
	}

	private void updateHeights() {
		for (int x = 0; x < width; x++) {
			int h = height - 1;
			while (h >= 0 && grid[x][h] != true)
				h--;
			heights[x] = h + 1;
		}
	}

	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		if (committed)
			return;
		maxHeight = xMaxHeight;
		for (int x = 0; x < width; x++)
			System.arraycopy(xGrid[x], 0, grid[x], 0, grid[0].length);
		System.arraycopy(xWidths, 0, widths, 0, widths.length);
		System.arraycopy(xHeights, 0, heights, 0, heights.length);
		committed = true;
		sanityCheck();
	}
	
	
	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		if (committed)
			return;
		committed = true;
		xMaxHeight = maxHeight;
		for (int x = 0; x < width; x++)
			System.arraycopy(grid[x], 0, xGrid[x], 0, grid[0].length);
		System.arraycopy(widths, 0, xWidths, 0, widths.length);
		System.arraycopy(heights, 0, xHeights, 0, heights.length);
	}


	
	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility) 
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height - 1; y >= 0; y--) {
			buff.append('|');
			for (int x = 0; x < width; x++) {
				if (getGrid(x, y))
					buff.append('+');
				else
					buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x = 0; x < width + 2; x++)
			buff.append('-');
		return (buff.toString());
	}
	
	// Some ivars are stubbed out for you:
	private boolean DEBUG = true;
	private int width;
	private int height;
	private boolean committed;
	private int maxHeight;
	private boolean[][] grid;
	private int[] widths;
	private int[] heights;
	private int xMaxHeight;
	private boolean[][] xGrid;
	private int[] xWidths;
	private int[] xHeights;
}


