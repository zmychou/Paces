package com.zmychou.paces.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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
public class RunningEntryUtils {

    public static final String TABLE_NAME = "RunningEntry";

    public static SQLiteDatabase sDatabase = null;
    public RunningEntryUtils(Context context){
        sDatabase = new SqliteHelper(context).getWritableDatabase();
    }
    public void insert(){

        sDatabase.execSQL("");
    }
}
