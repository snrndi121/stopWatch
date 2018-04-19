package com.uki121.pooni;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

public class dialogCustomSet extends DialogFragment {
    private View setting_dial_view;
    private bookShelf sb;
    private Fragment fragment;

    public dialogCustomSet() {
        fragment = new Fragment();
        sb =  new bookShelf();
    }
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
                                //View v = dialog.getCustomView();
                                sb.AddBooks(setting_dial_view);
                                sb.printBooks();
                            }
                        } catch (NullPointerException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                })
                .customView(R.layout.dialog_custom_set, wrapInScrollView)
                .build();
        return startDialog;
    }
}
