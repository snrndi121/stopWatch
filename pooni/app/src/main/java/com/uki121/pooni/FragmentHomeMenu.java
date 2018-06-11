package com.uki121.pooni;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.gson.Gson;

public class FragmentHomeMenu extends Fragment {
    //Bundle
    private static final String CURB = "current_book_info";
    private String strCurBook;

    public FragmentHomeMenu() { };
    public static FragmentHomeMenu newInstance(String _gsonBook) {
        FragmentHomeMenu fragment = new FragmentHomeMenu();
        if (_gsonBook != null) {
            Bundle args = new Bundle();
            args.putString(CURB, _gsonBook);//key : value
            fragment.setArguments(args);
        }
        return fragment;
    }
    @Override
    public void onCreate(Bundle SavedInstancState) {
        super.onCreate(SavedInstancState);
        if (getArguments() != null) {;
            strCurBook = getArguments().getString(CURB);
        } else {
            strCurBook = null;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button btn_new_start = (Button) view.findViewById(R.id.btn_new_start);
        Button btn_quick_start = (Button) view.findViewById(R.id.btn_quick_start);
        Button btn_setlog = (Button) view.findViewById(R.id.btn_setlog);

        //create new dialog
        btn_new_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSetDialog();
            }
        });
        //start 'stopwatch' right away
        btn_quick_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                //transaction.replace(R.id.frag_home_container, FragmentLap.newInstance(strCurBook, false));
                transaction.replace(R.id.frag_home_container, FragmentAnsSheet.newInstance(strCurBook));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        //set your setting and check your logs
        btn_setlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new FragmentSetHistory();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frag_home_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.i("onResume", "################ Home Menu ################");
    }
    //Pop up a DialogFragment
    private void openSetDialog() {
        DialogFragment setDialogFragment = new DialogCustomSet();
        setDialogFragment.setTargetFragment(this, 0);
        setDialogFragment.show(getFragmentManager(), "setdialog");
    }
}
