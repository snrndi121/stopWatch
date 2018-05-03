package com.uki121.pooni;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

public class History {
    //def
    private final String TAG = "History";
    private final String TOTAL_HISOTRY = "total_history";
    private final String MONTH_HISTORY = "month_history";
    //var
    private DataTotal total_history;
    private DataMonth month_history;
    //Constructor
    public History() {}
    public History(ArrayList < ElapsedRecord > _elpList) {
        total_history = new DataTotal();
        setTotal_history(_elpList);
        //Todo : month_history later
        //month_history = new DataMonth();
    }
    public History(DataTotal _datatotal, DataMonth _datamonth) {
        try {
            this.total_history = _datatotal;
            this.month_history = _datamonth;
        } catch(Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
    //set
    public void setTotal_history(ArrayList < ElapsedRecord > _elpList){
        int[] res = new int[4];
        Iterator < ElapsedRecord > it = _elpList.iterator();
        while(it.hasNext()) {
            total_history.setData(it.next());
        }
    }
    public void setMonth_history(ArrayList < ElapsedRecord> _elpList) {}
    //get
    public DataTotal getHistoryToTal() {return total_history;}
    public DataMonth getHistoryMonth() {return month_history;}
}
