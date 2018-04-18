package com.uki121.pooni;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class FragmentLap extends Fragment implements HomeActivity.onKeyBackPressedListener {
    private final long FINISH_INTERVAL_TIME = 2000;
    private long   backPressedTime = 0;

    /* var_lap operation */
    float x, y;
    TextView myOutput, myRec;
    Button myBtnStart, myBtnRec, myBtnEnd,myBtnDel;

    final static int Init =0;
    final static int Run =1;
    final static int Pause =2;

    int cur_Status = Init;
    int myCount=1;
    long myBaseTime, myPauseTime, baseLapTime;
    List listLap = new ArrayList();

    public interface onKeyBackPressedListener {
        public void onBack();
    }
    public void FragmentLap(){};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lab_start, container, false);
        myOutput = (TextView) view.findViewById(R.id.time_out);
        myRec = (TextView) view.findViewById(R.id.record);
        myBtnStart = (Button) view.findViewById(R.id.btn_start);
        myBtnRec = (Button) view.findViewById(R.id.btn_rec);
        myBtnEnd = (Button) view.findViewById(R.id.btn_end);
        myBtnDel = (Button) view.findViewById(R.id.btn_del);
        myBtnDel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myCount > 2)
                {
                    myCount--;
                    coloringStrInDel(myRec);
                } else if (myCount == 2) {
                    myCount--;
                    myRec.setText("");
                } else { /* do nothing */
                    Log.d("Record_count ", "can't be lower than 0");
                }
            }
        });
        myBtnEnd.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Do something in response to button click
                /*
                Intent ssIntent = new Intent
                ssIntent.setData(Uri.parse(myRec.getText().toString()));
                startActivityForResult(ssIntent, 0);
                finish();
                */
            }
        });
        myRec.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    switch(cur_Status){
                        case Run:
                            String str = String.format("%d. %s\n", myCount, getLabTimeout());
                            //print
                            if(myCount % 5 != 0) {
                                myRec.append(str);
                            }
                            else {
                                setColorInPartitial(str, "","#FF1493",myRec);
                            }
                            listLap.add(str);
                            myCount++;
                            break;
                        case Pause:
                            //print colored a string every 5th.
                            myTimer.removeMessages(0);
                            //reset buttons and records
                            myBtnStart.setText("시작");
                            myBtnRec.setText("기록");
                            myOutput.setText("00:00:00");
                            cur_Status = Init;
                            myCount = 1;
                            myRec.setText("");
                            myBtnRec.setEnabled(false);
                            myBtnDel.setEnabled(false);
                            break;
                    }
                }
                return true;
            }
        });
        return view;
    }
    //A button listener for myBtnStart and myBtnRec
    public void myOnClick(View v){
        switch(v.getId()){
            case R.id.btn_start: //시작버튼을 클릭했을때 현재 상태값에 따라 다른 동작을 할수있게끔 구현.
                switch(cur_Status){
                    case Init:
                        myBaseTime = SystemClock.elapsedRealtime();
                        myTimer.sendEmptyMessage(0);    //myTimer 초기화
                        myBtnStart.setText("멈춤"); //버튼의 문자"시작"을 "멈춤"으로 변경
                        myBtnRec.setEnabled(true); //기록버튼 활성
                        myBtnDel.setEnabled(true); //삭제버튼 활성
                        cur_Status = Run; //현재상태를 런상태로 변경
                        break;
                    case Run:
                        adjustTimer_pause();  //processing of recording time that can not be saved after stopping.
                        myTimer.removeMessages(0); //clear the message of handler
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
                        baseLapTime = SystemClock.elapsedRealtime();
                        break;
                }
                break;
            case R.id.btn_rec:
                switch(cur_Status){
                    case Run:
                        String str = String.format("%d. %s\n", myCount, getLabTimeout());
                        //print colored a string every 5th.
                        if(myCount % 5 != 0) {
                            myRec.append(str);
                        }
                        else {
                            setColorInPartitial(str, "","#FF1493",myRec);
                        }
                        listLap.add(str);
                        myCount++; //add a count of lap
                        break;
                    case Pause:
                        //stop handler
                        myTimer.removeMessages(0);
                        //reset buttons and records
                        myBtnStart.setText("시작");
                        myBtnRec.setText("기록");
                        myOutput.setText("00:00:00");
                        cur_Status = Init;
                        myCount = 1;
                        myRec.setText("");
                        listLap.clear();
                        myBtnRec.setEnabled(false);
                        break;
                }
                break;
        }
    }
    //Handler for current time
    Handler myTimer = new Handler(){
        public void handleMessage(Message msg){
            myOutput.setText(getHour_MinTime());    //print the elapsed time into top board
            myTimer.sendEmptyMessage(0);    //sendEmptyMessage is now send a null message into Handler
        }
    };
    //Print the normal time by "hour:min:second"form
    String getTimeOut()
    {   //print time with format(min : seconds : millis)
        long now = SystemClock.elapsedRealtime(); //actual time(milli) after being loaded
        long outTime = now - myBaseTime;
        long millis = (outTime % 1000) /10, seconds = outTime/1000 , mins = seconds / 60;
        String easy_outTime = String.format("%02d:%02d:%02d", mins, seconds % 60, millis);
        return easy_outTime;
    }
    String getHour_MinTime()
    {   //print time with format(hour : min : seconds)
        long now = SystemClock.elapsedRealtime();
        long outTime = now - myBaseTime;
        long seconds = outTime/1000, mins = seconds /60, hours = mins /60;
        String hm_outTime = String.format("%02d:%02d:%02d", hours, mins % 60, seconds % 60);
        return hm_outTime;
    }
    //To calculate the lab time
    long setLabTimeout()
    {
        long curTime = SystemClock.elapsedRealtime();
        long _curLabTime = curTime - baseLapTime;
        baseLapTime = curTime;
        return _curLabTime;
    }
    //Print the lap time by "min:second:milli" form.
    String getLabTimeout()
    {   //calculate a lab time then return
        if(myCount <= 1 ) {
            baseLapTime = SystemClock.elapsedRealtime();
            return getTimeOut();
        }
        else {
            long outTime = setLabTimeout();
            String lap_outTime = String.format("%02d:%02d:%02d", outTime/1000 / 60, (outTime/1000)%60,(outTime%1000)/10);
            return lap_outTime;
        }
    }
    /* Todo : This fucntion is not used yet */
    long recordTolong(String _src, String _recordType)
    {
        int[] unit = new int[3];
        long res;
        StringTokenizer str = new StringTokenizer(_src, ":");
        int countTokens = str.countTokens();
        //Get the token of lap typed string.
        for (int i=0; i<countTokens; ++i) {
            unit[i] = Integer.parseInt(str.nextToken());
        }
        //Summation of lap
        if ( _recordType.equals("msms") ) {
            return res = unit[0] * 60000 + unit[1] * 1000 + unit[2] * 10;//format(min : seconds : milli)
        } else if( _recordType.equals("hms") ) {
            return res = unit[0] * 3600000 + unit[1] * 60000 + unit[2] * 1000;//format(hour : min : second)
        } else {
            System.out.println(">> There's no type of record");
            return 0;
        }

    }
    //Get rid of the differces between the sum of lap and the total operating time.
    //Processing of recording time that can not be saved after stopping.
    void adjustTimer_pause()
    {
        if ( myCount > 1) {
            long ignored_last = setLabTimeout(), temptime;
            myBaseTime += ignored_last;
            temptime = SystemClock.elapsedRealtime() - myBaseTime;
            long seconds = temptime / 1000, mins = seconds / 60, hours = mins / 60;
            String tempTime = String.format("%02d:%02d:%02d", hours, mins % 60, seconds % 60);
            myOutput.setText(tempTime);
        } else if ( myCount == 1 ) {
            myOutput.setText("00:00:00");
        }
    }
    //To highlight the every 5th in the text view
    private TextView setColorInPartitial(String _baseStr, String _attachedStr, String color, TextView textView)
    {
        //print into textview with colored string by the specified lenth
        SpannableStringBuilder builder = new SpannableStringBuilder(_baseStr + _attachedStr);
        builder.setSpan(new ForegroundColorSpan(Color.parseColor(color)), 0, _baseStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.append(builder);
        return textView;
    }
    //To print lap' textview with coloring after deleting the last element of lap.
    private void coloringStrInDel(TextView _myrecView)
    {
        try {
            //step1. deletion
            listLap.remove(listLap.size() - 1);

            //step2. clear view and print new lap strings into textview with Coloring
            int tcount = 0;
            Iterator it_lap = listLap.iterator();

            _myrecView.setText("");
            while (it_lap.hasNext()) {
                String _addedStr = (String) it_lap.next();
                if (++ tcount % 5 != 0) {
                    _myrecView.append(_addedStr);
                } else {
                    setColorInPartitial(_addedStr, "", "#FF1493", _myrecView);
                }
            }
        } catch (Exception e) {
            Log.d("Fail", e.getMessage());
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        HomeActivity activity;
        if (context instanceof HomeActivity) {
            activity = (HomeActivity) context;
            ((HomeActivity) activity).setOnKeyBackPressedListener(this);
        }
    }
    public void onBack() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        final HomeActivity activity = (HomeActivity) getActivity();
        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
        {
            activity.onBackPressed();
        }
        else
        {
            backPressedTime = tempTime;
            new MaterialDialog.Builder(getActivity())
                    .title(R.string.exit_lab)
                    .content(R.string.cancel_record)
                    .positiveText(R.string.agree)
                    .negativeText(R.string.disagree)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@android.support.annotation.NonNull MaterialDialog dialog, @android.support.annotation.NonNull DialogAction which) {
                            activity.setOnKeyBackPressedListener(null);
                            activity.onBackPressed();
                        }
                    })
                    .show();
        }
    }
}
