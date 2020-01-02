package com.example.admin.myuom.Notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.example.admin.myuom.MainActivity;

import static android.content.ContentValues.TAG;

public class AlarmReceiver  extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                // Set the alarm here.
                Log.d(TAG, "onReceive: BOOT_COMPLETED");
               // NotificationScheduler.setReminder(context, AlarmReceiver.class, 20, 5);
                return;
            }
        }
        Log.d(TAG, "onReceive: ");
        //Trigger the notification
        NotificationScheduler.showNotification(context, MainActivity.class,
                "Έχεις μάθημα :(", "");

    }
}
