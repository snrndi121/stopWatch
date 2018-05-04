package com.uki121.pooni;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class DataTotal {
    //def
    private static final String TAG = "Data Total";
    private final int DEFAULT_STANDARD = 60;
    //var
    private static final int NUM_CATE = 4;
    private int standard; //second
    private int[] category;
    private int totalNum;
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
    public void setData(ElapsedRecord _elp) {
        ArrayList < String > lap = _elp.getEachAccess();
        int taken_time, gap;
        for (int i=0; i<lap.size(); ++i) {
            taken_time = Integer.parseInt(lap.get(i));
            gap = taken_time - standard;
            if (gap < 0) {          //in_1_minutes
                category[0]++;
            } else if (gap < 60) {  //in_2_minutes
                category[1]++;
            } else if (gap < 240) { //in_4_minutes
                category[2]++;
            } else {                //bigger_than_4_minutes
                category[3]++;
            }
        }
        setTotalNum();
    }
    public void setData(int[] _data) {
        if (_data != null) {
            for (int i = 0; i < NUM_CATE; ++i) {
                this.category[i] = _data[i];
            }
        } else {
            Log.w(TAG, "the argument of setData is null");
        }
    }
    private void setTotalNum() {
        if (category != null) {
            for (int i = 0; i < NUM_CATE; ++i) {
                this.totalNum = this.category[i];
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
