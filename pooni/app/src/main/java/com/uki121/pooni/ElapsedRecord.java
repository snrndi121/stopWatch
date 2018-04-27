package com.uki121.pooni;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class ElapsedRecord {
    //var
    private final int UNIT_SIZE = 6;
    private Book baseBook;
    private String recordId;
    private float recordAvg;
    private float cutTop10, cutBottom10;
    ArrayList eachAccess;
    String strAccess;
    //method
    public ElapsedRecord() {};
    public ElapsedRecord(Book _bs, List < String > _records) {
        eachAccess = new ArrayList < String >();
        baseBook = new Book(_bs);
        recordId = new String();
        strAccess = new String();
        //Set eachRecord
        Iterator < String > it = _records.iterator();
        while (it.hasNext()) {
            eachAccess.add(it.next());
        }
        Collections.sort(eachAccess);
    }
    //Set
    public void setBaseBook(Book _bs) { baseBook = _bs;}
    public void setStrAccess() {
        //serialize list 'eachAccess' to string'strAccess'
        StringBuffer src = convertStrAcc();
        if (src != null) {
            Log.i("Record converting", "Done well");
            strAccess = src.toString();
        } else {
            Log.w("Record converting","Err : Check convertStrAcc()");
        }
    }
    //Get
    public float getRecAvg() { return recordAvg;}
    public float getCutTop10() { return cutTop10;}
    public float getCutBottom10() { return cutBottom10;}
    public Book getBaseBook() { return baseBook;}
    //Calculate
    private StringBuffer convertStrAcc() {
        //The substring of each string as a unit has a size by 6
        //The basic string format is like "1. 00:00:00", and it would be cut out, 000000.
        StringBuffer res = new StringBuffer();
        Iterator < String > it = eachAccess.iterator();
        while (it.hasNext()) {
            StringBuffer element = new StringBuffer();
            //Extract index
            int begin = it.next().indexOf(" ");
            //Extract Time
            StringTokenizer str = new StringTokenizer(it.next().substring(begin), ":");
            for (int i = 0; str.hasMoreTokens(); ) {
                element.append(str.nextToken());
            }
            if (element.length() != UNIT_SIZE) {
                Log.w("Convert List to String", "String is missing");
                break;
            }
            res.append(element.toString());
        }
        return res;
    }
    private void calEachAvg() {
        /* ToDo : It may have a error because of 'round()'*/
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
}