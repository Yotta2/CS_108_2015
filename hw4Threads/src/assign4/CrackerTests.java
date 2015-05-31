package assign4;

import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;

import org.junit.Test;

public class CrackerTests {
	@Test
	public void testGenerate() throws NoSuchAlgorithmException {
		assertEquals("86f7e437faa5a7fce15d1ddcb9eaeaea377667b8", Cracker.generate("a"));
		assertEquals("adeb6f2a18fe33af368d91b09587b68e3abcb9a7", Cracker.generate("fm"));
		assertEquals("34800e15707fae815d7c90d49de44aca97e2d759", Cracker.generate("a!"));
		assertEquals("66b27417d37e024c46526c2f6d358a754fc552f3", Cracker.generate("xyz"));
	}
	
	@Test
	public void testCrack0() throws NoSuchAlgorithmException {
		System.out.println("Testing: java assign4/Cracker 4181eecbd7a755d19fdf73887c54837cbecf63fd 5 8");
		Cracker.main(new String[]{"4181eecbd7a755d19fdf73887c54837cbecf63fd", "5", "8"});
		
		System.out.println("Testing: java assign4/Cracker 86f7e437faa5a7fce15d1ddcb9eaeaea377667b8 5 8");
		Cracker.main(new String[]{"86f7e437faa5a7fce15d1ddcb9eaeaea377667b8", "2", "8"});
		
		System.out.println("Testing: java assign4/Cracker adeb6f2a18fe33af368d91b09587b68e3abcb9a7 2 8");
		Cracker.main(new String[]{"adeb6f2a18fe33af368d91b09587b68e3abcb9a7", "2", "8"});
		
		System.out.println("Testing: java assign4/Cracker 34800e15707fae815d7c90d49de44aca97e2d759 2 8");
		Cracker.main(new String[]{"34800e15707fae815d7c90d49de44aca97e2d759", "2", "8"});
		
		System.out.println("Testing: java assign4/Cracker 66b27417d37e024c46526c2f6d358a754fc552f3 3 8");
		Cracker.main(new String[]{"66b27417d37e024c46526c2f6d358a754fc552f3", "3", "8"});
		
		System.out.println("Testing yotta2");
		Cracker.main(new String[]{Cracker.generate("yotta2"), "6", "20"});
	}
}
