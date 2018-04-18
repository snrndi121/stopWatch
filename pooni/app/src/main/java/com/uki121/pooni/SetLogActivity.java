package com.uki121.pooni;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SetLogActivity extends AppCompatActivity {

    class BtnOnClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_setlist :
                    finish();
                    break;
                case R.id.btn_setoption1:
                    break;
                case R.id.btn_setoption2:
                    break;
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceStates) {
        super.onCreate(savedInstanceStates);
        setContentView(R.layout.activity_setting_home);

        BtnOnClickListener onClickListener = new BtnOnClickListener();

        Button btnSetList = (Button) findViewById(R.id.btn_setlist);
        btnSetList.setOnClickListener(onClickListener);
    }
}
