package com.uki121.pooni;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;

public class FragmentSaveShare extends Fragment implements HomeActivity.onKeyBackPressedListener {
    //Bundle
    //Todo : 3 variables need : for current book if it is new, for personal record data, for elapsed time

    //for current book
    private static final String CURCB = "current_book_info";
    private static String strCurBook;
    private onUpdateStateListener updateToHomeListener;
    private Book curbook;
    private boolean IsCurrentBook = false;

    //view
    private Button btnShare;
    final String LAP_RECORD = "elapsed_record";

    public void FragmentSaveShare(){};
    public static FragmentSaveShare newInstance(String _gsonBook) {
        strCurBook = _gsonBook;
        FragmentSaveShare fragment = new FragmentSaveShare();
        Bundle args = new Bundle();
        args.putString(CURCB, _gsonBook);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle SavedInstancState) {
        super.onCreate(SavedInstancState);
        if (getArguments() != null) {
            String strCurBook = getArguments().getString(CURCB);
            Gson gson = new Gson();
            curbook = gson.fromJson(strCurBook, Book.class);
            IsCurrentBook = true;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_savenshare, container, false);

        String msg = getArguments().getString(LAP_RECORD);
        System.out.println(">> RECORD_STRING_FROM_LAP:" + msg);

        btnShare = (Button) view.findViewById(R.id.btn_share);
        btnShare.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateToHomeListener.onUpdateBook(strCurBook);
                updateToHomeListener.onSharingSNS(strCurBook);
            }
        });
        return view;
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
}
//private onUpdateStateListener updateStateListener;
interface onUpdateStateListener {
    public boolean onUpdateBook(String _strBook);//can be added more parameter
    public boolean onSharingSNS(String _strBook);
}