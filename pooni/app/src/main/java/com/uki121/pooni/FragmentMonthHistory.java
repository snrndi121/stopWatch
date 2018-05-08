package com.uki121.pooni;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import java.util.ArrayList;

public class FragmentMonthHistory extends Fragment {
    //def
    private final String TAG = "FragmentMonthHistory";
    private static final String ARG = "month_history";
    //var
    private DataMonth monthhistory;
    private boolean isMonthhistory = false;
    //chart
    private CombinedChart combinedchart;
    private final int itemcount = 12;
    protected String[] mMonths = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "June", "July","Aug", "Sep", "Oct", "Nov", "Dec"
    };

    public FragmentMonthHistory() {};
    public static FragmentMonthHistory newInstance(String _monthHistory) {
        FragmentMonthHistory fragment = new FragmentMonthHistory();
        if (_monthHistory != null) {
            Bundle args = new Bundle();
            args.putString(ARG, _monthHistory);
            fragment.setArguments(args);
        }
        return fragment;
    }
    @Override
    public void onCreate(Bundle SavedInstancState) {
        super.onCreate(SavedInstancState);
        if (getArguments() != null) {
            Log.d(TAG, "onCreate : argument is get");
            String str = getArguments().getString(ARG);
            isMonthhistory = str != null? true : false;
            monthhistory = new DataMonth().ToClass(str);
        } else {
            Log.d(TAG, "onCreate : argument is null");
            isMonthhistory = false;
            monthhistory = null;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history_month, container, false);
        init(v);
        return v;
    }
    public void init(View view) {
        setup_chart(view);
    }
    private void setup_chart(View view) {
        combinedchart = (CombinedChart) view.findViewById(R.id.combined_chart_month_history);
        combinedchart.getDescription().setEnabled(false);
        combinedchart.setBackgroundColor(Color.WHITE);
        combinedchart.setDrawGridBackground(false);
        combinedchart.setDrawBarShadow(false);
        combinedchart.setHighlightFullBarEnabled(false);

        // draw bars behind lines
        combinedchart.setDrawOrder(new CombinedChart.DrawOrder[]{
                DrawOrder.BAR, DrawOrder.LINE
        });
        //legend
        Legend l = combinedchart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        //Axis : set Axis position to left or right or both
        YAxis rightAxis = combinedchart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis = combinedchart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        //set X-Axis lables
        XAxis xAxis = combinedchart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mMonths[(int) value % mMonths.length];
            }
        });
        //create CombinedData object to set the LineData and BarData object
        CombinedData data = new CombinedData();
        data.setData(generateLineData());
        data.setData(generateBarData());

        xAxis.setAxisMaximum(data.getXMax() + 0.25f);
        combinedchart.setData(data);
        combinedchart.invalidate();
    }
    private LineData generateLineData() {
        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();
        entries = getLineEntriesData(entries);

        LineDataSet set = new LineDataSet(entries, "Line DataSet");
        set.setColor(Color.rgb(213, 45, 23));
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.rgb(213, 45, 23));
        set.setCircleRadius(5f);
        set.setFillColor(Color.rgb(213, 45, 23));
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(213, 45, 23));
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }
    private BarData generateBarData() {
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        entries = getBarEnteries(entries);

        BarDataSet set1 = new BarDataSet(entries, "BarEntry");
        set1.setColor(Color.rgb(50, 143, 70));
        set1.setValueTextColor(Color.rgb(60, 220, 78));
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        float barWidth = 0.45f; // x2 dataset
        BarData d = new BarData(set1);
        d.setBarWidth(barWidth);

        return d;
    }
    private ArrayList<Entry> getLineEntriesData(ArrayList<Entry> entries) {
        //default-mode
        if (isMonthhistory == false) {
            for (int index = 0; index < itemcount; index++) {
                entries.add(new Entry(index, getRandom(15, 5)));
            }
        } else {//history data-set
            for (int index = 0; index < itemcount; index++) {
                float _avgByprob = monthhistory.getMonth(index).getAvgByprob();
                entries.add(new Entry(index, _avgByprob));
            }
        }
        return entries;
    }
    private ArrayList<BarEntry> getBarEnteries(ArrayList<BarEntry> entries) {
        //default-mode
        if (isMonthhistory == false) {
            Log.w(TAG, "Default data mode active");
            for (int index = 0; index < itemcount; index++) {
                entries.add(new BarEntry(index, getRandom(25, 25)));
            }
        } else {//history data-set
            Log.d(TAG, "Valid data mode active");
            for (int index = 0; index < itemcount; index++) {
                int _totalexcess = monthhistory.getMonth(index).getTotalExcess();
                entries.add(new BarEntry(index, _totalexcess));
            }
        }
        return  entries;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionToggleLineValues: {
                for (IDataSet set : combinedchart.getData().getDataSets()) {
                    if (set instanceof LineDataSet)
                        set.setDrawValues(!set.isDrawValuesEnabled());
                }
                combinedchart.invalidate();
                break;
            }
            case R.id.actionToggleBarValues: {
                for (IDataSet set : combinedchart.getData().getDataSets()) {
                    if (set instanceof BarDataSet)
                        set.setDrawValues(!set.isDrawValuesEnabled());
                }

                combinedchart.invalidate();
                break;
            }
            case R.id.actionRemoveDataSet: {

                int rnd = (int) getRandom(combinedchart.getData().getDataSetCount(), 0);
                combinedchart.getData().removeDataSet(combinedchart.getData().getDataSetByIndex(rnd));
                combinedchart.getData().notifyDataChanged();
                combinedchart.notifyDataSetChanged();
                combinedchart.invalidate();
                break;
            }
        }
        return true;
    }
    protected float getRandom(float range, float startsfrom) {
        return (float) (Math.random() * range) + startsfrom;
    }
}