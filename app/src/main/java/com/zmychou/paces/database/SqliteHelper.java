package com.zmychou.paces.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * <pre>
 * Package    :com.zmychou.paces.database
 * Author     : zmychou
 * Create time:17-4-3
 * Last Modify:
 * Email      :zmychou@gmail.com
 * Version    :V1.0
 * description:
 * </pre>
 */
public class SqliteHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "MyDatabase";
    public static final int VERSION = 1;
    public SqliteHelper(Context context){
        this(context,DB_NAME,null,VERSION);
    }
    public SqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
