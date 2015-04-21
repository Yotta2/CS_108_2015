// Test cases for CharGrid -- a few basic tests are provided.
package assign1;

import static org.junit.Assert.*;
import org.junit.Test;

public class CharGridTest {
	
	@Test
	public void testCharArea1() {
		char[][] grid = new char[][] {
				{'a', 'y', ' '},
				{'x', 'a', 'z'},
			};
		
		
		CharGrid cg = new CharGrid(grid);
				
		assertEquals(4, cg.charArea('a'));
		assertEquals(1, cg.charArea('z'));
	}
	
	
	@Test
	public void testCharArea2() {
		char[][] grid = new char[][] {
				{'c', 'a', ' '},
				{'b', ' ', 'b'},
				{' ', ' ', 'a'}
			};
		
		CharGrid cg = new CharGrid(grid);
		
		assertEquals(6, cg.charArea('a'));
		assertEquals(3, cg.charArea('b'));
		assertEquals(1, cg.charArea('c'));
	}
	
	@Test
	public void testCharArea3() {
		char[][] grid = new char[][] {
				{'c', 'a', ' '},
				{'b', ' ', 'b'},
				{'b', ' ', 'b'},
				{' ', ' ', 'a'}
			};
		
		CharGrid cg = new CharGrid(grid);
		
		assertEquals(8, cg.charArea('a'));
		assertEquals(6, cg.charArea('b'));
		assertEquals(1, cg.charArea('c'));
		assertEquals(12, cg.charArea(' '));
	}
	
	@Test
	public void testCharArea4() {
		char[][] grid = new char[][] {
				{'8', '8', '8'},
				{'8', '8', '8'},
				{'8', '8', '8'}
		};
		
		CharGrid cg = new CharGrid(grid);
		assertEquals(9, cg.charArea('8'));
		assertEquals(0, cg.charArea('0'));
	}
	
	@Test
	public void testCharArea5() {
		char[][] grid = new char[][] {
				{'8', '8', '8'},
				{'8', '8', '8'},
				{'8', '8', '7'}
		};
		
		CharGrid cg = new CharGrid(grid);
		assertEquals(9, cg.charArea('8'));
		assertEquals(0, cg.charArea('0'));
	}

	@Test
	public void testCountPlus1() {
		char[][] grid = new char[][] {
				{' ', ' ', 'p', ' ', ' ', ' ', ' ', ' ', ' '},
				{' ', ' ', 'p', ' ', ' ', ' ', ' ', 'x', ' '},
				{'p', 'p', 'p', 'p', 'p', ' ', 'x', 'x', 'x'},
				{' ', ' ', 'p', ' ', ' ', 'y', ' ', 'x', ' '},
				{' ', ' ', 'p', ' ', 'y', 'y', 'y', ' ', ' '},
				{'z', 'z', 'z', 'z', 'z', 'y', 'z', 'z', 'z'},
				{' ', ' ', 'x', 'x', ' ', 'y', ' ', ' ', ' '},
		};
		
		CharGrid cg = new CharGrid(grid);
		assertEquals(2, cg.countPlus());
	}
	
	@Test
	public void testCountPlus2() {
		char[][] grid = new char[][] {
				{' ', ' ', 'p', ' ', ' ', ' ', ' ', ' ', ' '},
				{' ', ' ', 'p', ' ', ' ', ' ', ' ', 'x', ' '},
				{'p', 'p', 'p', 'p', 'p', ' ', 'x', 'x', 'x'},
				{' ', ' ', 'p', ' ', ' ', 'y', ' ', 'x', ' '},
				{' ', ' ', 'p', ' ', 'y', 'y', 'y', ' ', ' '},
				{'z', 'z', 'z', 'z', 'z', 'y', 'z', 'z', 'z'},
				{' ', ' ', 'x', 'x', ' ', ' ', ' ', ' ', ' '},
		};
		
		CharGrid cg = new CharGrid(grid);
		assertEquals(3, cg.countPlus());
	}
	
	@Test
	public void testCountPlus3() {
		char[][] grid = new char[][] {
				{'c', 'a', ' '},
				{'b', ' ', 'b'},
				{' ', ' ', 'a'}
			};
		
		CharGrid cg = new CharGrid(grid);
		
		assertEquals(0, cg.countPlus());
	}
	
	@Test
	public void testCountPlus4() {
		char[][] grid = new char[][] {
				{'c', 'a', ' '},
				{'a', 'a', 'a'},
				{' ', 'a', 'a'}
		};
		
		CharGrid cg = new CharGrid(grid);
		
		assertEquals(1, cg.countPlus());
	}
	
	@Test
	public void testCountPlus5() {
		char[][] grid = new char[][] {
				{'8', '8', '8'},
				{'8', '8', '8'},
				{'8', '8', '8'}
		};
		
		CharGrid cg = new CharGrid(grid);
		
		assertEquals(1, cg.countPlus());
	}
}
