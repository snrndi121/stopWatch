package com.uki121.pooni;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObservable;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

/* TODO : class DBHELPER */
public class bookDBHelper extends SQLiteOpenHelper {
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
    /*TODO : The table could not be found now*/
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
            onCreate(db);
        } catch (SQLException e)
        {
            Log.d("SQL_onUpgrade", e.getMessage());
        }
    }

    public void dropTable(String _table)
    {
        System.out.println("###################### Start ######################");
        System.out.println(" Drop table and Recreate");
        SQLiteDatabase db = getWritableDatabase();
        try {
            StringBuffer sql_drop_table = new StringBuffer(ContractDBinfo.SQL_DROP_TBL)
                    .append(_table);
            db.execSQL(sql_drop_table.toString());
        } catch (SQLException e) {
            Log.d("SQL_DROP", e.getMessage());
        } finally {
            System.out.println("####################### End #######################");
        }

    }

    public void init_table(SQLiteDatabase db) {
        System.out.println("###################### Start ######################");
        System.out.println("  Initialize Tables");
        try {
            db.execSQL(ContractDBinfo.SQL_CREATE_BOOK);
            db.execSQL(ContractDBinfo.SQL_CREATE_USER);
         } catch(SQLException e) {
            Log.d("SQL_onCreate", e.getMessage());
        } finally {
            System.out.println("####################### End #######################");
        }
    }
    /* ToDo: issue #15 */
    public long insertData(String targetTable, Book bs) {
        System.out.println("###################### Start ######################");
        System.out.println(" Insert into db");

        ContentValues cv = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        try {
            if (targetTable.equals(ContractDBinfo.TBL_BOOK)) {
                cv.put(ContractDBinfo.COL_ID, getLast(ContractDBinfo.TBL_BOOK));
                Log.d(" >> last index ", String.valueOf(getLast(ContractDBinfo.TBL_BOOK)));
                cv.put(ContractDBinfo.COL_TITLE, bs.getTitle());
                cv.put(ContractDBinfo.COL_TOTIME, bs.getToTime());
                cv.put(ContractDBinfo.COL_EATIME, bs.getEachTime());
                cv.put(ContractDBinfo.COL_RETIME, bs.getRestTime());
                cv.put(ContractDBinfo.COL_NOPROB, bs.getNumProb());
                cv.put(ContractDBinfo.COL_NOACC, 1);
                //cv.put("num_access", dataC);
                long newRowid = db.insert(ContractDBinfo.TBL_BOOK, null, cv);
                System.out.println(" >> newRowId :" + newRowid);
                return newRowid;
            } else if (targetTable.equals(ContractDBinfo.TBL_USER)) {
                /*
                return getWritableDatabase().insert(ContractDBinfo.TBL_USER, null, cv);
                */
            }
        } catch (SQLException e) {
            Log.e("SQL_INSERT", e.getMessage());
            return -1;
        } finally {
            System.out.println("####################### End #######################");
        }
        return -1;
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
}
