package com.uki121.pooni;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FragmentSaveShare extends Fragment implements HomeActivity.onKeyBackPressedListener {
    //Bundle
    //Todo : 3 variables need : for current book if it is new, for personal record data, for elapsed time
    //for current book
    private static final String APPR = "applied_record_info";
    private static String strUserRecord;
    private onUpdateStateListener updateToHomeListener;
    private ElapsedRecord curUserRec;
    private static boolean IsNewBook = false;
    private static boolean IsSaved;
    //view
    private Button btnShare, btnSave;

    public void FragmentSaveShare(){};
    public static FragmentSaveShare newInstance(String _gsonUser, boolean _IsNewSet) {
        strUserRecord = _gsonUser;//pair < book, list < string > > to String
        IsNewBook = _IsNewSet;
        FragmentSaveShare fragment = new FragmentSaveShare();
        Bundle args = new Bundle();
        args.putString(APPR, _gsonUser);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle SavedInstancState) {
        super.onCreate(SavedInstancState);
        if (getArguments() != null) {
            strUserRecord = getArguments().getString(APPR);
            Gson gson = new Gson();
            curUserRec = gson.fromJson(strUserRecord, ElapsedRecord.class);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_savenshare, container, false);
        Log.i("onCreateView", "on");
        init(view);
        return view;
    }
    void init(View view) {
        IsSaved = false;
        btnShare = (Button) view.findViewById(R.id.btn_share);
        btnShare.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateToHomeListener.onUpdateRecord(strUserRecord, IsNewBook);
                updateToHomeListener.onSharingSNS(strUserRecord);
            }
        });
        btnSave = (Button) view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                SweetAlertDialog sweetdialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                /*ToDo : 저장항목을 선택가능하도록 */
                sweetdialog.setTitleText(getResources().getString(R.string.save))
                        .setContentText(getResources().getString(R.string.saveAfteraccum))
                        .setConfirmText(getResources().getString(R.string.agree))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                updateToHomeListener.onUpdateRecord(strUserRecord, IsNewBook);
                                IsSaved = true;
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelButton(getResources().getString(R.string.disagree), new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }
        });
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        HomeActivity activity;
        activity = (HomeActivity) context;
        try {
            if (context instanceof HomeActivity) {
                (activity).setOnKeyBackPressedListener(this);
                updateToHomeListener = (onUpdateStateListener) activity;
            }
        } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString() + " must implement onUpdateStateListener");
        }
    }
    @Override
    public void onBack() {
        System.out.println(">> SaveShare_back");
        if (IsSaved == false) { //already saved the data
            Log.i("onBack", "Not saved states");
            backDialog();
        } else { //Go back to home
            //Home backHandler reset
            HomeActivity activity = (HomeActivity) getActivity();
            activity.setOnKeyBackPressedListener(null);
            //Fragment transition
            FragmentManager fragmentmanager = getFragmentManager();
            FragmentTransaction transaction = fragmentmanager.beginTransaction();
            fragmentmanager.popBackStack();
            Log.i("onBack", "SaveShare go to existing home");
            transaction.replace(R.id.frag_home_container, new FragmentHomeMenu());
            transaction.commit();
        }
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
                        FragmentManager fragmentManager = getActivity().getFragmentManager();
                        fragmentManager.popBackStack();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        if (curUserRec != null) {
                            transaction.replace(R.id.frag_home_container, FragmentLap.newInstance(strUserRecord, IsNewBook));
                        } else {
                            transaction.replace(R.id.frag_home_container, new FragmentLap());
                        }
                        transaction.commit();
                    }
                })
                .build();
        backDialog.show();
    }
}
//private onUpdateStateListener updateStateListener;
interface onUpdateStateListener {
    public boolean onUpdateRecord(String _strUserRec, boolean _isNewBook);//can be added more parameter
    public boolean onSharingSNS(String _strUserRec);
}