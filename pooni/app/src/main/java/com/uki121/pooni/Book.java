package com.uki121.pooni;

import android.util.Log;

import java.util.regex.Pattern;

class Book {
    final private int sTitle = 0, smaxTotalTIme = 1, smaxTimeEach = 2, sresOftime = 3, snumOfprob = 4;
    final int MAX_CATEGORY =5;
    public String[] category;
    public Book(){
        for (int i=0; i<MAX_CATEGORY; ++i) {
            category[i] = new String("NULL");
        }
    }
    public Book(String[] _data)
    {
        category = new String[MAX_CATEGORY];
        category[sTitle] = _data[sTitle];
        if ( isNumber(_data[smaxTotalTIme]) ) { category[smaxTotalTIme] = _data[smaxTotalTIme];}
        if ( IsRangeOf(_data[smaxTotalTIme], _data[smaxTimeEach]) ) { category[smaxTimeEach] = _data[smaxTimeEach];}
        if ( IsRangeOf(_data[smaxTotalTIme], _data[sresOftime]) ) { category[sresOftime] = _data[sresOftime];}
        if ( IsValidNum(_data[snumOfprob]) ) { category[snumOfprob] = _data[snumOfprob];}
    }
    public Book(Book bs)
    {
        this.category = bs.category;
    }
    private boolean isNumber(String _str)
    {
        return Pattern.matches("^[0-9]*$", _str);
    }
    private boolean IsRangeOf(String base, String target)
    {
        int a = Integer.parseInt(base), b = Integer.parseInt(target);
        boolean c1 = isNumber(target), c2 = a > b;
        //c1 is true -> baseTime is number
        //c2 is true -> baseTime > targetTime
        return c1 && c2;
    }
    private boolean IsValidNum(String _target)
    {
        boolean c1 = isNumber(_target), c2 = Integer.parseInt(_target) > 0;
        //c1 is true -> c1 is number
        //c2 is true -> c2 > 0
        return c1 && c2;//c1 && c2 is true -> target is valid
    }
    public final String[] getBook() {
        for (int i=0; i<MAX_CATEGORY; ++i) {
            Log.d("book_info_category " + i, category[i]);
        }
        return category;
    }
    public final String getTitle() {
        return category[sTitle];
    }
};