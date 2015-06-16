package model;

import java.util.HashMap;

/**
 * 
 * @author Yotta2
 *
 */
public class AccountManager {

	public AccountManager() {
		nameToPassword = new HashMap<>();
		nameToPassword.put("Patrick", "1234");
		nameToPassword.put("Molly", "FloPup");
	}

	/**
	 * Check if an account exists
	 */
	public boolean exists(String name) {
		return nameToPassword.containsKey(name);
	}

	/**
	 * Check if the given account name and password matches.
	 * Return false if such account doesn't exist.
	 */
	public boolean matched(String name, String password) {
		if (!exists(name))
			return false;

		return nameToPassword.get(name).equals(password);
	}

	/**
	 * Try to create a new account, return false if such account name exists.
	 */
	public boolean createAccount(String name, String password) {
		if (exists(name))
			return false;
		nameToPassword.put(name, password);
		return true;
	}

	private HashMap<String, String> nameToPassword;
}
