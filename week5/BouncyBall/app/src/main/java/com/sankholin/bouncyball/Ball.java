package com.sankholin.bouncyball;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Ball {
    private static float ballRadius = 40;

    private WorldView worldView;
    private Bitmap bmp;

    public float x;
    public float y;

    private float xSpeed;
    private float ySpeed;

    private int screenWidth;
    private int screenHeight;

    public Ball(WorldView worldView, Bitmap bmp, int screenWidth, int screenHeight) {
        this.worldView = worldView;
        this.bmp = bmp;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        setX(120);
        setY(120);
        setXSpeed(0);
        setYSpeed(0);
    }

    //initialize the state of motion for the ball
    public void resetCoords(float screenWidth, float screenHeight, float x, float y, float xSpeed, float ySpeed) {
        this.x = (x / screenWidth) * this.screenWidth;
        this.y = ballRadius;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed * -1;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getXSpeed() {
        return xSpeed;
    }

    public void setXSpeed(float xSpeed) {
        this.xSpeed = xSpeed;
    }

    public float getYSpeed() {
        return ySpeed;
    }

    public void setYSpeed(float ySpeed) {
        this.ySpeed = ySpeed;
    }

    public void updatePhysics() {
        if (x > screenWidth - ballRadius) {
            //Reverse direction and slow down ball
            setXSpeed(getXSpeed() * -1);
        }
        if (x < ballRadius) {
            //Reverse direction and slow down ball
            setXSpeed(getXSpeed() * -1);
        }
        if (y > screenHeight - ballRadius) {
            //Reverse direction and slow down ball
            setYSpeed(getYSpeed() * -1);
        }
        if (y < ballRadius) {
            if (worldView.connected == false) {
                //Reverse direction and slow down ball
                setYSpeed(getYSpeed() * -1);
            } else {
                if (worldView.onScreen == true) {
                    sendBluetoothMessage();
                    //Send a message to connected phone to show ball
                }
            }
        }
    }

    public void moveBall() {
        x = x + xSpeed;
        y = y + ySpeed;
    }

    public void sendBluetoothMessage() {
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("ShowOnScreen");
            sb.append(",");
            sb.append(String.valueOf(screenWidth));
            sb.append(",");
            sb.append(String.valueOf(screenHeight));
            sb.append(",");
            sb.append(String.valueOf(x));
            sb.append(",");
            sb.append(String.valueOf(y));
            sb.append(",");
            sb.append(String.valueOf(xSpeed));
            sb.append(",");
            sb.append(String.valueOf(ySpeed));
            sb.append("\n");

            worldView.onScreen = false;
            worldView.outputStream.write(sb.toString().getBytes());
            worldView.outputStream.flush();
        } catch (Exception e) {
        }
    }

    // Draw the geometry into the canvas
    public void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        //smooth out the edges of what is being draw
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        updatePhysics();
        if (worldView.onScreen) {
            moveBall();
            //draw the circle to the canvas
            canvas.drawCircle(x, y, ballRadius, paint);
        }
    }
}