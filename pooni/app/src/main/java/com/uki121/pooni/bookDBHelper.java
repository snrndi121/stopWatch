package com.uki121.pooni;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObservable;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

public class bookDBHelper extends SQLiteOpenHelper {
    private static final String TAG = "BookDBHelper";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME ="pooni.db";

    public bookDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        init_table(getWritableDatabase());
    }
    public bookDBHelper(Context context, String name,
                        SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        init_table(sqLiteDatabase);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion)
    {
        try {
            dropTable(ContractDBinfo.TBL_BOOK);
            dropTable(ContractDBinfo.TBL_USER);
            dropTable(ContractDBinfo.TBL_RECORD);
            onCreate(db);
        } catch (SQLException e)
        {
            Log.d("SQL_onUpgrade", e.getMessage());
        }
    }
    //init
    public void init_table(SQLiteDatabase db) {
        Log.d(TAG, "###################### Start ######################");
        Log.d(TAG, "  Initialize Tables");
        try {
            createTable(ContractDBinfo.TBL_BOOK);
            createTable(ContractDBinfo.TBL_RECORD);
            createTable(ContractDBinfo.TBL_USER);
        } catch(SQLException e) {
            Log.d("SQL_onCreate", e.getMessage());
        } finally {
            Log.d(TAG, "####################### End #######################");
        }
    }
    //create
    public void createTable(String _tablename) {
        SQLiteDatabase db = getWritableDatabase();
        switch(_tablename) {
            case ContractDBinfo.TBL_BOOK :
                Log.d(TAG, "create table Book");
                db.execSQL(ContractDBinfo.SQL_CREATE_BOOK);break;
            case ContractDBinfo.TBL_RECORD :
                Log.d(TAG, "create table Record");
                db.execSQL(ContractDBinfo.SQL_CREATE_REC);break;
            case ContractDBinfo.TBL_USER :
                Log.d(TAG, "create table User");
                db.execSQL(ContractDBinfo.SQL_CREATE_USER);break;
            case ContractDBinfo.TBL_HISTORY_PIE :
                Log.d(TAG, "create table History_pie");
                db.execSQL(ContractDBinfo.SQL_CREATE_HISTORY_PIE);break;
            case ContractDBinfo.TBL_HISTORY_LINE :
                Log.d(TAG, "create table History_line");
                db.execSQL(ContractDBinfo.SQL_CREATE_HISTORY_LINE);break;
            default :
                Log.w(TAG, "The reqested talbe '" + _tablename + "' has no registered in command lines");
                break;
        }
    }
    //select
    public Cursor selectFromTable(String _tablename, String _query) {
        SQLiteDatabase db = getReadableDatabase();
        try {
            switch (_tablename) {
                case ContractDBinfo.TBL_HISTORY_PIE:
                    Cursor cursor1 = db.rawQuery(_query, null);
                    return cursor1;
                case ContractDBinfo.TBL_HISTORY_LINE:
                    Cursor cursor2 = db.rawQuery(_query, null);
                    return cursor2;
                default:
                    break;
            }
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }
    //insert
    public long insertData(History history, String _targetTable) {
        Log.d(TAG, "###################### Start ######################");
        Log.d(TAG, " Insert into history of db");
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        try {
            switch (_targetTable) {
                case ContractDBinfo.TBL_HISTORY_PIE:
                    int[] c = history.getHistoryToTal().getData();
                    cv.put(ContractDBinfo.COL_CATE1, c[0]);
                    cv.put(ContractDBinfo.COL_CATE2, c[1]);
                    cv.put(ContractDBinfo.COL_CATE3, c[2]);
                    cv.put(ContractDBinfo.COL_CATE4, c[3]);
                    long newRowid = db.insert(ContractDBinfo.TBL_HISTORY_PIE, null, cv);
                    return newRowid;
                case ContractDBinfo.TBL_HISTORY_LINE:
                    break;
                default:
                    Log.w(TAG, "There is no such table");
                    break;
            }
        } catch (SQLException e_sql) {
            Log.e(TAG, e_sql.getMessage());
        } finally {
            db.close();
            Log.d(TAG, "####################### End #######################");
        }
        return -1;
    }
    public long insertData(ElapsedRecord elp, String _targetTable) {
        Log.d(TAG, "###################### Start ######################");
        Log.d(TAG, " Insert into " + _targetTable);

        ContentValues cv = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        try {
            if (_targetTable.equals(ContractDBinfo.TBL_BOOK)) {
                Book _bs = new Book(elp.getBaseBook());
                cv.put(ContractDBinfo.COL_TITLE, _bs.getTitle());
                cv.put(ContractDBinfo.COL_TOTIME, _bs.getToTime());
                cv.put(ContractDBinfo.COL_EATIME, _bs.getEachTime());
                cv.put(ContractDBinfo.COL_RETIME, _bs.getRestTime());
                cv.put(ContractDBinfo.COL_NOPROB, _bs.getNumProb());
                cv.put(ContractDBinfo.COL_NOACC, 1);
                //cv.put("num_access", dataC);
                long newRowid = db.insert(ContractDBinfo.TBL_BOOK, null, cv);
                Log.d(TAG, " >> newRowId :" + newRowid);
                return newRowid;
            } else if (_targetTable.equals(ContractDBinfo.TBL_USER)) {
                /*
                return getWritableDatabase().insert(ContractDBinfo.TBL_USER, null, cv);
                */
            } else if (_targetTable.equals(ContractDBinfo.TBL_RECORD)) {
                cv.put(ContractDBinfo.COL_BOOKID, Integer.parseInt(elp.getBookId()));
                cv.put(ContractDBinfo.COL_SOVLED, elp.getNumOfRec());
                cv.put(ContractDBinfo.COL_STRACC, elp.getStrExcess());
                long newRowid = db.insert(ContractDBinfo.TBL_RECORD, null, cv);
                Log.d(TAG, " >> newRowId :" + newRowid);
                return newRowid;
            } else {
                Log.d(TAG, "No such table in Db");
            }
        } catch (SQLException e) {
            Log.e("SQL_INSERT", e.getMessage());
            return -1;
        } finally {
            db.close();
            Log.d(TAG, "####################### End #######################");
        }
        return -1;
    }
    //drop
    public void dropTable(String _table)
    {
        Log.d(TAG, "###################### Start ######################");
        Log.d(TAG, " Drop table and Recreate");
        SQLiteDatabase db = getWritableDatabase();
        try {
            StringBuffer sql_drop_table = new StringBuffer(ContractDBinfo.SQL_DROP_TBL)
                    .append(_table);
            db.execSQL(sql_drop_table.toString());
        } catch (SQLException e) {
            Log.d("SQL_DROP", e.getMessage());
        } finally {
            db.close();
            Log.d(TAG, "####################### End #######################");
        }

    }
    /*
    public void insertAllDatas(ArrayList <Book> bs) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        Iterator<Book> it = bs.iterator();
        try {
            while (it.hasNext()) {
                ContentValues cv = new ContentValues();
                cv.put("title", it.next().getTitle());
                cv.put("total_time", it.next().getToTime());
                cv.put("each_titme", it.next().getEachTime());
                cv.put("rest_time", it.next().getRestTime());
                cv.put("prob_num", it.next().getNumProb());
                //cv.put("num_access", dataC);
                db.insert(ContractDBinfo.TBL_BOOK, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
        }
    }
    */
    //update
    public int updateData(String _attr, String _whereArgs, String _targetTable) {
        System.out.println("###################### Start ######################");
        System.out.println(" Update into db");

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        try {
            if (_targetTable.equals(ContractDBinfo.TBL_BOOK)) {
                switch(_attr) {
                    case ContractDBinfo.COL_TITLE :
                        cv.put(ContractDBinfo.COL_TITLE, _whereArgs);
                        return db.update(_targetTable, cv, ContractDBinfo.WHERE_TITLE, new String[]{_whereArgs});
                    case ContractDBinfo.COL_TOTIME  :
                        cv.put(ContractDBinfo.COL_TOTIME, _whereArgs);
                        return db.update(_targetTable, cv, ContractDBinfo.WHERE_TITLE, new String[]{_whereArgs});
                    case ContractDBinfo.COL_EATIME  :
                        cv.put(ContractDBinfo.COL_EATIME, _whereArgs);
                        return db.update(_targetTable, cv, ContractDBinfo.WHERE_TITLE, new String[]{_whereArgs});
                    case ContractDBinfo.COL_RETIME  :
                        cv.put(ContractDBinfo.COL_RETIME, _whereArgs);
                        return db.update(_targetTable, cv, ContractDBinfo.WHERE_TITLE, new String[]{_whereArgs});
                    case ContractDBinfo.COL_NOPROB  :
                        cv.put(ContractDBinfo.COL_NOPROB, _whereArgs);
                        return db.update(_targetTable, cv, ContractDBinfo.WHERE_TITLE, new String[]{_whereArgs});
                    case ContractDBinfo.COL_NOACC  :
                        cv.put(ContractDBinfo.COL_NOACC, _whereArgs);
                        return db.update(_targetTable, cv, ContractDBinfo.WHERE_TITLE, new String[]{_whereArgs});
                    default :
                        return -1;
                }
            } else if (_targetTable.equals(ContractDBinfo.TBL_USER)) {
                switch(_attr) {
                    case ContractDBinfo.COL_EXECPROB :
                        cv.put(ContractDBinfo.COL_EXECPROB, _whereArgs);
                        return db.update(_targetTable, cv, ContractDBinfo.WHERE_EXECPROB, new String[]{_whereArgs});
                    case ContractDBinfo.COL_SOLVEDPROB  :
                        cv.put(ContractDBinfo.COL_SOLVEDPROB, _whereArgs);
                        return db.update(_targetTable, cv, ContractDBinfo.WHERE_SOLVEDPROB, new String[]{_whereArgs});
                    case ContractDBinfo.COL_CORRPROB  :
                        cv.put(ContractDBinfo.COL_CORRPROB, _whereArgs);
                        return db.update(_targetTable, cv, ContractDBinfo.WHERE_CORRPROB, new String[]{_whereArgs});
                    default :
                        return -1;
                }/*
                return getWritableDatabase().insert(ContractDBinfo.TBL_USER, null, cv);
                */
            }
        } catch (SQLException e) {
            Log.e("SQL_INSERT", e.getMessage());
        } finally {
            System.out.println("####################### End #######################");
        }
        return -1;
    }
    //option
    public void showTable(String _table)
    {
        System.out.println("###################### Start ######################");
        System.out.println(" Load table");
        Book bData = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            StringBuffer sql_select = new StringBuffer(ContractDBinfo.SQL_SELECT)
                    .append(_table);
            Cursor cursor = db.rawQuery(sql_select.toString(), null);
            while(cursor.moveToNext()) {
                bData = new Book();
                bData.setTitle(cursor.getString(1));
                bData.setToTime(cursor.getString(2));
                bData.setEachTime(cursor.getString(3));
                bData.getBook();
            }
        } catch (SQLException e) {
            Log.e("SQL_SELECT", e.getMessage());
        } finally {
            System.out.println("####################### End #######################");
        }

    }
    public int getLast(String _table)
    {
        System.out.println("###################### Start ######################");
        System.out.println(" >> Get last Index of Table");
        try {
            SQLiteDatabase db = getReadableDatabase();
            StringBuffer sql_select = new StringBuffer(ContractDBinfo.SQL_SELECT)
                    .append(_table);
            Cursor cursor = db.rawQuery(sql_select.toString(), null);
            if (cursor.moveToLast() != false) {
                int lastIndx = cursor.getInt(0);
                System.out.println(" >> last Index :" + lastIndx);
                return lastIndx + 1;
            }
        } catch (SQLException e) {
            Log.e("SQL_SELECT", e.getMessage());
        } finally {
            System.out.println("####################### End #######################");
        }
        return 0;
    }
    public int getNumOfAcc(String _bookname) {
        int _numOfaccess = -1;
        SQLiteDatabase db = getReadableDatabase();
        StringBuffer sql_select_where = new StringBuffer(ContractDBinfo.SQL_SELECT_BOOK);
        sql_select_where.append("WHERE title = ")
                .append(_bookname);
        try {
            Cursor cursor = db.rawQuery(sql_select_where.toString(), null);
            if (cursor.moveToNext()) {
                Log.i("Get_num Of Access", "The required book is found");
                _numOfaccess = cursor.getInt(6);
            }
        } catch (SQLException e) {
            Log.i("Get_num Of Access", e.getMessage());
        }
        return _numOfaccess;
    }
    public int getBookId(String _title) {
        SQLiteDatabase db = getReadableDatabase();
        StringBuffer sql_select_where = new StringBuffer(ContractDBinfo.SQL_SELECT_BOOK);
        sql_select_where.append(" Where ")
                        .append("title=")
                        .append(_title);
        Cursor cursor = db.rawQuery(sql_select_where.toString(), null);
        if (cursor.moveToNext()) {
          return cursor.getInt(0);
        }
        return -1;
    }
    public Book findBookByid(int _bookid) {
        SQLiteDatabase db = getReadableDatabase();
        StringBuffer sql_find_book = new StringBuffer(ContractDBinfo.SQL_SELECT_BOOK);
        sql_find_book.append(" where ")
                    .append("bid=")
                    .append( _bookid);
        Cursor cursor = db.rawQuery(sql_find_book.toString(), null);
        if (cursor.moveToNext()) {
            Book _book = new Book();
            _book.setTitle(cursor.getString(1));
            _book.setToTime(cursor.getString(2));
            _book.setEachTime(cursor.getString(3));
            _book.setRestTime(cursor.getString(4));
            _book.setNumProb(cursor.getString(5));
            _book.setNumAcc(cursor.getString(6));
            return _book;
        }
        return null;
    }
    public ArrayList < ElapsedRecord > getElapsedRecord(StringBuffer _whereQuery) {
        SQLiteDatabase db = getReadableDatabase();
        StringBuffer sql_select_record = new StringBuffer(ContractDBinfo.SQL_SELECT_RECORD);
        //ToDo : if _whereQuery is null, is it good
        if (_whereQuery != null) { sql_select_record.append(_whereQuery.toString());}
        Cursor cursor = db.rawQuery(sql_select_record.toString(), null);
        int bookIdx = 0;
        ArrayList < ElapsedRecord > elplist = new ArrayList< ElapsedRecord>();
        while (cursor.moveToNext()) {
            ElapsedRecord elp = new ElapsedRecord();
            bookIdx = cursor.getInt(1);
            System.out.println(" >> cursor_book : " + bookIdx);
            if (bookIdx != -1) {
                elp.setBaseBook(findBookByid(bookIdx));
            }
            elp.setEachExcess(cursor.getString(6));
            elplist.add(elp);
        }
        if (elplist.isEmpty() != false)
            return elplist;
        return null;
    }
}
