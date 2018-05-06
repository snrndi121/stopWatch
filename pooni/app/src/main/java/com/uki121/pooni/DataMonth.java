package com.uki121.pooni;

import android.util.Log;

public class DataMonth
{
    //def
    private String TAG = "DataMonth";
    //var
    private String name;//month
    private int totalExcess;//total amount of access in this month
    private int numOfprob;//total amount of problems solved in this month
    private int numOfbook;//total amount of booked solved in this month

    //constructort
    public DataMonth() {}
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
}
