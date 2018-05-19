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
    //Debug
    private static final String TAG = "HistoryActivity";
    //DB
    private bookDBHelper dbhelper;
    private ArrayList<ElapsedRecord> newRecord;
    private History history;
    //SharedPreference
    private final String SYNC_DATE = "date_synchronized", SYNC_REINDX = "recindex_synchronized";
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private String sync_date, sync_reindx;
    private boolean IsNeedSync = false; //to define whether a synchronization is needed
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

        //결과적으로 history만 주면 되도록, activity에서 세팅을 마치도록 할
        //Todo : reinforcement selective because of addtion of DataMonth
        //set up viewpager and tab_layout
        if (IsNeedSync != true) {//no sync date
            hisAdapter = new HistoryAdapter(getSupportFragmentManager(), newRecord);//view pager
        } else {//if history data are set
            hisAdapter = new HistoryAdapter(getSupportFragmentManager(), history);
        }
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

    //Load elapsed record from db
    public void onLoadRecord() {
        System.out.println("onLoadRecord_syncDate : " + sync_date);
        if (IsNeedSync != false) { //have to be update
            //where Query
            StringBuffer _where_reindx = null;
            if (sync_reindx.equals("") != true) {
                _where_reindx = new StringBuffer(ContractDBinfo.COL_RECID);
                _where_reindx.append(">")
                        .append(sync_reindx);
            }
            //dbhelper
            newRecord = new ArrayList<>(dbhelper.getElapsedRecord(_where_reindx, true));
            //Todo : delete
            Iterator <ElapsedRecord> it = newRecord.iterator();
            System.out.println(" >> newRecord size is " + newRecord.size());
            while (it.hasNext()) {
                ElapsedRecord _elp = it.next();
                _elp.getInfo();
                _elp.getBaseBook().getBook();
            }
            onUpdateSyncDate();//update sync date
        }
        //update history by updated record
        history.onUpdateByrecord(newRecord);
    }
    //Load synchronized date from sharedPrefereces
    private boolean onLoadSyncInfo() {
        //saved synchronized date
        SharedPreferences sp_date = getSharedPreferences(SYNC_DATE, 0),
                sp_reindx = getSharedPreferences(SYNC_REINDX, 0);
        sync_date = new String(sp_date.getString(SYNC_DATE, ""));
        sync_reindx = new String(sp_reindx.getString(SYNC_REINDX, ""));
        //current date in Record table
        StringBuffer _where_reindx = null;
        Log.d(TAG, " >> sync_date : " + sync_date);
        Log.d(TAG, " >> sync_reindex : " + sync_reindx);
        if (sync_reindx.equals("") != true) {
            _where_reindx = new StringBuffer(ContractDBinfo.COL_RECID)
                    .append("=")
                    .append(sync_reindx);
        }
        ArrayList <ElapsedRecord> lastitem = dbhelper.getElapsedRecord(_where_reindx, true);
        //define whether it will be updated
        if (lastitem.size() <= 1) {
            IsNeedSync = false;
            Log.d(TAG, "A synchronized date would not be updated");
        } else {
            IsNeedSync = true;
            Log.d(TAG, "Synchronized date can be updated");
        }
        return IsNeedSync;
    }
    public void onLoadHistory() {
        history.setHistory(LoadHistory(ContractDBinfo.TBL_HISTORY_PIE, ContractDBinfo.SQL_SELECT_HISTORY_PIE));//history total setting
        history.setHistory(LoadHistory(ContractDBinfo.TBL_HISTORY_LINE, ContractDBinfo.SQL_SELECT_HISTORY_LINE));//history month setting

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
        onClearSyncDate();
    }
    //Save Synchronized data for HistoryActivity
    private void onUpdateSyncDate() {
        //if (IsSyncRequest == true) {
        SharedPreferences spf = getSharedPreferences(SYNC_DATE, 0);
        SharedPreferences.Editor editor = spf.edit();
        String _sync_date = getTime();
        editor.putString(SYNC_DATE, _sync_date);
        editor.commit();
        //}
    }
    private void onClearSyncDate() {
        SharedPreferences pref = getSharedPreferences(SYNC_DATE, 0);
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