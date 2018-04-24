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

public class FragmentHomeMenu extends Fragment {

    public FragmentHomeMenu() { }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button btn_start = (Button) view.findViewById(R.id.btn_new_start);
        Button btn_quick = (Button) view.findViewById(R.id.btn_quick_start);
        Button btn_setlog = (Button) view.findViewById(R.id.btn_setlog);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do something in response to button click
                openSetDialog();
            }
        });
        btn_quick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag =  String.valueOf(R.string.TAG_LAB);
                Fragment newFragment = new FragmentLap();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.frag_home_container, newFragment, tag);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });
        btn_setlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Send to current set book info

                Fragment newFragment = new FragmentSetLog();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.frag_home_container, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });
        return view;
    }
    private void openSetDialog() {
        DialogFragment setDialogFragment = new DialogCustomSet();
        setDialogFragment.setTargetFragment(this, 0);
        setDialogFragment.show(getFragmentManager(), "setdialog");
    }
}
