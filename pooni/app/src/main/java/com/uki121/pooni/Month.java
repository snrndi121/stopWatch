package com.uki121.pooni;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Iterator;

public class Month {
    //def
    private final String TAG = "Month";
    //var
    private String name;//month
    private int totalExcess;//total amount of access in this month
    private int numOfprob;//total amount of problems solved in this month
    private int numOfbook;//total amount of booked solved in this month
    protected final String[] mMonths = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};

    //constructort
    public Month() {}
    public Month(String _name, int[] _val) {
        this.name = _name;
        if (_val != null) {
            this.totalExcess = _val[0];
            this.numOfbook = _val[1];
            this.numOfprob = _val[2];
        }
    }
    public Month(ElapsedRecord _elp) {
        int _pos = _elp.getDate().indexOf("-") + 1;
        int _key = Integer.parseInt(_elp.getDate().substring(_pos, _pos + 1));
        this.name = new String(mMonths[_key]);
        Iterator<String> it = _elp.getEachAccess().iterator();
        while (it.hasNext()) {
            int _oneExcess = Integer.parseInt(it.next());
            if (_oneExcess > 0) {
                this.totalExcess += _oneExcess;
            }
        }
        this.numOfprob += Integer.valueOf(_elp.getEachAccess().size());
        this.numOfbook = 1;
    }
    //set
    private float setAvg(String _by) {
        //exception
        if (this.numOfbook == 0 || this.numOfprob == 0) {
            throw new NullPointerException();
        }
        try {
            switch (_by) {
                case "prob":
                    return this.totalExcess / this.numOfprob;
                case "book":
                    return this.totalExcess / this.numOfbook;
                default:
                    Log.e(TAG, "In setAvg() in error");
                    break;
            }
        } catch(Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return -1;
    }
    public void accumMonth(Month _month) {
        this.totalExcess += _month.getTotalExcess();
        this.numOfprob += _month.getNumOfprob();
        this.numOfbook++;
    }
    //get
    public String getName() { return this.name;}
    public int getTotalExcess() { return this.totalExcess;}
    public int getNumOfprob() { return this.numOfprob;}
    public int getNumOfbook() {return this.numOfbook;}
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
