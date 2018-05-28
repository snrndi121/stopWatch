package com.uki121.pooni;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Iterator;

public class DataTotal {
    //def
    private static final String TAG = "Data Total";
    private final int LESS_THAN_BY_1 = 60000;
    private final int LESS_THAN_BY_2 = 120000;
    private final int LESS_THAN_BY_4 = 240000;
    //var
    private static final int NUM_CATE = 5;
    private int[] category;
    private int totalNum = 0;//sum of category
    //constructor
    public DataTotal(){
        this.category = new int[]{0, 0, 0, 0, 0};
        this.totalNum = 0;
    };
    public DataTotal(int[] _category) {
        this.category = _category;
        setTotalNum();
    }
    public DataTotal(String _strData) {
        DataTotal d = ToClass(_strData);
        this.category = d.getData();
        this.totalNum = d.getSize();
    }
    //set
    public boolean setData(ElapsedRecord _elp) {
        Log.d(TAG, " setData");
        if (_elp == null & _elp.getEachLaptime().size() <= 0) {
            Log.w(TAG, " > the record data is not valid");
            return false;
        }
        try {
            //count each excess
            ArrayList < String > _excess_list = _elp.getEachExcess();
            Iterator < String > _excess_it = _excess_list.iterator();
            int gap;
            while(_excess_it.hasNext())
            {
                gap = Integer.parseInt(_excess_it.next());
                //Log.d(TAG, " > gap : " + gap);
                if (gap < 0) {          //in_range_of_standard
                    category[0]++;
                } else if (gap < LESS_THAN_BY_1) {  //exceed_by_more than_1_minutes
                    category[1]++;
                } else if (gap < LESS_THAN_BY_2) { //exceed_by__more than_2_minutes
                    category[2]++;
                } else if (gap < LESS_THAN_BY_4){                //exceed_by__more than_4_minutes
                    category[3]++;
                } else {
                    category[4]++;
                }
            }
            setTotalNum();
            return true;
        } catch(Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return false;
    }
    public boolean setData(int[] _data) {
        try {
            if (_data != null) {
                for (int i = 0; i < NUM_CATE; ++i) {
                    this.category[i] = _data[i];
                }
                return true;
            } else {
                Log.w(TAG, "the argument of setData is null");
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        return false;
    }
    private void setTotalNum() {
        if (category != null) {
            for (int i = 0; i < NUM_CATE; ++i) {
                totalNum += category[i];
            }
        } else {
            Log.w(TAG, "category date is empty, totalNum cannot be set.");
        }
    }
    //get
    public int[] getData() { return category; }
    public int getSize() { return totalNum;}
    public String ToString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this, DataTotal.class);
    }
    public static DataTotal ToClass(String _str) {
        Gson gson = new Gson();
        return gson.fromJson(_str, DataTotal.class);
    }
}
