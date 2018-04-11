package com.uki121.pooni;

import java.util.regex.Pattern;

class Book {
    final private int sname = 0, smaxTotalTIme = 1, smaxTimeEach = 2, sresOftime = 3, snumOfprob = 4;
    public String[] category;
    public Book(){}
    public Book(String[] _data)
    {
        category[sname] = _data[0];
        if ( isNumber(_data[smaxTotalTIme])) { category[smaxTotalTIme] = _data[smaxTotalTIme];}
        if ( IsRangeOf(_data[smaxTotalTIme], _data[smaxTimeEach])) { category[smaxTimeEach] = _data[smaxTimeEach];}
        if ( IsRangeOf(_data[smaxTotalTIme], _data[sresOftime])) { category[sresOftime] = _data[sresOftime];}
        if ( IsValidNum(_data[snumOfprob])) { category[snumOfprob] = _data[snumOfprob];}
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
        return c1 && c2;
    }
    private boolean IsValidNum(String _temp)
    {
        boolean c1 = isNumber(_temp), c2 = Integer.parseInt(_temp) > 0;
        return c1 && c2;
    }
};