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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    public long insertHistory(History history, String _targetTable) {
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
                    cv.put(ContractDBinfo.COL_CATE4, c[4]);
                    long newRowid = db.insert(ContractDBinfo.TBL_HISTORY_PIE, null, cv);
                    return newRowid;
                case ContractDBinfo.TBL_HISTORY_LINE:
                    Month[] _months = history.getHistoryMonth().getMonth();
                    db.beginTransaction();
                    for (int i = 0; i < 12; ++i) {
                        cv.put(ContractDBinfo.COL_MONTH, _months[i].getName());
                        cv.put(ContractDBinfo.COL_EXCESS, _months[i].getTotalExcess());
                        cv.put(ContractDBinfo.COL_NUM_BOOKS, _months[i].getNumOfbook());
                        cv.put(ContractDBinfo.COL_NUM_SOLVED, _months[i].getNumOfprob());
                    }
                    db.setTransactionSuccessful();
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
            } else if (_targetTable.equals(ContractDBinfo.TBL_RECORD)) {
                cv.put(ContractDBinfo.COL_BOOKID, Integer.parseInt(elp.getBookId()));
                cv.put(ContractDBinfo.COL_DATE, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                cv.put(ContractDBinfo.COL_SOVLED, elp.getNumOfRec());
                cv.put(ContractDBinfo.COL_STRLAP, elp.getStrData("lap"));
                long newRowid = db.insert(ContractDBinfo.TBL_RECORD, null, cv);
                Log.d(TAG, " >> newRowId :" + newRowid);
                return newRowid;
            } else if (_targetTable.equals(ContractDBinfo.TBL_USER)) {
                /*
                return getWritableDatabase().insert(ContractDBinfo.TBL_USER, null, cv);
                */
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
    public int updateBook(String _attr, ContentValues _changes, String _whereArgs) {
        System.out.println("###################### Start ######################");
        System.out.println(" Update into db");
        SQLiteDatabase db = getWritableDatabase();
        try {
            String _where = _attr + "=? ";
            return db.update(ContractDBinfo.TBL_BOOK, _changes, _where, new String[]{_whereArgs});
        } catch (SQLException e) {
            Log.e("SQL_INSERT", e.getMessage());
        } finally {
            System.out.println("####################### End #######################");
        }
        return -1;
    }
    //조정중
    //history 상태에 따라서 insert와 update 동작하도록
    public int updateHistory(History _history, String _table) {
        System.out.println("###################### Start ######################");
        System.out.println(" Update into db");
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        Iterator <ContentValues> it_changes = _changes.iterator();
        try {
            if (_table.equals(ContractDBinfo.TBL_HISTORY_PIE)) {
                String _where = _attr + "=? ";
                return db.update(ContractDBinfo.TBL_BOOK, it_changes.next(), _where, new String[]{_whereArgs[0]});
            } else if (_table.equals(ContractDBinfo.TBL_HISTORY_LINE)) {
                int i = 0;
                while(it_changes.hasNext()) {
                    String _where = _attr + "=? ";
                    db.update(ContractDBinfo.TBL_BOOK, it_changes.next(), _where, new String[]{_whereArgs[i++]});
                }
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
                        .append("title=\"")
                        .append(_title)
                        .append("\"");
        Cursor cursor = db.rawQuery(sql_select_where.toString(), null);
        cursor.moveToFirst();
        if (cursor != null & cursor.getCount() > 0) {
            Log.d(TAG, "getBookId - found element!!!");
            return cursor.getInt(0);
        }
        return -1;
    }
    public Book findBookByid(int _bookid) {
        SQLiteDatabase db = getReadableDatabase();
        StringBuffer sql_find_book = new StringBuffer(ContractDBinfo.SQL_SELECT_BOOK);
        sql_find_book.append(" where ")
                    .append("id=")
                    .append( _bookid);
        Cursor cursor = db.rawQuery(sql_find_book.toString(), null);
        if (cursor != null & cursor.getCount() != 0) {
            Log.d(TAG, "findBookById has valid cursor");
            cursor.moveToFirst();
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
    //load record and set elp class
    public ArrayList < ElapsedRecord > getElapsedRecord(String _whereQuery, boolean _switch) {
        Log.d(TAG, " #### START : getElapsedRercord #### ");
        try {
            if (_switch != false) {
                //load database
                SQLiteDatabase db = getReadableDatabase();
                StringBuffer sql_select_record = new StringBuffer(ContractDBinfo.SQL_SELECT_RECORD);
                //set where_query
                if (_whereQuery != null) {
                    sql_select_record.append(" where ")
                            .append(_whereQuery);
                }
                //execute cursor(where_query)
                Cursor cursor = db.rawQuery(sql_select_record.toString(), null);
                //checking exception then
                if (cursor != null && cursor.getCount() != 0) {
                    Log.d(TAG, " >> cursor count : " + cursor.getCount());
                    int bookIdx = 0;
                    ArrayList<ElapsedRecord> elplist = new ArrayList<ElapsedRecord>();
                    cursor.moveToFirst();
                    do {
                        //new item
                        ElapsedRecord elp = new ElapsedRecord();
                        elp.setRecordId(String.valueOf(cursor.getInt(0)));//elp.recid
                        bookIdx = cursor.getInt(1);//elp.book & bookid
                        if (bookIdx != -1) {
                            elp.setBookId(String.valueOf(bookIdx));
                            elp.setBaseBook(findBookByid(bookIdx));
                        } else {
                            Log.w(TAG, "an index of book saved in record table is -1");
                            throw new SQLException();
                        }
                        elp.setDate(cursor.getString(2));//elp.date
                        elp.setEachLaptime(cursor.getString(4));//elp.eachLap
                        //                    //elp.setExcessFromLap();//elp.eachExcess
                        elplist.add(elp);
                    } while (cursor.moveToNext());
                    return elplist;
                } else {
                    Log.d(TAG, " > no such data for this whereQuery");
                }
            }
        } catch (SQLException e_sql) {
            Log.d(TAG, e_sql.getMessage());
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        } finally {
            Log.d(TAG, " #### END : getElapsedRercord #### ");
        }
        return null;
    }
    public int getNumOfrecord() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ContractDBinfo.SQL_SELECT_RECORD, null);
        if (cursor != null) {
            return cursor.getCount();
        }
        return -1;
    }
}
