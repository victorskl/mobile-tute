package io.github.cluo29.androiduitutorial;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    public MyService() {
    }


    @Override
    public void onCreate() {

        Log.d("ServiceWords", "Alive");


        int dataInt = 1;
        Intent intent = new Intent("IntentNameWhatever");
        intent.putExtra("dataInt", dataInt);
        sendBroadcast(intent);


        IntentFilter filter = new IntentFilter();
        filter.addAction("IntentNameWhatever2");
        registerReceiver(contextBR2, filter);
    }




    private ContextReceiver2 contextBR2 = new ContextReceiver2();
    public class ContextReceiver2 extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("IntentNameWhatever2"))
            {
                int naive = intent.getExtras().getInt("dataInt2");
                Log.d("ServiceWords", "dataInt2 = "+naive);

            }

        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
