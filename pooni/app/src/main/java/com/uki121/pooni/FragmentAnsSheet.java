package com.uki121.pooni;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;

import java.util.ArrayList;

public class FragmentAnsSheet extends Fragment {
    private Book curBook;
    private ArrayList<SheetItem> mSource;
    private static final String CUR_BOOK = "current_book";
    private final String TAG = "FragmentAnsSheet";
    //Recycle view
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static FragmentAnsSheet newInstance(String _gsonBook) {
        System.out.println(">> new :" + _gsonBook);
        FragmentAnsSheet fragment = new FragmentAnsSheet();
        Bundle args = new Bundle();
        args.putString(CUR_BOOK, _gsonBook);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle SavedInstancState) {
        super.onCreate(SavedInstancState);
        if (getArguments() != null) {
            String strCurBook = getArguments().getString(CUR_BOOK);
            Gson gson = new Gson();
            curBook = gson.fromJson(strCurBook, Book.class);
            //curBook.getBook();
        } else {
            Log.e(TAG, "fatal error in OnCreate()");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_answer_sheet, container, false);

        return view;
    }
    public void init(View view) {
        for(int i=0; i<10; ++i) {
            mSource.add(new SheetItem());
        }
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_omr);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new SheetAdapter(this.getContext(), mSource);
        mRecyclerView.setAdapter(mAdapter);
    }
}
