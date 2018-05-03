package com.uki121.pooni;

import android.content.SharedPreferences;
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
import java.util.Map;
import java.util.Set;


public class HistoryActivity extends AppCompatActivity implements historyDBHandler {
    //Debug
    private static final String TAG = "HistoryActivity";
    //DB
    private historyDBHandler hhandler;
    private bookDBHelper dbhelper;
    private ArrayList< ElapsedRecord > newRecord;
    //SharedPreference
    private static final String SYNC_DATE = "date synchronized";
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private String sync_date;
    private boolean IsSyncDate = false, //Is set Synchronized date
                    IsSyncRequest = false;//request from other fragment
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
    public void init() {
        //assignment
        sync_date = new String();

        //db create and open
        dbhelper = new bookDBHelper(HistoryActivity.this);
        dbhelper.createTable(ContractDBinfo.TBL_HISTORY_PIE);

        //sharedPreferences
        onLoadSyncDate();
        onLoadRecord();

        //set up viewpager and tab_layout
        hisAdapter = new HistoryAdapter(getSupportFragmentManager(), newRecord);//view pager
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
    public void onLoadRecord() {
        if (sync_date == null) {
            newRecord = dbhelper.getElapsedRecord(null);
        } else {
            newRecord = null;
        }
    }
    private void onLoadSyncDate() {
        SharedPreferences sp = getSharedPreferences(SYNC_DATE, 0);
        sync_date = sp.getString(SYNC_DATE, "");
        if (sync_date.equals("") != true) {
            IsSyncDate = false;
            Log.d(TAG, "There is no synchronized date.");
        } else {
            IsSyncDate = true;
            Log.d(TAG, "Synchronized date : " + sync_date);
        }
    }
    @Override
    public History onLoadHistory(String _table, String _query) {
        Cursor cursor = dbhelper.selectFromTable(_table, _query);
        cursor.moveToLast();
        if (_table.equals(ContractDBinfo.TBL_HISTORY_PIE)) {
            int[] c = new int[4];
            for (int i=0; i<4; ++i) {
                c[i] = cursor.getInt(i);
            }
            return new History(new DataTotal(c), null);
        } else {

        }
        return null;
    }
    private void onUpdateSyncDate() {
        //if (IsSyncRequest == true) {
            SharedPreferences spf = getSharedPreferences(SYNC_DATE, 0);
            SharedPreferences.Editor editor = spf.edit();
            String _sync_date = getTime();
            editor.putString(SYNC_DATE, _sync_date);
            editor.commit();
        //}
    }
    private String getTime() {//YYYY:MM:DD
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        String ydmTime = sdf.format(date);
        return ydmTime;
    }
}