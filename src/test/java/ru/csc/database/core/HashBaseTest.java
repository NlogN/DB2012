package ru.csc.database.core;

import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Artemii Chugreev achugr@yandex-team.ru
 *         30.10.12
 */
public class HashBaseTest {

    @Test
    public void testAddCommand() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        {
            HashBase hashBase = new HashBase(10);
            DBRecord dbRecord = new DBRecord("123", new String[]{"artemij", "chugreev"});
            hashBase.create(dbRecord);
            DBRecord retrievedDbRecord = hashBase.retrieve("123");
            Assert.assertEquals(dbRecord, retrievedDbRecord);
        }
        {
            HashBase hashBase = new HashBase();
            DBRecord dbRecord = new DBRecord("123", new String[]{"artemij", "chugreev"});
            hashBase.create(dbRecord);
            DBRecord retrievedDbRecord = hashBase.retrieve("123");
            Assert.assertEquals(dbRecord, retrievedDbRecord);
        }
    }

}
