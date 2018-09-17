package com.example.shojishunsuke.musicpro.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.shojishunsuke.musicpro.R;
import com.example.shojishunsuke.musicpro.adapter.ListTrackAdapter;
import com.example.shojishunsuke.musicpro.model.Track;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TrackService extends Service{

    private MediaPlayer mediaPlayer ;
    private int counter = 0;
    public Context context;
    public String id;
    Track track;


    public TrackService(Context context,Track item){
        this.context = context;
        this.track = new Track();

    }


    @Override
    public void onCreate() {
        super.onCreate();


        Log.d("debug","nCreate()");

        final MediaPlayer mediaPlayer = new MediaPlayer();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Log.d("debug","onStartCommand");

        int requestCode = intent.getIntExtra("REQUEST_CODE",0);
        Context context = getApplicationContext();
        String channelId = "default";
        String title = context.getString(R.string.app_name);

        PendingIntent pendingIntent=
                PendingIntent.getActivity(context,requestCode,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channel = new NotificationChannel(
                channelId,title,NotificationManager.IMPORTANCE_DEFAULT
        );

        if (notificationManager == null){
            notificationManager.createNotificationChannel(channel);

            Notification notification = new Notification.Builder(context,channelId)
                    .setContentTitle(title)
                    .setSmallIcon(R.drawable.back)
                    .setContentText("MusicPro")
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .build();


            startForeground(1,notification);


            audioStart();






        }


        return super.onStartCommand(intent, flags, startId);


    }

    private void audioStart() {


        counter++;




        if (mediaPlayer.isPlaying()) {


            mediaPlayer.stop();
            mediaPlayer.reset();

            Toast.makeText(context, "STOP", Toast.LENGTH_SHORT).show();
//          holder.trackTextView.setTextColor(Color.BLACK);

        } else {
            try {
                mediaPlayer.setDataSource(track.path);
                mediaPlayer.prepare();

            } catch (IOException e) {
                e.printStackTrace();
            }

            mediaPlayer.start();
            Toast.makeText(context, "PLAY", Toast.LENGTH_SHORT).show();
//            holder.trackTextView.setTextColor(Color.BLUE);
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d("debug","end of audio");
                mediaPlayer.stop();
                mediaPlayer.reset();

//                holder.trackTextView.setTextColor(Color.BLACK);
                Toast.makeText(context,"End",Toast.LENGTH_SHORT).show();
            }
        });

    }
    @NonNull
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
}
