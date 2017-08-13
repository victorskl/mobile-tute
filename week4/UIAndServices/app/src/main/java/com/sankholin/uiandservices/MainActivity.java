package com.sankholin.uiandservices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private ContextReceiver contextReceiver = new ContextReceiver();
    private ToggleButton toggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI Element Exercise

        final TextView textView = (TextView) findViewById(R.id.textView);
        final EditText editText = (EditText) findViewById(R.id.editText);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();
                textView.setText(text);
            }
        });


        // Background Service Exercise - MyService

        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                Intent myIntentService = new Intent(MainActivity.this, MyService.class); // Intent wrapper to MyService

                if (b) {
                    // The toggle is enabled
                    startService(myIntentService);
                } else {
                    // The toggle is disabled
                    stopService(myIntentService);
                }
            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction("MyIntentService"); // Define in AndroidManifest intent filter
        registerReceiver(contextReceiver, filter);
    }

    public class ContextReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("MyIntentService")) { // Define in AndroidManifest intent filter
                int dataInt = intent.getExtras().getInt("dataInt");
                Log.i("Receiving: ", ""+dataInt);
                if (dataInt == 1)
                    toggleButton.setChecked(false);
            }
        }
    }
}
