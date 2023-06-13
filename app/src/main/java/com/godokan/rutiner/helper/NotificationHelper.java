package com.godokan.rutiner.helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.godokan.rutiner.R;

public class NotificationHelper extends ContextWrapper {
    private static final String CHANNEL_ID = "ID_Rutin";
    private static final String CHANNEL_NAME = "CH_Rutin";
    private NotificationManager notificationManager;

    public NotificationHelper(Context context) {
        super(context);
        createChannel();
    }
    public void createChannel(){
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getNotificationManager().createNotificationChannel(channel);

    }

    public NotificationManager getNotificationManager() {
        if (notificationManager == null)
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        return notificationManager;
    }


    public NotificationCompat.Builder getChannel1Notification(String title, String message){
        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_foreground);
    }
}
