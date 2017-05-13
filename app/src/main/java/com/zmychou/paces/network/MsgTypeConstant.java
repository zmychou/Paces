
package com.zmychou.paces.network;

public class MsgTypeConstant {

    /**
     * Client send a file which contain the running data to the server
     */
    public static final int TYPE_SAVE_FILE = 0x01;

    public static final int TYPE_SIGNUP = 0x02;
    public static final int TYPE_SIGNIN = 0x03;
    public static final int TYPE_SIGNOUT = 0x04;

    public static final int TYPE_GET_MSG = 0x05;
    public static final int TYPE_GET_ALL_RUN = 0x06;
    public static final int TYPE_GET_SINGLE_RUN = 0x07;
    public static final int TYPE_GET_UPDATE = 0x08;

    /**
     * Check whether the app has a new version or not .
     */
    public static final int TYPE_CHECK_APP_VERSION = 0x09;

    /**
     * Let server know an client is alive and send data to it if any new 
     * update have make
     */
    public static final int TYPE_HEARTBEAT = 0x0a;

    public static final int TYPE_UPLOAD_FILE = 0x0b;
    public static final int TYPE_UPLOAD_IMG = 0x0c;
    public static final int TYPE_UPLOAD_MOMENT = 0x0d;
    public static final int TYPE_DOWNLOAD_RECORD = 0x0e;
    public static final int TYPE_UPDATE_USER_INFO = 0x0f;
}
