package com.shojishunsuke.musicpro;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {

    public static final String CHANNEL_ID = "trackServiceChannel1";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){

            NotificationChannel trackServiceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Track Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(trackServiceChannel);

        }
    }
}
