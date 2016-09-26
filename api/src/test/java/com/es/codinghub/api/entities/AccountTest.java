package com.es.codinghub.api.entities;

import com.es.codinghub.api.facade.OnlineJudge;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class AccountTest {

	private Account acc1, acc2, acc3, acc4;

	@Test
	public void testConstructor() {
		acc1 = new Account(OnlineJudge.Codeforces, "VictorAA");

		assertEquals(0, acc1.getId());
		assertEquals(OnlineJudge.Codeforces, acc1.getJudge());
		assertEquals("VictorAA", acc1.getUsername());

		acc2 = new Account(OnlineJudge.UVa, "arthurCF");

		assertEquals(0, acc2.getId());
		assertEquals(OnlineJudge.UVa, acc2.getJudge());
		assertEquals("arthurCF", acc2.getUsername());
	}

	@Test
	public void testEquality() {
		acc1 = new Account(OnlineJudge.Codeforces, "VictorAA");
		acc2 = new Account(OnlineJudge.Codeforces, "VictorAA");

		acc3 = new Account(OnlineJudge.UVa, "arthurCF");
		acc4 = new Account(OnlineJudge.UVa, "arthurCF");

		assertEquals(acc1, acc1);
		assertEquals(acc3, acc3);

		assertEquals(acc1.hashCode(), acc1.hashCode());
		assertEquals(acc3.hashCode(), acc3.hashCode());

		assertEquals(acc1, acc2);
		assertEquals(acc2, acc1);

		assertEquals(acc3, acc4);
		assertEquals(acc4, acc3);

		assertEquals(acc1.hashCode(), acc2.hashCode());
		assertEquals(acc3.hashCode(), acc4.hashCode());
	}

	@Test
	public void testInequality() {
		acc1 = new Account(OnlineJudge.Codeforces, "VictorAA");
		acc2 = new Account(OnlineJudge.UVa, "VictorAA");

		acc3 = new Account(OnlineJudge.Codeforces, "arthurCF");
		acc4 = new Account(OnlineJudge.UVa, "arthurCF");

		assertNotEquals(acc1, acc2);
		assertNotEquals(acc1, acc3);
		assertNotEquals(acc1, acc4);

		assertNotEquals(acc2, acc1);
		assertNotEquals(acc2, acc3);
		assertNotEquals(acc2, acc4);

		assertNotEquals(acc3, acc1);
		assertNotEquals(acc3, acc2);
		assertNotEquals(acc3, acc4);

		assertNotEquals(acc4, acc1);
		assertNotEquals(acc4, acc2);
		assertNotEquals(acc4, acc3);
	}

	@Test
	public void testJSON() {
		acc1 = new Account(OnlineJudge.Codeforces, "VictorAA");
		acc2 = new Account(OnlineJudge.UVa, "VictorAA");

		acc3 = new Account(OnlineJudge.Codeforces, "arthurCF");
		acc4 = new Account(OnlineJudge.UVa, "arthurCF");

		assertEquals("{\"id\":0,\"judge\":\"Codeforces\",\"username\":\"VictorAA\"}", acc1.toJSONString());
		assertEquals("{\"id\":0,\"judge\":\"UVa\",\"username\":\"VictorAA\"}", acc2.toJSONString());

		assertEquals("{\"id\":0,\"judge\":\"Codeforces\",\"username\":\"arthurCF\"}", acc3.toJSONString());
		assertEquals("{\"id\":0,\"judge\":\"UVa\",\"username\":\"arthurCF\"}", acc4.toJSONString());
	}
}
