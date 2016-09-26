package com.es.codinghub.api.entities;

import com.es.codinghub.api.facade.OnlineJudge;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

public class UserTest {

	private User u1, u2, u3, u4;

	@Test
	public void testConstructor() {
		u1 = new User("vct@mail.com", "123123");

		assertEquals(0, u1.getId());
		assertEquals("vct@mail.com", u1.getEmail());
		assertEquals("123123", u1.getPassword());
		assertTrue(u1.getAccounts().isEmpty());

		u2 = new User("arthur@mail.com", "password");

		assertEquals(0, u2.getId());
		assertEquals("arthur@mail.com", u2.getEmail());
		assertEquals("password", u2.getPassword());
		assertTrue(u2.getAccounts().isEmpty());
	}

	@Test
	public void testSetPassword() {
		u1 = new User("vct@mail.com", "123123");
		u1.setPassword("password");

		assertEquals("password", u1.getPassword());

		u2 = new User("arthur@mail.com", "password");
		u2.setPassword("123123");

		assertEquals("123123", u2.getPassword());
	}

	@Test
	public void testAccounts() {
		u1 = new User("vct@mail.com", "123123");

		try {
			u1.addAccount(OnlineJudge.Codeforces, "VictorAA");
		} catch (IllegalArgumentException e) {
			fail();
		}

		try {
			u1.addAccount(OnlineJudge.Codeforces, "VictorAA");
			fail();
		} catch (IllegalArgumentException e) {}

		assertEquals(1, u1.getAccounts().size());
		assertTrue(u1.getAccounts().contains(new Account(OnlineJudge.Codeforces, "VictorAA")));

		u1.removeAccount(0);

		assertEquals(0, u1.getAccounts().size());
		assertFalse(u1.getAccounts().contains(new Account(OnlineJudge.Codeforces, "VictorAA")));

		u1.removeAccount(0);

		assertEquals(0, u1.getAccounts().size());
		assertFalse(u1.getAccounts().contains(new Account(OnlineJudge.Codeforces, "VictorAA")));
	}

	@Test
	public void testEquality() {
		u1 = new User("vct@mail.com", "123123");
		u2 = new User("vct@mail.com", "password");

		u2.addAccount(OnlineJudge.Codeforces, "VictorAA");

		u3 = new User("arthur@mail.com", "123123");
		u4 = new User("arthur@mail.com", "password");

		u4.addAccount(OnlineJudge.UVa, "arthurCF");

		assertEquals(u1, u1);
		assertEquals(u3, u3);

		assertEquals(u1.hashCode(), u1.hashCode());
		assertEquals(u3.hashCode(), u3.hashCode());

		assertEquals(u1, u2);
		assertEquals(u2, u1);

		assertEquals(u3, u4);
		assertEquals(u4, u3);

		assertEquals(u1.hashCode(), u2.hashCode());
		assertEquals(u3.hashCode(), u4.hashCode());
	}

	@Test
	public void testInequality() {
		u1 = new User("vct@mail.com", "123123");
		u2 = new User("arthur@mail.com", "123123");

		u3 = new User("dqt@mail.com", "password");
		u4 = new User("mari@mail.com", "password");

		u3.addAccount(OnlineJudge.Codeforces, "VictorAA");
		u4.addAccount(OnlineJudge.Codeforces, "VictorAA");

		assertNotEquals(u1, u2);
		assertNotEquals(u1, u3);
		assertNotEquals(u1, u4);

		assertNotEquals(u2, u1);
		assertNotEquals(u2, u3);
		assertNotEquals(u2, u4);

		assertNotEquals(u3, u1);
		assertNotEquals(u3, u2);
		assertNotEquals(u3, u4);

		assertNotEquals(u4, u1);
		assertNotEquals(u4, u2);
		assertNotEquals(u4, u3);
	}

	@Test
	public void testJSON() {
		u1 = new User("vct@mail.com", "123123");
		u2 = new User("vct@mail.com", "123123");

		u2.addAccount(OnlineJudge.Codeforces, "VictorAA");
		u2.addAccount(OnlineJudge.UVa, "arcthurCF");

		assertEquals("{\"id\":0,\"email\":\"vct@mail.com\",\"accounts\":[]}", u1.toJSONString());
		assertEquals("{\"id\":0,\"email\":\"vct@mail.com\",\"accounts\":[{\"id\":0,\"judge\":\"Codeforces\",\"username\":\"VictorAA\"},{\"id\":0,\"judge\":\"UVa\",\"username\":\"arcthurCF\"}]}", u2.toJSONString());
	}
}
