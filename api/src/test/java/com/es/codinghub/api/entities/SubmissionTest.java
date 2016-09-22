package com.es.codinghub.api.entities;

import com.es.codinghub.api.facade.OnlineJudge;

import org.json.JSONStringer;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.security.InvalidParameterException;

import static org.junit.Assert.assertEquals;

public class SubmissionTest {

    private Submission defaultSubmission;
    private Submission testSubmission;

    private Problem auxiliarProblem;
    private Verdict auxiliarVerdict;

    private OnlineJudge codeforces = OnlineJudge.Codeforces;
    private OnlineJudge uva = OnlineJudge.UVa;

    @Before
    public void before() {
        auxiliarProblem = new Problem("problem1", "Problem 1", codeforces, 3);
        auxiliarVerdict = Verdict.ACCEPTED;
        defaultSubmission = new Submission(123, 123, auxiliarProblem, auxiliarVerdict);
    }

    @Test
    public void testConstructor() {
        try {
            testSubmission = new Submission(-1, 123, auxiliarProblem, auxiliarVerdict);
            Assert.fail();
        }
        catch (InvalidParameterException ipe){

        }
        try {
            testSubmission = new Submission(123, -2, auxiliarProblem, auxiliarVerdict);
            Assert.fail();
        }
        catch (InvalidParameterException ipe){

        }
        try {
            testSubmission = new Submission(123, 123, null, auxiliarVerdict);
            Assert.fail();
        }
        catch (InvalidParameterException ipe){

        }
        try {
            testSubmission = new Submission(123, 123, auxiliarProblem, null);
            Assert.fail();
        }
        catch (InvalidParameterException ipe){

        }
    }

    @Test
    public void testGets() {
        Assert.assertEquals(123, defaultSubmission.getId());
        Assert.assertEquals(123, defaultSubmission.getTimestamp());
        Assert.assertEquals(auxiliarProblem, defaultSubmission.getProblem());
        Assert.assertEquals(auxiliarVerdict, defaultSubmission.getVerdict());
    }

    @Test
    public void testEquals() {
        testSubmission = new Submission(123, 123, auxiliarProblem, auxiliarVerdict);
        Assert.assertTrue(testSubmission.equals(defaultSubmission));
        testSubmission = new Submission(12, 123, auxiliarProblem, auxiliarVerdict);
        Assert.assertFalse(testSubmission.equals(defaultSubmission));
        Assert.assertFalse(defaultSubmission.equals(null));
    }


    @Test
    public void testJson() {
        String expectedJson = (new JSONStringer()
                .object()
                .key("id").value(123)
                .key("timestamp").value(123)
                .key("problem").value(auxiliarProblem)
                .key("verdict").value(auxiliarVerdict)
                .endObject()).toString();
        Assert.assertEquals(expectedJson, defaultSubmission.toJSONString());
    }
}
