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

    @Override
    public void onCreate() {
        super.onCreate();
        sWorker = new PlayBackWorker();
        sWorker.start();
    }

    public AudioPlaybackService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flag, int id) {
        String uri = intent.getStringExtra(AudioListAdapter.AudioItemHolder.TAG);

        Handler handler = sWorker.getHandler();
        Message msg = new Message();
        msg.obj = uri;
        handler.sendMessage(msg);
        return id;
    }

//    class


    class PlayBackWorker extends Thread {
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
                    Log.e("where am i",Thread.currentThread().getName());
                    model.start(uri);
                }
            };
            Looper.loop();
        }
    }
}
