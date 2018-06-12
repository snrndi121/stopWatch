package com.uki121.pooni;

public class SheetItem {
    public static int number = 1;//what index of this problem is
    private int mIndex;
    private int mAnswer;//which one is marked
    public SheetItem() {
        mAnswer = -1;
        mIndex = number++;
    }
    public void setAnswer(int _ans) { mAnswer = _ans;}
    public int getNumber() {return number;}
    public int getAnswer() { return mAnswer;}
}
