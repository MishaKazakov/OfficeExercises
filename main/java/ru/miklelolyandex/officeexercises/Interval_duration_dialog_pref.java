package ru.miklelolyandex.officeexercises;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by mike on 05.03.18.
 */


public class Interval_duration_dialog_pref extends DialogPreference {
    private Button rightArrow;
    private Button leftArrow;
    private TextView number;
    private Context mContext;
    private SharedPreferences prefs;

    public Interval_duration_dialog_pref(Context context, AttributeSet  attributeSet){
        super(context, attributeSet);
        mContext = context;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (!positiveResult)
            return;

        super.onDialogClosed(positiveResult);

        String minutes = mContext.getResources().getString(R.string.seconds);
        String intervalMinutes = number.getText().toString() + " " + minutes;
        // String in SharedPreferences!
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
        editor.putString("interval_duration", number.getText().toString());
        editor.commit();


        setSummary(intervalMinutes);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        leftArrow = view.findViewById(R.id.interval_duration_dialog_left_arrow);
        leftArrow.setOnClickListener(leftArrowClickInerval);
        rightArrow = view.findViewById(R.id.interval_duration_dialog_right_arrow);
        rightArrow.setOnClickListener(rightArrowClickInerval);
        number = view.findViewById(R.id.interval_duration_dialog_number);

        prefs = android.preference.PreferenceManager.
                getDefaultSharedPreferences(getContext());
        String seconds = prefs.getString("interval_duration", "5");

        number.setText(seconds);
    }


    private View.OnClickListener rightArrowClickInerval  = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String secStr = number.getText().toString();
            int sec = Integer.parseInt(secStr);
            sec = sec+1;
            number.setText(String.valueOf(sec));
        }
    };

    private View.OnClickListener leftArrowClickInerval  = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String secStr = number.getText().toString();
            int sec = Integer.parseInt(secStr);
            sec = sec-1;
            number.setText(String.valueOf(sec));
        }
    };


}
