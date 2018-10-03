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
import android.net.Uri;
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

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TrackService extends Service{
    private static String EXTRA_SONG_URI = "song_uri";

    public static void start(Context context, Uri uri) {
        Intent intent = createIntent(context);
        intent.putExtra(EXTRA_SONG_URI, uri);
        context.startService(intent);
    }

    private static Intent createIntent(Context context) {
        return new Intent(context, TrackService.class);
    }

    public Context context;
    public String id;
    public Track track;
    public String path;
    final MediaPlayer mediaPlayer = new MediaPlayer();


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("debug","onCreate()");

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


       intent.getParcelableExtra(EXTRA_SONG_URI);

       audioStart();





        return START_STICKY;
    }

    private void audioStart() {



        if (mediaPlayer.isPlaying()) {


            mediaPlayer.stop();
            mediaPlayer.reset();

            Toast.makeText(context, "STOP", Toast.LENGTH_SHORT).show();
//          holder.trackTextView.setTextColor(Color.BLACK);

        } else {

//
            try {


                path = track.path;
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

    @Override
    public void onDestroy(){
        super.onDestroy();

        mediaPlayer.stop();

    }

    @NonNull
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
}
