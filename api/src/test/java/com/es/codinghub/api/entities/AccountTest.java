package com.es.codinghub.api.entities;

import com.es.codinghub.api.facade.OnlineJudge;

import org.json.JSONStringer;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.security.InvalidParameterException;

import static org.junit.Assert.assertEquals;

public class AccountTest {

    private Account defaultAccount;
    private Account testAccount;

    private OnlineJudge codeforces = OnlineJudge.Codeforces;

    @Before
    public void before() {
        defaultAccount = new Account(codeforces, "marianne");
    }

    @Test
    public void testConstructor() {
        try {
            testAccount = new Account(codeforces, "");
            Assert.fail();
        }
        catch (InvalidParameterException ipe){

        }
        try {
            testAccount = new Account(codeforces, null);
            Assert.fail();
        }
        catch (InvalidParameterException ipe){

        }
        try {
            testAccount = new Account(null, "test");
            Assert.fail();
        }
        catch (InvalidParameterException ipe){

        }
    }

    @Test
    public void testGets() {
        Assert.assertEquals(0, defaultAccount.getId());
        Assert.assertEquals(codeforces, defaultAccount.getJudge());
        Assert.assertEquals("marianne", defaultAccount.getUsername());
    }

    @Test
    public void testJson() {
        String expectedJson = (new JSONStringer()
                .object()
                .key("id").value(0)
                .key("judge").value(codeforces)
                .key("username").value("marianne")
                .endObject()).toString();
        Assert.assertEquals(expectedJson, defaultAccount.toJSONString());
    }
}
