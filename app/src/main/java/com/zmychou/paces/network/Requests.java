package com.zmychou.paces.network;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.zmychou.paces.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * <pre>
 * Package    :com.zmychou.paces.network
 * Author     : zmychou
 * Create time:17-4-3
 * Last Modify:
 * Email      :zmychou@gmail.com
 * Version    :V1.0
 * description:
 * </pre>
 */
public class Requests extends AsyncTask<HashMap<String, String>, Void, InputStream>{

    protected String mUrl = "http://10.42.0.1:8080/paces/MyServlet";
    protected ArrayList<String> filePaths;

    protected AlertDialog mAlertDialog;

    protected void showWaitingDialog(Context context) {
        mAlertDialog = new AlertDialog.Builder(context)
                .setView(R.layout.waiting_view)
                .create();
        mAlertDialog.show();
    }

    protected void dismissWaitingDialog() {
        mAlertDialog.dismiss();
    }

    public void setFilePaths(ArrayList<String> filePaths) {
        this.filePaths = filePaths;
    }

    protected InputStream request(String forWhat) {
        try {
            URL url = new URL(mUrl);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            os.write(forWhat.getBytes());
            os.flush();
            os.close();
            return conn.getInputStream();
        } catch (MalformedURLException e) {

        } catch (IOException e) {}
        return null;

    }
    public InputStream constructJson(HashMap<String, String> map) {
        StringBuilder sb = new StringBuilder();
        Set<String> keys = map.keySet();
        sb.append("{");
        for (String key : keys) {
           sb.append("\"") ;
            sb.append(key);
            sb.append("\":");
            sb.append("\"");
            sb.append(map.get(key));
            sb.append("\",");
        }
        sb.append("\"files\":[");
        sb.append(moreContent());
        sb.append("]");
//        sb.deleteCharAt(sb.length() - 1);
        sb.append("}");
        Log.e("debug",sb.toString());
        return request(sb.toString());
    }

    protected String moreContent() {
        return "[]";
    }

    @Override
    protected InputStream doInBackground(HashMap<String, String>... params) {
        return constructJson(params[0]);
    }

}
