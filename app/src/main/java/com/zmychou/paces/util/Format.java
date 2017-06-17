package com.zmychou.paces.util;

/**
 * Created by ming on 17-6-17.
 */
public class Format {

    public static String formatTime(long timestamp) {
        long totalSeconds = timestamp / 1000;
        int secondsInMinute = (int) totalSeconds % 60;
        int minutesInHour = (int) totalSeconds / 60 % 60;
        int hours = (int) totalSeconds / 3600;
        StringBuffer sb = new StringBuffer();
        sb.append(hours);
        sb.append(":");
        if (minutesInHour < 10) {
            sb.append("0");
        }
        sb.append(minutesInHour);
        sb.append(":");
        if (secondsInMinute < 10) {
            sb.append("0");
        }
        sb.append(secondsInMinute);
        return sb.toString();
    }
}
