package ru.miklelolyandex.officeexercises;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



/**
 * Created by mike on 28.02.18.
 */

public class Duartion_dialog_pref extends DialogPreference {
    private Button rightArrow;
    private Button leftArrow;
    private TextView number;
    private Context mContext;
    private SharedPreferences prefs;

    public Duartion_dialog_pref(Context context, AttributeSet  attributeSet){
        super(context, attributeSet);
        mContext = context;
    }
    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (!positiveResult)
            return;

        super.onDialogClosed(positiveResult);

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
        editor.putString("exercise_duration", number.getText().toString());
        editor.commit();

        String minutes = mContext.getResources().getString(R.string.seconds);
        setSummary(number.getText().toString() + " " + minutes);

    }

    @Override
    protected void onBindDialogView(View view) {
        rightArrow = view.findViewById(R.id.duration_dialog_right_arrow);
        rightArrow.setOnClickListener(rightArrowClick);
        leftArrow = view.findViewById(R.id.duration_dialog_left_arrow);
        leftArrow.setOnClickListener(leftArrowClick);
        number = view.findViewById(R.id.duration_dialog_number);

        prefs = android.preference.PreferenceManager.
                getDefaultSharedPreferences(getContext());
        String seconds = prefs.getString("exercise_duration", "32");

        number.setText(seconds);
        super.onBindDialogView(view);
    }


    private View.OnClickListener rightArrowClick  = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String secStr = number.getText().toString();
            int sec = Integer.parseInt(secStr);
            sec = sec+1;
            number.setText(String.valueOf(sec));
        }
    };

    private View.OnClickListener leftArrowClick  = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String secStr = number.getText().toString();
            int sec = Integer.parseInt(secStr);
            sec = sec-1;
            if (sec < 15){
                smallDefentition();
            } else {
                number.setText(String.valueOf(sec));
            }
        }
    };

    private void smallDefentition(){

        Toast toast = Toast.makeText(mContext.getApplicationContext(),
                R.string.pref_duration_toast_too_small, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void bigDefentition(){

        Toast toast = Toast.makeText(mContext.getApplicationContext(),
                R.string.pref_duration_toast_too_small, Toast.LENGTH_SHORT);
        toast.show();
    }
}
