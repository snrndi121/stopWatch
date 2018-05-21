package com.uki121.pooni;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.ParcelableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.text.SimpleDateFormat;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by uki121 on 2018-04-03.
 */

public class HomeActivity extends AppCompatActivity implements onUpdateStateListener {
    //def
    private final String TAG = "HomeActivity";
    private final String DEFAULT_TITLE = "default_book";
    private final long FINISH_INTERVAL_TIME = 2000;
    //var
    private long backPressedTime = 0;
    private onKeyBackPressedListener mOnKeyBackPressedListener;
    private bookShelf bookshelf;
    private bookDBHelper dbhelper = null;
    //SharedPreferences
    private final String sharedCurBook = "shared_cur_book";
    private final String sharedKey = "cur_book_info";
    //var
    private boolean IsSharedPref = false;
    //Bundle
    private static String strCurBook;

    @Override
    protected void onCreate(Bundle savedInstanceStates) {
        super.onCreate(savedInstanceStates);
        setContentView(R.layout.activity_home);
        init();
    }
    //Load data and initializing variables
    public void init() {
        //Initialize variables.
        bookshelf = new bookShelf();
        dbhelper = new bookDBHelper(HomeActivity.this);

        onLoadBookShelf();//Load books' info from database
        onSearchInnerData();//Load a basic book setting user sets in the sharedPreferences before
        //Inflate HomeFragment
        try {
            //Create fragment
            String tag = "frag_home";
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.add(R.id.frag_home_container, FragmentHomeMenu.newInstance(strCurBook), tag);
            fragmentTransaction.commit();
        } catch(Exception e) {
            Log.e("HOME_ERROR", e.getMessage());
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        onSaveInnerData();//preserve current book instance
        //reset();
    }
    protected void onSaveInnerData() {
        try {
            if (strCurBook != null) {
                Log.i(TAG, "Save new shardPrefernces - strCurbook is saved");
                SharedPreferences sp = getSharedPreferences(sharedCurBook, 0);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(sharedKey, strCurBook);
                editor.commit();
            } else { Log.w(TAG,"Save shardPrefernces - no strCurbook is found");}
        } catch (Exception e) {
            Log.e("Search_sharedPrefernces",e.getMessage());
        }
    }
    //Load a default book from SharedPreferences
    protected void onSearchInnerData() {
        try {
            SharedPreferences sp = getSharedPreferences(sharedCurBook, 0);
            String strCurBook = sp.getString(sharedKey, "");
            //Load a initial setting of Book from SharedPreferences
            if (strCurBook.equals("") != false) { //found
                Log.i(TAG, "SharedPreferences - Inner Found.");
                System.out.println(" >> strCurBook :" + strCurBook);
                IsSharedPref = true;
            } else {//not found
                Log.i("SharedPreferences", "default is applied.");
                //set a default book from bookshelf
                Book _defaultbook = bookshelf.getBook(0);
                if (_defaultbook != null) {
                    Gson gson = new GsonBuilder().create();
                    strCurBook = new String(gson.toJson(_defaultbook, Book.class));
                } else {
                    strCurBook = null;
                }
            }
        } catch (Exception e) {
            Log.e("Search_sharedPrefernces",e.getMessage());
        }
    }
    //Back-listener to receive back event from each fragments
    public interface onKeyBackPressedListener {
        public void onBack();
    }
    public void setOnKeyBackPressedListener(onKeyBackPressedListener listener) {
        mOnKeyBackPressedListener = listener;
    }
    public void onBackPressed() {
        // 개별 onBack함수 구현된 것 실현
        if (mOnKeyBackPressedListener != null) {
            mOnKeyBackPressedListener.onBack();
        } else {
            //초기 홈에서만 동작
            System.out.println(">> Home_back");
            long tempTime = System.currentTimeMillis();
            long intervalTime = tempTime - backPressedTime;

            if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
            {
                super.onBackPressed();
            }
            else
            {
                backPressedTime = tempTime;
                Toast.makeText(getApplicationContext(), "한번 더 뒤로가기 누르면 종료합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //Load book data from Database
    public void onLoadBookShelf() {
        System.out.println("###################### Start ######################");
        System.out.println(" Load Book DB");
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        StringBuffer sql_select_book = new StringBuffer(ContractDBinfo.SQL_SELECT_BOOK);
        try {
            Cursor cursor = db.rawQuery(sql_select_book.toString(), null);
            if (cursor.getCount() == 0) {
                Log.w("Cursor","No DB of book is found");
                return ;
            }
            if (cursor.moveToFirst()) {
                Book abook;
                while (cursor.moveToNext()) {
                    abook = new Book();
                    abook.setTitle(cursor.getString(1));
                    abook.setToTime(String.valueOf(cursor.getInt(2)));
                    abook.setEachTime(String.valueOf(cursor.getInt(3)));
                    abook.setRestTime(String.valueOf(cursor.getInt(4)));
                    abook.setNumProb(String.valueOf(cursor.getInt(5)));
                    abook.getBook();
                    bookshelf.AddBooks(abook);/* ToDo : If the return value is false, then delete from db */
                }
            }
        } catch (SQLException e) {
            Log.e("LOAD_CONFIGURE_SQL",e.getMessage());
        } catch (NullPointerException e) {
            Log.e("LOAD_CONFIGURE_NULL", e.getMessage());
        }
        finally {
            System.out.println("###################### Ends ######################");
        }
    }
    public void loadBookUser() {

    }
    //It will be called from FragmentSaveShare when book setting is saved
    @Override
    public boolean onUpdateRecord(String _strUserRec, boolean _IsNewBook) {
        //Convert json format into ElapsedRecord class
        Log.d(TAG, " ##### onUpdateRecord start #####");
        Log.d(TAG, " strUserRec : " + _strUserRec);
        //restore class from strUserRec
        Gson gsonUser = new Gson();
        ElapsedRecord _elp = gsonUser.fromJson(_strUserRec, ElapsedRecord.class);
        //set book from elp
        int bid = -1;
        Book _target = new Book(_elp.getBaseBook());
        String _title = _target.getTitle();
        if (_title != null) {
            Log.d(TAG, " book title : " + _title);
            Log.d(TAG, " book valid : " + _elp.IsBookSet());
            //exception Todo : delete
            if (_title.equals(DEFAULT_TITLE) != true & _elp.IsBookSet() == false) {
                Log.e(TAG, "fatal error is occured!!!");
                return false;
            }
            //target have been already existed in table_book
            if (dbhelper.getBookId(_title) != -1) {
                Log.i("Current Book", "Insert unnecessary, instead update its attributes");
                ContentValues _updatedata = new ContentValues();
                _updatedata.put(ContractDBinfo.COL_NOACC, _target.getNumAcc() + 1);
                bid = dbhelper.updateBook(ContractDBinfo.COL_TITLE, _updatedata, _target.getTitle());
            } else { //new book is added
                Log.i(TAG,"Book is inserted sucessfully");
                bid = (int) dbhelper.insertData(_elp, ContractDBinfo.TBL_BOOK);
            }
            _elp.setBookId(String.valueOf(bid));
            //Save Record
            _elp.getInfo();
            dbhelper.insertData(_elp, ContractDBinfo.TBL_RECORD);
            Log.d(TAG, "#### onUpdateRecrod end ####");
            return true;
        } else {
            Log.w(TAG, "updateRecord - fail due to a fact that no book is set");
        }
        Log.d(TAG, "#### onUpdateRecrod end ####");
        return false;
    };
    public void reset() {
        DelShpf();
        dbhelper.dropTable(ContractDBinfo.TBL_BOOK);
        dbhelper.dropTable(ContractDBinfo.TBL_USER);
        dbhelper.dropTable(ContractDBinfo.TBL_RECORD);
    }
    public void DelShpf() {
        SharedPreferences pref = getSharedPreferences(sharedCurBook, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
    //It will be called from FragmentSaveShare when a record is sharing
    @Override
    public boolean onSharingSNS(String _strUserRec) {
        Gson gson = new Gson();
        ElapsedRecord elp = gson.fromJson(_strUserRec, ElapsedRecord.class);
        if (elp != null) {
            Intent toggleIntent = new Intent();
            String shareStr = elp.getRecord();
            toggleIntent.setAction(Intent.ACTION_SEND);
            toggleIntent.putExtra(Intent.EXTRA_TEXT, shareStr);
            toggleIntent.setType("text/plain");
            startActivityForResult(Intent.createChooser(toggleIntent, "Share your Records"), 0);
            //getIntent.putExtra("re", ">> Sharing's done");
        }
        return false;
    };
}
