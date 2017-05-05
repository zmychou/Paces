
package com.zmychou.paces.database.server;

import java.util.ArrayList;

public class UserInfoEntryUtil {

    public static final String TABLE_NAME = "UserInfo";

    public static final String _ID = "_id";
    public static final String PASSWORD = "password";

    public static final String NICK_NAME = "nickName";

    public static final String BIRTHDAY = "birthday";
    public static final String LOCATION = "location";
    public static final String GENDER = "gender";
    public static final String HEIGHT = "height";
    public static final String WEIGHT = "weight";
    public static final String LEVEL = "level";
    public static final String AVATAR = "avatar";
    public static final String NEW_MOMENT = "newMoment";
    
    public static ArrayList<String> getConstant() {
        ArrayList<String> list = new ArrayList<>();
        list.add(_ID);
        list.add(NICK_NAME);
        list.add(BIRTHDAY);
        list.add(LOCATION);
        list.add(GENDER);
        list.add(HEIGHT);
        list.add(WEIGHT);
        list.add(LEVEL);
        list.add(AVATAR);
        list.add(NEW_MOMENT);
        return list;
    }
}
