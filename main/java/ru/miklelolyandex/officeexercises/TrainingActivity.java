package ru.miklelolyandex.officeexercises;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import static ru.miklelolyandex.officeexercises.NotificationScheduler.turnOffOnNotificaton;

public class TrainingActivity extends AppCompatActivity {
    private ImageButton prevButton;
    private ImageButton nextButton;
    private ImageButton pauseButton;
    private int exerciseNum = 0;
    private TextView exerciseDescription;
    private ImageView exerciseImage;
    private int exerciseId;
    private int amountOfExercises;
    private ProgressBar trProgressBar;
    private int ANIMATION_DURATION;
    private ObjectAnimator animator = null;
    private Timer timer;
    private View myView;
    private boolean isPuseButton = true;
    private int timerTime;
    private long timerStartTime;
    private int timerExercise;
    private Exercise db;
    public static final String  SHARED_PREFS    = "SHARED_PREFS";
    private PowerManager.WakeLock wl;
    private int cardNum;
    private String imageName;
    private AssetManager assetManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        getExtras();
        onCreateSetViews();
        onCreateSetVariables();
        startProgressBar();
        stopNotification();
        timerExercise = (int) System.currentTimeMillis();
        getText();
        setImage();
    }

    private void stopNotification(){
        turnOffOnNotificaton(getApplicationContext(), false);
        stopService( new Intent(this, notificationService.class));
    }

    private void getExtras(){
        Intent mIntent = getIntent();
        cardNum = mIntent.getIntExtra("cardNum", 0);
        imageName = mIntent.getStringExtra("imageName");
        amountOfExercises = mIntent.getIntExtra("amountOfExercises", 0);
    }

    private void getText(){
        new ExerciseAsyncTask().execute(cardNum, exerciseId);
    }

    private void setImage(){
        String path = String.valueOf(cardNum) + "/" + imageName + String.valueOf(exerciseId) +".png";
        try {
            InputStream is = assetManager.open(path);
            Drawable drawable = Drawable.createFromStream(is, null);
            exerciseImage.setImageDrawable(drawable);
            is.close();
        }
        catch(IOException ex)
        {
            return;
        }

    }

    private void onCreateSetVariables(){
        exerciseId = 1;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        ANIMATION_DURATION = Integer.valueOf(preferences.getString("exercise_duration", "30")) * 1000;
        timerTime = ANIMATION_DURATION;
        prevButton.setVisibility(View.INVISIBLE);
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
        exerciseImage = findViewById(R.id.exercise_image);
        myView = getWindow().getDecorView();
        assetManager = getAssets();
    }

    public void onNextButtonClick(View view){
        timerTime = ANIMATION_DURATION;
        if (exerciseNum == 0){
            prevButton.setVisibility(View.VISIBLE);
        }
        exerciseNum++;
        if (exerciseNum == amountOfExercises){
            timerExercise = (int) System.currentTimeMillis() - timerExercise;
            Intent intent = new Intent(view.getContext(), FinishActivity.class);
            intent.putExtra("time", timerExercise);
            startActivity(intent);
        }
        if (exerciseNum < amountOfExercises) {
            getText();
            startProgressBar();
        }
        exerciseId++;
        setImage();
    }

    public void onPreviousButtonClick(View view){
        timerTime = ANIMATION_DURATION;
        exerciseNum--;
        getText();
        startProgressBar();
        if (exerciseNum==0){
            prevButton.setVisibility(View.INVISIBLE);
        }
        if (exerciseNum == amountOfExercises - 2){
            nextButton.setVisibility(View.VISIBLE);
        }
        exerciseId--;
        setImage();
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
        String text = db.getText();
        exerciseDescription.setText(text);
    }

    private class ExerciseAsyncTask extends AsyncTask<Integer, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Integer[] context){
            Log.d("params", String.valueOf(context[0]));
            Log.d("params", String.valueOf(context[1]));
            db = AppDatabase.getInstance(getApplicationContext()).exerciseDao().getByID(context[0],context[1]);
            return true;
        }
        @Override
        protected void onPostExecute(Boolean voids){
            setText();
        }
    }
}
