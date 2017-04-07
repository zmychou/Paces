package com.zmychou.paces.running;

import android.widget.RemoteViews;

/**
 * <pre>
 * Package    :com.zmychou.paces.running
 * Author     : zmychou
 * Create time:17-4-6
 * Last Modify:
 * Email      :zmychou@gmail.com
 * Version    :V1.0
 * description:
 * </pre>
 */
public class NotificationView extends RemoteViews {
    public static NotificationView CREATOR;
    public NotificationView(String packageName, int layoutId) {
        super(packageName, layoutId);
    }
}
