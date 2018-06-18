package ru.miklelolyandex.officeexercises;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.FloatMath;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class notificationService extends Service implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private float[] mGravity;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    private Timer timer;
    private int scheduleTime;
    private Context context;
    @Override
    public void onCreate() {
        Log.d("sensor","hello");
        setTime();
        context = this;
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        sensorManager.registerListener(this,accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        startTimer();
    }

    private void setTime(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        scheduleTime = Integer.valueOf(preferences.getString("interval_timer", "1")) * 1000 * 60;
        scheduleTime = 10000;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void startTimer(){
        Log.d("timer","started");
        Log.d("timer",String.valueOf(scheduleTime));
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d("timer", "ok");
                NotificationScheduler.setRightNow(context, AlarmReceiver.class);
            }
        }, scheduleTime);
    }

    private void stopTimer() {
        Log.d("timer", "timer stoped");
        timer.cancel();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            mGravity = event.values.clone();
            // Shake detection
            float x = mGravity[0];
            float y = mGravity[1];
            float z = mGravity[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float)Math.sqrt(x*x + y*y + z*z);
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            // Make this higher or lower according to how much
            // motion you want to detect
            if(mAccel > 3){
                Log.d("sensor", "sensor is working");
                stopTimer();
                startTimer();
            }
        }
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onDestroy() {
        Log.d("sensor", "bye");
        stopTimer();
    }
}
