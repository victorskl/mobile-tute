package com.sankholin.helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int _MY_REQUEST_CODE = 007;

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("@MainActivity onCreate");

        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.btnAct1);
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // move from ActivityOne to ActivityTwo
                        Intent intent = new Intent(MainActivity.this, ActivityTwo.class);
                        intent.putExtra("text", "Text from Activity 1"); // workshop exercise: Use putExtra

                        // A few way to start the second Activity.
                        //startActivity(intent);
                        startActivityForResult(intent, _MY_REQUEST_CODE); // workshop exercise: Use startActivityForResult() when A creates B
                    }
                }
        );

        Log.d("Hello World App", "At Activity 1 onCreate");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == _MY_REQUEST_CODE) {

            TextView textView2 = (TextView) findViewById(R.id.textView2);
            TextView textView3 = (TextView) findViewById(R.id.textView3);

            if (resultCode == RESULT_OK) {
                String rocket = data.getStringExtra("rocket");
                textView3.setText(rocket);

                SimpleMessage msgFromActivity2 = (SimpleMessage) data.getSerializableExtra("msg"); // exercise: get Serializable Object

                if (msgFromActivity2 != null) {
                    textView2.setText(msgFromActivity2.getMessage());
                }
            }

            if (resultCode == RESULT_CANCELED) {
                textView2.setText("Just Air Gun...");
                textView3.setText("Bubble Pop!");
            }
        }
    }


    // observing life cycle

    protected void onStart(){
        // The activity is about to become visible.
        super.onStart();
        System.out.println("@MainActivity onStart");
    }

    protected void onResume(){
        // The activity has become visible (it is now "resumed").
        super.onResume();
        System.out.println("@MainActivity onResume");
    }

    protected void onPause(){
        // Another activity is taking focus (this activity is about to be "paused").
        super.onPause();
        System.out.println("@MainActivity onPause");
    }

    protected void onStop(){
        // The activity is no longer visible (it is now "stopped")
        super.onStop();
        System.out.println("@MainActivity onStop");
    }

    protected void onRestart(){
        super.onRestart();
        System.out.println("@MainActivity onRestart");
    }

    protected void onDestroy(){
        // The activity is about to be destroyed.
        super.onDestroy();
        System.out.println("@MainActivity onDestory");
    }
}
