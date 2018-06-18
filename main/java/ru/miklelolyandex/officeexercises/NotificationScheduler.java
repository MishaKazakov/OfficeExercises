package ru.miklelolyandex.officeexercises;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by mike on 25.03.18.
 */

public class NotificationScheduler {

    public static final int DAILY_REMINDER_REQUEST_CODE=100;
    public static final String TAG="NotificationScheduler";

    public static void setReminder(Context context, Class<?> cls, int hour, int min)
    {
        Calendar calendar = Calendar.getInstance();

        Calendar setcalendar = Calendar.getInstance();
        setcalendar.set(Calendar.HOUR_OF_DAY, hour);
        setcalendar.set(Calendar.MINUTE, min);
        setcalendar.set(Calendar.SECOND, 0);

        if(setcalendar.before(calendar))
            setcalendar.add(Calendar.DATE,1);

        // Enable a receiver
        enableReciver(context,cls,setcalendar);
    }

    public static void setRightNow(Context context, Class<?> cls) {
        Calendar calendar = Calendar.getInstance();
        long newTime = calendar.getTimeInMillis() + 10;
        calendar.setTimeInMillis(newTime);
        enableReciver(context,cls,calendar);
    }

    private static void enableReciver(Context context, Class<?> cls, Calendar setcalendar) {

        ComponentName receiver = new ComponentName(context, AlarmReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, setcalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public static void cancelReminder(Context context,Class<?> cls)
    {
        // Disable a receiver

        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    public static void showNotification(Context context,Class<?> cls,String title,String content)
    {
        cls = MainActivity.class;
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent notificationIntent = new Intent(context, cls);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(cls);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(DAILY_REMINDER_REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String idChannel = "my_channel_01";
            builder = new NotificationCompat.Builder(context,idChannel);
            NotificationChannel mChannel = new NotificationChannel(idChannel,context.getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.setDescription("asdasdasd");
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(mChannel);
            builder.setChannelId(idChannel);
        } else {
            builder = new NotificationCompat.Builder(context);
        }

        Notification notification = builder.setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(pendingIntent).build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(DAILY_REMINDER_REQUEST_CODE, notification);
    }

    public static void turnOffOnNotificaton(Context context, boolean notificationIsOn){
        if (notificationIsOn) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String beginTime = preferences.getString("beginNotification", "9:00");
            String endTime = preferences.getString("endNotification", "17:00");
            String intervalPref = preferences.getString("interval_timer", "60");
            int begPosition = beginTime.indexOf(":");
            int beginHour = Integer.valueOf(beginTime.substring(0,begPosition));
            int beginMinute = Integer.valueOf(beginTime.substring(begPosition+1));

            int endPosition = endTime.indexOf(":");
            int endHour = Integer.valueOf(endTime.substring(0,endPosition));
            int endMinute = Integer.valueOf(endTime.substring(endPosition+1));
            int interval = Integer.valueOf(intervalPref);

            Calendar rightNow = Calendar.getInstance();
            int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
            int currentMinute = rightNow.get(Calendar.MINUTE);
            if (currentMinute + interval > 60){
                currentMinute = currentMinute + interval - 60;
                currentHour++;
            } else{
                currentMinute += interval;
            }
            int currentTime = currentHour * 60 + currentMinute;
            final int midnight = 24*60;
            if (currentTime >= midnight){
                currentTime -= midnight;
                currentHour = 0;
            }

            int timeEnd = endHour*60 + endMinute;
            int timeBegin = beginHour*60 + beginMinute;

            if (currentTime < timeEnd && currentTime > timeBegin){
                setReminder(context, AlarmReceiver.class, currentHour, currentMinute);
            } else if (currentTime < timeBegin || currentTime > timeEnd) {
                if (beginMinute + interval > 60){
                    beginMinute = beginMinute + interval - 60;
                    beginHour++;
                } else {
                    beginMinute += interval;
                }
                setReminder(context, AlarmReceiver.class, beginHour, beginMinute);
            }

        } else {
            cancelReminder(context, AlarmReceiver.class);
        }
    }
}
