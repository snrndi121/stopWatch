package com.uki121.pooni;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;


import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by uki121 on 2018-04-03.
 */

public class HomeActivity extends AppCompatActivity {
    //onBackbutton_pressed
    private final long FINISH_INTERVAL_TIME = 2000;
    private long   backPressedTime = 0;

    private EditText name,stot,smot,snop, srot;
    private SettingBook sb;
    private View setting_dial_view;
    @Override
    protected void onCreate(Bundle savedInstanceStates) {
        super.onCreate(savedInstanceStates);
        setContentView(R.layout.activity_home);

        Button button_start = (Button) findViewById(R.id.button_start);
        button_start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                boolean wrapInScrollView = true;
                MaterialDialog setDialog =  new MaterialDialog.Builder(HomeActivity.this)
                        .title("setting")
                        .positiveText("확인")
                        .negativeText("취소")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@android.support.annotation.NonNull MaterialDialog dialog, @android.support.annotation.NonNull DialogAction which) {
                                // Some unimportant data stuff
                                /* Todo _ BIG*/
                                try {
                                    setting_dial_view = dialog.getCustomView();
                                    sb.AddBooks(setting_dial_view);
                                    sb.printBooks();
                                } catch(NullPointerException e)
                                {
                                    System.out.println(e.getMessage());
                                }
                            }
                        })
                        .customView(R.layout.lap_setting, wrapInScrollView)
                        .show();
            }
        });
        Button button_quick = (Button) findViewById(R.id.button_quick);
        button_quick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent op_quick = new Intent(HomeActivity.this, LabActivity.class);
                startActivity(op_quick);
            }
        });
        Button button_sample = (Button) findViewById(R.id.button_sample);
        button_sample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(HomeActivity.this)
                        .title("기다려")
                        .content("잠깐")
                        .progress(true, 0)
                        .show();
            }
        });
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
            Toast.makeText(getApplicationContext(), "한번 더 뒤로가기 누르면 종료", Toast.LENGTH_SHORT).show();

        }
    }
}
