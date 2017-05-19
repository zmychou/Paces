package com.zmychou.paces.music;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.concurrent.ThreadPoolExecutor;

public class AudioPlaybackService extends Service {

    /**
     * Commands
     */
    public static final String EXTRA_COMMAND = "com.zmychou.paces.EXTRA_COMMAND";
    public static final int CMD_START = 0x01;
    public static final int CMD_RESTART = 0x02;
    public static final int CMD_PAUSE = 0x04;
    public static final int CMD_STOP = 0x08;
    public static final int CMD_NEXT = 0x10;
    public static final int CMD_PREV = 0x20;

//    public static final byte START = 0x01;

    private static PlayBackWorker sWorker ;

    static {

        //We must initialize this worker thread and start it when the class loaded,
        //or we may fail to initiate the handler and cause a NullPointerException
        // because the worker thread can not  get the chance to execute
        sWorker = new PlayBackWorker();
        sWorker.setPriority(Thread.MAX_PRIORITY);
        sWorker.start();
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public AudioPlaybackService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flag, int id) {
        final int command = intent.getIntExtra(EXTRA_COMMAND, 0xff);
        Handler handler = sWorker.getHandler();
        if (handler == null || command == 0xff) {
            return 1;
        }
        Message msg = Message.obtain();
        if (command != 0xff) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    switch (command) {
                        case CMD_NEXT:
                            AudioPlaybackModel.getInstance().next();
                            break;
                        case CMD_PAUSE:
                            AudioPlaybackModel.getInstance().pause();
                            break;
                        case CMD_RESTART:
                            AudioPlaybackModel.getInstance().restart();
                            break;
                        default:break;
                    }
                }
            });
            return 1;
        }
        String uri = intent.getStringExtra(AudioListAdapter.AudioItemHolder.URI);
        int pos = intent.getIntExtra(AudioListAdapter.AudioItemHolder.INDEX,0);

        msg.obj = uri;
        msg.arg1 = pos;
        if (handler != null) {
            handler.sendMessage(msg);
        }
        return id;
    }

    static class PlayBackWorker extends Thread {
        Handler mHandler;

        public Handler getHandler() {
            return mHandler;
        }

        @Override
        public void run() {
            Looper.prepare();

            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    AudioPlaybackModel model = AudioPlaybackModel.getInstance();
                    model.init();
                    String uri = (String)msg.obj;
                    int position = msg.arg1;
                    model.start(uri, position);
                }
            };
            this.setPriority(Thread.NORM_PRIORITY);
            Looper.loop();
        }
    }
}
