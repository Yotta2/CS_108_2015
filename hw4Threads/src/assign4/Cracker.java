package assign4;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CountDownLatch;

public class Cracker {
	// Array of chars used to produce strings
	public static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789.,-!".toCharArray();	
	
	public Cracker(int workerCount) {
		this.workerCount = workerCount;
	}

	/*
	 Given a byte[] array, produces a hex String,
	 such as "234a6f". with 2 chars for each byte in the array.
	 (provided code)
	*/
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}
	
	/*
	 Given a string of hex byte values such as "24a26f", creates
	 a byte[] array of those values, one byte value -128..127
	 for each 2 chars.
	 (provided code)
	*/
	public static byte[] hexToArray(String hex) {
		byte[] result = new byte[hex.length()/2];
		for (int i=0; i<hex.length(); i+=2) {
			result[i/2] = (byte) Integer.parseInt(hex.substring(i, i+2), 16);
		}
		return result;
	}
	
	// possible test values:
	// a 86f7e437faa5a7fce15d1ddcb9eaeaea377667b8
	// fm adeb6f2a18fe33af368d91b09587b68e3abcb9a7
	// a! 34800e15707fae815d7c90d49de44aca97e2d759
	// xyz 66b27417d37e024c46526c2f6d358a754fc552f3
	
	public static void main(String args[]) throws NoSuchAlgorithmException {
		if (args.length == 1) {
			System.out.println(generate(args[0]));
		}
		else if (args.length == 3) {
			Cracker cracker = new Cracker(Integer.parseInt(args[2]));
			cracker.crack(args[0], Integer.parseInt(args[1]));
		}
	}

	/**
	 * Given a password, generate and return the SHA hash string.
	 */
	public static String generate(String password) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA");
		return hexToString(md.digest(password.getBytes()));
	}

	/**
	 * Given a string form of a hexadecimal hash value and the max length of to-be-cracked password,
	 * crack the password concurrently. The worker thread will print out all possible password to console 
	 */
	public void crack(String hashValue, int maxLen) {
		latch = new CountDownLatch(workerCount);
		for (int i = 0; i < workerCount; i++) {
			int startIndex = CHARS.length / workerCount * i;
			int endIndex = (i == workerCount - 1) ? CHARS.length - 1 : startIndex + CHARS.length / workerCount - 1;
			Thread worker = new Worker(startIndex, endIndex, hashValue, maxLen);
			worker.start();
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("all done");
	}
	
	private final class Worker extends Thread {
		/**
		 * Create a worker thread object which will try all passwords starting from CHARS[startIndex ... endIndex]
		 */
		public Worker(int startIndex, int endIndex, String hashValue, int maxLen) {
			this.startIndex = startIndex;
			this.endIndex = endIndex;
			this.hashValue = hashValue;
			this.maxLen = maxLen;
		}
		
		@Override
		public void run() {
			for (int i = startIndex; i <= endIndex; i++) {
				for (int len = 1; len <= maxLen; len++) {
					char[] pwChars = new char[len];
					pwChars[0] = CHARS[i];
					recSolve(pwChars, 1, len - 1);
				}
			}
			latch.countDown();
		}
		
		private void recSolve(char[] pwChars, int startIndex, int endIndex) {
			if (startIndex > endIndex) {
				String password = String.valueOf(pwChars);
				try {
					String hashValue = generate(password);
					if (this.hashValue.equals(hashValue))
						System.out.println(password);
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return;
			}
			for (int i = 0; i < CHARS.length; i++) {
				pwChars[startIndex] = CHARS[i];
				recSolve(pwChars, startIndex + 1, endIndex);
			}
		}

		private int startIndex;
		private int endIndex;
		private String hashValue;
		private int maxLen;		
	}

	private CountDownLatch latch;
	private int workerCount;
}
