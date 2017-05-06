package com.zmychou.paces.network;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.zmychou.paces.database.RunningEntryUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * <pre>
 * Package    :com.zmychou.paces.network
 * Author     : zmychou
 * Create time:17-5-6
 * Last Modify:
 * Email      :zmychou@gmail.com
 * Version    :V1.0
 * description:
 * </pre>
 */

public class UploadFileRequests extends Requests {

    @Override
    protected String moreContent() {
        if (filePaths == null || filePaths.size() < 1) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder() ;
        for (String filePath : filePaths) {
            try {
                InputStreamReader reader = new InputStreamReader(
                        new FileInputStream(new File(filePath)));
                char[] buff = new char[1024];
                for (int len; (len = reader.read(buff)) != -1; ) {
                    sb.append(buff, 0, len);
                }
                sb.append(",");
            } catch (FileNotFoundException e) {

            } catch (IOException e) {}
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public ArrayList<String> getRecords(Context context, String id) {
        RunningEntryUtils utils = new RunningEntryUtils(context);
        Cursor cursor = utils.getSpecificData(id);
        cursor.moveToFirst();
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++ ) {
            list.add(cursor.getString(cursor.getColumnIndex(RunningEntryUtils.LATLNG_FILE_PATH)));
        }
        filePaths = list;
        return list;
    }
}
