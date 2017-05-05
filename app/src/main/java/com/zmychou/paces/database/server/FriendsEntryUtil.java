

package com.zmychou.paces.database.server;

import java.sql.*;
import java.util.*;

public class FriendsEntryUtil {

    public static final String TABLE_NAME = "FriendEntry";
    public static final String USER_ID = "userId";
    public static final String _ID = "id";
    public static final String FRIEND_ID = "friendId";
    public static final String FRIEND_NICKNAME = "friendNickname";
    public static final String FRIEND_AVATAR = "friendAvatar";

    public ArrayList<String> retrieveComents(Statement statement, String where) {
        try {
            String sql = "SELECT "+FRIEND_ID+","+FRIEND_NICKNAME+","+FRIEND_AVATAR
                +" FROM "+TABLE_NAME
                +" WHERE "+USER_ID+"='"+where+"'";
            ResultSet result = statement.executeQuery(sql);
            if (!result.first()) {
                return null;
            }
            ArrayList<String> list = new ArrayList<>();
            StringBuilder sb = null;
            do {
                sb = new StringBuilder();
                sb.append("{");

                sb.append("\"");
                sb.append(FRIEND_NICKNAME);
                sb.append("\":\"");
                sb.append(result.getString(FRIEND_NICKNAME));
                sb.append("\",\"");
                sb.append(FRIEND_AVATAR);
                sb.append("\":\"");
                sb.append(result.getString(FRIEND_AVATAR));
                sb.append("\",\"");
                sb.append(FRIEND_ID);
                sb.append("\":\"");
                sb.append(result.getString(FRIEND_ID));
                sb.append("\"}");
                list.add(sb.toString());
            } while (result.next());
            return list;

        } catch(SQLTimeoutException e) {
        } catch(SQLException e) {
        }
        return null;
    }
}
