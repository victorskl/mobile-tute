package com.sankholin.fragmentapplication;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements FragmentA.Communicator {
    // invoke getSupportFragmentManager() to get the FragmentManager
    FragmentManager fm = getSupportFragmentManager();
    FragmentA fa;
    FragmentB fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //finds a fragment that was identified by given id when inflated from XML
        fa = (FragmentA) fm.findFragmentById(R.id.fragmentA);
        fb = (FragmentB) fm.findFragmentById(R.id.fragmentB);
    }

    @Override
    public void respond(String data) {
        fb.changeData(data);
    }
}
