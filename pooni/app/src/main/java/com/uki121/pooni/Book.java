package com.uki121.pooni;

import android.util.Log;

import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

class Book {
    private final int TITLE = 0, TOTAL_TIME = 1, EACH_TIME = 2, REST_TIME = 3, NUM_PROB = 4, NUM_ACC = 5;
    private final int MAX_CATEGORY = 6;
    public String[] category;
    public Book(){ category = new String[MAX_CATEGORY];};
    public Book(String[] _data)
    {
        try {
           category = new String[]{_data[TITLE],
                                    _data[TOTAL_TIME],
                                    _data[EACH_TIME],
                                    _data[REST_TIME],
                                    _data[NUM_PROB],
                                    _data[NUM_ACC]};
        } catch (Exception e) {
            Log.e("BOOK_CONSTRUCTOR_EXCEPTION", e.getMessage());
        } catch (ExceptionInInitializerError e) {
            Log.e("BOOK_CONSTRUCTOR_INIT", e.getMessage());
        }
    }
    public Book(Book bs)
    {
        if (bs != null) {
            this.category = bs.category;
        } else {
            category = new String[MAX_CATEGORY];
        }
    }
    /*TODO : mask flag*/
    //Check a form of book regardless of its title
    final public boolean IsBookValid() {
        boolean ttflag = category[TOTAL_TIME] != null ? IsNumber(category[TOTAL_TIME]) : false,
                mptflag = category[TOTAL_TIME] != null ? IsRangeOf(category[TOTAL_TIME], category[EACH_TIME]) : false,
                rtflag = category[TOTAL_TIME] != null ? IsRangeOf(category[TOTAL_TIME], category[REST_TIME]) : true,
                nopflag = category[NUM_PROB] != null ? IsValidNum(category[NUM_PROB]) : true;
        return ttflag && mptflag && rtflag && nopflag;
    }
    //Check whether it is a number
    public boolean IsNumber(String _str)
    {
        try {
            return Pattern.matches("^[0-9]*$", _str);
        } catch (Exception e) {
            Log.e("Book_PatternCheck", e.getMessage());
            return false;
        }
    }
    public boolean IsRangeOf(String base, String target)
    {
        try {
            int a = Integer.parseInt(base), b = Integer.parseInt(target);
            boolean c1 = IsNumber(target), c2 = a > b;
            //c1 is true -> baseTime is number
            //c2 is true -> baseTime > targetTime
            return c1 && c2;
        } catch (Exception e) {
            Log.e("Book_RangeCheck ", e.getMessage());
            return false;
        }
    }
    public boolean IsValidNum(String _target)
    {
        try {
            boolean c1 = IsNumber(_target), c2 = Integer.parseInt(_target) > 0;
            //c1 is true -> c1 is number
            //c2 is true -> c2 > 0
            return c1 && c2;//c1 && c2 is true -> target is valid
        } catch (Exception e) {
            Log.e("Book_NumCheck ", e.getMessage());
            return false;
        }
    }
    /* essential function */
    //Set a title for a book
    public void setTitle(String _title) { this.category[TITLE] = _title; }
    //Set total time when whole problems are solved
    public void setToTime(String _totalTime) { this.category[TOTAL_TIME] = _totalTime; }
    //Set max time when one problem is solved
    public void setEachTime(String _maxtimePer) { this.category[EACH_TIME] = _maxtimePer; }
    //Set rest time between subjects
    public void setRestTime(String _restTime) { this.category[REST_TIME] = _restTime;}
    //Set how much a book has problems
    public void setNumProb(String _numProb) { this.category[NUM_PROB] = _numProb;}
    //Set the number of access this book data
    public void setNumAcc(String _numAcc) { this.category[NUM_ACC] = _numAcc;}
    //Get a title for a book
    public final String getTitle() { return category[TITLE]; }
    //Get total time when whole problems are solved
    public final String getToTime() { return category[TOTAL_TIME]; }
    //Get max time when one problem is solved
    public final String getEachTime() { return category[EACH_TIME]; }
    //Get rest time between subjects
    public final String getRestTime() { return category[REST_TIME]; }
    //Get how much a book has problems
    public final String getNumProb() { return category[NUM_PROB]; }
    //Get the number of access this book data
    public final String getNumAcc() { return category[NUM_ACC]; }
    //Get current book' info
    public final String[] getBook() {
        System.out.println("Book_title " + category[TITLE]);
        for (int i = 1; i < MAX_CATEGORY - 1; ++i) {
            System.out.println("book_info : " + category[i]);
        }
        System.out.println("\n");
        return category;
    }
};