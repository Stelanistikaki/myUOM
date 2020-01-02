package com.example.admin.myuom.Notification;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import com.example.admin.myuom.R;

import java.util.Calendar;

import androidx.core.app.NotificationCompat;

import static android.content.Context.ALARM_SERVICE;

public class NotificationScheduler
{
    private static AlarmManager am;
    private static int REQUEST_CODE ;

    public void setReminder(Context context, Class<?> cls, int hour, int min, int requestCode)
    {
        REQUEST_CODE = requestCode;
        Calendar calendar = Calendar.getInstance();
        Calendar setcalendar = Calendar.getInstance();
        setcalendar.set(Calendar.HOUR_OF_DAY, hour);
        setcalendar.set(Calendar.MINUTE, min);
        setcalendar.set(Calendar.SECOND, 0);

        //if the time has passed no notification again
        if(setcalendar.before(calendar))
            setcalendar.add(Calendar.DATE,1);

        // Enable a receiver
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);


        Intent intent1 = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent1, 0);
        am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        //sync the alarm according to the device
        SyncAlarm(pendingIntent, setcalendar.getTimeInMillis());

    }

    private void SyncAlarm(PendingIntent pendingIntent, long time) {
        if (Build.VERSION.SDK_INT >= 23) {
            // Wakes up the device in Doze Mode
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= 19) {
            // Wakes up the device in Idle Mode
            am.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        } else {
            // Old APIs
            am.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        }
    }

    public static void showNotification(Context context, Class<?> cls, String title, String content)
    {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent notificationIntent = new Intent(context, cls);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String channelId = String.valueOf(R.string.default_notification_channel_id);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(cls);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent moovitIntent = new Intent(context, MoovitApp.class);
        PendingIntent moovitPendingIntent = PendingIntent.getActivity(context, REQUEST_CODE, moovitIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);
        NotificationCompat.Builder notification = builder.setContentTitle(title)
                .setChannelId(channelId)
                .setSound(alarmSound)
                .setSmallIcon(R.drawable.uomlogo)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_menu_send, "Πάμε με Moovit", moovitPendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, notification.build());

    }
}
