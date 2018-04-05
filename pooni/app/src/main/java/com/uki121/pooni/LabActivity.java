package com.uki121.pooni;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by uki121 on 2018-04-03.
 */

public class LabActivity extends AppCompatActivity {

    float x, y;
    TextView myOutput, myRec;
    Button myBtnStart, myBtnRec, myBtnEnd;

    final static int Init =0;
    final static int Run =1;
    final static int Pause =2;

    int cur_Status = Init; //현재의 상태를 저장할변수를 초기화함.
    int myCount=1;
    long myBaseTime, myPauseTime;
    long beforeLapTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_start);

        myOutput = (TextView) findViewById(R.id.time_out);
        myRec = (TextView) findViewById(R.id.record);
        myBtnStart = (Button) findViewById(R.id.btn_start);
        myBtnRec = (Button) findViewById(R.id.btn_rec);
        myBtnEnd = (Button) findViewById(R.id.btn_end);
        myRec.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    switch(cur_Status){
                        case Run:
                            String str = myRec.getText().toString();
                            str = String.format("%d. %s\n", myCount, getLabTimeout());
                            //분기점(5)마다 텍스트를 다른색으로 적용
                            if(myCount % 5 != 0) {
                                myRec.append(str);
                            }
                            else {
                                setColorInPartitial(str, "","#FF1493",myRec);
                            }
                            myCount++; //카운트 증가
                            break;
                        case Pause:
                            //핸들러를 멈춤
                            myTimer.removeMessages(0);
                            //버튼 및 레코드값 초기화
                            myBtnStart.setText("시작");
                            myBtnRec.setText("기록");
                            myOutput.setText("00:00:00");
                            cur_Status = Init;
                            myCount = 1;
                            myRec.setText("");
                            myBtnRec.setEnabled(false);
                            break;
                    }
                }
                return true;
            }
        });
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
    public void myOnClick(View v){
        switch(v.getId()){
            case R.id.btn_start: //시작버튼을 클릭했을때 현재 상태값에 따라 다른 동작을 할수있게끔 구현.
                switch(cur_Status){
                    case Init:
                        myBaseTime = SystemClock.elapsedRealtime();
                        System.out.println(myBaseTime);
                        myTimer.sendEmptyMessage(0);//myTimer 초기화
                        myBtnStart.setText("멈춤"); //버튼의 문자"시작"을 "멈춤"으로 변경
                        myBtnRec.setEnabled(true); //기록버튼 활성
                        cur_Status = Run; //현재상태를 런상태로 변경
                        break;
                    case Run:
                        myTimer.removeMessages(0); //핸들러 메세지 제거
                        myPauseTime = SystemClock.elapsedRealtime();
                        myBtnStart.setText("시작");
                        myBtnRec.setText("리셋");
                        myBtnEnd.setEnabled(true);
                        cur_Status = Pause;
                        break;
                    case Pause:
                        long now = SystemClock.elapsedRealtime();
                        myTimer.sendEmptyMessage(0);
                        myBaseTime += (now- myPauseTime);
                        myBtnStart.setText("멈춤");
                        myBtnRec.setText("기록");
                        cur_Status = Run;
                        break;
                }
                break;
            case R.id.btn_rec:
                switch(cur_Status){
                    case Run:
                        String str = myRec.getText().toString();
                        str +=  String.format("%d. %s\n",myCount,getTimeOut());
                        myRec.setText(str);
                        myCount++; //카운트 증가

                        break;
                    case Pause:
                        //핸들러를 멈춤
                        myTimer.removeMessages(0);

                        myBtnStart.setText("시작");
                        myBtnRec.setText("기록");
                        myOutput.setText("00:00:00");

                        cur_Status = Init;
                        myCount = 1;
                        myRec.setText("");
                        myBtnRec.setEnabled(false);
                        myBtnEnd.setEnabled(false);
                        break;
                }
                break;
        }
    }
    Handler myTimer = new Handler(){
        public void handleMessage(Message msg){
            myOutput.setText(getHour_MinTime());    //최상단 output 구간에 현재 시간값을 출력
            myTimer.sendEmptyMessage(0);    //sendEmptyMessage 는 비어있는 메세지를 Handler 에게 전송하는겁니다.
        }
    };
    String getTimeOut()
    {   //분 : 초 : 밀리초 단위로 문자열값 출력
        long now = SystemClock.elapsedRealtime(); //애플리케이션이 실행되고나서 실제로 경과된 시간;
        long outTime = now - myBaseTime;
        String easy_outTime = String.format("%02d:%02d:%02d", outTime/1000 / 60, (outTime/1000)%60,(outTime%1000)/10);
        return easy_outTime;
    }
    String getHour_MinTime()
    {   //시간 : 분 : 초 단위로 문자열값 출력
        long now = SystemClock.elapsedRealtime();
        long outTime = now - myBaseTime;
        String hm_outTime = String.format("%02d:%02d:%02d", outTime/1000 /60 / 60, outTime/1000 / 60, (outTime/1000)%60);
        return hm_outTime;
    }
    String getLabTimeout()
    {   //lab time을 계산하여 반환
        if(myCount <= 1 ) {
            beforeLapTime = SystemClock.elapsedRealtime();
            return getTimeOut();
        }
        else {
            long curTime = SystemClock.elapsedRealtime();
            long outTime = curTime - beforeLapTime;
            beforeLapTime = curTime;
            String lap_outTime = String.format("%02d:%02d:%02d", outTime/1000 / 60, (outTime/1000)%60,(outTime%1000)/10);
            return lap_outTime;
        }
    }
    private TextView setColorInPartitial(String pre_string, String string, String color, TextView textView)
    {   //지정된 단어 길이만큼 색을 변경하여 텍스트뷰에 프린트
        SpannableStringBuilder builder = new SpannableStringBuilder(pre_string + string);
        builder.setSpan(new ForegroundColorSpan(Color.parseColor(color)), 0, pre_string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.append(builder);
        return textView;
    }
}
