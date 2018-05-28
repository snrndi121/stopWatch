package com.uki121.pooni;

import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class HistoryActivity extends AppCompatActivity {
    //def
    private static final String TAG = "HistoryActivity";
    //DB
    private bookDBHelper dbhelper;
    private ArrayList<ElapsedRecord> newRecord;
    private History history;
    //SharedPreference
    private final String SYNC_POINT = "sync_point";
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private String sync_point;
    //Tab-Fragment
    private HistoryAdapter hisAdapter;
    private ViewPager viewpager;
    private TabLayout histab;

    @Override
    protected void onCreate(Bundle savedInstanceStates) {
        super.onCreate(savedInstanceStates);
        this.overridePendingTransition(R.anim.start_enter, R.anim.start_exit);//animation activity
        setContentView(R.layout.fragment_container_history);
        init();
    }

    // Todo : current db don't operate now
    public void init() {
        //db create and open
        dbhelper = new bookDBHelper(HistoryActivity.this);
        dbhelper.createTable(ContractDBinfo.TBL_HISTORY_PIE);
        dbhelper.createTable(ContractDBinfo.TBL_HISTORY_LINE);
        //assignment
        history = new History();
        //load
        onLoadSyncInfo();//from sharedPreferences
        onLoadHistory();//read history table
        onLoadRecord(); //read record table
        //set adapter
        if (history.IsSet() == true)
            hisAdapter = new HistoryAdapter(getSupportFragmentManager(), history);
        else
            hisAdapter = new HistoryAdapter(getSupportFragmentManager(), null);
        //set up viewpager and tab_layout
        viewpager = (ViewPager) findViewById(R.id.viewpager_history);
        viewpager.setAdapter(hisAdapter);
        histab = (TabLayout) findViewById(R.id.tab_history);//tab layout
        histab.setupWithViewPager(viewpager);
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.end_enter, R.anim.end_exit);
    }
    //Load synchronized date from sharedPrefereces
    private void onLoadSyncInfo() {
        //load syn_point by sharedpreferences
        SharedPreferences sp_date = getSharedPreferences(SYNC_POINT, 0);
        sync_point = new String(sp_date.getString(SYNC_POINT, "-1"));
        //current date in Record table
        StringBuffer _where_reindx = null;
        Log.d(TAG, " >> sync_point : " + sync_point);
        //saved sync_point existed
        if (sync_point.equals("-1") != true) {
            Log.d(TAG, "onLoadSyncinfo - synchronized history has existed");
        } else {
            Log.d(TAG, "onLoadSyncinfo - no synchronized history is found");
        }
    }
    //Load history from db
    public void onLoadHistory() {
        history.setHistory(LoadHistory(ContractDBinfo.TBL_HISTORY_PIE, ContractDBinfo.SQL_SELECT_HISTORY_PIE));//history total setting
        history.setHistory(LoadHistory(ContractDBinfo.TBL_HISTORY_LINE, ContractDBinfo.SQL_SELECT_HISTORY_LINE));//history month setting

    }
    //Load elapsed record from db
    public void onLoadRecord() {
        Log.d(TAG, " >> onLoadRecord");
        //where Query
        StringBuffer _where_reindx = new StringBuffer();
        _where_reindx.append(ContractDBinfo.COL_RECID)
                     .append(">\"")
                     .append(sync_point)
                     .append("\"");
        //find record from record_table
        newRecord = dbhelper.getElapsedRecord(_where_reindx.toString(), true);
        //Todo : delete
        if (newRecord != null) {
            Iterator<ElapsedRecord> it = newRecord.iterator();
            System.out.println(" >> newRecord size is " + newRecord.size());
            /*
            while (it.hasNext()) {
                ElapsedRecord _elp = it.next();
                _elp.getBaseBook().getBook();
            }
            */
        }
        //update history by updated record
        boolean isUpdate = history.onUpdateByrecord(newRecord);
        //update sync date
        onUpdateSyncDate(isUpdate);
    }
    private History LoadHistory(String _table, String _query) {
        Cursor cursor = dbhelper.selectFromTable(_table, _query);
        if (cursor != null && cursor.moveToFirst()) {
            Log.d(TAG, "onLoadHistory - " + _table + " table is loading now...");
            if (_table.equals(ContractDBinfo.TBL_HISTORY_PIE)) {
                //set DataTotal
                int[] _contents = new int[4];
                for (int i = 0; i < 4; ++i) {
                    _contents[i] = cursor.getInt(i);
                }
                return new History(new DataTotal(_contents), null);
            } else if (_table.equals(ContractDBinfo.TBL_HISTORY_LINE)) {
                ArrayList<Month> _month = new ArrayList<>();
                while (cursor.moveToNext()) {
                    //set DataMonth
                    String _name = new String(cursor.getString(0));
                    int[] _contents = new int[3];
                    for (int i = 0; i < 3; ++i) {
                        _contents[i] = cursor.getInt(i);
                    }
                    _month.add(new Month(_name, _contents));//add to Arraylist
                }
                return new History(null, new DataMonth(_month));
            } else {
                ;
            }
        } else {
            Log.w(TAG, "onLoadHistory - " + _table + " table is empty");
        }
        return null;
    }
    /*Todo : get rid of it
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onSume");
    }
    */
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "on Stop");
        onClearSyncDate();
    }
    //Save Synchronized data for HistoryActivity
    private void onUpdateSyncDate(boolean _isupdate) {
        Log.d(TAG, " >> onUpdateSyncDate");
        Log.d(TAG, " _switch : " + _isupdate);
        SharedPreferences spf = getSharedPreferences(SYNC_POINT, 0);
        SharedPreferences.Editor editor = spf.edit();
        //if the update of history was completed successfully
        if (_isupdate != false) {
            //Todo : check that newrecord has to be sorted
            int sz = newRecord.size();
            String _curStr_rid = newRecord.get(sz - 1).getRecordId();
            Log.i(TAG, " synchronized point will be updated");
            sync_point = _curStr_rid;
        } else {
            //syn_point is unvalid or the number of record is lack than sync_point
            if (dbhelper.getNumOfrecord() <= Integer.parseInt(sync_point) + 1) {
                Log.w(TAG, " synchronized point will be reset because the size of recordTable is lower than sync_point");
                sync_point = "-1";
            } else {
                Log.i(TAG, " it is currently up-to-date");
            }
        }
        editor.putString(SYNC_POINT, sync_point);
        editor.commit();
    }
    private void onClearSyncDate() {
        SharedPreferences pref = getSharedPreferences(SYNC_POINT, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
    private String getTime() {//YYYY:MM:DD
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        String ydmTime = sdf.format(date);
        return ydmTime;
    }
}