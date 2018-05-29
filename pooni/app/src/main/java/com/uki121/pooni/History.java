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
    private DataTotal total_history;
    private DataMonth month_history;
    private boolean isDataTotal = false , isDataMonth = false;
    //Constructor
    public History() {
        this.total_history = new DataTotal();
        this.month_history = new DataMonth();
    }
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
            isDataTotal = _datatotal != null? true : false;
            isDataMonth = _datamonth != null? true : false;
            this.total_history = isDataTotal != false ? _datatotal : null;
            this.month_history = isDataMonth != false ? _datamonth : null;
        } catch(Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
    //set
    //set History by History class
    public boolean setHistory(History _history) {
        if (_history != null) {
            if (_history.IsTotalHistory() != false) {
                Log.d(TAG, "setHistory() has Total data");
                total_history = _history.getHistoryToTal();
                isDataTotal = true;
                return true;
            } else {
                Log.d(TAG, "setHistory() has no Data");
            }
            if (_history.IsTotalHistory() != false) {
                Log.d(TAG, "setHistory() has Month data");
                month_history = _history.getHistoryMonth();
                isDataMonth = true;
                return true;
            } else {
                Log.d(TAG, "setHistory() has no Month data");
            }
        } else {
            Log.d(TAG, "setHistory() has null history now");
            Log.d(TAG, " > no change in this history");
        }
        return false;
    }
    //set History by ElapsedRecord class
    public void setHistory(ArrayList < ElapsedRecord > _elpList) {
        isDataTotal = setTotal_history(_elpList);
        isDataMonth = setMonth_history(_elpList);
    }
    //set TotalHistory
    private boolean setTotal_history(ArrayList < ElapsedRecord > _elpList){
        if (_elpList.isEmpty()) {
            Log.w(TAG, "setTotal_history() has 0 size list");
            return false;
        }
        Iterator < ElapsedRecord > it = _elpList.iterator();
        while(it.hasNext()) {
            boolean _flag = total_history.setData(it.next());
            if (_flag == false )
                return false;
        }
        return true;
    }
    //set MonthHistory
    private boolean setMonth_history(ArrayList < ElapsedRecord> _elpList) {
        if (_elpList.size() < 0) {
            Log.w(TAG, "setMonth_history() has 0 size list");
            return false;
        }
        try {
            //part1.create Month
            Map < Integer, Month > _mMap = new HashMap<>();
            Iterator < ElapsedRecord > it = _elpList.iterator();
            while(it.hasNext()) {
                ElapsedRecord _elp = it.next();
                /*
                //step1.check a infomation of elp elements
                _elp.getInfo();
                */
                //step2.classify their month
                //step2.1.find target as month "05" and extract (int)5 from (string)05
                String[] _src = _elp.getDate().split("-");//ex) 2018, 05, 11, 05 is a target.
                int _mkey = Integer.parseInt(_src[1]);
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
            return month_history.setData(new DataMonth(arg_months));
        } catch (Exception e) {
            Log.w(TAG, "constructor of Month class : " + e.getMessage());
        }
        return false;
    }
    public boolean onUpdateByrecord(ArrayList < ElapsedRecord > _newrecord) {
        if (_newrecord != null && _newrecord.size() > 0) {
            Iterator < ElapsedRecord > it = _newrecord.iterator();
            while (it.hasNext()) {
                ElapsedRecord item = new ElapsedRecord(it.next());
                try {
                    item.getInfo();
                    isDataTotal = total_history.setData(item);
                    isDataMonth = month_history.setData(item);
                    return true;
                } catch (Exception e) {
                    Log.e(TAG, "onUpdateByrecord() - " + e.getMessage());
                    return false;
                }
            }
        } else {
            Log.d(TAG, "There is no update for history because record data is null");
        }
        return false;
    }
    //get
    public DataTotal getHistoryToTal() {return isDataTotal == true? total_history : null;}
    public DataMonth getHistoryMonth() {return isDataMonth == true? month_history : null;}
    public boolean IsTotalHistory() { return isDataTotal;}
    public boolean IsMonthHistory() { return isDataMonth;}
    public boolean IsSet(){ return isDataMonth & isDataTotal;}
}
