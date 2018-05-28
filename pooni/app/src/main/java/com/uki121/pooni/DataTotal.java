package com.uki121.pooni;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class DataTotal {
    //def
    private static final String TAG = "Data Total";
    private final int DEFAULT_STANDARD = 60000;//1 min to milli
    private final int MORE_THAN_BY_1 = 60000;
    private final int MORE_THAN_BY_2 = 120000;
    private final int MORE_THAN_BY_4 = 240000;
    //var
    private static final int NUM_CATE = 4;
    private int standard; //milli
    private int[] category;
    private int totalNum;//sum of category
    //constructor
    public DataTotal(){
        this.standard = DEFAULT_STANDARD;
        this.category = new int[]{0, 0, 0, 0};
        this.totalNum = 0;
    };
    public DataTotal(int[] _category) {
        this.standard = DEFAULT_STANDARD;
        this.category = _category;
        setTotalNum();
    }
    public DataTotal(String _strData) {
        DataTotal d = ToClass(_strData);
        this.standard = d.getStandard();
        this.category = d.getData();
        this.totalNum = d.getSize();
    }
    //set
    public boolean setData(ElapsedRecord _elp) {
        Log.d(TAG, " setData");
        try {
            ArrayList<String> lap = _elp.getEachLaptime();
            int taken_time, gap;
            //set eachtime from book
            String _str_eachtime = _elp.getBaseBook().getEachTime();
            Log.d(TAG, " > each time : " + _str_eachtime + " (sec)");
            //set standard
            standard = _str_eachtime != null ? Integer.parseInt(_str_eachtime) * 1000 : DEFAULT_STANDARD;
            for (int i = 0; i < lap.size(); ++i) {
                taken_time = Integer.parseInt(lap.get(i));
                gap = taken_time - standard;
                if (gap < 0) {          //in_range_of_standard
                    category[0]++;
                } else if (gap < MORE_THAN_BY_1) {  //exceed_by_more than_1_minutes
                    category[1]++;
                } else if (gap < MORE_THAN_BY_2) { //exceed_by__more than_2_minutes
                    category[2]++;
                } else {                //exceed_by__more than_4_minutes
                    category[3]++;
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
                this.totalNum += this.category[i];
            }
        } else {
            Log.w(TAG, "category date is empty, totalNum cannot be set.");
        }
    }
    //get
    public int getStandard() { return this.standard;}
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
