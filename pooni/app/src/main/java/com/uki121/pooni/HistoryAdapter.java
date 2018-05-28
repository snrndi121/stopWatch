package com.uki121.pooni;



import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

public class HistoryAdapter extends FragmentPagerAdapter {
    //var
    private static final String TAG = "HistoryAdapter";
    private final int FRAG_NUM = 2;
    private History history;
    //private ArrayList < ElapsedRecord > curElp;
    /*
    public HistoryAdapter(FragmentManager fragmentmanager, ArrayList < ElapsedRecord > _elp) {
        super(fragmentmanager);
        Log.d(TAG, "constructor(1) is active");
        if (_elp != null) {
            Log.d(TAG, "constructor(1)_Elp is valid");
            //curElp = new ArrayList<>(_elp);
            history = new History(_elp);
        } else {
            Log.d(TAG, "constructor(1)_Elp is empty");
            //curElp = null;
            history = null;
        }
    }
    */
    public HistoryAdapter(FragmentManager fragmentmanager, History _history) {
        super(fragmentmanager);
        if (_history != null) {
            Log.d(TAG, "constructor - History is set");
            history = new History(_history);
        } else {
            Log.w(TAG, "constructor - History is empty");
            history = null;
        }
    }
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                Log.d(TAG, " ## start : TotalHistory");
                if (history !=null) {
                    return FragmentTotalHistory.newInstance(history.getHistoryToTal().ToString());
                } else {
                    return FragmentTotalHistory.newInstance(null);
                }
            case 1:
                Log.d(TAG, " ## start : MonthHistory");
                if (history !=null) {
                    return FragmentMonthHistory.newInstance(history.getHistoryMonth().ToString());
                } else {
                    return FragmentMonthHistory.newInstance(null);
                }
            default :
                return null;
        }
    }
    @Override
    public int getCount() {
        return FRAG_NUM;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "종합 기록";
            case 1:
                return "월별 기록";
            default :
                return "NULL";
        }
    }
}