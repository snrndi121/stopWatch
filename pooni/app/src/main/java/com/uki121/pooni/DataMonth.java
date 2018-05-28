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
    private Month[] months;

    public DataMonth(){
        months = new Month[MONTH_NUM];
        for(int i = 0; i < MONTH_NUM; ++i) {
            months[i] = new Month(i);
        }
    }
    public DataMonth(String _json) {
        DataMonth month = ToClass(_json);
        months = new Month[MONTH_NUM];//Todo : is need?
        setData(month);
    }
    public DataMonth(Month[] _month) {
        if (_month != null) {
            months = _month;
        }
    }
    public DataMonth(ArrayList < Month > _month) {
        int sz = _month.size();
        try {
            if (sz > 0) {
                for (int i = 0; i < sz; ++i) {
                    months[i] = _month.get(i);
                }
            }
        } catch(Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
    //set
    public boolean setData(DataMonth _src) {
        try {
            months = _src.getMonth();
            if (months != null)
                return true;
        } catch(Exception e) {
            Log.d(TAG, e.getMessage());
        }
        return false;
    }
    public boolean setData(ElapsedRecord _src) {
        Log.d(TAG, "setData");
        //set new month
        try {
            Month _month = new Month(_src);
            int _pos = _month.getKey();//find index of month
            if (_pos < 0) {
                Log.e(TAG, " > elp date has an error, pos is lower than 0");
                return false;
            }
            Log.d(TAG, " > month : " + (_pos + 1));
            //size check
            if (months == null) {
                months = new Month[MONTH_NUM];
                for (int i = 0; i< MONTH_NUM; ++i)
                    months[i] = new Month(i);
            }
            //accumulate it into an origin
            months[_pos].accumMonth(_month);
            return true;
        } catch(Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return false;
    }
    //get
    public Month getMonth(int _index) {
        try {
            if (months != null) {
                return months[_index];
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }
    public Month[] getMonth() { return months != null? months : null;}
    public String ToString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this, DataMonth.class);
    }
    public static DataMonth ToClass(String _str) {
        Gson gson = new Gson();
        return gson.fromJson(_str, DataMonth.class);
    }
}
