package com.zmychou.paces.music;

/**
 * <pre>
 * Package    :com.zmychou.paces.music
 * Author     : zmychou
 * Create time:17-4-21
 * Last Modify:
 * Email      :zmychou@gmail.com
 * Version    :V1.0
 * description:
 * </pre>
 */

public interface Subject {
    void registerForNotify(Observable o);
    void unregisterForNotify(Observable o);
}
