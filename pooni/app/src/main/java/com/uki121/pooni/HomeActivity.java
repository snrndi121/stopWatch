package com.uki121.pooni;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by uki121 on 2018-04-03.
 */

public class HomeActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceStates) {
        super.onCreate(savedInstanceStates);
        setContentView(R.layout.activity_home);

        Button button_start = (Button) findViewById(R.id.button_start);
        button_start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                new MaterialDialog.Builder(HomeActivity.this)
                        .title(R.string.setting_title)
                        .content(R.string.setting_name)
                        .inputType(InputType.TYPE_CLASS_TEXT) // | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                        .input(R.string.setting_name, R.string.name_prefill, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                // Do something
                            }
                        })
                        .show();
            }
        });
        Button button_quick = (Button) findViewById(R.id.button_quick);
        button_quick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                Intent op_quick = new Intent(HomeActivity.this, LabActivity.class);
                startActivity(op_quick);
                finish();
            }
        });
    }
}
