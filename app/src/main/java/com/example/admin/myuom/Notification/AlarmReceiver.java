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
        //Trigger the notification
        NotificationScheduler.showNotification(context, MainActivity.class,
                "Έχεις μάθημα :(", "");
    }
}
