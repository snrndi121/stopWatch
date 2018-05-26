package com.uki121.pooni;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import java.util.ArrayList;

public class FragmentTotalHistory extends Fragment {
    //def
    private String TAG = "FragmentTotalHistory";
    private static final String ARG = "total_history";
    private final String HISTORY_PIE = "총 풀이 기록";
    private final int NUM_CATEGORY = 4;
    private final String[] pie_category = {"통과", "1분 미만", "2분 미만", "4분 초과"};
    //var
    private PieChart chartTotalHistory;
    private DataTotal totalhistory;
    private float[] pie_raw_value = new float[]{1.0f, 2.0f, 93.0f, 4.0f};//defalut
    private boolean isSetHistory = false;

    public FragmentTotalHistory (){};
    public static FragmentTotalHistory newInstance(String _totalHistory) {
        FragmentTotalHistory fragment = new FragmentTotalHistory();
        if (_totalHistory != null) {
            Bundle args = new Bundle();
            args.putString(ARG, _totalHistory);
            fragment.setArguments(args);
        }
        return fragment;
    }
    @Override
    public void onCreate(Bundle SavedInstancState) {
        super.onCreate(SavedInstancState);
        if (getArguments() != null) {
            Log.d(TAG,"History custom data is set");
            String str = getArguments().getString(ARG);
            isSetHistory = str != null? true : false;
            totalhistory = isSetHistory != false? new DataTotal(str) : null;
        } else {
            Log.d(TAG,"History is null now");
            isSetHistory = false;
            totalhistory = null;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_total, container, false);
        init(view);
        return view;
    }
    public void init(View view) {
        Log.d(TAG, "onCreate: starting to create chart");
        chartTotalHistory = (PieChart) view.findViewById(R.id.piechart_total_history);
        //chartTotalHistory.setDescription("Sales by employee (In Thousands $");
        chartTotalHistory.setRotationEnabled(true);
        //pieChart.setUsePercentValues(true);
        //pieChart.setHoleColor(Color.BLUE);
        //pieChart.setCenterTextColor(Color.BLACK);
        chartTotalHistory.setHoleRadius(25f);
        chartTotalHistory.setTransparentCircleAlpha(0);
        chartTotalHistory.setCenterText(HISTORY_PIE);
        chartTotalHistory.setCenterTextSize(10);
        //pieChart.setDrawEntryLabels(true);
        //pieChart.setEntryLabelTextSize(20);
        //More options just check out the documentation!
        setDataSet();
        addDataSet();

        chartTotalHistory.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.d(TAG, "onValueSelected: Value select from chart.");
                Log.d(TAG, "onValueSelected: " + e.toString());
                Log.d(TAG, "onValueSelected: " + h.toString());
                int pos = (int) h.getX();
                //If history is set, then show the number of value of each category and its percent
                if (isSetHistory == true) {
                    Toast.makeText(getActivity(), "카테고리:" + pie_category[pos] + "\n" + "수치: " + pie_raw_value[pos] + "% (" + pie_raw_value[pos] + "개)", Toast.LENGTH_LONG).show();
                }
                //If a default history is set, then show its percent
                else {
                    Toast.makeText(getActivity(), "카테고리:" + pie_category[pos] + "\n" + "수치: " + pie_raw_value[pos] + "%", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onNothingSelected() {
            }
        });
    }
    public void setDataSet() {
        if (isSetHistory != false) {
            Log.d(TAG, "SetDataSet - custom data");
            int total = totalhistory.getSize();
            int[] val = totalhistory.getData();
            try {
                for (int i = 0; i < NUM_CATEGORY; ++i) {
                    pie_raw_value[i] = val[i] / total;
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else {
            Log.w(TAG, "SetDataSet - Default data mode active");
        }
    }
    public void addDataSet() {
        ArrayList < PieEntry > pie_value = new ArrayList<>();
        ArrayList < String > pie_name = new ArrayList<>();
        //set pie data on chart
        for (int i = 0; i < NUM_CATEGORY; ++i) {
            pie_name.add(pie_category[i]);
            pie_value.add(new PieEntry(pie_raw_value[i] , pie_name.get(i)));
        }
        //create data set based on pie data
        PieDataSet pieDataSet = new PieDataSet(pie_value, "전체 기록");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);
        //add colors to data set
        ArrayList <Integer> colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.GREEN);
        colors.add(Color.CYAN);
        colors.add(Color.MAGENTA);
        //colors.add(Color.GRAY);
        //colors.add(Color.RED);
        //colors.add(Color.YELLOW);
        pieDataSet.setColors(colors);
        //add legend to chart
        Legend legend = chartTotalHistory.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
        //create pie data object based on data set
        PieData pieData = new PieData(pieDataSet);
        chartTotalHistory.setData(pieData);
        chartTotalHistory.invalidate();
    }
}
