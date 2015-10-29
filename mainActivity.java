package com.example.orientationsensor;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import io.fabric.sdk.android.Fabric;

public class mainActivity extends Activity implements SensorEventListener {

    private static final String TAG = "mainActivity";

    private CustomSurfaceView customSurfaceView;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private boolean firstTime = true;

    private float prev_X;
    private float prev_Y;
    private float prev_Z;

    private long prevTime;

    public  void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }
    protected void onResume()
    {
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
        super.onResume();
    }
    protected void onPause()
    {
        mSensorManager.unregisterListener(this, mSensor);
        super.onPause();
    }
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        float X,Y,Z;
        //drawView.setAngle(sensorEvent.values[0]);
        Z = sensorEvent.values[2];
        Y = sensorEvent.values[1];
        X = sensorEvent.values[0];

        //Log.i(TAG, "X: " + String.format("%3.3f", X) + "\t\t Y: " + String.format("%3.3f", Y)
               //+ "\t\t Z: " + String.format("%3.3f", Z));
        if (Z < -2.0f) {
            if (firstTime) {
                if (isFixed(X, Y, Z)) {
                    customSurfaceView.setDraw(true);
                    customSurfaceView.setStartedPoint(prev_X, prev_Y);
                    customSurfaceView.postInvalidate();
                    firstTime = false;
                }
            } else {
                customSurfaceView.setDraw(true);
                customSurfaceView.setCurrentPoint(X, Y);
                customSurfaceView.postInvalidate();
            }
        } else {
            prevTime = System.currentTimeMillis();
            customSurfaceView.setDraw(false);
        }

    }
    protected void onDestroy()
    {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
        mSensorManager = null;
        mSensor = null;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());


        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);

        customSurfaceView = new CustomSurfaceView(this);
        this.setContentView(customSurfaceView);

        prevTime = System.currentTimeMillis();
    }

    private boolean isFixed(float X, float Y, float Z)
    {
        if (Math.abs(Math.max(Math.max(Math.abs(prev_X) - Math.abs(X), Math.abs(prev_Y) - Math.abs(Y)),
                Math.abs(prev_Z) - Math.abs(Z))) < 0.05)
        {

            prev_X = X;
            prev_Y = Y;
            prev_Z = Z;
            if (System.currentTimeMillis() - prevTime > 1000) return true; else return false;
        } else {
            prev_X = X;
            prev_Y = Y;
            prev_Z = Z;
            return false;
        }
    }

}
