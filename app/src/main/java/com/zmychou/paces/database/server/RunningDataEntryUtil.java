
package com.zmychou.paces.database.server;

import java.util.ArrayList;

/**
 * A constant and helper class for database RunningData table
 *
 * @author  zmychou
 * @since   2017/4/29
 * @version v1.0
 */

public class RunningDataEntryUtil {

    public static final String TABLE_NAME = "RunningData";
    public static final String USER_ID = "userId";
    public static final String _ID = "_id";
    public static final String TIMESTAMP = "_timestamp";
    public static final String DISTANCE = "distance";
    public static final String DURATION = "duration";
    public static final String LAT_LNG_FILE = "latLngFile";

    public static ArrayList<String> getConstant() {
        ArrayList<String> list = new ArrayList<>();

        list.add(USER_ID);
        list.add(_ID);
        list.add(TIMESTAMP);
        list.add(DISTANCE);
        list.add(DURATION);
        list.add(LAT_LNG_FILE);
        return list;
    }
}
