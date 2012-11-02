package ru.csc.database.core;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Epifanov
 * Date: 9/12/12
 */


/** stores records in ArrayList.
 * each position in ArrayList also is a list.
 * This list consist of records which hashes is the same.
 */

public class HashBase implements Serializable {

    private final static int DEFAULT_BASE_SIZE = 1000;
    private final int baseSize;
    private final ArrayList<DBRecord> store;

    public HashBase() {
        this(DEFAULT_BASE_SIZE);
    }

    public HashBase(int baseSize) {
        this.baseSize = baseSize;
        store = new ArrayList<DBRecord>(baseSize);
        // TO DO: performance trouble
        String [] nullarr = {};
        for (int i=0; i<baseSize; ++i) {
            store.add(new DBRecord("null_head_record", nullarr));
        }
    }

    boolean create(DBRecord record) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        DBRecord fatherRecord = find(record.id);
        if (fatherRecord != null) { // there is record with the same id in base
            return false;
        }

        int index = hash(record.id);
        DBRecord endListRecord = store.get(index);
        while (endListRecord.next != null)  {
            endListRecord = endListRecord.next;
        }
        endListRecord.next = record;
        return true;
    }

    DBRecord retrieve (String id) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        DBRecord result = find(id);
        if (result == null) {
           // System.out.println("Retrieve: No record with id == " + id);
            return null;
        }
        return result.next;
    }

    public List<DBRecord> retrieveAll() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        List<DBRecord> result = new ArrayList<DBRecord>();
        for (DBRecord first : store) {
            DBRecord next = first.next; // первая запись - пустая, => не нужна
            while (next != null) {
                result.add(next);
                next = next.next;
            }
        }
        return result;
    }

//    public List<DBRecord> retrieveAll() throws NoSuchAlgorithmException, UnsupportedEncodingException {
//        List<DBRecord> result = new ArrayList<DBRecord>();
//        for (DBRecord rec : store) {
//                result.add(rec);
//        }
//        return result;
//    }


    void update(DBRecord record) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        DBRecord fatherRecord = find(record.id);
        if (fatherRecord == null) { // there is NO record with the same id
            create(record);
        } else {
            record.next = fatherRecord.next.next;
            fatherRecord.next = record;
        }
    }

    boolean delete(String id) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        DBRecord fatherRecord = find(id);
        if (fatherRecord == null) { // nothing to delete
           // System.out.println("Delete: There is no record with id == " + id);
            return false;
        }
        fatherRecord.next = fatherRecord.next.next;
        return true;
    }



    public static int hash(String id, int maxHashValue) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] recordInBytes = id.getBytes("UTF-8");
        byte[] hashInBytes = md.digest(recordInBytes);

        // convert hashInBytes to int
        int result = 0;
        for (int i : hashInBytes) {
            result *= 256;
            result += i + 128;
            result %= maxHashValue;
        }

        return  result;
    }

    private int hash(String id) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return  hash(id, baseSize);
    }


    /** returns _father_ of element with given id
     * if store has it, otherwise - null
     *
     *
     * @param id
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */

    private DBRecord find(String id) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        int index = hash(id);
        DBRecord record = store.get(index);
        while ((record.next != null) && (! record.next.id.equals(id)))  {
            record = record.next;
        }
        return (record.next != null ? record : null);
    }

    public void print(){
        for (DBRecord dbRecord:store){
            System.out.println(dbRecord.toString());
        }
    }
}
