package com.uki121.pooni;

public class SheetItem {
    public static int number = 1;
    private int answer;
    public SheetItem() {
        answer = -1;
        number++;
    }
    public void setAnswer(int _ans) { answer = _ans;}
    public int getNumber() {return number;}
    public int getAnswer() { return answer;}
}
