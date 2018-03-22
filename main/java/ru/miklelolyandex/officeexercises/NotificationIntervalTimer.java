package ru.miklelolyandex.officeexercises;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Created by mike on 11.03.18.
 */

public class NotificationIntervalTimer extends DialogPreference {
    private Context mContext;
    private Button rightArrow;
    private Button leftArrow;
    private TextView number;
    private SharedPreferences prefs;

    NotificationIntervalTimer(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        mContext = context;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (!positiveResult)
            return;

        super.onDialogClosed(positiveResult);

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
        editor.putString("interval_timer", number.getText().toString());
        editor.commit();

        String minutes = mContext.getResources().getString(R.string.minutes);
        setSummary(number.getText().toString() + " " + minutes);
    }


    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        rightArrow = view.findViewById(R.id.duration_dialog_right_arrow);
        rightArrow.setOnClickListener(rightArrowClick);
        leftArrow = view.findViewById(R.id.duration_dialog_left_arrow);
        leftArrow.setOnClickListener(leftArrowClick);
        number = view.findViewById(R.id.duration_dialog_number);

        prefs = android.preference.PreferenceManager.
                getDefaultSharedPreferences(getContext());
        String minutes = prefs.getString("interval_timer", "60");

        number.setText(minutes);
        super.onBindDialogView(view);
    }

    private View.OnClickListener rightArrowClick  = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String secStr = number.getText().toString();
            int minutes = Integer.parseInt(secStr);
            minutes = minutes+1;
            number.setText(String.valueOf(minutes));
        }
    };

    private View.OnClickListener leftArrowClick  = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String secStr = number.getText().toString();
            int minutes = Integer.parseInt(secStr);
            minutes = minutes-1;
            if (minutes == 0) {
                minutes = 1;
                smallDefentition();
            }

            number.setText(String.valueOf(minutes));
        }
    };

    private void smallDefentition(){

        Toast toast = Toast.makeText(mContext.getApplicationContext(),
                R.string.pref_duration_toast_too_small, Toast.LENGTH_SHORT);
        toast.show();
    }


}
