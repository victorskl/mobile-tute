package com.sankholin.threadsandsensors;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private PrimeThread primeThread;
    private Thread runnerThread;
    private int originalTextColor;

    private TextView textView3;
    private ProgressBar progressBar;
    private Button button1;

    private ThreadPoolExecutor executor;

    private ContextReceiver contextBR = new ContextReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ToggleButton toggleButton1 = (ToggleButton) findViewById(R.id.toggleButton1);
        ToggleButton toggleButton2 = (ToggleButton) findViewById(R.id.toggleButton2);

        final TextView textView1 = (TextView) findViewById(R.id.textView1);
        originalTextColor = textView1.getCurrentTextColor();
        final TextView textView2 = (TextView) findViewById(R.id.textView2);

        primeThread = new PrimeThread();
        runnerThread = new Thread(new PrimeRun());

        toggleButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    if (primeThread == null)
                        primeThread = new PrimeThread();
                    primeThread.start();
                    textView1.setTextColor(Color.RED);
                    Log.i("PrimeThread", "PrimeThread is started.");
                } else {
                    primeThread.interrupt();
                    textView1.setTextColor(originalTextColor);
                    primeThread = null;
                    Log.i("PrimeThread", "PrimeThread is interrupted.");
                }
            }
        });

        toggleButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (runnerThread == null)
                        runnerThread = new Thread(new PrimeRun());
                    runnerThread.start();
                    textView2.setTextColor(Color.RED);
                    Log.i("PrimeRun", "PrimeRun is started.");
                } else {
                    runnerThread.interrupt();
                    textView2.setTextColor(originalTextColor);
                    runnerThread = null;
                    Log.i("PrimeRun", "PrimeRun is interrupted.");
                }
            }
        });


        // ProgressBar and AsyncTask
        textView3 = (TextView) findViewById(R.id.textView3);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(MAX);
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
                new PrimeAsyncTask().execute(MAX);
            }
        });


        // NOTE!!! this is not a usual thread pool use case. this is not how you should use thread pool.
        // when u use thread pool, you tend to run the runnable task all the way to end of the app life cycle.
        // i.e. a task never stopping. i leave it here, just for exercise purpose.

        // ThreadPoolExecutor
        // https://developer.android.com/reference/java/util/concurrent/ThreadPoolExecutor.html
        // https://developer.android.com/training/multiple-threads/index.html
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        Log.i("NUMBER_OF_CORES", " " + NUMBER_OF_CORES);

        final TextView textView4 = (TextView) findViewById(R.id.textView4);
        final ToggleButton toggleButton3 = (ToggleButton) findViewById(R.id.toggleButton3);
        toggleButton3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    // usually non-stopping runnable task. but runnerThread is stoppable i.e. interrupt
                    // if u want to use cancellable runnable, check https://dzone.com/articles/interrupting-executor-tasks
                    executor.execute(runnerThread);

                    textView4.setTextColor(Color.RED);
                    Log.i("ThreadPoolExecutor", "ThreadPoolExecutor is started.");

                } else {

                    // normally this graceful clean up shutdown goes with the shutdown hook at
                    // the end of app life cycle, like this.
                    //      Runtime.getRuntime().addShutdownHook(new ShutdownService(servicePool));
                    // i leave it here, just for exercise purpose.

                    executor.shutdown(); // won't interrupt submitted task but won't accept any more new tasks
                    toggleButton3.setEnabled(false);

                    try {
                        // Wait a while for existing tasks to terminate
                        if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                            executor.shutdownNow(); // Cancel currently executing tasks
                            // Wait a while for tasks to respond to being cancelled
                            if (!executor.awaitTermination(10, TimeUnit.SECONDS))
                                System.err.println("Pool did not terminate");
                        }
                    } catch (InterruptedException ie) {
                        // (Re-)Cancel if current thread also interrupted
                        executor.shutdownNow();
                        // Preserve interrupt status
                        Thread.currentThread().interrupt();
                    }

                    textView4.setTextColor(originalTextColor);
                    Log.i("ThreadPoolExecutor", "ThreadPoolExecutor is shutdown.");
                }
            }
        });


        // Start SensorService
        startService(new Intent(MainActivity.this, SensorService.class));
        IntentFilter filter = new IntentFilter();
        filter.addAction("SensorService");
        registerReceiver(contextBR, filter);
    }

    @Override
    protected void onDestroy() {

        // clean up threads onDestroy.
        if (primeThread != null) {
            primeThread.interrupt();
            primeThread = null;
            Log.i("PrimeThread", "PrimeThread is interrupted onDestroy.");
        }

        if (runnerThread != null) {
            runnerThread.interrupt();
            runnerThread = null;
            Log.i("PrimeRun", "PrimeRun is interrupted onDestroy.");
        }

        unregisterReceiver(contextBR);

        super.onDestroy();
    }

    // ProgressBar and AsyncTask tutorial inspire from
    //      http://www.concretepage.com/android/android-asynctask-example-with-progress-bar
    //      https://developer.android.com/reference/android/os/AsyncTask.html
    private class PrimeAsyncTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... params) {
            int cnt = params[0];
            while (cnt > 0) {
                try {
                    Log.i("counting down: ", "" + cnt);
                    cnt--;
                    Thread.sleep(1000);
                    publishProgress(cnt);
                } catch (InterruptedException e) {
                    // Restore interrupt status.
                    Thread.currentThread().interrupt();
                }
            }
            return "PrimeAsyncTask completed.";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            textView3.setTextColor(Color.RED);
            button1.setEnabled(false);
            textView3.setText("PrimeAsyncTask starting.");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //progressBar.setVisibility(View.GONE);
            textView3.setTextColor(originalTextColor);
            textView3.setText(s);
            button1.setEnabled(true);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            textView3.setText("PrimeAsyncTask running..."+ values[0]);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            button1.setEnabled(true);
            textView3.setTextColor(originalTextColor);
        }
    }

    public class ContextReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("SensorService")) {
                int dataInt = intent.getExtras().getInt("dataInt");
                Log.i("SensorService", "dataInt = "+dataInt);
            }

        }
    }

    private static final int MAX = 10;
    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
}
