package com.es.codinghub.api.entities;

import com.es.codinghub.api.facade.OnlineJudge;

import org.json.JSONStringer;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class UserTest {

    private User defaultUser;
    private User testUser;

    private OnlineJudge codeforces = OnlineJudge.Codeforces;
    private OnlineJudge uva = OnlineJudge.UVa;

    @Before
    public void before() {
        defaultUser = new User("mari@gmail.com", "123456");
    }

    @Test
    public void testConstructor() {
        try {
            testUser = new User("mari@gmail.com", null);
            Assert.fail();
        }
        catch (InvalidParameterException ipe){

        }
        try {
            testUser = new User(null, "123456");
            Assert.fail();
        }
        catch (InvalidParameterException ipe) {

        }
    }

    @Test
    public void testGets() {
        Assert.assertEquals(0, defaultUser.getId());
        Assert.assertEquals(new ArrayList<Account>(), defaultUser.getAccounts());
    }

    @Test
    public void testAddAcount() {

        defaultUser.addAccount(codeforces, "mari");
        defaultUser.addAccount(uva, "mari");

        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account(codeforces, "mari"));
        accounts.add(new Account(uva, "mari"));

        Assert.assertEquals(accounts, defaultUser.getAccounts());
    }

    @Test
    public void testEquals() {
        testUser = new User("mari@gmail.com", "123456");
        Assert.assertTrue(testUser.equals(defaultUser));
        testUser = new User("mariemail@gmail.com", "123456");
        Assert.assertFalse(testUser.equals(defaultUser));
        testUser = null;
        Assert.assertFalse(defaultUser.equals(testUser));
    }


    @Test
    public void testJson() {
        String expectedJson = (new JSONStringer()
                .object()
                .key("id").value(0)
                .key("email").value("mari@gmail.com")
                .key("accounts").value(new ArrayList<Account>())
                .endObject()).toString();
        Assert.assertEquals(expectedJson, defaultUser.toJSONString());
    }
}
