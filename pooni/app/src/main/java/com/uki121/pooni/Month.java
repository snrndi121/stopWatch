package com.uki121.pooni;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Month {
    //def
    private final String TAG = "Month";
    //var
    private String name;//month
    private int totalExcess;//total amount of access in this month
    private int numOfprob;//total amount of problems solved in this month
    private int numOfbook;//total amount of booked solved in this month

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
