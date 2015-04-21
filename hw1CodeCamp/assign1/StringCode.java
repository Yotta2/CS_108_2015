package assign1;

import java.util.HashSet;
import java.util.Set;

// CS108 HW1 -- String static methods

public class StringCode {

	/**
	 * Given a string, returns the length of the largest run.
	 * A a run is a series of adajcent chars that are the same.
	 * @param str
	 * @return max run length
	 */
	public static int maxRun(String str) {
		if (str.isEmpty())
			return 0;
		int i = 0;
		int maxLen = 0;
		while (true) {
			int count = 1;
			while (i + 1 < str.length() && str.charAt(i) == str.charAt(i + 1)) {
				i++;
				count++;
			}
			maxLen = Math.max(count, maxLen);
			i++;
			if (i == str.length())
				break;
		}
		return maxLen;
	}

	
	/**
	 * Given a string, for each digit in the original string,
	 * replaces the digit with that many occurrences of the character
	 * following. So the string "a3tx2z" yields "attttxzzz".
	 * @param str
	 * @return blown up string
	 */
	public static String blowup(String str) {
		String blowed = "";
		for (int i = 0; i < str.length(); i++)
			if (Character.isDigit(str.charAt(i))) {
				if (i != str.length() - 1) {
					for (int j = 0; j < str.charAt(i) - '0'; j++)
						blowed += str.charAt(i + 1);
				}
			} else {
				blowed += str.charAt(i);
			}
				
		return blowed;
	}
	
	/**
	 * Given 2 strings, consider all the substrings within them
	 * of length len. Returns true if there are any such substrings
	 * which appear in both strings.
	 * Compute this in linear time using a HashSet. Len will be 1 or more.
	 */
	public static boolean stringIntersect(String a, String b, int len) {
		if (len > a.length() || len > b.length())
			return false;
		HashSet<String> subStrs = new HashSet<String>();
		int i = 0;
		while (i + len - 1 < a.length()) {
			subStrs.add(a.substring(i, i + len - 1));
			i++;
		}
		i = 0;
		while (i + len - 1 < b.length()) {
			if (subStrs.contains(b.substring(i, i + len - 1)))
				return true;
			i++;
		}
		return false;
	}
}
