package com.sankholin.threadsandsensors;

import android.util.Log;

public class PrimeThread extends Thread {

    @Override
    public void run() {
        int cnt = 10;
        while (cnt > 0) {
            try {
                Log.i("counting down: ", "" + cnt);
                cnt--;
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // Restore interrupt status.
                //Thread.currentThread().interrupt();
                return;
            }
        }
        Log.i("PrimeThread", "PrimeThread count down is done. Pls interrupt PrimeThread now.");
    }
}
