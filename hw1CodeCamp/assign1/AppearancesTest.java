package assign1;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;

public class AppearancesTest {
	// utility -- converts a string to a list with one
	// elem for each char.
	private List<String> stringToList(String s) {
		List<String> list = new ArrayList<String>();
		for (int i=0; i<s.length(); i++) {
			list.add(String.valueOf(s.charAt(i)));
			// note: String.valueOf() converts lots of things to string form
		}
		return list;
	}
	
	@Test
	public void testSameCount1() {
		List<String> a = stringToList("abbccc");
		List<String> b = stringToList("cccbba");
		assertEquals(3, Appearances.sameCount(a, b));
	}
	
	@Test
	public void testSameCount2() {
		// basic List<Integer> cases
		List<Integer> a = Arrays.asList(1, 2, 3, 1, 2, 3, 5);
		assertEquals(1, Appearances.sameCount(a, Arrays.asList(1, 9, 9, 1)));
		assertEquals(2, Appearances.sameCount(a, Arrays.asList(1, 3, 3, 1)));
		assertEquals(1, Appearances.sameCount(a, Arrays.asList(1, 3, 3, 1, 1)));
	}
	
	@Test
	public void testSameCount3() {
		List<String> aList = stringToList("abbccc");
		List<String> bList = stringToList("cccbba");
		HashSet<String> a = new HashSet<String>(aList);
		HashSet<String> b = new HashSet<String>(bList);
		
		assertEquals(3, Appearances.sameCount(a, b));
	}
	
	@Test
	public void testSameCount4() {
		List<String> aList = stringToList("abbccca");
		List<String> bList = stringToList("cccbbab");
		LinkedList<String> a = new LinkedList<String>(aList);
		LinkedList<String> b = new LinkedList<String>(bList);
		
		assertEquals(1, Appearances.sameCount(a, b));
	}
}
