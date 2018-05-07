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
    private DataTotal total_history = null;
    private DataMonth month_history = null;
    private boolean isDataTotal = false , isDataMonth = false;
    //Constructor
    public History() {}
    public History(History history) {
        this.total_history = history.getHistoryToTal();
        this.month_history = history.getHistoryMonth();
        isDataTotal = this.total_history != null ? true : false;
        isDataMonth = this.month_history != null ? true : false;
    }
    public History(ArrayList < ElapsedRecord > _elpList) {
        total_history = new DataTotal();
        setTotal_history(_elpList);
        //Todo : month_history later
        //month_history = new DataMonth();
    }
    public History(DataTotal _datatotal, DataMonth _datamonth) {
        try {
            //selective assignment
            this.total_history = _datatotal != null ? _datatotal : null;
            this.month_history = _datamonth != null ? _datamonth : null;
        } catch(Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
    //set
    public void setTotal_history(ArrayList < ElapsedRecord > _elpList){
        if (_elpList.isEmpty()) {
            Log.w(TAG, "setTotal_history() has 0 size list");
            return;
        }
        isDataTotal = true;
        int[] res = new int[4];
        Iterator < ElapsedRecord > it = _elpList.iterator();
        while(it.hasNext()) {
            total_history.setData(it.next());
        }
    }
    public void setTotal(int[] _data) { total_history.setData(_data);}
    public void setHistory(History _history) {
        if (_history != null) {
            if (_history.getHistoryToTal() != null) {
                Log.d(TAG, "setHistory() has Total data");
                this.total_history = _history.getHistoryToTal();
                isDataTotal = true;
            } else {
                Log.d(TAG, "setHistory() has no Data");
            }
            if (_history.getHistoryMonth() != null) {
                Log.d(TAG, "setHistory() has Month data");
                this.month_history = _history.getHistoryMonth();
                isDataMonth = true;
            } else {
                Log.d(TAG, "setHistory() has no Month data");
            }
        } else {
            Log.d(TAG, "setHistory() has null history now");
        }
    }
    public void setMonth_history(ArrayList < ElapsedRecord> _elpList) {}
    //get
    public DataTotal getHistoryToTal() {return total_history;}
    public DataMonth getHistoryMonth() {return month_history;}
    public boolean IsTotalHistory() { return isDataTotal;}
    public boolean IsMonthHistory() { return isDataMonth;}
}
