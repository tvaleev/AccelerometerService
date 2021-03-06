package com.tvaleev.accelerometr.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class AccelerometrService extends Service implements SensorEventListener {

    private SensorManager sensorManager;
    private final IBinder mBinder = new AccBinder();
    private static float dataFromAccelerometer = 0;

    public float getDataFromAccelerometr() {
        return dataFromAccelerometer;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Service is created", Toast.LENGTH_SHORT).show();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean sr = sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL); // SENSOR_DELAY_GAME
        Toast.makeText(this, "Service is run " + sr, Toast.LENGTH_SHORT).show();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service is stopped", Toast.LENGTH_SHORT).show();
        sensorManager.unregisterListener(this);
        dataFromAccelerometer = 0;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event);
        }
    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;

        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);

        dataFromAccelerometer = accelationSquareRoot;
    }

    public class AccBinder extends Binder {
        AccelerometrService getService() {
            return AccelerometrService.this;
        }
    }

}
