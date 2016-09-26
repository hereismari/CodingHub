package com.es.codinghub.api.entities;

import com.es.codinghub.api.facade.OnlineJudge;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ContestTest {

	private Contest c1, c2, c3, c4;

	@Test
	public void testConstructor() {
		c1 = new Contest(OnlineJudge.Codeforces, "709B", 1474907875, 18000);

		assertEquals(OnlineJudge.Codeforces, c1.getJudge());
		assertEquals("709B", c1.getName());
		assertEquals(1474907875, c1.getTimestamp());
		assertEquals(18000, c1.getDuration());

		c2 = new Contest(OnlineJudge.UVa, "721E", 1474809237, 7200);

		assertEquals(OnlineJudge.UVa, c2.getJudge());
		assertEquals("721E", c2.getName());
		assertEquals(1474809237, c2.getTimestamp());
		assertEquals(7200, c2.getDuration());
	}

	@Test
	public void testEquality() {
		c1 = new Contest(OnlineJudge.Codeforces, "709B", 1474907875, 18000);
		c2 = new Contest(OnlineJudge.Codeforces, "709B", 1474809237, 7200);

		c3 = new Contest(OnlineJudge.UVa, "721E", 1474907875, 18000);
		c4 = new Contest(OnlineJudge.UVa, "721E", 1474809237, 7200);

		assertEquals(c1, c1);
		assertEquals(c3, c3);

		assertEquals(c1.hashCode(), c1.hashCode());
		assertEquals(c3.hashCode(), c3.hashCode());

		assertEquals(c1, c2);
		assertEquals(c2, c1);

		assertEquals(c3, c4);
		assertEquals(c4, c3);

		assertEquals(c1.hashCode(), c2.hashCode());
		assertEquals(c3.hashCode(), c4.hashCode());
	}

	@Test
	public void testInequality() {
		c1 = new Contest(OnlineJudge.Codeforces, "709B", 1474907875, 18000);
		c2 = new Contest(OnlineJudge.UVa, "709B", 1474907875, 18000);

		c3 = new Contest(OnlineJudge.Codeforces, "721E", 1474809237, 7200);
		c4 = new Contest(OnlineJudge.UVa, "721E", 1474809237, 7200);

		assertNotEquals(c1, c2);
		assertNotEquals(c1, c3);
		assertNotEquals(c1, c4);

		assertNotEquals(c2, c1);
		assertNotEquals(c2, c3);
		assertNotEquals(c2, c4);

		assertNotEquals(c3, c1);
		assertNotEquals(c3, c2);
		assertNotEquals(c3, c4);

		assertNotEquals(c4, c1);
		assertNotEquals(c4, c2);
		assertNotEquals(c4, c3);
	}

	@Test
	public void testJSON() {
		c1 = new Contest(OnlineJudge.Codeforces, "709B", 1474907875, 18000);
		c2 = new Contest(OnlineJudge.UVa, "721E", 1474809237, 7200);

		assertEquals("{\"name\":\"709B\",\"judge\":\"Codeforces\",\"timestamp\":1474907875,\"duration\":18000}", c1.toJSONString());
		assertEquals("{\"name\":\"721E\",\"judge\":\"UVa\",\"timestamp\":1474809237,\"duration\":7200}", c2.toJSONString());
	}
}
