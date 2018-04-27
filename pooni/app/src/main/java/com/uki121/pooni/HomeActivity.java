package com.uki121.pooni;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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


import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by uki121 on 2018-04-03.
 */

public class HomeActivity extends AppCompatActivity implements onUpdateStateListener {
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    private onKeyBackPressedListener mOnKeyBackPressedListener;
    private bookShelf bookshelf;
    private bookDBHelper dbhelper = null;
    //SharedPreferences
    private final String sharedCurBook = "shared_cur_book";
    private final String sharedKey = "cur_book_info";
    private Book curbook;
    private boolean IsSharedPref = false;
    //Bundle
    private static String strCurBook;
    private static String NewCurBook = "new_cur_book";

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

        LoadBookShelf();//Load books' info from database
        onSearchInnerData();//Load a basic book setting user sets in the sharedPreferences before
        //Inflate HomeFragment
        try {
            if (IsSharedPref = true) {
                //convert book to gson
                Gson gson = new GsonBuilder().create();
                strCurBook = gson.toJson(curbook, Book.class);
            } else {
                strCurBook = null;
            }
                //Create fragment
                String tag = "frag_home";
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                if (curbook != null) {
                    fragmentTransaction.add(R.id.frag_home_container, FragmentHomeMenu.newInstance(strCurBook), tag);}
                else {  //No data in the book shelf
                    fragmentTransaction.add(R.id.frag_home_container, new FragmentHomeMenu(), tag);}
                fragmentTransaction.commit();

        } catch(Exception e) {
            Log.e("HOME_ERROR", e.getMessage());
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        //onSaveInnerData();//preserve current book instance
        reset();
    }
    protected void onSaveInnerData() {
        try {
            if (curbook != null) {
                Log.i("Save new shardPrefernces","Excuted");
                Gson gson = new GsonBuilder().create();
                String strCurBook = gson.toJson(curbook, Book.class);

                SharedPreferences sp = getSharedPreferences(sharedCurBook, 0);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(sharedKey, strCurBook);
                editor.commit();
            } else { Log.w("Save shardPrefernces", "ignored");}
        } catch (Exception e) {
            Log.e("Search_sharedPrefernces",e.getMessage());
        }
    }
    protected void onSearchInnerData() {
        try {
            SharedPreferences sp = getSharedPreferences(sharedCurBook, 0);
            String strCurBook = sp.getString(sharedKey, "");
            //conversion
            if (strCurBook.equals("") == false) {
                Log.i("SharedPreferences", "Inner Found.");
                System.out.println(" >> strCurBook :" + strCurBook);
                Gson gson = new Gson();
                curbook = gson.fromJson(strCurBook, Book.class);
                IsSharedPref = true;
            } else {
                Log.i("SharedPreferences", "default is applied.");
                curbook = bookshelf.getBook(0);
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
    public void LoadBookShelf() {
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
                    bookshelf.AddBooks(abook);
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
    public boolean onUpdateBook(String _newCurBook, boolean _IsNewBook) {
        /* ToDo : update for User data */
        //Convert json format into Book class
        if ( _newCurBook != null) {
            Gson gson = new Gson();
            Book _target = gson.fromJson(_newCurBook, Book.class);
            try {
                if (_IsNewBook == false) {  //If the setting have already existed
                    Log.w("Current Book", "Insert unnecessary,instead update its attributes");
                    //In this method, There is a only change for the number of access now
                    dbhelper.updateData(ContractDBinfo.COL_NOACC, _target.getNumAcc() + 1, ContractDBinfo.TBL_BOOK);
                } else {    //If new setting is added
                    dbhelper.insertData(_target, ContractDBinfo.TBL_BOOK);
                    Log.i("Current Book", "Insert sucessful");
                }
                return true;
            } catch (Exception e) {
                Log.e("UpdateDB on HomeActivity", e.getMessage());
            }
        } else {    //No Book is setting then no action
            Log.w("Update_Book","No Book is founded");
            return false;
        }
        return false;
    };
    public void reset() {
        DelShpf();
        dbhelper.dropTable(ContractDBinfo.TBL_BOOK);
        dbhelper.dropTable(ContractDBinfo.TBL_USER);
    }
    public void DelShpf() {
        SharedPreferences pref = getSharedPreferences(sharedCurBook, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
    //It will be called from FragmentSaveShare when a record is sharing
    @Override
    public boolean onSharingSNS(String _newRecord) {
        if (_newRecord != null) {
            Intent toggleIntent = new Intent();
            String shareStr = _newRecord;
            toggleIntent.setAction(Intent.ACTION_SEND);
            toggleIntent.putExtra(Intent.EXTRA_TEXT, shareStr);
            toggleIntent.setType("text/plain");
            startActivityForResult(Intent.createChooser(toggleIntent, "Share your Records"), 0);
            //getIntent.putExtra("re", ">> Sharing's done");
        }
        return false;
    };
}