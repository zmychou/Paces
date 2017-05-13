package com.zmychou.paces.network;

import android.content.Context;
import android.content.SharedPreferences;

import com.zmychou.paces.database.server.UserInfoEntryUtil;
import com.zmychou.paces.login.LoginActivity;

import java.util.HashMap;

/**
 * <pre>
 * Package    :com.zmychou.paces.network
 * Author     : zmychou
 * Create time:17-5-12
 * Last Modify:
 * Email      :zmychou@gmail.com
 * Version    :V1.0
 * description:
 * </pre>
 */

public class UpdateUserInfoRequests extends Requests {

    public void update(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(LoginActivity.TAG, Context.MODE_PRIVATE);
        String id = sp.getString(UserInfoEntryUtil._ID, "");
        HashMap<String, String> map = new HashMap<>();
        map.put(key, value);
        map.put(JsonKey.MSG_TYPE, MsgTypeConstant.TYPE_UPDATE_USER_INFO + "");
        map.put(UserInfoEntryUtil._ID, id);
        execute(map);
    }
}
