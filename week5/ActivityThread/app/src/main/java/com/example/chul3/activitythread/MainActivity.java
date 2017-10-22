package com.example.chul3.activitythread;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PrimeThread p = new PrimeThread(143);
        p.start();
    }

    class PrimeThread extends Thread {
        long minPrime;

        PrimeThread(long minPrime) {
            this.minPrime = minPrime;
        }

        public void run() {
            // compute primes larger than minPrime
            while (true) {
                Log.d("THREAD", "going");
                minPrime = minPrime + 1;

                if (minPrime == 155) {
                    finish();
                    Log.d("THREAD", "activity dead");
                    //activity is dead, thread is alive still
                    //so don't access dead objects in activity after this
                }

                if (minPrime == 166) {
                    Thread.currentThread().interrupt();
                    Log.d("THREAD", "interrupt");
                }

                if (Thread.currentThread().interrupted())  //Thread refers to current thread
                {
                    Log.d("THREAD", "interrupted");
                    return;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
