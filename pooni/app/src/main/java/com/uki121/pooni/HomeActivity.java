package com.uki121.pooni;


import android.content.Intent;
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

public class HomeActivity extends AppCompatActivity {
    private final long FINISH_INTERVAL_TIME = 2000;
    private long   backPressedTime = 0;

    private bookShelf sb;
    private View setting_dial_view;

    class BtnOnClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.button_start:
                    // Do something in response to button click
                    boolean wrapInScrollView = true;
                    final MaterialDialog startDialog = new MaterialDialog.Builder(HomeActivity.this)
                            .title("setting")
                            .positiveText("확인")
                            .negativeText("취소")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    // Some unimportant data stuff
                                    /* Todo_BIG*/
                                    try {
                                        setting_dial_view = dialog.getCustomView();
                                        if (setting_dial_view != null)
                                        {
                                            //View v = dialog.getCustomView();
                                            sb.AddBooks(setting_dial_view);
                                            sb.printBooks();
                                        }
                                    } catch (NullPointerException e) {
                                        System.out.println(e.getMessage());
                                    }
                                }
                            })
                            .customView(R.layout.lap_setting, wrapInScrollView)
                            .build();
                        startDialog.show();
                    break;
                case R.id.button_quick:
                    Intent op_quick = new Intent(HomeActivity.this, LabActivity.class);
                    startActivity(op_quick);
                    break;
                case R.id.button_setting:
                    Intent op_set = new Intent(HomeActivity.this, SetLogActivity.class);
                    startActivity(op_set);
                    break;
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceStates) {
        super.onCreate(savedInstanceStates);
        setContentView(R.layout.activity_home);

        sb = new bookShelf();

        BtnOnClickListener onClickListener = new BtnOnClickListener() ;

        Button btn_start = (Button) findViewById(R.id.button_start);
        btn_start.setOnClickListener(onClickListener);

        Button btn_quick = (Button) findViewById(R.id.button_quick);
        btn_quick.setOnClickListener(onClickListener);

        Button btn_sample = (Button) findViewById(R.id.button_setting);
        btn_sample.setOnClickListener(onClickListener);
    }
    public void onBackPressed()
    {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;
        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
        {
            super.onBackPressed();
        }
        else
        {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), R.string.msg_back, Toast.LENGTH_SHORT).show();
        }
    }
}
