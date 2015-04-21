// TabooTest.java
// Taboo class tests -- nothing provided.
package assign1;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

public class TabooTest {
	// utility -- converts a string to a list with one
	// elem for each char.
	private List<String> stringToList(String s) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < s.length(); i++) {
			list.add(String.valueOf(s.charAt(i)));
			// note: String.valueOf() converts lots of things to string form
		}
		return list;
	}

	@Test
	public void testNoFollow1() {
		List<String> rules = stringToList("acab");
		Taboo<String> taboo = new Taboo<String>(rules);
		assertEquals(new HashSet<String>(Arrays.asList("c", "b")), taboo.noFollow("a"));
		assertEquals(new HashSet<String>(Arrays.asList("a")), taboo.noFollow("c"));
	}

	@Test
	public void testNoFollow2() {
		List<String> rules = stringToList("acabad");
		Taboo<String> taboo = new Taboo<String>(rules);
		assertEquals(new HashSet<String>(Arrays.asList("c", "b", "d")), taboo.noFollow("a"));
		assertEquals(new HashSet<String>(Arrays.asList("a")), taboo.noFollow("c"));
		assertEquals(Collections.emptySet(), taboo.noFollow("d"));
	}
	
	@Test
	public void testNoFollow3() {
		List<String> rules = new ArrayList<String>(Arrays.asList("a", "b", null, "c", "d"));
		Taboo<String> taboo = new Taboo<String>(rules);
		assertEquals(Collections.emptySet(), taboo.noFollow("b"));
		assertEquals(new HashSet<String>(Arrays.asList("b")), taboo.noFollow("a"));
	}
	
	@Test
	public void testReduce1() {
		List<String> rules = stringToList("acab");
		Taboo<String> taboo = new Taboo<String>(rules);
		List<String> list = new ArrayList<String>(Arrays.asList("a", "c", "b", "x", "c", "a"));
		taboo.reduce(list);
		assertEquals(Arrays.asList("a", "x", "c"), list);
	}
	
	@Test
	public void testReduce2() {
		List<String> rules = stringToList("acabxc");
		Taboo<String> taboo = new Taboo<String>(rules);
		List<String> list = new ArrayList<String>(Arrays.asList("a", "c", "b", "x", "c", "a"));
		taboo.reduce(list);
		assertEquals(Arrays.asList("a", "x", "a"), list);
	}
	
	@Test
	public void testReduce3() {
		List<String> rules = stringToList("bacabxcx");
		Taboo<String> taboo = new Taboo<String>(rules);
		List<String> list = new ArrayList<String>(Arrays.asList("a", "c", "b", "x", "c", "a"));
		taboo.reduce(list);
		assertEquals(Arrays.asList("a", "x", "a"), list);
	}
	
	@Test
	public void testReduce4() {
		List<String> rules = new ArrayList<String>(Arrays.asList("a", "b", null, "c", "d"));
		Taboo<String> taboo = new Taboo<String>(rules);
		List<String> list = new ArrayList<String>(Arrays.asList("a", "c", "b", "c", "d"));
		taboo.reduce(list);
		assertEquals(Arrays.asList("a", "c", "b", "c"), list);
	}
}
