package com.uki121.pooni;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentMonthHistory extends Fragment {
    public FragmentMonthHistory() {};
    public static FragmentMonthHistory newInstance() {
        FragmentMonthHistory fragment = new FragmentMonthHistory();
        return fragment;
    }
    @Override
    public void onCreate(Bundle SavedInstancState) {
        super.onCreate(SavedInstancState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history_month, container, false);
        return v;
    }
}
