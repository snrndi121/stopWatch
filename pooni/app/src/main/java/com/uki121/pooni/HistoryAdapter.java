package com.uki121.pooni;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

public class HistoryAdapter extends FragmentPagerAdapter {
    //db
    private historyDBHandler hisHander;
    //var
    private static final String TAG = "HistoryAdapter";
    private final int FRAG_NUM = 2;
    private ArrayList< ElapsedRecord > curElp;
    private History history;

    public HistoryAdapter(FragmentManager fragmentmanager, ArrayList < ElapsedRecord > _elp) {
        super(fragmentmanager);

        if (_elp != null) {
            Log.d(TAG, "Elp is valid");
            curElp = new ArrayList<>(_elp);
            history = new History(_elp);
        } else {
            Log.d(TAG, "Elp is empty");
            curElp = null;
            history = hisHander.onLoadHistory(ContractDBinfo.TBL_HISTORY_PIE, ContractDBinfo.SQL_SELECT_HISTORY_PIE);//ToDo : Record //
        }
    };
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                Log.d(TAG, "TotalHistory");
                if (history != null) {
                    return FragmentTotalHistory.newInstance(history.getHistoryToTal().ToString());
                } else {
                    return FragmentTotalHistory.newInstance(null);
                }
            case 1:
                Log.d(TAG, "MonthHistory");
                return FragmentMonthHistory.newInstance();
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
interface historyDBHandler {
    public History onLoadHistory(String _tablename, String _query);
}
