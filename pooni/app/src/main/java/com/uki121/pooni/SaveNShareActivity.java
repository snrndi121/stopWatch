package com.uki121.pooni;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SaveNShareActivity extends AppCompatActivity {

    private EditText mEditText;
    private Button btnShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_done);
        btnShare = (Button) findViewById(R.id.btn_share);
        btnShare.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toggleIntent = new Intent(), getIntent = getIntent();
                String shareStr = getIntent.getDataString();
                toggleIntent.setAction(Intent.ACTION_SEND);
                toggleIntent.putExtra(Intent.EXTRA_TEXT, shareStr);
                toggleIntent.setType("text/plain");
                startActivityForResult(Intent.createChooser(toggleIntent, "Share your Records"), 0);
                getIntent.putExtra("re", ">> Sharing's done");
                setResult(RESULT_OK, getIntent);
                finish();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) // 액티비티가 정상적으로 종료되었을 경우
        {
            if(requestCode==1) // requestCode==1 로 호출한 경우에만 처리합니다.
            {
                Log.d("Message", ""+data.getStringExtra("re"));
            }
        }
        else {
            Log.d("Message", ">> something wrong!!!");
        }
    }
}