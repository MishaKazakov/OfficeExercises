package ru.miklelolyandex.officeexercises;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class TrainingActivity extends AppCompatActivity {
    private ImageButton prevButton;
    private ImageButton nextButton;
    private ImageButton pauseButton;
    private int exerciseNum = 0;
    private TextView exerciseDescription;
    private String[] textExercise;
    private ProgressBar trProgressBar;
    private int ANIMATION_DURATION;
    private ObjectAnimator animator = null;
    private Timer timer;
    private View myView;
    private boolean isPuseButton = true;
    private int timerTime;
    private long timerStartTime;
    public static final String  SHARED_PREFS    = "SHARED_PREFS";
    private PowerManager.WakeLock wl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        onCreateSetViews();
        onCreateSetVariables();
        setText();
        startProgressBar();
    }

    private void onCreateSetVariables(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        ANIMATION_DURATION = Integer.valueOf(preferences.getString("exercise_duration", "30")) * 1000;
        timerTime = ANIMATION_DURATION;
        prevButton.setVisibility(View.INVISIBLE);
        textExercise = getResources().getStringArray(R.array.training_1);
        timer = new Timer();
        boolean onDisplay = preferences.getBoolean("screen_on", true);
        if (onDisplay) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    private void onCreateSetViews(){
        prevButton    = findViewById(R.id.previous_button);
        nextButton    = findViewById(R.id.next_button);
        pauseButton   = findViewById(R.id.pause_button);
        trProgressBar = findViewById(R.id.progressBar);
        exerciseDescription = findViewById(R.id.exercise_description);
        myView = getWindow().getDecorView();
    }

    public void onNextButtonClick(View view){
        timerTime = ANIMATION_DURATION;
        if (exerciseNum == 0){
            prevButton.setVisibility(View.VISIBLE);
        }
        exerciseNum++;
        if (exerciseNum == textExercise.length - 1){
            nextButton.setVisibility(View.INVISIBLE);
        }
        if (exerciseNum <textExercise.length) {
            setText();
            startProgressBar();
        }
    }

    public void onPreviousButtonClick(View view){
        timerTime = ANIMATION_DURATION;
        exerciseNum--;
        setText();
        startProgressBar();
        if (exerciseNum==0){
            prevButton.setVisibility(View.INVISIBLE);
        }
        if (exerciseNum == textExercise.length - 2){
            nextButton.setVisibility(View.VISIBLE);
        }
    }

    public void onPauseButtonClick(View view){
        if (isPuseButton) {
            pauseButton.setImageDrawable(getResources().getDrawable( R.drawable.ic_play_arrow_black_24dp));
            animator.pause();
            pauseTimer();
            isPuseButton = false;
        } else {
            pauseButton.setImageDrawable(getResources().getDrawable( R.drawable.ic_pause_black_24dp));
            animator.resume();
            remuseTime();
            isPuseButton = true;
        }
    }

    private void pauseTimer(){
        timerTime = timerTime - (int)(System.currentTimeMillis() - timerStartTime);
        timer.cancel();
    }

    private void remuseTime(){
        timer = new Timer();
        startTimer();
        timerStartTime = System.currentTimeMillis();
    }

    private void startTimer(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onNextButtonClick(myView);
                    }
                });
            }
        }, timerTime);
    }

    private void startProgressBar(){
        if (animator != null){
            animator.start();
        } else {
            animator = ObjectAnimator.ofInt(trProgressBar, "progress", 1000);
            TimeInterpolator GAUGE_ANIMATION_INTERPOLATOR = new LinearInterpolator();
            animator.setInterpolator(GAUGE_ANIMATION_INTERPOLATOR);
            animator.setDuration(ANIMATION_DURATION);
        }
        startTimer();
        timerStartTime = System.currentTimeMillis();
        animator.start();
    }

    private void setText(){
        exerciseDescription.setText(textExercise[exerciseNum]);
    }
}
