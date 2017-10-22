package com.example.chul3.accsensor;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class MyService extends Service  implements SensorEventListener {
    private static float accelerometer_x = 0;
    private static float accelerometer_y = 0;
    private static float accelerometer_z = 0;


    SensorManager mSensorMgr;

    Handler handler;

    public MyService() {
    }

    @Override
    public void onCreate() {
        //here is still main thread!

        //dont do anything heavy here



        mSensorMgr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        HandlerThread mHandlerThread = new HandlerThread("sensorThread");

        mHandlerThread.start();

        //to stop
        //mHandlerThread.quitSafely();

        handler = new Handler(mHandlerThread.getLooper());

        mSensorMgr.registerListener(this, mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST, handler);
        //handle it in a new thread

        //if service paused, even if you kill activity, thread will run unless you close it

        //close sensor

        //mSensorMgr.unregisterListener(this);

        //if service dead, all dead

        //write a new thread for compuation

        MyRun2 myRun2=new MyRun2();
        Thread thread=new Thread(myRun2);
        thread.start();


        //to close

        //make thread as a class variable

        //thread.interrupt();
    }

    class MyRun2 implements  Runnable
    {
        public int run=1;
        @Override
        public void run() {


            //

            while (run==1)
            {
                Log.d("SENSORS", "accelerometer_x= " + accelerometer_x);




                if(Thread.interrupted())  //Thread refers to current thread
                    return;


                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;

        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // accelerometer data
            accelerometer_x = event.values[0];
            accelerometer_y = event.values[1];
            accelerometer_z = event.values[2];

            //Log.d("SENSORS", "accelerometer_x= " + accelerometer_x);
            //Log.d("SENSORS", "accelerometer_y= " + accelerometer_y);
            //Log.d("SENSORS", "accelerometer_z= " + accelerometer_z);


        }
    }
}
