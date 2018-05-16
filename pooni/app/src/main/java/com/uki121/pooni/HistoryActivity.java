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
    private final String SYNC_DATE = "date_synchronized";
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private String sync_date;
    private boolean IsSetSync = false, //Is set Synchronized date
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

    // Todo : current db don't operate now
    public void init() {
        //db create and open
        dbhelper = new bookDBHelper(HistoryActivity.this);
        dbhelper.createTable(ContractDBinfo.TBL_HISTORY_PIE);
        dbhelper.createTable(ContractDBinfo.TBL_HISTORY_LINE);
        //assignment
        history = new History();
        //load
        onLoadSyncDate();//from sharedPreferences
        onLoadRecord(); //from db

        //Todo : reinforcement selective because of addtion of DataMonth
        //set up viewpager and tab_layout
        if (IsSetSync != true) {//no sync date
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
    //ToDo : 전체적으로 난잡한 코드
    //현재 getInfo 작업에서 isBookset = false, strLap 부분이 null값이 등장.
    // 제대로 다 받아오고 있지 못학 있는데, 저장된 값을 다시 클래스로 넣는 작업이 제대로 안되는 듯
    public void onLoadRecord() {
        System.out.println("onLoadRecord_syncDate : " + sync_date);
        //처음 실행되었을 경우 : 프로그램 전체적으로 한번 실행될 판단문
        if (sync_date.equals("") == true) {
            Log.d(TAG, "Load ElpRecord from db");
            //no synchronized information then read all elapsed records from db
            newRecord = new ArrayList<>(dbhelper.getElapsedRecord(null));
            //Todo : delete
            Iterator <ElapsedRecord> it = newRecord.iterator();
            System.out.println(" >> newRecord size is " + newRecord.size());
            while (it.hasNext()) {
                ElapsedRecord _elp = it.next();
                _elp.getInfo();
                _elp.getBaseBook().getBook();
            }
            if (newRecord != null) {
                //lap to excess
                setExcessFromLap();
                //update sync date
                onUpdateSyncDate();
            } else {
                Log.d(TAG, "Record from db is null");
                newRecord = null;
            }
        } else {//동기화 날짜가 존재하는 경우 = history 영역이 존재 한다고 일맥 상통?
            Log.d(TAG, "Load History from db");
            //if there is a history of synchronizing, then read history data
            newRecord = null;
            history.setHistory(onLoadHistory(ContractDBinfo.TBL_HISTORY_PIE, ContractDBinfo.SQL_SELECT_HISTORY_PIE));//history total setting
            history.setHistory(onLoadHistory(ContractDBinfo.TBL_HISTORY_LINE, ContractDBinfo.SQL_SELECT_HISTORY_LINE));//history month setting
        }
    }
    public void setExcessFromLap() {
        Log.d(TAG, "############### start ###############");
        System.out.println(" >> newRecord size is " + newRecord.size());
        Iterator <ElapsedRecord> it = newRecord.iterator();
        while (it.hasNext()) {
            ElapsedRecord _elp = new ElapsedRecord(it.next());
            //Error here
            //the upper module can call book setting, but this module could not, why?
            _elp.setEachExcess();
            _elp.getInfo();//Todo :delete
        }
        Log.d(TAG, "############### end ###############");
    }
    //Load synchronized date from sharedPrefereces
    private void onLoadSyncDate() {
        SharedPreferences sp = getSharedPreferences(SYNC_DATE, 0);
        sync_date = new String(sp.getString(SYNC_DATE, ""));
        if (sync_date.equals("") == true) {
            IsSetSync = false;
            Log.d(TAG, "There is no synchronized date.");
        } else {
            IsSetSync = true;
            Log.d(TAG, "Synchronized date : " + sync_date);
        }
    }
    public History onLoadHistory(String _table, String _query) {
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