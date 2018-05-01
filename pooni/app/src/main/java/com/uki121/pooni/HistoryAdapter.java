package com.uki121.pooni;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class HistoryAdapter extends FragmentPagerAdapter {
    private final int FRAG_NUM = 2;

    public HistoryAdapter(FragmentManager fragmentmanager) {
        super(fragmentmanager);
    };
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return FragmentTotalHistory.newInstance();
            case 1:
                return FragmentMonthHistory.newInstance();
            default :
                return null;
        }
    }
    @Override
    public int getCount() {
        return FRAG_NUM;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "종합 기록";
            case 1:
                return "월별 기록";
            default :
                return "NULL";
        }
    }
}
