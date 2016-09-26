package com.es.codinghub.api.entities;

import com.es.codinghub.api.facade.OnlineJudge;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ProblemTest {

	private Problem p1, p2, p3, p4;

	@Test
	public void testConstructor() {
		p1 = new Problem("709B", "Vanya and Strings", OnlineJudge.Codeforces);

		assertEquals("709B", p1.getId());
		assertEquals("Vanya and Strings", p1.getName());
		assertEquals(OnlineJudge.Codeforces, p1.getJudge());
		assertEquals(-1, (long) p1.getSolvedCount());

		p2 = new Problem("722E", "Xenia and Strings", OnlineJudge.UVa, 2781);

		assertEquals("722E", p2.getId());
		assertEquals("Xenia and Strings", p2.getName());
		assertEquals(OnlineJudge.UVa, p2.getJudge());
		assertEquals(2781, (long) p2.getSolvedCount());
	}

	@Test
	public void testEquality() {
		p1 = new Problem("709B", "Vanya and Strings", OnlineJudge.Codeforces);
		p2 = new Problem("709B", "Xenia and Strings", OnlineJudge.UVa, 2781);

		p3 = new Problem("722E", "Vanya and Strings", OnlineJudge.Codeforces);
		p4 = new Problem("722E", "Xenia and Strings", OnlineJudge.UVa, 2781);

		assertEquals(p1, p1);
		assertEquals(p3, p3);

		assertEquals(p1.hashCode(), p1.hashCode());
		assertEquals(p3.hashCode(), p3.hashCode());

		assertEquals(p1, p2);
		assertEquals(p2, p1);

		assertEquals(p3, p4);
		assertEquals(p4, p3);

		assertEquals(p1.hashCode(), p2.hashCode());
		assertEquals(p3.hashCode(), p4.hashCode());
	}

	@Test
	public void testInequality() {
		p1 = new Problem("709B", "Vanya and Strings", OnlineJudge.Codeforces);
		p2 = new Problem("722E", "Vanya and Strings", OnlineJudge.Codeforces);

		p3 = new Problem("709E", "Xenia and Strings", OnlineJudge.UVa, 2781);
		p4 = new Problem("722B", "Xenia and Strings", OnlineJudge.UVa, 2781);

		assertNotEquals(p1, p2);
		assertNotEquals(p1, p3);
		assertNotEquals(p1, p4);

		assertNotEquals(p2, p1);
		assertNotEquals(p2, p3);
		assertNotEquals(p2, p4);

		assertNotEquals(p3, p1);
		assertNotEquals(p3, p2);
		assertNotEquals(p3, p4);

		assertNotEquals(p4, p1);
		assertNotEquals(p4, p2);
		assertNotEquals(p4, p3);
	}

	@Test
	public void testJSON() {
		p1 = new Problem("709B", "Vanya and Strings", OnlineJudge.Codeforces);
		p2 = new Problem("722E", "Xenia and Strings", OnlineJudge.UVa, 2781);

		assertEquals("{\"id\":\"709B\",\"name\":\"Vanya and Strings\",\"judge\":\"Codeforces\",\"solvedCount\":-1}", p1.toJSONString());
		assertEquals("{\"id\":\"722E\",\"name\":\"Xenia and Strings\",\"judge\":\"UVa\",\"solvedCount\":2781}", p2.toJSONString());
	}
}
