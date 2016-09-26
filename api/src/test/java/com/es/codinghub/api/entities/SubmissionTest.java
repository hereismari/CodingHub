package com.es.codinghub.api.entities;

import com.es.codinghub.api.facade.OnlineJudge;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SubmissionTest {

	private Submission s1, s2, s3, s4;
	private Problem p1, p2;

	@Before
	public void SetUp() {
		p1 = new Problem("709B", "Vanya and Strings", OnlineJudge.Codeforces);
		p2 = new Problem("722E", "Xenia and Strings", OnlineJudge.UVa, 2781);
	}

	@Test
	public void testConstructor() {
		s1 = new Submission(4291, 1474907875, p1, "C++", Verdict.ACCEPTED);

		assertEquals(4291, s1.getId());
		assertEquals(1474907875, s1.getTimestamp());
		assertEquals(p1, s1.getProblem());
		assertEquals("C++", s1.getLanguage());
		assertEquals(Verdict.ACCEPTED, s1.getVerdict());

		s2 = new Submission(5432, 1474809237, p2, "Python", Verdict.WRONG_ANSWER);

		assertEquals(5432, s2.getId());
		assertEquals(1474809237, s2.getTimestamp());
		assertEquals(p2, s2.getProblem());
		assertEquals("Python", s2.getLanguage());
		assertEquals(Verdict.WRONG_ANSWER, s2.getVerdict());
	}

	@Test
	public void testEquality() {
		s1 = new Submission(4291, 1474907875, p1, "C++", Verdict.ACCEPTED);
		s2 = new Submission(4291, 1474809237, p2, "Python", Verdict.WRONG_ANSWER);

		s3 = new Submission(5432, 1474907875, p1, "C++", Verdict.ACCEPTED);
		s4 = new Submission(5432, 1474809237, p2, "Python", Verdict.WRONG_ANSWER);

		assertEquals(s1, s1);
		assertEquals(s3, s3);

		assertEquals(s1.hashCode(), s1.hashCode());
		assertEquals(s3.hashCode(), s3.hashCode());

		assertEquals(s1, s2);
		assertEquals(s2, s1);

		assertEquals(s3, s4);
		assertEquals(s4, s3);

		assertEquals(s1.hashCode(), s2.hashCode());
		assertEquals(s3.hashCode(), s4.hashCode());
	}

	@Test
	public void testInequality() {
		s1 = new Submission(4291, 1474907875, p1, "C++", Verdict.ACCEPTED);
		s2 = new Submission(5432, 1474907875, p1, "C++", Verdict.ACCEPTED);

		s3 = new Submission(7653, 1474809237, p2, "Python", Verdict.WRONG_ANSWER);
		s4 = new Submission(6456, 1474809237, p2, "Python", Verdict.WRONG_ANSWER);

		assertNotEquals(s1, s2);
		assertNotEquals(s1, s3);
		assertNotEquals(s1, s4);

		assertNotEquals(s2, s1);
		assertNotEquals(s2, s3);
		assertNotEquals(s2, s4);

		assertNotEquals(s3, s1);
		assertNotEquals(s3, s2);
		assertNotEquals(s3, s4);

		assertNotEquals(s4, s1);
		assertNotEquals(s4, s2);
		assertNotEquals(s4, s3);
	}

	@Test
	public void testJSON() {
		s1 = new Submission(4291, 1474907875, p1, "C++", Verdict.ACCEPTED);
		s2 = new Submission(5432, 1474809237, p2, "Python", Verdict.WRONG_ANSWER);

		assertEquals("{\"id\":4291,\"timestamp\":1474907875,\"problem\":{\"id\":\"709B\",\"name\":\"Vanya and Strings\",\"judge\":\"Codeforces\",\"solvedCount\":-1},\"language\":\"C++\",\"verdict\":\"ACCEPTED\"}", s1.toJSONString());
		assertEquals("{\"id\":5432,\"timestamp\":1474809237,\"problem\":{\"id\":\"722E\",\"name\":\"Xenia and Strings\",\"judge\":\"UVa\",\"solvedCount\":2781},\"language\":\"Python\",\"verdict\":\"WRONG_ANSWER\"}", s2.toJSONString());
	}
}
