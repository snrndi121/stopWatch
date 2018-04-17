package com.uki121.pooni;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class bookDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private Context bcontext;
    private final String TABLE_POONI = "TABLE_POONI";//Table name

    public static SQLiteDatabase mDB;

    public static SQLiteDatabase bookdb;

    public bookDBHelper(Context context, String name,
                        SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.bcontext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuffer create_table = new StringBuffer();
        create_table.append("CREATE TABLE ")
                .append(TABLE_POONI)
                .append("(")
                .append(" BTITLE varchar(20),")
                .append(" BTIME_TOTAL int,")
                .append(" BTIME_MAX_PER int,")
                .append(" BCOUNT int,")
                .append(" BTIME_REST int)");

        sqLiteDatabase.execSQL(create_table.toString());
        Toast.makeText(bcontext, "pooni table 생성 성공", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion)
    {
        Toast.makeText(bcontext, "pooni 안드로이드 버전이 변경되었습니다.", Toast.LENGTH_SHORT).show();
        StringBuffer drop_sql = new StringBuffer();
        drop_sql.append("DROP TABLE IF EXISTS")
                .append(TABLE_POONI);
        db.execSQL(drop_sql.toString());
        onCreate(db);
    }
    public void testDB() {
        SQLiteDatabase db = getReadableDatabase();
    }
}
