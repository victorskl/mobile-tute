package com.example.chul3.locationdata;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class MyService extends Service {
    public MyService() {
    }



    @Override
    public void onCreate() {
        //here is still main thread!

        MyRun2 myRun2=new MyRun2();
        Thread thread=new Thread(myRun2);
        thread.start();


    }

    class MyRun2 implements  Runnable
    {

        @Override
        public void run() {



            while (true)
            {
                boolean gps_enabled = false;
                boolean network_enabled = false;

                LocationManager lm = (LocationManager)  getSystemService(Context.LOCATION_SERVICE);

                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                Location net_loc = null, gps_loc = null, finalLoc = null;

                if ( Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    continue;
                }
                if (gps_enabled)
                    gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (network_enabled)
                    net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if(!gps_enabled&&!network_enabled)
                {
                    Log.d("SENSORS", "false");
                }


                if (gps_loc != null && net_loc != null) {

                    //smaller the number more accurate result will
                    if (gps_loc.getAccuracy() > net_loc.getAccuracy())
                        finalLoc = net_loc;
                    else
                        finalLoc = gps_loc;

                    // I used this just to get an idea (if both avail, its upto you which you want to take as I've taken location with more accuracy)

                } else {

                    if (gps_loc != null) {
                        finalLoc = gps_loc;
                    } else if (net_loc != null) {
                        finalLoc = net_loc;
                    }
                    else{
                        Log.d("SENSORS", "all null");
                    }
                }

                if(finalLoc!=null)
                {
                    Log.d("SENSORS", "getLatitude= "+finalLoc.getLatitude());

                    //we are in melbourne, we got negative latitude.
                    //it means south part of the earth.

                    Log.d("SENSORS", "getLong= "+finalLoc.getLongitude());
                    Log.d("SENSORS", "provider= "+finalLoc.getProvider());
                }
                else
                {
                    Log.d("SENSORS", "null");
                }

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
}
