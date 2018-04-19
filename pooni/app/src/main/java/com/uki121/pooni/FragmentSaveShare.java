package com.uki121.pooni;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

public class FragmentSaveShare extends Fragment implements HomeActivity.onKeyBackPressedListener {
    private Button btnShare;
    final String LAP_RECORD = "elapsed_record";
    private final long FINISH_INTERVAL_TIME = 2000;
    private long   backPressedTime = 0;

    public void FragmentSaveShare(){};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_savenshare, container, false);

        btnShare = (Button) view.findViewById(R.id.btn_share);
        btnShare.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                String msg = getArguments().getString(LAP_RECORD);
            }
        });
        return view;
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
        System.out.println(">> SaveShare_back");
        backDialog();
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
                        fragmentManager.popBackStack();
                        Fragment bf = new FragmentLap();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.frag_home_container, bf);
                        transaction.commit();
                    }
                })
                .build();
        backDialog.show();
    }
/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        HomeActivity activity;
        if (context instanceof HomeActivity) {
            activity = (HomeActivity) context;
            ((HomeActivity) activity).setOnKeyBackPressedListener(this);
        }
    }
    @Override
    public void onBack() {
        onCustomBack();
        HomeActivity activity = (HomeActivity) getActivity();
        activity.setOnKeyBackPressedListener(null);
        //activity.onBackPressed();
    }
    private void onCustomBack() {

        Fragment bf = new FragmentLap();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frag_home_container, bf);
        transaction.commit();

        try {
            FragmentManager fragmentManager = getActivity().getFragmentManager();
            fragmentManager.beginTransaction().remove(FragmentSaveShare.this).commit();
            fragmentManager.popBackStack();
        } catch(NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }
    */
}