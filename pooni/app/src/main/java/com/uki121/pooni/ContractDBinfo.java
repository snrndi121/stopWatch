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
                COL_TOTIME + " INTEGER NOT NULL DEFAULT '0'" + ", " +
                COL_EATIME + " INTEGER NOT NULL DEFAULT '0'" + ", " +
                COL_RETIME + " INTEGER NOT NULL DEFAULT '0'" + ", " +
                COL_NOPROB + " INTEGER NOT NULL DEFAULT '0'" + ", " +
                COL_NOACC + " INTEGER NOT NULL DEFAULT '0'" +
            ")" ;
    public static final String TBL_USER = "TABLE_USER";
    public static final String COL_BOOKID = "bid";
    public static final String COL_RECID = "rid";
    public static final String COL_EXECPROB = "prob_excess";
    public static final String COL_SOLVEDPROB = "prob_solved";
    public static final String COL_CORRPROB = "prob_corrected";
    public static final String SQL_CREATE_USER ="CREATE TABLE IF NOT EXISTS " + TBL_USER +
            "(" +
                COL_RECID + " INTEGER NOT NULL" + ", " +
                COL_BOOKID + " INTEGER NOT NULL" + ", " +
                COL_EXECPROB + " INTEGER DEFAULT(0)" + ", " +
                COL_SOLVEDPROB + " INTEGER DEFAULT(0)" + ", " +
                COL_CORRPROB + " INTEGER DEFAULT(0)" + ",  " +
                "PRIMARY KEY(" + COL_RECID + ") " + ", " +
                "FOREIGN KEY("  + COL_BOOKID + ")" +
                "REFERENCES " + TBL_BOOK + "(" + COL_ID + ")" +
                " ON DELETE CASCADE" +
            ")" ;
    public static final String TBL_RECORD = "TABLE_RECORD";
    //public static final String COL_RECAVG = "avg";
    //public static final String COL_RECTOP = "cut_top10";
    //public static final String COL_RECBOT = "cut_bottom10";
    public static final String COL_DATE = "date_record";
    public static final String COL_SOVLED = "numOfsolved";
    public static final String COL_STRACC = "string_access";
    public static final String SQL_CREATE_REC ="CREATE TABLE IF NOT EXISTS " + TBL_RECORD +
            "(" +
            COL_RECID + " INTEGER AUTOINCREMENT" + ", " +
            COL_BOOKID + " INTEGER " + ", " +
            COL_DATE + " TEXT DEFAULT 'DATE('now')' " + ", " +
            COL_SOVLED + " INTEGER DEFALUT '0'" + ", " +
            COL_STRACC + " VARCHAR(1024)" + ", " +
            "PRIMARY KEY(" + COL_RECID + ") " + ", " +
            "FOREIGN KEY("  + COL_BOOKID + ")" +
            "REFERENCES " + TBL_BOOK + "(" + COL_ID + ")" +
            " ON DELETE CASCADE" +
            ")" ;
    public static final String TBL_HISTORY_PIE = "TABLE_HISTORY_PIE";
    public static final String COL_CATE1 = "in_1_minutes";
    public static final String COL_CATE2 = "in_2_minutes";
    public static final String COL_CATE3 = "in_4_minutes";
    public static final String COL_CATE4 = "out_of_time";
    public static final String SQL_CREATE_HISTORY_PIE = "CREATE TABLE IF NOT EXISTS " + TBL_HISTORY_PIE +
            "(" +
            COL_DATE + " TEXT DEFALUT 'DATE('now')'" + ", " +
            COL_CATE1 + " INTEGER DEFALUT '0'" + ", " +
            COL_CATE2 + " INTEGER DEFALUT '0'" + ", " +
            COL_CATE3 + " INTEGER DEFALUT '0'" + ", " +
            COL_CATE4 + " INTEGER DEFALUT '0'" +
            ")" ;
    public static final String TBL_HISTORY_LINE = "TABLE_HISTORY_LINE";
    //Drop
    public static final String SQL_DROP_TBL = "DROP TABLE IF EXISTS ";
    public static final String SQL_DROP_TBL_BOOK = "DROP TABLE IF EXISTS " + TBL_BOOK;
    public static final String SQL_DROP_TBL_USER = "DROP TABLE IF EXISTS " + TBL_USER;
    public static final String SQL_DROP_TBL_RECORD = "DROP TABLE IF EXISTS " + TBL_RECORD;
    public static final String SQL_DROP_TBL_HISTORY = "DROP TABLE IF EXISTS " + TBL_HISTORY_PIE;
    //Select
    public static final String SQL_SELECT = "SELECT * FROM ";
    public static final String SQL_SELECT_BOOK = "SELECT * FROM " + TBL_BOOK;
    public static final String SQL_SELECT_USER = "SELECT * FROM " + TBL_USER;
    public static final String SQL_SELECT_RECORD = "SELECT * FROM " + TBL_RECORD;
    public static final String SQL_SELECT_HISTORY_PIE = "SELECT * FROM " + TBL_HISTORY_PIE;
    //Insert
    public static final String SQL_INSERT_BOOK = "INSERT OR REPLACE INTO " + TBL_BOOK + " VALUES ";
    public static final String SQL_INSERT_USER = "INSERT OR REPLACE INTO " + TBL_USER + " VALUES ";
    public static final String SQL_INSERT_RECORD = "INSERT OR REPLACE INTO " + TBL_RECORD + " VALUES ";
    public static final String SQL_INSERT_HISTORY_PIE = "INSERT OR REPLACE INTO " + TBL_HISTORY_PIE + " VALUES ";
    public static final String SQL_DELETE = "DELETE FROM ";
    //Update
    public static final String WHERE_TITLE = "title=?";    //Book
    public static final String WHERE_TOTIME = "total_time=?";
    public static final String WHERE_EATIME = "each_time=?";
    public static final String WHERE_RETIME = "rest_time=?";
    public static final String WHERE_NOPROB = "prob_num=?";
    public static final String WHERE_NOACC = "access_num=?";
    public static final String WHERE_RECID = "rid=?";    //User
    public static final String WHERE_BOOKID = "bid=?";
    public static final String WHERE_EXECPROB = "prob_excess=?";
    public static final String WHERE_SOLVEDPROB = "prob_solved=?";
    public static final String WHERE_CORRPROB = "prob_corrected=?";
    public static final String WHERE_RECAVG = "avg=?";    //Record
    public static final String WHERE_RECTOP= "cut_top10=?";
    public static final String WHERE_RECBOT = "cut_bottom10=?";
    public static final String WHERE_STRACC = "string_access=?";
    public static final String WHERE_DATE = "where " + COL_DATE + " between ";
}
