package assign3;

import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
	// Provided grid data for main/testing
	// The instance variable strategy is up to you.
	
	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
	"1 6 4 0 0 0 0 0 2",
	"2 0 0 4 0 3 9 1 0",
	"0 0 5 0 8 0 4 0 7",
	"0 9 0 0 0 6 5 0 0",
	"5 0 0 1 0 2 0 0 8",
	"0 0 8 9 0 0 0 3 0",
	"8 0 9 0 4 0 2 0 0",
	"0 7 3 5 0 9 0 0 1",
	"4 0 0 0 0 0 6 7 9");
	
	
	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
	 "530070000",
	 "600195000",
	 "098000060",
	 "800060003",
	 "400803001",
	 "700020006",
	 "060000280",
	 "000419005",
	 "000080079");
	
	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
	"3 7 0 0 0 0 0 8 0",
	"0 0 1 0 9 3 0 0 0",
	"0 4 0 7 8 0 0 0 3",
	"0 9 3 8 0 0 0 1 2",
	"0 0 0 0 4 0 0 0 0",
	"5 2 0 0 0 6 7 9 0",
	"6 0 0 0 2 1 0 4 0",
	"0 0 0 5 3 0 9 0 0",
	"0 3 0 0 0 0 0 5 1");
	
	
	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;
	
	// Provided various static utility methods to
	// convert data formats to int[][] grid.
	
	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}
	
	
	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}
		
		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}
	
	/**
	 * Given a 9x9 grid, return the text form of the grid
	 * @param g 2d array
	 * @return text string of 81 numbers
	 */
	public static String gridToText(int[][] g) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < g.length; i++) {
			for (int j = 0; j < g[0].length; j++)
				sb.append(" " + g[i][j]);
			sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}


	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		Sudoku sudoku;
		sudoku = new Sudoku(hardGrid);
		
		System.out.println(sudoku); // print the raw problem
		int count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());
	}
	
	
	

	/**
	 * Sets up based on the given ints.
	 */
	public Sudoku(int[][] ints) {
		grid = ints.clone();
		sol = grid.clone();
		findUnsignedSpots();
		Collections.sort(unsignedSpots);
		solText = "";
	}

	/**
	 * Sets up based on the given grid text
	 */
	public Sudoku(String text) {
		this(textToGrid(text));
	}

	@Override
	public String toString() {
		return gridToText(grid);
	}


	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	public int solve() {
		startTime = System.currentTimeMillis();
		count = 0;

		recSolve(0);
		timeConsumed = System.currentTimeMillis() - startTime;

		return count;
	}
	
	public String getSolutionText() {
		return solText;
	}
	
	public long getElapsed() {
		return timeConsumed;
	}

	/**
	 * get a list of unsigned spots of the original grid
	 * @return
	 */
	public List<Spot> getUnsignedSpots() {
		return unsignedSpots;
	}

	/*
	 * Spot in sol grid
	 */
	public final class Spot implements Comparable<Spot>
	{
		public Spot(int r, int c, int num) {
			this.r = r;
			this.c = c;
			sol[r][c] = num;
			assignableNums = new ArrayList<Integer>();
			findAssignableNums();
		}

		@Override
		public int compareTo(Spot spot) {
			return assignableNums.size() - spot.getAssignableNums().size();
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("row: " + r + ",");
			sb.append("col: " + c + ",");
			sb.append("num: " + sol[r][c] + ",");
			sb.append("assignable nums: " + assignableNums + "\n");

			return sb.toString();
		}

		public int getNum() {
			return sol[r][c];
		}

		/**
		 * Attempt to set spot num, return true if setting such num is legal(not violating all rules),
		 * other return false, and leaving the sol grid intact
		 */
		public boolean setNum(int num) {
			if (num == 0) {
				sol[r][c] = num;
				return true;
			}

			for (int i = 0; i < SIZE; i++)
				if (sol[i][c] == num)
					return false;
			for (int j = 0; j < SIZE; j++)
				if (sol[r][j] == num)
					return false;
			for (int i = r / PART * PART; i < r / PART * PART + PART; i++)
				for (int j = c / PART * PART; j < c / PART * PART + PART; j++)
					if (sol[i][j] == num)
						return false;

			sol[r][c] = num;

			return true;
		}

		public List<Integer> getAssignableNums() {
			return assignableNums;
		}

		private void findAssignableNums() {
			boolean[] assignable = new boolean[10];
			Arrays.fill(assignable, true);

			for (int i = 0; i < SIZE; i++)
				assignable[grid[i][c]] = false;
			for (int j = 0; j < SIZE; j++)
				assignable[grid[r][j]] = false;
			for (int i = r / PART * PART; i < r / PART * PART + PART; i++)
				for (int j = c / PART * PART; j < c / PART * PART + PART; j++)
					assignable[grid[i][j]] = false;
			for (int i = 1; i < 10; i++)
				if (assignable[i])
					assignableNums.add(i);
		}

		private int r, c;
		private List<Integer> assignableNums; // assignable numbers for this spot, based on the original grid
	}

	private void findUnsignedSpots()
	{
		unsignedSpots = new ArrayList<Spot>();

		for (int r = 0; r < grid.length; r++)
			for (int c = 0; c < grid[0].length; c++)
				if (grid[r][c] == 0)
					unsignedSpots.add(new Spot(r, c, 0));
	}

	/*
	 * find all solution, given a current grid sol, and unsigned spots i...unsignedSpots.size() - 1
	 */
	private void recSolve(int i)
	{
		if (count == MAX_SOLUTIONS)
			return;
		if (i == unsignedSpots.size()) {
			count++;
			if (count == 1)
				buildSolText();
			return;
		}
		Spot uSpot = unsignedSpots.get(i);
		for (int j = 0; j < uSpot.getAssignableNums().size(); j++) {
			if (uSpot.setNum(uSpot.getAssignableNums().get(j))) {
				//System.out.println("Assigned # " + i + " unsigned spot");
				//System.out.println(gridToText(sol));
				//System.out.println(uSpot);
				recSolve(i + 1);
				uSpot.setNum(0);
			}
		}
	}

	private void buildSolText() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < sol.length; i++) {
			for (int j = 0; j < sol[0].length; j++)
				sb.append(" " + sol[i][j]);
			sb.append("\n");
		}
		solText =  sb.toString();
	}

	private int[][] grid;
	private int[][] sol;
	private int count;
	private long startTime;
	private long timeConsumed;
	private List<Spot> unsignedSpots; // unsigned spots in the original grid
	private String solText;
}
