package com.uki121.pooni;

import android.content.ContentValues;
import android.content.Context;
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
    private Context context;
    public bookDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public bookDBHelper(Context context, String name,
                        SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
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
            Toast.makeText(context, "pooni 안드로이드 버전이 변경되었습니다.", Toast.LENGTH_SHORT).show();
            dropTable(ContractDBinfo.TBL_BOOK);
            dropTable(ContractDBinfo.TBL_USER);
            onCreate(db);
        } catch (SQLException e)
        {
            Log.d("SQL_onUpgrade", e.getMessage());
        }
    }
    public void testDB() {
        SQLiteDatabase db = getReadableDatabase();
    }
    public void init_table(SQLiteDatabase db) {
        try {
            db.execSQL(ContractDBinfo.SQL_CREATE_BOOK);
            db.execSQL(ContractDBinfo.SQL_CREATE_USER);
            Toast.makeText(context, "pooni db 생성 성공", Toast.LENGTH_SHORT).show();
        } catch(SQLException e) {
            Log.d("SQL_onCreate", e.getMessage());
        }
    }
    public long insertData(String targetTable, Book bs) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        try {
            if (targetTable.equals(ContractDBinfo.TBL_BOOK)) {
                cv.put("title", bs.getTitle());
                cv.put("total_time", bs.getToTime());
                cv.put("each_titme", bs.getEachTime());
                cv.put("rest_time", bs.getRestTime());
                cv.put("prob_num", bs.getNumProb());
                //cv.put("num_access", dataC);
                long newRowid = db.insert(ContractDBinfo.TBL_BOOK, null, cv);
                return newRowid;
            } else if (targetTable.equals(ContractDBinfo.TBL_USER)) {
                cv.put("rid", bs.getTitle());
                cv.put("bid", bs.getToTime());
                cv.put("prob_excess", bs.getEachTime());
                cv.put("prob_solved", bs.getRestTime());
                cv.put("prob_corrected", bs.getNumProb());
                return getWritableDatabase().insert(ContractDBinfo.TBL_USER, null, cv);
            }
        } catch (SQLException e) {
            Log.d("SQL_INSERT", e.getMessage());
            return -1;
        }
        return -1;
    }
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
            db.endTransaction();
        }
    }
    public void createTable(String _tableName, String _attr)
    {
        System.out.println(" >> Create table");
        SQLiteDatabase db = getWritableDatabase();
        try {
            StringBuffer create_table = new StringBuffer();
            create_table.append("CREATE TABLE IF NOT EXISTS ")
                    .append(_tableName)
                    .append("(")
                    .append(_attr)
                    .append(")");
            db.execSQL(create_table.toString());
            Toast.makeText(context, "Insert 완료", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            Log.d("SQL_CREATE", e.getMessage());
        } finally {
            db.close();
        }
    }
    public void dropTable(String _name)
    {
        SQLiteDatabase db = getWritableDatabase();
        try {
            StringBuffer drop_table = new StringBuffer("DROP TABLE IF EXISTS")
                    .append(_name);
            db.execSQL(drop_table.toString());
            onCreate(db);
        } catch (SQLException e) {
            Log.d("SQL_DROP", e.getMessage());
        }
    }
    public void dropDatabase(String _name)
    {
        try {
            context.deleteDatabase(_name);
            Toast.makeText(context, "데이터베이스가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            Log.d("SQL_DROP_DATABASE", e.getMessage());
        }
    }
    public void showTable(String _table)
    {
        SQLiteDatabase db = getReadableDatabase();
        StringBuffer selectAll = new StringBuffer("SELECT ALL FROM ");
        selectAll.append(_table);
        Cursor cursor = db.rawQuery(selectAll.toString(), null);
        Book bData = null;
        try {
            while(cursor.moveToNext()) {
                bData = new Book();
                bData.setTitle(cursor.getString(1));
                bData.setToTime(cursor.getString(2));
                bData.setEachTime(cursor.getString(3));
                bData.getBook();
            }
        } catch (SQLException e) {
            Log.d("SQL_SELECT", e.getMessage());
        } finally {
            db.close();
        }
    }
}
