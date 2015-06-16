package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import model.AccountManager;

import org.junit.Before;
import org.junit.Test;


public class AccountManagerTest {
	@Before
	public void setUp() {
		accountMgr = new AccountManager();
	}
	
	@Test
	public void testExists() {
		assertTrue(accountMgr.exists("Patrick"));
		assertTrue(accountMgr.exists("Molly"));
	}

	@Test
	public void testMatched() {
		assertTrue(accountMgr.matched("Patrick", "1234"));
		assertTrue(accountMgr.matched("Molly", "FloPup"));
	}

	@Test
	public void testCreateAccount() {
		assertFalse(accountMgr.createAccount("Patrick", "Patrick"));
		
		assertFalse(accountMgr.exists("Yotta2"));
		assertTrue(accountMgr.createAccount("Yotta2", "1234"));
		assertTrue(accountMgr.exists("Yotta2"));
	}

	private AccountManager accountMgr;
}
