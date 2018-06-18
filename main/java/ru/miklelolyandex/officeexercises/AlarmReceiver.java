package ru.miklelolyandex.officeexercises;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by mike on 25.03.18.
 */

public class AlarmReceiver extends BroadcastReceiver{

    String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String mystring =  context.getResources().getString(R.string.notification_title);
        NotificationScheduler.showNotification(context, MainActivity.class,
                mystring, "");
        Log.d("asdsad","okok");
    }
}
