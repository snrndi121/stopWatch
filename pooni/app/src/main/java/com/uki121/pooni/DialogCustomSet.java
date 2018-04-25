package com.uki121.pooni;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DialogCustomSet extends DialogFragment {
    //view
    private View setting_dial_view;
    private Book temp_book;
    public DialogCustomSet() {
        temp_book = new Book();
    }
    /* ToDo : If a new setting doesn't match the given format then notice it to user*/
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        boolean wrapInScrollView = true;
        MaterialDialog startDialog = new MaterialDialog.Builder(getActivity())
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
                                setBookFromview(setting_dial_view);
                                if (temp_book != null) {
                                    new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("설정")
                                            .setContentText("문제를 시작합니다")
                                            .show();
                                    onCreateLap();
                                }
                            }
                        } catch (NullPointerException e) {
                            Log.e("Book_SetDialog", e.getMessage());
                        }
                    }
                })
                .customView(R.layout.dialog_custom_set, wrapInScrollView)
                .build();
        return startDialog;
    }
    public void setBookFromview(View view) {
        try {
            if (view != null) {
                String[] bdata = {((EditText) view.findViewById(R.id.setting_name)).getText().toString(),
                        ((EditText) view.findViewById(R.id.setting_totime)).getText().toString(),
                        ((EditText) view.findViewById(R.id.setting_maxtime)).getText().toString(),
                        ((EditText) view.findViewById(R.id.setting_rest)).getText().toString(),
                        ((EditText) view.findViewById(R.id.setting_count)).getText().toString()};
                temp_book = new Book(bdata);
            }
        } catch(Exception e) {
            Log.e(" >> Book to String_fail from ", e.getMessage());
        } catch(ExceptionInInitializerError e) {
            Log.e(" >> Initialziing_variable", e.getMessage());
        }
    }
    public void onCreateLap() {
        Fragment newFragment = new FragmentLap();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.frag_home_container, newFragment, null);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
}
