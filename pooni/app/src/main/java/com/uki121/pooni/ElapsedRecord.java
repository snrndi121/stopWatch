package com.uki121.pooni;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class ElapsedRecord {
    //var
    private final String TAG = "ElapsedRecord";
    private final int[] time_unit = {3600, 60, 1};//hour, min, secon
    private final int TOKEN_SIZE = 6;
    private Book baseBook;
    private String bookid, recordid;
    private String date;
    private ArrayList eachExcess;//save it as seconds
    private int num; //acutal size of record
    private String strExcess;
    private boolean isBookSet = false;
    //private float recordAvg;
    //private float cutTop10, cutBottom10;
    //method
    public ElapsedRecord() {};
    public ElapsedRecord(Book _bs, List < String > _records) {
        if (_bs != null) {
            isBookSet = true;
            baseBook = new Book(_bs);
        }
        num = _records.size();
        recordid = new String();
        bookid = new String();
        date = new String();
        strExcess = new String();
        eachExcess = new ArrayList < String >();
        //Set eachRecord
        Iterator < String > it = _records.iterator();
        while (it.hasNext()) {
            int _item = getSecond(it.next());
            if (_item > 0) { eachExcess.add(_item);}
        }
        Collections.sort(eachExcess);
    }
    //Set
    public void setBaseBook(Book _bs) { baseBook = _bs;}
    public void setDate(String _date) { this.date = _date;}
    public void setEachExcess(ArrayList eachAccess) { this.eachExcess = eachExcess; }
    public void setEachExcess(String _src) {
        this.eachExcess = convertStrTolist(_src);
    }
    public void setBookId(String _bid) { this.bookid = _bid;}
    //Get
    public boolean IsBookSet() { return isBookSet;}
    public String getBookId() { return this.bookid;}
    public String getRecordId() { return recordid;}
    public String getDate() {return this.date;}
    public int getNumOfRec() {return num;}
    //public float getRecAvg() { return recordAvg;}
    //public float getCutTop10() { return cutTop10;}
    //public float getCutBottom10() { return cutBottom10;}
    public Book getBaseBook() { return baseBook;}
    public ArrayList getEachAccess() { return eachExcess; }
    public String getRecord() {
        Iterator < String > it = eachExcess.iterator();
        StringBuffer res = new StringBuffer();
        while(it.hasNext()) {
            res.append(it.next());
        }
        return res.toString();
    }
    public String getStrExcess() {
        if (strExcess != null) { return this.strExcess; }
        //serialize list 'eachExcess' to string'strExcess'
        StringBuffer src = convertListTostr();
        Log.d(TAG, "getStrExcess : " + src.toString());
        if (src != null) {
            Log.i("Record converting", "Done well");
            strExcess = src.toString();
            return strExcess;
        } else {
            Log.w("Record converting","Err : Check convertStrAcc()");
        }
        return null;
    }
    public void getInfo(){
        System.out.println("date : " + date);
        System.out.println("Record id : " + recordid);
        System.out.println("isBookSet : " + isBookSet);
        System.out.println("Book : " + baseBook.getTitle());
    }
    //Calculate
    private StringBuffer convertListTostr() {
        //The substring of each string as a unit has a size by 6
        //The basic string format is like "1. 00:00:00", and it would be cut out, 000000.
        StringBuffer res = new StringBuffer();
        Iterator < String > it = eachExcess.iterator();
        while (it.hasNext()) {
            res.append(it.next());
            if (it.hasNext()) { res.append(":"); }    //delimeter
        }
        return res;
    }
    public ArrayList convertStrTolist(String _from) {
        int strSize = _from.length();
        Log.d(TAG, ">> (Before) String is " + _from);
        Log.d(TAG, ">> (Before) String size : " + strSize);
        ArrayList < String > list_rec = new ArrayList<>();
        StringTokenizer str = new StringTokenizer(_from, ":");
        for (int i = 0; str.hasMoreElements(); ) {
            list_rec.add(str.nextToken());
        }
        return list_rec;
    }
    //Each Record date is convert to int as a second
    public int getSecond(String _record) {
        Log.d(TAG, "getSeconds");
        Log.d(TAG, "String : " + _record);
        int second = 0;
        StringBuffer element = new StringBuffer();
        //Extract index
        int begin = _record.indexOf(" ") + 1;//ex) 1. 00:06:66
        //Extract Time
        StringTokenizer str = new StringTokenizer(_record.substring(begin, begin + 7), ":");//ex) 00:06:66
        //Exception
        if (str.countTokens() < 3) {
            Log.w(TAG, "Record date has a wrong format");
            return -1;
        }
        //Todo : split(":"), is it more efficient?
        for (int i = 2; i >= 0; --i) {
            second += (time_unit[i] * Integer.parseInt(str.nextToken()));
        }
        if (second < 0) {
            Log.d(TAG, "current record has no excess time");
            return 0;
        }
        return second;
    }
    /*
    //ToDo : It may have a error because of 'round()'
    private void calEachAvg() {

        if (eachAccess.size() > 0) {
            try {
                int sum = 0;
                int sizeOfprob = eachAccess.size();
                int cutIndex = (int) Math.round(sizeOfprob * 0.1);
                int endBottom = cutIndex, beginTop = sizeOfprob - cutIndex;
                //Average Of normal
                for (int i = endBottom + 1; i < beginTop; ++i) {
                    sum += eachAccess.indexOf(i);
                }
                recordAvg = (float) Math.ceil(sum / beginTop - endBottom - 1);
                //Of Top
                sum = 0;
                for (int i = 0; i <= endBottom; ++i) {
                    sum += eachAccess.indexOf(i);
                }
                cutTop10 = (float) Math.ceil(sum / endBottom);
                //Of Bottom
                sum = 0;
                for (int i = beginTop; i < sizeOfprob; ++i) {
                    sum += eachAccess.indexOf(i);
                }
                cutBottom10 = (float) Math.ceil(sum / sizeOfprob - beginTop + 1);
            } catch (Exception e) {
                Log.e("Caculation Average", e.getMessage());
            }
        }
    }
    */
}