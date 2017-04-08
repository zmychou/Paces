package com.zmychou.paces.running;

import com.zmychou.paces.running.RunningActivity;

/**
 * <pre>
 * Package    :com.zmychou.paces.running
 * Author     : zmychou
 * Create time:17-4-8
 * Last Modify:
 * Email      :zmychou@gmail.com
 * Version    :V1.0
 * description:
 * </pre>
 */
public interface State {
    void handle(RunningActivity activity);
    void start(RunningActivity activity);
    void stop(RunningActivity activity);
    void pause(RunningActivity activity);
    void restart(RunningActivity activity);
}
