package tetris;

import static org.junit.Assert.*;

import org.junit.*;

public class BoardTest {

	// This shows how to build things in setUp() to re-use
	// across tests.
	
	// In this case, setUp() makes shapes,
	// and also a 3X6 board, with pyr placed at the bottom,
	// ready to be used by tests.
	@Before
	public void setUp() throws Exception {
		b = new Board(3, 6);
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
		
		b.place(pyr1, 0, 0);
	}
	
	// Check the basic width/height/max after the one placement
	@Test
	public void testSample1() {
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
	}
	
	// Place sRotated into the board, then check some measures
	@Test
	public void testSample2() {
		b.commit();
		int result = b.place(sRotated, 1, 1);
		assertEquals(Board.PLACE_OK, result);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(4, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
		assertEquals(4, b.getMaxHeight());
	}
	
	// Make  more tests, by putting together longer series of 
	// place, clearRows, undo, place ... checking a few col/row/max
	// numbers that the board looks right after the operations.
	
	// Test dropHeight
	@Test
	public void testSample3() {
		b.commit();
		assertEquals(2, b.dropHeight(sRotated, 0));
		assertEquals(1, b.dropHeight(sRotated, 1));
		assertEquals(2, b.dropHeight(s, 0));
		assertEquals(1, b.dropHeight(pyr2, 1));
		assertEquals(2, b.dropHeight(pyr3, 0));
	}

	// Test place()/clearRows() series, and then an undo()
	@Test
	public void testSample4() {
		System.out.println(b.toString());
		b.clearRows();
		assertEquals(1, b.getMaxHeight());
		assertEquals(1, b.getRowWidth(0));
		assertEquals(0, b.getRowWidth(1));
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(1, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));
		
		b.undo();
		assertEquals(0, b.getMaxHeight());
		assertEquals(0, b.getRowWidth(0));
		assertEquals(0, b.getRowWidth(1));
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(0, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));
		
		b.place(pyr1, 0, 0);
		System.out.println(b.toString());
		b.commit();
		b.place(pyr1, 0, 2);
		System.out.println(b.toString());
		b.clearRows();
		assertEquals(2, b.getMaxHeight());
		assertEquals(1, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));
	}

	// Test place in the air
	@Test
	public void TestSample5() {
		b.undo();
		Piece square = new Piece(Piece.SQUARE_STR);
		assertEquals(Board.PLACE_OK, b.place(square, 0, 4));
		assertEquals(6, b.getColumnHeight(0));
		assertEquals(6, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));
		
		b.undo();
		assertEquals(Board.PLACE_OK, b.place(square, 0, 3));
		assertEquals(5, b.getColumnHeight(0));
		assertEquals(5, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));
	}

	// Advance testing, simulate a more complicated sequences
	@Test
	public void TestSample6() {
		// TODO
	}

	Board b;
	Piece pyr1, pyr2, pyr3, pyr4, s, sRotated;
}
