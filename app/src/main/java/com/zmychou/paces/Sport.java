package com.zmychou.paces;

/**
 * <pre>
 * Package    :com.zmychou.paces.pedestrian
 * Author     : zmychou
 * Create time:17-4-3
 * Last Modify:
 * Email      :zmychou@gmail.com
 * Version    :V1.0
 * description:
 * </pre>
 */
public interface Sport {
    void start();
    void pause();
    void stop();
    void restart();
    void doCount() ;
    void verifyData() ;
}
