package com.uki121.pooni;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class ElapsedRecord {
    //def
    private final int LAP_SIZE = 8;
    private final String TAG = "ElapsedRecord";
    private final int[] time_unit = {1, 1000, 60000};//milli:second:min
    //var
    private Book baseBook;
    private String bookid, recordid;
    private String date;
    /*
    * eachLaptime set when FragmentLap is done
    * eachExcess will be set when HistoryActivity is called
    *
    * */
    private ArrayList eachExcess, eachLaptime;//save it as milli
    private int num; //acutal size of record
    private String strExcess, strLap;
    private boolean isBookSet = false;
    //private float cutTop10, cutBottom10;
    //private float recordAvg;
    //method
    public ElapsedRecord() {
        eachLaptime = new ArrayList(){};
        eachExcess = new ArrayList(){};
    };
    public ElapsedRecord(ElapsedRecord _elp) {
        baseBook = _elp.getBaseBook();
        bookid = _elp.getBookId();
        recordid = _elp.getRecordId();
        date = _elp.getDate();
        eachLaptime = _elp.getEachLaptime();
        eachExcess = _elp.getEachExcess();
    }
    public ElapsedRecord(Book _bs, List < String > _records) {//used by FragmentLap
        //book setting
        if (_bs != null) {
            isBookSet = true;
            baseBook = new Book(_bs);
        }
        num = _records.size();
        recordid = new String();
        bookid = new String();
        date = new String();
        eachLaptime = new ArrayList < String >();
        //Set eachRecord
        Iterator < String > it = _records.iterator();
        while (it.hasNext()) {
            int _item = getSecond(it.next());
            if (_item > 0) { eachLaptime.add(_item);}
        }
        Collections.sort(eachLaptime);
        setStrData("lap");
    }
    //Set
    public void setRecordId(String _rid) { this.recordid = _rid;}
    public void setBookId(String _bid) { this.bookid = _bid;}
    public void setBaseBook(Book _bs) { this.baseBook = new Book(_bs);}
    public void setDate(String _date) { this.date = _date;}
    public void setExcessFromLap() {
        Log.d(TAG, " #### START : setEachExcess #### ");
        //eachLaptime -> eachExcess
        if (baseBook == null) {
            Log.w(TAG, "There is no book setting in Elp");
            Log.d(TAG, " #### END : setEachExcess #### ");
            return ;
        }
        if (eachLaptime == null) {
            Log.w(TAG, "There is no eachLapTime in Elp");
            Log.d(TAG, " #### END : setEachExcess #### ");
            return ;
        }
        //standard time from basebook
        int standard = Integer.parseInt(baseBook.getEachTime()) * 1000;//convert second into milli
        Log.d(TAG, " >> Standard for (int)excess time : " + standard);
        Iterator < String > it = eachLaptime.iterator();
        Log.d(TAG, " >> eachLaptime size : " + eachLaptime.size());
        while(it.hasNext()) {
            int _excess = Integer.parseInt(it.next()) - standard;
            Log.d(TAG, " >> CONVERTING string to (int)excess time : " + _excess);
            if ( _excess > 0) {
                eachExcess.add(String.valueOf(_excess));
            }
        }
        Log.d(TAG, " >> RESULT : eachExcess size : " + eachExcess.size());
        Log.d(TAG, " #### END : setEachExcess #### ");
    }
    public void setEachLaptime(String _src) {
        this.eachLaptime = convertStrTolist(_src);
    }
    private void setStrData(String _targetname) {
        String src = convertListTostr(_targetname);
        //exception
        if (src != null) {
            Log.i("Record converting", ">> Done well");
        } else {
            Log.w("Record converting",">> Err");
            return ;
        }
        switch(_targetname) {
            case "excess" :{
                strExcess = src;
                break;
            }
            case "lap" :{
                strLap = src;
            }
            default :
                break;
        }
    }
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
    public ArrayList getEachLaptime() { return this.eachLaptime;}
    public ArrayList getEachExcess() { return this.eachExcess; }
    public String getStrLap() { return this.strLap;}
    public String getStrExcess() { return this.strExcess;}
    public String getRecord() {
        if (eachLaptime.isEmpty() != true ) {
            Iterator<String> it = eachLaptime.iterator();
            StringBuffer res = new StringBuffer();
            while (it.hasNext()) {
                res.append(it.next());
            }
            return res.toString();
        }
        Log.i(TAG, "Elp has no lap time data");
        return null;
    }
    //cal
    public String getStrData(String _tartgetname) {
        switch (_tartgetname) {
            case "excess" : {
                if (strExcess != null) { return this.strExcess;}
                setStrData(_tartgetname);
                return this.strExcess;
            }
            case "lap" : {
                if (strLap != null) { return this.strLap;}
                setStrData(_tartgetname);
                return this.strLap;
            }
            default :
                return null;
        }
    }
    public void getInfo(){
        System.out.println("date : " + date);
        System.out.println("Record id : " + recordid);
        System.out.println("isBookSet : " + isBookSet);
        System.out.println("Book : " + baseBook.getTitle());
        System.out.println("strLap : " + strLap);
        System.out.println("strExcess : " + strExcess);
    }

    //Calculate
    private String convertListTostr(String _listname) {//convert List to string
        //The substring of each string as a unit has a size by 6
        //The basic string format is like "1. 00:00:00", and it would be cut out, 000000.
        StringBuffer res = new StringBuffer();
        Iterator < String > it;
        if (_listname.equals("excess")) {
            it = eachExcess.iterator();
        } else if (_listname.equals("lap")) {//lap
            it = eachLaptime.iterator();
        } else {
            Log.e(TAG, "convertListToStr has fatal error");
            return null;
        }
        while (it.hasNext()) {
            res.append(String.valueOf(it.next()));
            if (it.hasNext()) {
                res.append(":");
            }    //delimeter
        }
        return res.toString();
    }
    public ArrayList convertStrTolist(String _str) {
        int strSize = _str.length();
        Log.d(TAG, ">> (Before) String is " + _str);
        ArrayList < String > list_rec = new ArrayList<>();
        StringTokenizer str = new StringTokenizer(_str, ":");
        for (int i = 0; str.hasMoreElements(); ) {
            String _element = str.nextToken();
            Log.d(TAG, " token : " + _element);
            list_rec.add(_element);
        }
        return list_rec;
    }
    //Each Record date is convert to int as a second
    public int getSecond(String _laptime) {
        Log.d(TAG, "getSeconds - lap time is : " + _laptime);
        int second = 0;
        //Extract index
        int begin = _laptime.indexOf(" ") + 1;//ex) 1. 00:06:66
        //Extract Time
        StringTokenizer str = new StringTokenizer(_laptime.substring(begin, begin + LAP_SIZE), ":");//ex) 00:06:66
        //Exception
        if (str.countTokens() < 3) {
            Log.w(TAG, "Record date has a wrong format");
            return -1;
        }
        //Todo : split(":"), is it more efficient?
        for (int i = 2; i >= 0; --i) {
            String _tok = str.nextToken();
            second += (time_unit[i] * Integer.parseInt(_tok));
        }
        //Todo : delete because it is imposible case
        if (second < 0) {
            Log.d(TAG, "laptime has negative number");
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