package com.zmychou.paces.running;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DataDeriveService extends Service {
    public DataDeriveService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
