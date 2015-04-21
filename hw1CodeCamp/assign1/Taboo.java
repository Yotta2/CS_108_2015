/*
 HW1 Taboo problem class.
 Taboo encapsulates some rules about what objects
 may not follow other objects.
 (See handout).
*/
package assign1;

import java.util.*;

public class Taboo<T> {
	
	/**
	 * Constructs a new Taboo using the given rules (see handout.)
	 * @param rules rules for new Taboo
	 */
	public Taboo(List<T> rules) {
		for (int i = 0; i < rules.size() - 1; i++) {
			T elem = rules.get(i);
			T tabooElem = rules.get(i + 1);
			if (tabooElem != null) {
				HashSet<T> taboos = elemNonFollowingSetHM.get(elem);
				if (taboos == null)
					taboos = new HashSet<T>();
				taboos.add(tabooElem);
				elemNonFollowingSetHM.put(elem, taboos);
			}
		}
	}
	
	/**
	 * Returns the set of elements which should not follow
	 * the given element.
	 * @param elem
	 * @return elements which should not follow the given element
	 */
	public Set<T> noFollow(T elem) {
		if (elemNonFollowingSetHM.containsKey(elem))
			return elemNonFollowingSetHM.get(elem);
		else
			return Collections.emptySet();
	}
	
	/**
	 * Removes elements from the given list that
	 * violate the rules (see handout).
	 * @param list collection to reduce
	 */
	public void reduce(List<T> list) {
		Iterator<T> itr = list.iterator();
		T curr = itr.next();
		while (itr.hasNext()) {
			T next = itr.next();
			if (elemNonFollowingSetHM.containsKey(curr))
				while (elemNonFollowingSetHM.get(curr).contains(next)) {
					itr.remove();
					if (!itr.hasNext())
						break;
					next = itr.next();
				}
			curr = next;
		}
	}
	
	private HashMap<T, HashSet<T>> elemNonFollowingSetHM = new HashMap<T, HashSet<T>>(); 
}
