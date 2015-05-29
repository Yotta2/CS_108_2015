package assign4;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class BankTests {

	private static int WORKERS = 100;

	@Before
	public void setUp() {
		testBank = new Bank();
	}

	@Test
	public void testProcess0() throws IOException {
		testBank.process("input/5k.txt", WORKERS);
		List<Account> accounts = testBank.getAccounts();
		for (int i = 0; i < Bank.ACCOUNT_COUNT; i++)
			assertEquals(Bank.ACCOUNT_INIT_BAL, accounts.get(i).getBal());
	}

	@Test
	public void testProcess1() throws IOException {
		testBank.process("input/100k.txt", WORKERS);
		List<Account> accounts = testBank.getAccounts();
		for (int i = 0; i < Bank.ACCOUNT_COUNT; i++)
			assertEquals(Bank.ACCOUNT_INIT_BAL, accounts.get(i).getBal());
	}

	private Bank testBank;
}
