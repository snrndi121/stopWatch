package com.uki121.pooni;

public class DataMonth {
    //var
    private int numOfmonth;
    private int totalAmount;
    private float[] accMonth;
    private float[] accAvg;
    //constructort
    public DataMonth() {}
    //set
    //get
    public float[] getAverage() { return this.accAvg;}
    public float[] getAccOfmonth() { return this.accMonth;}
    public int getTotalAmount() { return this.totalAmount;}
    public int getNumOfmonth() { return this.numOfmonth;}
}
