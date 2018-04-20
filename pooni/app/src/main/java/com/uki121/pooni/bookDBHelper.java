package com.uki121.pooni;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

/* TODO : class DBHELPER */
public class bookDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private final String TABLE_BOOKS = "TABLE_BOOKS";//table for books regarding to each setting.
    private final String TABLE_USERS = "TABLE_USERS_RECORDS";//table for user regarding to your records
    private static final String DATABASE_NAME ="pooni.db";

    public static SQLiteDatabase userDB;
    public static SQLiteDatabase bookdb;
    private Context bcontext;

    public bookDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public bookDBHelper(Context context, String name,
                        SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.bcontext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            StringBuffer table_books_attr = new StringBuffer(
                    "id int NOT NULL AUTO_INCREMENT, title varchar(30) NOT NULL, total_time int NOT NULL, each_titme int, rest_time int, prob_num int, num_access int default(0)");
            StringBuffer table_users_attr = new StringBuffer(
                    "rid char(16) NOT NULL, bid int NOT NULL, prob_excess int, prob_solved int, prob_corrected int,")
                    .append("PRIMARY KEY(rid), References bid on")
                    .append(TABLE_BOOKS)
                    .append("(id)")
                    .append("ON DELETE CASCADE");
            createTable(sqLiteDatabase, TABLE_BOOKS, table_books_attr.toString());
            createTable(sqLiteDatabase, TABLE_USERS, table_users_attr.toString());
            Toast.makeText(bcontext, "pooni db 생성 성공", Toast.LENGTH_SHORT).show();
        } catch(SQLException e) {
            Log.d("SQL_onCreate", e.getMessage());
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion)
    {
        try {
            Toast.makeText(bcontext, "pooni 안드로이드 버전이 변경되었습니다.", Toast.LENGTH_SHORT).show();
            dropTable(db, TABLE_BOOKS);
            dropTable(db, TABLE_USERS);
            onCreate(db);
        } catch (SQLException e)
        {
            Log.d("SQL_onUpgrade", e.getMessage());
        }
    }
    public void testDB() {
        SQLiteDatabase db = getReadableDatabase();
    }
    public long insertData(String targetTable, Book bs) {
        ContentValues cv = new ContentValues();
        if (targetTable.equals(TABLE_BOOKS)) {
            cv.put("title", bs.getTitle());
            cv.put("total_time", bs.getToTime());
            cv.put("each_titme", bs.getEachTime());
            cv.put("rest_time", bs.getRestTime());
            cv.put("prob_num", bs.getNumProb());
            //cv.put("num_access", dataC);
            return getWritableDatabase().insert(TABLE_BOOKS, null, cv);
        } else if (targetTable.equals(TABLE_USERS)) {
            cv.put("rid", bs.getTitle());
            cv.put("bid", bs.getToTime());
            cv.put("prob_excess", bs.getEachTime());
            cv.put("prob_solved", bs.getRestTime());
            cv.put("prob_corrected", bs.getNumProb());
            return getWritableDatabase().insert(TABLE_USERS, null, cv);
        }
        return -1;
    }
    public void insertAllDatas(ArrayList <Book> bs) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        Iterator < Book> it = bs.iterator();
        try {
            while (it.hasNext()) {
                ContentValues cv = new ContentValues();
                cv.put("title", it.next().getTitle());
                cv.put("total_time", it.next().getToTime());
                cv.put("each_titme", it.next().getEachTime());
                cv.put("rest_time", it.next().getRestTime());
                cv.put("prob_num", it.next().getNumProb());
                //cv.put("num_access", dataC);
                db.insert(TABLE_BOOKS, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    public void createTable(SQLiteDatabase sqLiteDatabase, String _name, StringBuffer _attr)
    {
        StringBuffer create_table = new StringBuffer();
        create_table.append("CREATE TABLE")
                .append(_name)
                .append("(")
                .append(_attr)
                .append(")");
        _sqLiteDatabase.execSQL(create_table.toString());
    }
    public void dropTable(SQLiteDatabase _sqLiteDatabases, String _name)
    {
        StringBuffer drop_table = new StringBuffer("DROP TABLE IF EXISTS")
                .append(_name);
        _sqLiteDatabases.execSQL(drop_table.toString());
        onCreate(_sqLiteDatabases);
    }
}
