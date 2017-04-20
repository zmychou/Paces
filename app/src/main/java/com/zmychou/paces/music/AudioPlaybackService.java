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

    private static PlayBackWorker sWorker ;

    static {

        //We must initialize this worker thread and start it when the class loaded,
        //or we may fail to initiate the handler and cause a NullPointerException
        // because the worker thread can not  get the chance to execute
        sWorker = new PlayBackWorker();
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
        String uri = intent.getStringExtra(AudioListAdapter.AudioItemHolder.URI);
        int pos = intent.getIntExtra(AudioListAdapter.AudioItemHolder.INDEX,0);
        Handler handler = sWorker.getHandler();
        Message msg = Message.obtain();
        msg.obj = uri;
        msg.arg1 = pos;
        handler.sendMessage(msg);
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
            Looper.loop();
        }
    }
}
