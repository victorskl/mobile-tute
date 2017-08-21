package io.github.cluo29.androiduitutorial;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        startService(new Intent(MainActivity.this, MyService.class));

        IntentFilter filter = new IntentFilter();
        filter.addAction("IntentNameWhatever");
        registerReceiver(contextBR, filter);

    }


    private ContextReceiver contextBR = new ContextReceiver();
    public class ContextReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("IntentNameWhatever"))
            {
                int naive = intent.getExtras().getInt("dataInt");
                Log.d("ActivityWords", "dataInt = "+naive);




                int dataInt2 = 2;
                Intent intent2 = new Intent("IntentNameWhatever2");
                intent2.putExtra("dataInt2", dataInt2);
                sendBroadcast(intent2);
            }

        }
    }

}
