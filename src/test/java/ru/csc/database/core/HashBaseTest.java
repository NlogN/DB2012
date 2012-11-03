package ru.csc.database.core;

import org.junit.Assert;
import org.junit.Test;
import java.io.*;
import java.security.NoSuchAlgorithmException;

/**
 * @author Artemii Chugreev achugr@yandex-team.ru
 *         30.10.12
 */
public class HashBaseTest {
    private static final DBRecord DB_RECORD = new DBRecord("123", new String[]{"artemij", "chugreev"});

    @Test
    public void testAddCommand() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        {
            HashBase hashBase = new HashBase(10);
            hashBase.create(DB_RECORD);
            DBRecord retrievedDbRecord = hashBase.retrieve("123");
            Assert.assertEquals(DB_RECORD, retrievedDbRecord);
        }
        {
            HashBase hashBase = new HashBase();
            hashBase.create(DB_RECORD);
            DBRecord retrievedDbRecord = hashBase.retrieve("123");
            Assert.assertEquals(DB_RECORD, retrievedDbRecord);
        }
    }

    @Test
    public void testGetAll() throws NoSuchAlgorithmException, UnsupportedEncodingException {
            HashBase hashBase = new HashBase(10);
            DBRecord DB_RECORD1 = new DBRecord("21", new String[]{"artemij11", "chugreev11"});
            hashBase.create(DB_RECORD);
            hashBase.create(DB_RECORD1);
            ConsoleApp.getAll(hashBase);

    }


    @Test
    public void flushLoadTest() throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        final HashBase hashBase = new HashBase();
        final String surname = "chugreev";
        final String telNumber = "123";
//        add record
        ConsoleApp.perform("add(" + surname + "," + telNumber + ")", hashBase, new PrintWriter(System.out));
//        flush base
        ConsoleApp.perform("flush", hashBase, new PrintWriter(System.out));
//        exit
        ConsoleApp.perform("exit", hashBase, new PrintWriter(System.out));

        HashBase loadedHashBase = new HashBase();
//        load base
        loadedHashBase = ConsoleApp.perform("load", loadedHashBase,
                new PrintWriter(System.out));
//        base should contain my record
        Assert.assertNotNull(loadedHashBase.retrieve(surname));
    }

}
