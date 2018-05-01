package com.uki121.pooni;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentTotalHistory extends Fragment {
    public FragmentTotalHistory (){};
    public static FragmentTotalHistory newInstance() {
        FragmentTotalHistory fragment = new FragmentTotalHistory();
        return fragment;
    }
    @Override
    public void onCreate(Bundle SavedInstancState) {
        super.onCreate(SavedInstancState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history_total, container, false);
        return v;
    }
}
