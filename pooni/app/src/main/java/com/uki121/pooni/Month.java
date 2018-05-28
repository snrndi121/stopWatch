package com.uki121.pooni;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Iterator;

public class Month {
    //def
    private final String TAG = "Month";
    private final int TOKEN_MONTH = 2;
    //var
    private String name;//month
    private int totalExcess = 0;//total amount of access in this month
    private int numOfprob = 0;//total amount of problems solved in this month
    private int numOfbook = 0;//total amount of booked solved in this month
    protected final String[] mMonths = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};

    //constructort
    public Month(int _pos) {
        this.name = new String(mMonths[_pos]);
    }
    public Month(String _name, int[] _val) {
        this.name = _name;
        if (_val != null) {
            this.totalExcess = _val[0];
            this.numOfbook = _val[1];
            this.numOfprob = _val[2];
        }
    }
    public Month(ElapsedRecord _elp) {
        //Log.d(TAG, " ## month constructor ");
        int _pos = _elp.getDate().indexOf("-") + 1;
        //Log.d(TAG, " > elp date : " + _elp.getDate());
        //Log.d(TAG, " > date pos : " + _pos);
        //Log.d(TAG, " > substring of date : " + _elp.getDate().substring(_pos, _pos + TOKEN_MONTH));
        int _key = Integer.parseInt(_elp.getDate().substring(_pos, _pos + TOKEN_MONTH)) - 1;
        //Log.d(TAG, " > data key : " + _key);
        this.name = new String(mMonths[_key]);
        //get each amount of excess from laptme in elp
        Iterator < String > it = _elp.getEachExcess().iterator();
        while (it.hasNext()) {
            int _oneExcess = Integer.parseInt(it.next());
            //if excess is positive, it means 'excess'
            if (_oneExcess > 0) {
                this.totalExcess += _oneExcess;
            }
        }
        numOfprob += _elp.getNumOfRec();
        Log.d(TAG, " ## num of prob : " + numOfprob);
        numOfbook = 1;//ToDo : this is wrong assigning
    }
    //set
    private float setAvg(String _by) {
        //exception
        if (numOfbook <= 0 || numOfprob <= 0) {
            Log.w(TAG, name + ", this month has no data");
            return 0;
        }
        try {
            Log.d(TAG, name + ", this month has data");
            switch (_by) {
                case "prob":
                    return this.totalExcess / this.numOfprob;//mill by num
                case "book":
                    return this.totalExcess / this.numOfbook;//mill by num
                default:
                    Log.e(TAG, "In setAvg() in error");
                    break;
            }
        } catch(Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return 0;
    }
    public void accumMonth(Month _month) {
        this.totalExcess += _month.getTotalExcess();
        this.numOfprob += _month.getNumOfprob();
        this.numOfbook++;
    }
    //get
    public String getName() { return this.name;}
    public int getKey() {
        for (int i = 0; i < mMonths.length; ++i) {
            if (name.equals(mMonths[i])) { return i;}
        }
        return -1;
    }
    public int getTotalExcess() { return totalExcess;}
    public int getNumOfprob() { return numOfprob;}
    public int getNumOfbook() {return numOfbook;}
    public float getAvgByprob() { return setAvg("prob");}
    public float getAvgBybook() { return setAvg("book");}

    public String ToString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this, Month.class);
    }
    public static Month ToClass(String _str) {
        Gson gson = new Gson();
        return gson.fromJson(_str, Month.class);
    }
}
