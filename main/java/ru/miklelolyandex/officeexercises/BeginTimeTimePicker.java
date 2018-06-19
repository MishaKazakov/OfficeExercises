package ru.miklelolyandex.officeexercises;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import java.util.HashSet;

/**
 * Created by mike on 07.03.18.
 */

public class BeginTimeTimePicker extends DialogPreference {
    private TimePicker timePicker;
    private Context mContext;
    private int hour;
    private int minute;

    public BeginTimeTimePicker(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        mContext = context;
    }

    @Override
    protected void onBindDialogView(View view) {
        timePicker = view.findViewById(R.id.beginNotificationTimePicker);
        timePicker.setIs24HourView(true);

        String summary =  getSummary().toString();
        if (summary == null || summary.isEmpty()){
            hour = 9;
            minute = 0;
        } else {
            hour = Integer.parseInt(summary.substring(0,2));
            minute = Integer.parseInt(summary.substring(3));
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(minute);
        } else {
            timePicker.setHour(hour);
            timePicker.setMinute(minute);
        }

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                hour = i;
                minute = i1;
            }
        });
        super.onBindDialogView(view);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if(!positiveResult) {
            return;
        }
        super.onDialogClosed(positiveResult);

        String minuteStr = String.valueOf(minute);
        String housrStr = String.valueOf(hour);

        if (minute < 10){
            minuteStr = "0" + minuteStr;
        }
        if (hour < 10){
            housrStr = "0" + housrStr;
        }

        String time = housrStr+":"+minuteStr;
        setSummary(time);

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
        editor.putString("beginNotification", time);
        editor.commit();


    }

}
