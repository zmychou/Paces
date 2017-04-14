package com.zmychou.paces.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

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
public class RunningEntryUtils implements BaseColumns{

    public static final String TABLE_NAME = "RunningEntry";
    public static final String NUMERIC_ORDER = "NumericOrder";
    public static final String TIME_STAMP = "Timestamp";
    public static final String START_TIME = "StartTime";
    public static final String FINISH_TIME = "FinishTime";
    public static final String DISTANCE = "Distance";
    public static final String DURATION = "Duration";
    public static final String STEPS = "Steps";
    public static final String CALORIES = "Calories";
    public static final String LATLNG_FILE_PATH = "LatLngFilePath";
    public static final String STEPS_FILE_PATH = "StepsFilePath";

    public static final String CREATE_RUNNING_ENTRY = "CREATE TABLE "+TABLE_NAME+"("+
            _ID+" INTEGER PRIMARY KEY,"+
            NUMERIC_ORDER+" INTEGER NOT NULL,"+
            TIME_STAMP+" INTEGER NOT NULL,"+
            START_TIME+" INTEGER NOT NULL,"+
            FINISH_TIME+" INTEGER NOT NULL,"+
            DISTANCE+" INTEGER NOT NULL,"+
            DURATION+" INTEGER NOT NULL,"+
            STEPS+" INTEGER NOT NULL,"+
            CALORIES+" INTEGER NOT NULL,"+
            LATLNG_FILE_PATH+" NOT NULL,"+
            STEPS_FILE_PATH+"  NOT NULL )";



    public static SQLiteDatabase sDatabase = null;
    public RunningEntryUtils(Context context){
        sDatabase = new SqliteHelper(context).getWritableDatabase();
    }
    public void insert(RunningData data){
        this.insert(data.getSequenceNumber(),data.getTimestamp(),data.getStartTime(),
                data.getFinishTime(),data.getDistance(),data.getDuration(),data.getSteps(),
                data.getCalories(),data.getLatLngFile(),data.getStepsFile()  );
    }
    public void insert(int sequence,long timestamp,long startTime,long finishTime,float distance,
                       float duration,int steps,int calories,String latLngFile,String stepsFile){
        String sql = "INSERT INTO "+TABLE_NAME+"("+
                NUMERIC_ORDER+","+
                TIME_STAMP +","+
                START_TIME+","+
                FINISH_TIME+","+
                DISTANCE+","+
                DURATION+","+
                STEPS+","+
                CALORIES+","+
                LATLNG_FILE_PATH+","+
                STEPS_FILE_PATH+")"+
                " VALUES ("+
                "'"+sequence+"',"+
                "'"+timestamp+"',"+
                "'"+startTime+"',"+
                "'"+finishTime+"',"+
                "'"+distance+"',"+
                "'"+duration+"',"+
                "'"+steps+"',"+
                "'"+calories+"',"+
                "'"+latLngFile+"',"+
                "'"+stepsFile+"')";
        sDatabase.execSQL(sql);
    }

    /**
     * Get the a specific running record's data
     * @return
     */
    public Cursor getSpecificData(String timestamp){
        return sDatabase.query(
                TABLE_NAME,
                null,
                TIME_STAMP+"=?",
                new String[]{timestamp},
                null,
                null,
                null);
    }

    /**
     * Get all data  summarize those contain timestamp,total distance,total duration
     * @return A cursor contain the data
     */
    public Cursor getAllSummarize(){
        return sDatabase.query(
                TABLE_NAME,
                new String[]{"sum("+DISTANCE+")",TIME_STAMP,"sum("+DURATION+")"},
                null,
                null,
                TIME_STAMP,
                null,
                null );
    }
}
