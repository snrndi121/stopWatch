package com.uki121.pooni;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
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

public class dialogCustomSet extends DialogFragment {
    private OnSetCreatedListener sListener;
    public interface OnSetCreatedListener {
        public boolean onSetCreated(Book b);//Uri articleUri);
    }
    private View setting_dial_view;
    private Book temp_book;

    public dialogCustomSet() {
        temp_book = new Book();
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
                                setBookFromview(setting_dial_view);
                                //temp_book.getBook();
                                boolean sflag = sListener.onSetCreated(temp_book);
                                if ( sflag ) Log.d("Book_SetDialog :","Success");
                            }
                        } catch (NullPointerException e) {
                            Log.d("Book_SetDialog", e.getMessage());
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
                        ((EditText) view.findViewById(R.id.setting_count)).getText().toString(),
                        ((EditText) view.findViewById(R.id.setting_rest)).getText().toString()};
                temp_book = new Book(bdata);
            }
        } catch(Exception e) {
            Log.d(" >> Book to String_fail from ", e.getMessage());
        } catch(ExceptionInInitializerError e) {
            Log.d(" >> Initialziing_variable", e.getMessage());
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        HomeActivity activity;
        activity = (HomeActivity) context;
        try {
            if (context instanceof HomeActivity) {
                sListener = (OnSetCreatedListener) activity;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }

}
