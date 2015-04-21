package assign1;

import java.util.*;

public class Appearances {
	
	/**
	 * Returns the number of elements that appear the same number
	 * of times in both collections. Static method. (see handout).
	 * @return number of same-appearance elements
	 */
	public static <T> int sameCount(Collection<T> a, Collection<T> b) {
		HashMap<T, Integer> aElemCountHM = makeHM(a);
		HashMap<T, Integer> bElemCountHM = makeHM(b);
		int count = 0;
		for (Map.Entry<T, Integer> entry : aElemCountHM.entrySet()) {
			T elem = entry.getKey();
			int app = entry.getValue();
			if (bElemCountHM.containsKey(elem) && bElemCountHM.get(elem) == app)
				count++;
		}
		return count;
	}
	
	public static <T> HashMap<T, Integer> makeHM(Collection<T> col) {
		HashMap<T, Integer> elemCountHM = new HashMap<T, Integer>();
		Iterator<T> itr = col.iterator();
		while (itr.hasNext()) {
			T elem = itr.next();
			if (elemCountHM.containsKey(elem))
				elemCountHM.put(elem, elemCountHM.get(elem) + 1);
			else
				elemCountHM.put(elem, 1);
		}
		return elemCountHM;
	}
}
