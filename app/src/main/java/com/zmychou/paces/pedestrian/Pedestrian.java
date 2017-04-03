package com.zmychou.paces.pedestrian;

import com.zmychou.paces.Sport;

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
public class Pedestrian implements Sport{

    public void start(){}
    public void pause(){}
    public void stop(){}
    public void restart(){}
    public int getStepCount(){
        return 0;
    }
    public int getStepRate(){
        return 0;
    }
    public int getIntensify(){
        return 0;
    }
    private boolean hasAccelerator(){
        return false;
    }
    private boolean hasBarometer(){
        return false;
    }
    private void compute(){
        deriveAcceleratorData();
        verifyData();
        doCount();
    }

    public void doCount() {

    }

    public void verifyData() {

    }

    private void deriveAcceleratorData() {

    }

    public boolean saveToExternalStorage(){
        return false;
    }
    public boolean uploadToServer(){
        return false;
    }

}
