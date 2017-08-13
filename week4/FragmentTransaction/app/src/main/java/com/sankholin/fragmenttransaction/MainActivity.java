package com.sankholin.fragmenttransaction;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btnAdd;
    Button btnReplace;
    Button btnRemove;
    FragmentManager manager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button) findViewById(R.id.add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define your fragment
                FragmentA fa = new FragmentA();
                // begin a transaction
                FragmentTransaction transaction = manager.beginTransaction();
                // call add() function to add this fragment into the view container
                transaction.add(R.id.fragmentPanel, fa, "fragmentA");
                // add the transaction to the stack
                transaction.addToBackStack(null);
                // each transaction should call commit() function to confirm transaction
                transaction.commit();
            }
        });

        btnRemove = (Button) findViewById(R.id.remove);
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // define the transaction that you want to remove via its tag
                FragmentA fa = (FragmentA) manager.findFragmentByTag("fragmentA");
                FragmentTransaction transaction = manager.beginTransaction();
                // if this transaction exists, it will be removed
                if (fa != null) {
                    transaction.addToBackStack(null);
                    transaction.remove(fa);
                    transaction.commit();
                }
            }
        });

        btnReplace = (Button) findViewById(R.id.replace);
        btnReplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentB fb = new FragmentB();
                FragmentTransaction transaction = manager.beginTransaction();
                // replace the current fragment with a FragmentB object
                transaction.replace(R.id.fragmentPanel, fb, "fragmentB");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}
