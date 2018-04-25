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
    //private static final String CURB = "current_book_info";
    //private static final String NEWB = "new_book_info";
    private static final String APPB = "applied_book_info";
    private static String strBook;
    private onUpdateStateListener updateToHomeListener;
    private Book tempBook;
    private static boolean IsNewBook = false;
    private static boolean IsSaved;
    //view
    private Button btnShare, btnSave;

    public void FragmentSaveShare(){};
    public static FragmentSaveShare newInstance(String _gsonBook, boolean _IsNewSet) {
        strBook = _gsonBook;
        IsNewBook = _IsNewSet;
        FragmentSaveShare fragment = new FragmentSaveShare();
        Bundle args = new Bundle();
        args.putString(APPB, _gsonBook);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle SavedInstancState) {
        super.onCreate(SavedInstancState);
        if (getArguments() != null) {
                strBook = getArguments().getString(APPB);
                Gson gson = new Gson();
                tempBook = gson.fromJson(strBook, Book.class);
                tempBook.getBook();
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
    @Override
    public void onResume() {
        super.onResume();
        Log.i("onResume", "on");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.i("onStop", "on");
    }
    void init(View view) {
        IsSaved = false;
        btnShare = (Button) view.findViewById(R.id.btn_share);
        btnShare.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateToHomeListener.onUpdateBook(strBook);
                updateToHomeListener.onSharingSNS(strBook);
            }
        });
        btnSave = (Button) view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                SweetAlertDialog sweetdialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                /*ToDo : 저장항목을 선택가능하도록 */
                sweetdialog.setTitleText("저장")
                        .setContentText("기록을 누적하고 새로운 설정은 저장합니다.")
                        .setConfirmText("확인")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                updateToHomeListener.onUpdateBook(strBook);
                                IsSaved = true;
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelButton("취소", new SweetAlertDialog.OnSweetClickListener() {
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
            backDialog();
        } else { //Go back to home
            String home_tag = "frag_home";
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.popBackStack();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            FragmentHomeMenu fragHome = (FragmentHomeMenu) fragmentManager.findFragmentByTag(home_tag);

            if(fragHome == null) {
                Log.i("onBack", "SaveShare go to new home");
                FragmentHomeMenu fragment = new FragmentHomeMenu();
                fragmentTransaction.add(R.id.frag_home_container, fragment, home_tag);
            } else {
                Log.i("onBack", "SaveShare go to existing home");
                fragmentTransaction.replace(R.id.frag_home_container, fragHome, home_tag);
            }
            fragmentTransaction.commit();

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
                        Fragment bf = new FragmentLap();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.frag_home_container, bf);
                        transaction.commit();
                    }
                })
                .build();
        backDialog.show();
    }
}
//private onUpdateStateListener updateStateListener;
interface onUpdateStateListener {
    public boolean onUpdateBook(String _strBook);//can be added more parameter
    public boolean onSharingSNS(String _strBook);
}