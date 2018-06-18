package ru.miklelolyandex.officeexercises;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import static ru.miklelolyandex.officeexercises.NotificationScheduler.turnOffOnNotificaton;

/**
 * Created by mike on 29.03.18.
 */

public class FinishActivity extends AppCompatActivity {
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_finish);
        textView = findViewById(R.id.elapsed_time);
        Intent mIntent = getIntent();
        int timerExercise = mIntent.getIntExtra("time", 0) /1000;
        setCaption(timerExercise);
        setNotifications();
    }

    private void setNotifications(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean timer = preferences.getBoolean("switch_notification",false);
        Boolean sensorPref = preferences.getBoolean("sensor_switch", false);
        if (timer) {
            if (sensorPref) {
                startService(new Intent(this, notificationService.class));
                return;
            }
            turnOffOnNotificaton(getApplicationContext(), timer);
        }
    }

    private void setCaption(int timerExercise) {
        String time;

        if (timerExercise >= 60){
            String sec;

            if (timerExercise%60 == 0) {sec = "00";}
            else { sec = String.valueOf(timerExercise%60); }

            time = String.valueOf(timerExercise/60) + ":" + sec + " минут";
        }
        else {time = String.valueOf(timerExercise) + " секунд";}

        textView.setText(time);
    }

    public void onFinishButtonClicked (View view){
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        intent.putExtra("setChrono", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){

    }
}
