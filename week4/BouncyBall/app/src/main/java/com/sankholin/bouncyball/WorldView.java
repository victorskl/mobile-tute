package com.sankholin.bouncyball;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.OutputStream;

public class WorldView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private SurfaceHolder surfaceHolder;
    private boolean running = false;
    public Ball ball;
    public boolean onScreen = true;
    public OutputStream outputStream;
    public boolean connected = false;

    private int width;
    private int height;

    public WorldView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // create a instance of the accelerometer
        AccelerometerSensor aSensor = new AccelerometerSensor(this, context);
        getHolder().addCallback(this);
        setFocusable(true);
    }

    @SuppressLint("WrongCall")
    public void run() {
        // draw into the worldview
        while (running) {
            Canvas canvas = null;
            try {
                canvas = surfaceHolder.lockCanvas(null);
                synchronized (surfaceHolder) {
                    onDraw(canvas);
                    ball.onDraw(canvas);
                }
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
            try {
                Thread.sleep(10);
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        this.running = true;

        width = getWidth();
        height = getHeight();
        ball = new Ball(this, null, width, height);

        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Draw the Background
        canvas.drawColor(Color.BLUE);
    }
}
