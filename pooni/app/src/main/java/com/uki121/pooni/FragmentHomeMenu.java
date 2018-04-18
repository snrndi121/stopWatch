package com.uki121.pooni;

import android.app.Activity;
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

    private View setting_dial_view;
    private bookShelf sb;


    public FragmentHomeMenu() { }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        sb = new bookShelf();

        Button btn_start = (Button) view.findViewById(R.id.btn_new_start);
        Button btn_quick = (Button) view.findViewById(R.id.btn_quick_start);
        Button btn_setlog = (Button) view.findViewById(R.id.btn_setlog);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do something in response to button click
                boolean wrapInScrollView = true;
                Fragment newFragment = new FragmentHomeMenu();
                final MaterialDialog startDialog = new MaterialDialog.Builder(newFragment.getActivity())
                        .title("setting")
                        .positiveText("확인")
                        .negativeText("취소")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                // Some unimportant data stuff
                                /* Todo_BIG*/
                                try {
                                    setting_dial_view = dialog.getCustomView();
                                    if (setting_dial_view != null)
                                    {
                                        //View v = dialog.getCustomView();
                                        sb.AddBooks(setting_dial_view);
                                        sb.printBooks();
                                    }
                                } catch (NullPointerException e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                        })
                        .customView(R.layout.customview_setting, wrapInScrollView)
                        .build();
                startDialog.show();
            }
        });
        btn_quick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new FragmentLap();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.frag_home_container, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });
        btn_setlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
}
