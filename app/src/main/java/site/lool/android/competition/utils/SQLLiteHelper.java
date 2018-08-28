package site.lool.android.competition.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLLiteHelper extends SQLiteOpenHelper {
    public SQLLiteHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //重写，新建数据表
        sqLiteDatabase.execSQL("create table dict(_id integer primary key autoincrement, word , detail)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
