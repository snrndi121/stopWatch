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
        month_history = new DataMonth();
        setTotal_history(_elpList);
        //Todo : month_history later
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
    public void setMonth_history(ArrayList < ElapsedRecord> _elpList) {
        //DataMonth :
        //      private int num; //the number of month
        //    private Month[] data;
        /*
        //Month
            private String name;//month
            private int totalExcess;//total amount of access in this month
            private int numOfprob;//total amount of problems solved in this month
            private int numOfbook;//total amount of booked solved in this month
            private int numOfmonth;
         *//*
         *      private static final String TAG = "ElapsedRecord";
                private static final int[] time_unit = {3600, 60, 1};//hour, min, secon
                private final int TOKEN_SIZE = 6;
                private Book baseBook;
                private String recordId;
                private String date;
                private ArrayList eachAccess;//save it as seconds
                private int num; //acutal size of record
                private String strAccess;
         * /
        //.기록된 데이터들이 몇개월의 데이터 인지 확인하고
        Map < String, int > indextable = new Hashmap<>();
        Iterator < ElapsedRecrod > it = _elpList.iterator();
        while(it.hasNext()) {

        }
        // 각 레코드별로 같은 월이면 해당 데이터 먼스값 추가
    }
    //get
    public DataTotal getHistoryToTal() {return total_history;}
    public DataMonth getHistoryMonth() {return month_history;}
    public boolean IsTotalHistory() { return isDataTotal;}
    public boolean IsMonthHistory() { return isDataMonth;}
}
