package com.es.codinghub.api.entities;

import com.es.codinghub.api.facade.OnlineJudge;

import org.json.JSONStringer;
import org.json.JSONWriter;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.Assert;

import java.security.InvalidParameterException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import static org.junit.Assert.assertEquals;

public class ProblemTest {

    private Problem defaultProblem;
    private Problem testProblem;

    private OnlineJudge uva = OnlineJudge.UVa;

    @Before
    public void before() {
        defaultProblem = new Problem("problem1", "Problem 1", uva, 3);
    }

    @Test
    public void testConstructor() {
        try {
            testProblem = new Problem(null, "Problem 1", uva, 3);
            Assert.fail();
        }
        catch (InvalidParameterException ipe){

        }
        try {
            testProblem = new Problem("problem1", null, uva, 3);
            Assert.fail();
        }
        catch (InvalidParameterException ipe){

        }
        try {
            testProblem = new Problem("problem1", "Problem 1", null, 3);
            Assert.fail();
        }
        catch (InvalidParameterException ipe){

        }
        try {
            testProblem = new Problem("problem1", "Problem 1", uva, -1);
            Assert.fail();
        }
        catch (InvalidParameterException ipe){

        }
        try {
            testProblem = new Problem("", "Problem 1", uva, 3);
            Assert.fail();
        }
        catch (InvalidParameterException ipe){

        }
        try {
            testProblem = new Problem("problem1", "", uva, 3);
            Assert.fail();
        }
        catch (InvalidParameterException ipe){

        }

    }

    @Test
    public void testGets() {
        Assert.assertEquals("problem1", defaultProblem.getId());
        Assert.assertEquals(uva, defaultProblem.getJudge());
        Assert.assertEquals("Problem 1", defaultProblem.getName());
        Assert.assertTrue(3 == defaultProblem.getSolvedCount());
    }

    @Test
    public void testEquals() {
        testProblem = new Problem("problem1", "Problem 2", uva, 5);
        Assert.assertTrue(testProblem.equals(defaultProblem));
        testProblem = new Problem("problem2", "Problem 2", uva, 5);
        Assert.assertFalse(testProblem.equals(defaultProblem));
        Assert.assertFalse(defaultProblem.equals(null));
    }

    @Test
    public void testJson() {
        String expectedJson = (new JSONStringer()
                .object()
                .key("id").value("problem1")
                .key("name").value("Problem 1")
                .key("judge").value(uva)
                .key("solvedCount").value(3)
                .endObject()
        ).toString();
        Assert.assertEquals(expectedJson, defaultProblem.toJSONString());
    }
}
