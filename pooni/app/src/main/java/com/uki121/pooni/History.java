package com.uki121.pooni;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

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
        setHistory(_elpList);
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
    //set History by History class
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
    //set History by ElapsedRecord class
    public void setHistory(ArrayList < ElapsedRecord > _elpList) {
        setTotal_history(_elpList);
        setMonth_history(_elpList);
    }
    //set TotalHistory
    private void setTotal_history(ArrayList < ElapsedRecord > _elpList){
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
    //set MonthHistory
    private void setMonth_history(ArrayList < ElapsedRecord> _elpList) {
        try {
            //part1.create Month
            Map < Integer, Month > _mMap = new HashMap<>();
            Iterator < ElapsedRecord > it = _elpList.iterator();
            while(it.hasNext()) {
                ElapsedRecord _elp = it.next();
                //step1.check a infomation of elp elements
                _elp.getInfo();
                //step2.classify their month
                //step2.1.find target as month "05" and extract (int)5 from (string)05
                String _src = new String(_elp.getDate());
                int _pos = _src.indexOf("-") + 1;//ex) 2018-05-11, 05 is a target.
                int _mkey = Integer.parseInt(_src.substring(_pos, _pos + 1));
                //step2.2.put that position as key and mMonth[position] as value into map
                if (_mMap.containsKey(_mkey) != true) {//New month
                    Month _mval = new Month(_elp);
                    _mMap.put(_mkey, _mval);
                } else {//already exists
                    Month _mnew = _mMap.get(_mkey), _mdata = new Month(_elp);
                    _mnew.accumMonth(_mdata);
                    _mMap.put(_mkey, _mnew);
                }
            }
            //part2.create DataMonth based on Month
            Map < Integer, Month > _months = new TreeMap<>(_mMap);
            Month[] arg_months = (Month[])_months.values().toArray();
            this.month_history.setData(new DataMonth(arg_months));
        } catch (Exception e) {
            Log.w(TAG, "constructor of Month class : " + e.getMessage());
        }

    }
    //get
    public DataTotal getHistoryToTal() {return total_history;}
    public DataMonth getHistoryMonth() {return month_history;}
    public boolean IsTotalHistory() { return isDataTotal;}
    public boolean IsMonthHistory() { return isDataMonth;}
}
