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
import java.util.List;

public class FragmentTotalHistory extends Fragment {
    //def
    private static String TAG = "FragmentTotalHistory";
    private static final String ARG = "total_history";
    private static final int NUM_CATEGORY = 4;
    //db
    private bookDBHelper dbhelper;
    //var
    private PieChart chartTotalHistory;
    private static final String[] pie_category = {"1분 미만", "2분 미만", "4분 미만", "기타"};
    private static float[] pie_value = {1.0f, 2.0f, 93.0f, 4.0f};//defalut
    private DataTotal totalhistory;
    private static boolean IsSetHistory = false;

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
            String str = getArguments().getString(ARG);
            totalhistory = new DataTotal(str);
            IsSetHistory = true;
            Log.d(TAG,"History is active");
        } else {
            totalhistory = null;
            Log.d(TAG,"History is null now");
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
        //db
        dbhelper = new bookDBHelper(getActivity());
        if (IsSetHistory == true) {
            dbhelper.insertData(new History(totalhistory, null), ContractDBinfo.TBL_HISTORY_PIE);
        }
        //
        Log.d(TAG, "onCreate: starting to create chart");
        chartTotalHistory = (PieChart) view.findViewById(R.id.piechart_total_history);
        //chartTotalHistory.setDescription("Sales by employee (In Thousands $");
        chartTotalHistory.setRotationEnabled(true);
        //pieChart.setUsePercentValues(true);
        //pieChart.setHoleColor(Color.BLUE);
        //pieChart.setCenterTextColor(Color.BLACK);
        chartTotalHistory.setHoleRadius(25f);
        chartTotalHistory.setTransparentCircleAlpha(0);
        chartTotalHistory.setCenterText("Super Cool Chart");
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

                int pos1 = e.toString().indexOf("(sum): ");
                String sales = e.toString().substring(pos1 + 4);

                for(int i = 0; i < pie_value.length; i++){
                    if(pie_value[i] == Float.parseFloat(sales)){
                        pos1 = i;
                        break;
                    }
                }
                String cate = pie_category[pos1 + 1];
                Toast.makeText(getActivity(), "카테고리 " + cate + "\n" + "Sales: $" + sales + "K", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected() {

            }
        });
    }
    public void setDataSet() {
        if (totalhistory != null) {
            Log.d(TAG, "SetDataSet");
            int total = totalhistory.getSize();
            int[] val = totalhistory.getData();
            for (int i=0; i<NUM_CATEGORY; ++i) {
                pie_value[i] = val[i] / total;
            }
        }
    }
    public void addDataSet() {
        ArrayList < PieEntry > value = new ArrayList<>();
        ArrayList < String > value_name = new ArrayList<>();

        //step1.initializing
        for (int i=0; i<pie_category.length; ++i) {
            value_name.add(pie_category[i]);
        }
        //ToDo : value is load by historyActivity and need to be delivered

        for (int i=0; i<pie_category.length; ++i) {
            value.add(new PieEntry(pie_value[i] , i));
        }
        //create the data set
        PieDataSet pieDataSet = new PieDataSet(value, "전체 기록");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        //colors.add(Color.GRAY);
        colors.add(Color.BLUE);
        //colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.CYAN);
        //colors.add(Color.YELLOW);
        colors.add(Color.MAGENTA);

        pieDataSet.setColors(colors);

        //add legend to chart
        Legend legend = chartTotalHistory.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        chartTotalHistory.setData(pieData);
        chartTotalHistory.invalidate();
    }
}
