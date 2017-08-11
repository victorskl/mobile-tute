package com.sankholin.helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ActivityTwo extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);

        Intent intent = getIntent();
        String strFromActivity1 = intent.getStringExtra("text"); // workshop exercise: use getStringExtra

        final EditText editText = (EditText) findViewById(R.id.editText);
        editText.setText(strFromActivity1);

        button = (Button) findViewById(R.id.btnGoBack);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityTwo.this, MainActivity.class);
                SimpleMessage msg = new SimpleMessage(editText.getText().toString());
                intent.putExtra("msg", msg); // exercise: sending Serializable Object
                intent.putExtra("rocket", "Nuke Nuke!"); // workshop exercise: onActivityResult
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        Log.d("Hello World App","At Activity 2 onCreate");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Activity 2", "onDestroy...");
    }
}
