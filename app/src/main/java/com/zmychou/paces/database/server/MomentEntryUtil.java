
package com.zmychou.paces.database.server;

import java.util.ArrayList;
/**
 * A constant and helper class for the table ,MomentEntry, of the paces database
 * @author  zmychou
 * @since   2017/4/29
 * @version v1.0
 *
 */
public class MomentEntryUtil {

    public static final String TABLE_NAME = "MomentEntry";
    public static final String USER_ID = "userId";
    public static final String PUBLIC_TIME = "publicTime";
    public static final String CONTENT = "content";
    public static final String PIC_URL = "picUrl";
    public static final String FAVOR = "favor";

    public static ArrayList<String> getConstant() {
        ArrayList<String> list = new ArrayList<>();
        list.add(USER_ID);
        list.add(PUBLIC_TIME);
        list.add(CONTENT);
        list.add(PIC_URL);
        list.add(FAVOR);
        return list;
    }
}
