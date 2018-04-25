package com.uki121.pooni;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;

public class FragmentHomeMenu extends Fragment {
    //Bundle
    private static final String CURB = "current_book_info";
    private static String strCurBook;
    private Book curbook;

    public FragmentHomeMenu() { };
    public static FragmentHomeMenu newInstance(String _gsonBook) {
        strCurBook = _gsonBook;
        FragmentHomeMenu fragment = new FragmentHomeMenu();
        Bundle args = new Bundle();
        args.putString(CURB, _gsonBook);//key : value
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle SavedInstancState) {
        super.onCreate(SavedInstancState);
        if (getArguments() != null) {;
            String strCurBook = getArguments().getString(CURB);
            Gson gson = new Gson();
            curbook = gson.fromJson(strCurBook, Book.class);
            curbook.getBook();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button btn_new_start = (Button) view.findViewById(R.id.btn_new_start);
        Button btn_quick_start = (Button) view.findViewById(R.id.btn_quick_start);
        Button btn_setlog = (Button) view.findViewById(R.id.btn_setlog);

        //feat1 : Create new dialog
        btn_new_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSetDialog();
            }
        });
        //feat2 : Start 'stopwatch' right away
        btn_quick_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frag_home_container, FragmentLap.newInstance(strCurBook));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        //feat3 : Set your setting and check your logs
        btn_setlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new FragmentSetLog();
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
        Log.i("onResume", "####################################");
    }
    //Pop up a DialogFragment
    private void openSetDialog() {
        DialogFragment setDialogFragment = new DialogCustomSet();
        setDialogFragment.setTargetFragment(this, 0);
        setDialogFragment.show(getFragmentManager(), "setdialog");
    }
}
