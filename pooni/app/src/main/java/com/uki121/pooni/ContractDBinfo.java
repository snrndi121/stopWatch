package com.uki121.pooni;

public class ContractDBinfo {
    private ContractDBinfo() {};
    public static final String TBL_BOOK = "TABLE_BOOK";
    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_TOTIME = "total_time";
    public static final String COL_EATIME = "each_time";
    public static final String COL_RETIME = "rest_time";
    public static final String COL_NOPROB = "prob_num";
    public static final String COL_NOACC = "access_num";
    public static final String SQL_CREATE_BOOK = "CREATE TABLE IF NOT EXISTS " + TBL_BOOK +
            "(" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + ", " +
                COL_TITLE + " VARCHAR(30) NOT NULL" + ", " +
                COL_TOTIME + " INTEGER NOT NULL" + ", " +
                COL_EATIME + " INTEGER NOT NULL" + ", " +
                COL_RETIME + " INTEGER" + ", " +
                COL_NOPROB + " INTEGER" + ", " +
                COL_NOACC + " INTEGER DEFAULT(0)" +
            ")" ;
    public static final String TBL_USER = "TABLE_USER";
    public static final String COL_RECID = "rid";
    public static final String COL_BOOKID = "bid";
    public static final String COL_EXECPROB = "prob_excess";
    public static final String COL_SOLVEDPROB = "prob_solved";
    public static final String COL_CORRPROB = "prob_corrected";
    public static final String SQL_CREATE_USER ="CREATE TABLE IF NOT EXISTS " + TBL_USER +
            "(" +
                COL_RECID + " INTEGER NOT NULL" + ", " +
                COL_BOOKID + " INTEGER NOT NULL" + ", " +
                COL_EXECPROB + " INTEGER DEFAULT(0)" + ", " +
                COL_SOLVEDPROB + " INTEGER DEFAULT(0)" + ", " +
                COL_CORRPROB + " INTEGER DEFAULT(0)" + ", " +
                "PRIMARY KEY(" + COL_RECID + ") " + ", " +
                "FOREIGN KEY("  + COL_BOOKID + ")" +
                "REFERENCES " + TBL_BOOK + "(" + COL_ID + ")" +
                " ON DELETE CASCADE" +
            ")" ;
    public static final String SQL_DROP_TBL = "DROP TABLE IF EXISTS ";
    public static final String SQL_DROP_TBL_BOOK = "DROP TABLE IF EXISTS " + TBL_BOOK;
    public static final String SQL_DROP_TBL_USER = "DROP TABLE IF EXISTS " + TBL_USER;
    public static final String SQL_SELECT = "SELECT * FROM ";
    public static final String SQL_SELECT_BOOK = "SELECT * FROM " + TBL_BOOK;
    public static final String SQL_SELECT_USER = "SELECT * FROM " + TBL_USER;
    public static final String SQL_INSERT_BOOK = "INSERT OR REPLACE INTO " + TBL_BOOK + " VALUES ";
    public static final String SQL_INSERT_USER = "INSERT OR REPLACE INTO " + TBL_USER + " VALUES ";
    public static final String SQL_DELETE = "DELETE FROM ";
}
