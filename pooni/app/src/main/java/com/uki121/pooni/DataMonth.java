package com.uki121.pooni;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class DataMonth
{
    //def
    private final String TAG = "Data Month";
    private final int MONTH_NUM = 12;
    //var
    private int num; //the number of month
    private Month[] months;

    public DataMonth(){
        months = new Month[MONTH_NUM];
    }
    public DataMonth(int _num) {
        months = new Month[MONTH_NUM];
        this.num = _num;
    }
    public DataMonth(Month[] _month) {
        if (_month != null) {
            this.num = _month.length;
            this.months = _month;
        }
    }
    public DataMonth(ArrayList < Month > _month) {
        int sz = _month.size();
        try {
            if (sz > 0) {
                this.num = sz;
                for (int i = 0; i < sz; ++i) {
                    months[i] = _month.get(i);
                }
            }
        } catch(Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
    //set
    public void setData(DataMonth _src) {
        this.num = _src.getNum();
        this.months = _src.getMonth();
    }
    public void setData(ElapsedRecord _src) {
        //set new month
        Month _month = new Month(_src);
        int _pos = _month.getKey();//find index of month
        //size check
        if (months == null) {
            this.num = _pos + 1;
            months = new Month[MONTH_NUM];
        }
        //accumulate it into an origin
        months[_pos].accumMonth(_month);
    }
    //get
    public Month getMonth(int _index) {
        if (num < _index) {
            Log.e(TAG, "Array index overflow");
            return null;
        }
        return months[_index];
    }
    public Month[] getMonth() { return this.months;}
    public int getNum() { return this.num;}
    public String ToString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this, DataMonth.class);
    }
    public static DataMonth ToClass(String _str) {
        Gson gson = new Gson();
        return gson.fromJson(_str, DataMonth.class);
    }
}
