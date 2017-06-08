package com.zmychou.paces.pedometer;

/**
 * <pre>
 * Package    :com.zmychou.paces.pedometer
 * Author     : zmychou
 * Create time:17-4-17
 * Last Modify:
 * Email      :zmychou@gmail.com
 * Version    :V1.0
 * description:
 * </pre>
 */

public interface DataChangeListener {

    void onUpdateStepRate(int steps);

    void onUpdateSteps(int tmp);

}
