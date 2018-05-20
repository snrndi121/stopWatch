package com.uki121.pooni;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/* ToDo : separte some functions from class FragmentLap, too big */
public class FragmentLap extends Fragment implements HomeActivity.onKeyBackPressedListener {
    //Bundle
    private static final String APPB = "applied_book_info";
    private static String strCurBook;
    private Book curBook;
    private static boolean IsNewBook = false;
    //View
    private Button btnStart, btnRec, btnEnd, btnDel;
    private TextView myOutput, myRec;
    //Time handler
    private final static int Init =0;
    private final static int Run =1;
    private final static int Pause =2;
    private int cur_Status = Init;
    private int myCount=1;
    private long baseTime, pauseTime, beforeLapTime;
    List listLap = new ArrayList();
    //Setting book in Time handler
    long total_time, each_time;
    //user
    int excess_prob = 0;
    long excess_time = 0;

    public void FragmentLap(){ };
    public static FragmentLap newInstance(String _gsonBook, boolean _isNewBook) {
        strCurBook = _gsonBook;
        IsNewBook = _isNewBook;
        System.out.println(">> new :" + strCurBook);
        FragmentLap fragment = new FragmentLap();
        Bundle args = new Bundle();
        args.putString(APPB, _gsonBook);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle SavedInstancState) {
        super.onCreate(SavedInstancState);
        if (getArguments() != null) {
            //Todo : same code is duplicated
            if (IsNewBook == true) {//case1. new Book is set
                strCurBook = getArguments().getString(APPB);
                Gson gson = new Gson();
                curBook = gson.fromJson(strCurBook, Book.class);
                curBook.getBook();
            } else {//case2. current book is set
                strCurBook = getArguments().getString(APPB);
                Gson gson = new Gson();
                curBook = gson.fromJson(strCurBook, Book.class);
                curBook.getBook();
            }
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lab_start, container, false);
        init(view);
        return view;
    }
    public void init(View view) {
        //textview on FragmentLap
        myOutput = (TextView) view.findViewById(R.id.time_out);
        myRec = (TextView) view.findViewById(R.id.record);
        //button on FragmentLap
        btnStart = (Button) view.findViewById(R.id.btn_start);
        btnRec = (Button) view.findViewById(R.id.btn_rec);
        btnDel = (Button) view.findViewById(R.id.btn_del);
        btnEnd = (Button) view.findViewById(R.id.btn_end);
        //button-clickListener
        BtnOnClickListener btnOnClickListener = new BtnOnClickListener() ;
        btnStart.setOnClickListener(btnOnClickListener);
        btnRec.setOnClickListener(btnOnClickListener);
        btnDel.setOnClickListener(btnOnClickListener);
        btnEnd.setOnClickListener(btnOnClickListener);

        /* [ToDo] : each time is considered as min. If not, it will cause error and bug */
        //apply current book's setting to count time
        if (curBook != null)
        {
            each_time = Integer.parseInt(curBook.getEachTime())  * 1000; //considered this as second
            total_time = Integer.parseInt(curBook.getToTime()) * 60000; //considered this as min
            if (IsNewBook == false) {
                Log.i("Book_SettingInLap", "Existing setting is applied");
            } else {    //(IsNewBook == true)
                Log.i("Book_SettingInLap", "New setting is applied");
            }
        }
    }
    //Handler for current time
    Handler myTimer = new Handler(){
        public void handleMessage(Message msg){
            myOutput.setText(getHour_MinTime());    //print the elapsed time into top board
            myTimer.sendEmptyMessage(0);    //sendEmptyMessage is now send a null message into Handler
        }
    };
    // print time with format(min : seconds : millis)
    String getTimeOut()
    {
        long now = SystemClock.elapsedRealtime(); //actual time(milli) after being loaded
        long outTime = now - baseTime;
        long millis = (outTime % 1000) /10, seconds = outTime/1000 , mins = seconds / 60;
        String easy_outTime = String.format("%02d:%02d:%02d", mins, seconds % 60, millis);
        return easy_outTime;
    }
    //print time with format(hour : min : seconds)
    String getHour_MinTime()
    {
        long now = SystemClock.elapsedRealtime();
        long outTime = now - baseTime;
        long seconds = outTime/1000, mins = seconds /60, hours = mins /60;
        String hm_outTime = String.format("%02d:%02d:%02d", hours, mins % 60, seconds % 60);
        return hm_outTime;
    }
    //To calculate the lab time
    long setLabTimeout()
    {
        long curTime = SystemClock.elapsedRealtime();
        long _curLabTime = curTime - beforeLapTime;
        beforeLapTime = curTime;
        return _curLabTime;
    }
    //Print the lap time by "min:second:milli" form.
    String getLabTimeout()
    {   //calculate a lab time then return
        if(myCount <= 1 ) {
            beforeLapTime = SystemClock.elapsedRealtime();
            return getTimeOut();
        }
        else {
            long outTime = setLabTimeout();
            String lap_outTime = String.format("%02d:%02d:%02d", outTime/1000 / 60, (outTime/1000)%60,(outTime%1000)/10);
            return lap_outTime;
        }
    }
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
            baseTime += ignored_last;
            temptime = SystemClock.elapsedRealtime() - baseTime;
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
            (activity).setOnKeyBackPressedListener(this);
        }
    }
    @Override
    public void onBack() {
        System.out.println(">> Lap_back");
        backDialog();
        HomeActivity activity = (HomeActivity) getActivity();
        activity.setOnKeyBackPressedListener(null);
        //activity.onBackPressed();
    }
    private void backDialog() {
        MaterialDialog backDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.exit_lab)
                .content(R.string.cancel_record)
                .positiveText(R.string.agree)
                .negativeText(R.string.disagree)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@android.support.annotation.NonNull MaterialDialog dialog, @android.support.annotation.NonNull DialogAction which) {
                        FragmentManager fragmentManager = getActivity().getFragmentManager();//
                        fragmentManager.beginTransaction().remove(FragmentLap.this).commit();//
                        fragmentManager.popBackStack();
                    }
                })
                .build();
        backDialog.show();
    }
    //Count the number of access if a current elapsed time exceed its each Bound time.
    public void checkEachBound(String _curLaptime) {
        long _inMilli = recordTolong(_curLaptime, "msms");
        if (each_time < _inMilli) {
            excess_prob++;
        }
        Log.i("Excess_problem", String.valueOf(excess_prob));
    }
    public String convertToRecord(Book src_book, List < String > src_lap) {
        ElapsedRecord userRecord = new ElapsedRecord(src_book, src_lap);//new instance of ElapsedRecord from record made
        Gson gson = new GsonBuilder().create();
        return gson.toJson(userRecord, ElapsedRecord.class);
    }
    //Calculate a mount of access time
    public void checkTotalBound() {
        String _curPauseTime = myOutput.getText().toString();
        long _curToTimeInMilli = recordTolong(_curPauseTime, "hms");
        excess_time = _curToTimeInMilli - total_time;
        Log.i("Excess_time", String.valueOf(excess_time));
    }
    class BtnOnClickListener implements Button.OnClickListener {
        final String LAP_RECORD = "elapsed_record";
        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.btn_start:
                    switch(cur_Status){
                        case Init:
                            baseTime = SystemClock.elapsedRealtime();
                            myTimer.sendEmptyMessage(0);    //myTimer 초기화
                            btnStart.setText("멈춤"); //버튼의 문자"시작"을 "멈춤"으로 변경
                            btnRec.setEnabled(true); //기록버튼 활성
                            btnDel.setEnabled(true); //삭제버튼 활성
                            cur_Status = Run; //현재상태를 런상태로 변경
                            break;
                        case Run:
                            adjustTimer_pause();  //processing of recording time that can not be saved after stopping.
                            myTimer.removeMessages(0); //clear the message of handler
                            pauseTime = SystemClock.elapsedRealtime();
                            btnStart.setText("시작");
                            btnRec.setText("리셋");
                            btnEnd.setEnabled(true);
                            cur_Status = Pause;
                            break;
                        case Pause:
                            long now = SystemClock.elapsedRealtime();
                            myTimer.sendEmptyMessage(0);
                            baseTime += (now- pauseTime);
                            btnStart.setText("멈춤");
                            btnRec.setText("기록");
                            cur_Status = Run;
                            beforeLapTime = SystemClock.elapsedRealtime();
                            break;
                    }
                    break;
                case R.id.btn_rec: {
                    switch (cur_Status) {
                        case Run:
                            String curlap = getLabTimeout();
                            checkEachBound(curlap);//Event excess
                            String str = String.format("%d. %s\n", myCount, curlap);
                            //print colored a string every 5th.
                            if (myCount % 5 != 0) {
                                myRec.append(str);
                            } else {
                                setColorInPartitial(str, "", "#FF1493", myRec);
                            }
                            listLap.add(str);
                            myCount++; //add a count of lap
                            break;
                        case Pause:
                            //stop handler
                            myTimer.removeMessages(0);
                            //reset buttons and records
                            btnStart.setText("시작");
                            btnRec.setText("기록");
                            myOutput.setText("00:00:00");
                            cur_Status = Init;
                            myCount = 1;
                            myRec.setText("");
                            listLap.clear();
                            btnRec.setEnabled(false);
                            break;
                    }
                    break;
                }
                case R.id.btn_del: {
                    if (myCount >= 2) {
                        myCount--;
                        coloringStrInDel(myRec);
                    } else { /* do nothing */
                        Log.d("Record_count ", "can't be lower than 0");
                    }
                    break;
                }
                case R.id.btn_end: {
                    checkTotalBound();
                    String strElp = convertToRecord(curBook, listLap);//convert
                    System.out.println(">> Record :" + strElp);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frag_home_container, FragmentSaveShare.newInstance(strElp, IsNewBook));
                    transaction.addToBackStack(null);
                    transaction.commit();
                    break;
                }
            }
        }
    }
}
