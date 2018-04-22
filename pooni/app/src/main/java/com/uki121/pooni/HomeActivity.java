package com.uki121.pooni;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
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


import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by uki121 on 2018-04-03.
 */

public class HomeActivity extends AppCompatActivity implements dialogCustomSet.OnSetCreatedListener {
    private final long FINISH_INTERVAL_TIME = 2000;
    private long   backPressedTime = 0;
    private onKeyBackPressedListener mOnKeyBackPressedListener;
    private bookShelf bookshelf;
    private bookDBHelper dbhelper = null;
    private static final String DATABASE_NAME ="pooni.db";

    @Override
    protected void onCreate(Bundle savedInstanceStates) {
        super.onCreate(savedInstanceStates);
        setContentView(R.layout.activity_home);
        init();
    }
    public void init() {
        //Initialize variables.
        bookshelf = new bookShelf();
        dbhelper = new bookDBHelper(HomeActivity.this);
        //dbhelper.dropTable(ContractDBinfo.TBL_BOOK);
        //dbhelper.dropTable(ContractDBinfo.TBL_USER);
        //Load HomeFragment
        try {
            String tag =  String.valueOf(R.string.TAG_HOME);
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.add(R.id.frag_home_container, new FragmentHomeMenu(), tag);
            fragmentTransaction.commit();
        } catch(Exception e) {
            Log.e("HOME_ERROR", e.getMessage());
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
    //Button listener for handling event prduced in dialogCustomset
    @Override
    public boolean onSetCreated(Book _book) {
        System.out.println(" >> HomaActivity_onSetCreated");
        boolean canBeSaved = bookshelf.AddBooks(_book);
        if ( canBeSaved == true ) {
            dbhelper.insertData(ContractDBinfo.TBL_BOOK, _book);
        } else {
            System.out.println(" >> new books can not be saved !!!");
        }
        bookshelf.printBooks();
        load_values();
        return canBeSaved;
    }
    public void load_values()
    {
        dbhelper.showTable(ContractDBinfo.TBL_BOOK);
    }
}
