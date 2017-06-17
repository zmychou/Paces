package com.zmychou.paces.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by ming on 17-6-16.
 */
public class PedometerDataEntryUtil implements BaseColumns {

    public static final String TABLE_NAME = "pedometer_data";
    public static final String TIMESTAMP = "timestamp";
    public static final String DATE = "date";
    public static final String HOUR = "hour";
    public static final String STEP = "step";
    public static final String ACTIVE_TIME = "active_time";
    public static final String DISTANCE = "distance";

    public static final String CREATE_PEDOMETER_ENTRY = "CREATE TABLE "+TABLE_NAME+"("+
            _ID+" INTEGER PRIMARY KEY,"+
            TIMESTAMP+" INTEGER NOT NULL,"+
            DATE+" NOT NULL,"+
            HOUR+"  NOT NULL,"+
            STEP+" INTEGER NOT NULL,"+
            DISTANCE+" INTEGER NOT NULL,"+
            ACTIVE_TIME+"  NOT NULL )";

    public static final String DROP_PEDOMETER_ENTRY = "DROP TABLE " + TABLE_NAME;


    public static void insert(Context context, long timestamp, String date, String hour, int step, float distance,
                       long active) {
        String sql = "INSERT INTO "+TABLE_NAME+"("+
                TIMESTAMP+","+
                DATE +","+
                HOUR+","+
                STEP+","+
                DISTANCE+","+
                ACTIVE_TIME+")"+
                " VALUES ("+
                "'"+timestamp+"',"+
                "'"+date+"',"+
                "'"+hour+"',"+
                "'"+step+"',"+
                "'"+distance+"',"+
                "'"+active+"')";
        SQLiteDatabase db = new SqliteHelper(context).getWritableDatabase();
        db.execSQL(sql);
    }

    public static Cursor retrieveDaily(Context context, String date) {
        SQLiteDatabase db = new SqliteHelper(context).getWritableDatabase();
        return db.query(
                TABLE_NAME,
                null,
                DATE+"=?",
                new String[]{date},
                null,
                null,
                null);
    }

    public static Cursor retrieveHistorySummerize(Context context) {

        SQLiteDatabase db = new SqliteHelper(context).getWritableDatabase();
        String[] column = {"sum(" + STEP + ")", "sum(" + DISTANCE + ")",
                "sum(" + ACTIVE_TIME + ")"};
        return db.query(TABLE_NAME, column, null, null, DATE, null, null);
    }
}
