package com.uki121.pooni;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class bookDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private Context bcontext;
    private final String TABLE_BOOKS = "TABLE_BOOKS";//table for books regarding to each setting.
    private final String TABLE_USERS = "TABLE_USERS_RECORDS";//table for user regarding to your records
    public static SQLiteDatabase mDB;

    public static SQLiteDatabase bookdb;

    public bookDBHelper(Context context, String name,
                        SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.bcontext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            StringBuffer table_books_attr = new StringBuffer(
                    "id int NOT NULL AUTO_INCREMENT, title varchar(30) NOT NULL, total_time int NOT NULL, each_titme int, prob_num int, rest_time int, num_access int default(0)");
            StringBuffer table_users_attr = new StringBuffer(
                    "rid char(16) NOT NULL, bid int NOT NULL, prob_excess int, prob_solved int, prob_corrected int,")
                    .append("PRIMARY KEY(rid), References bid on")
                    .append(TABLE_BOOKS)
                    .append("(id)")
                    .append("ON DELETE CASCADE");
            createTable(sqLiteDatabase, TABLE_BOOKS, table_books_attr);
            createTable(sqLiteDatabase, TABLE_USERS, table_users_attr);
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
        } catch (SQLException e)
        {
            Log.d("SQL_onUpgrade", e.getMessage());
        }
    }
    public void testDB() {
        SQLiteDatabase db = getReadableDatabase();
    }
    public void createTable(SQLiteDatabase _sqLiteDatabase, String _name, StringBuffer _attr)
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
