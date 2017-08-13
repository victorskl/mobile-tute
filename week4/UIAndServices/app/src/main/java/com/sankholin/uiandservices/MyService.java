package com.sankholin.uiandservices;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * https://developer.android.com/guide/components/services.html
 */
public class MyService extends IntentService {

    /**
     * A constructor is required, and must call the super IntentService(String)
     * constructor with a name for the worker thread.
     */
    public MyService() {
        super("MyService");
        Log.i("MyService", "Init service...");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // Normally we would do some work here, like download a file.
        // For our sample, we just sleep for x seconds.
        int cnt = 10;
        while (cnt > 0) {
            try {
                Log.i("counting down: ", ""+ cnt);
                cnt--;
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // Restore interrupt status.
                Thread.currentThread().interrupt();
            }
        }

        // Service Activity Communication
        int dataInt = 1;
        Intent myIntentService = new Intent("MyIntentService"); // Define in AndroidManifest intent filter
        myIntentService.putExtra("dataInt", dataInt);
        sendBroadcast(myIntentService);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("MyService", "MyService is running...");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i("MyService", "MyService has destroyed...");
        super.onDestroy();
    }
}
