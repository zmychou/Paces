package com.zmychou.paces.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * <pre>
 * Package    :com.zmychou.paces.util
 * Author     : zmychou
 * Create time:17-5-12
 * Last Modify:
 * Email      :zmychou@gmail.com
 * Version    :V1.0
 * description:
 * </pre>
 */

public class NetworkUtil {
    public static boolean networkConnectivity(Context context) {
        ConnectivityManager cm = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        if (info.getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }
        Toast.makeText(context, info.getReason(), Toast.LENGTH_SHORT);
        return false;
    }

}
