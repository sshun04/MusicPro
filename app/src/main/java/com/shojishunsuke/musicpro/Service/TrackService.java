package com.shojishunsuke.musicpro.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.shojishunsuke.musicpro.model.Track;

import java.io.IOException;

public class TrackService extends Service {
    private static String EXTRA_SONG_PATH = "song_path";

    public static void start(Context context, String path) {
        Intent intent = createIntent(context);
        intent.putExtra(EXTRA_SONG_PATH, path);
        context.startService(intent);
    }


    private static Intent createIntent(Context context) {
        return new Intent(context, TrackService.class);
    }

    public Context context;
    public String id;
    public Track track;
    public String path;
    private boolean flag = true;

    final MediaPlayer mediaPlayer = new MediaPlayer();


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("debug", "onCreate()");


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        他のトラックがタッチされたらそっちが再生されるようにしたい

        path = intent.getStringExtra(EXTRA_SONG_PATH);

        mediaPlayer.setLooping(true);

//
        if (flag) {
            setAudio();
            flag = false;
        }

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();

        } else {
            mediaPlayer.start();
        }


        return START_STICKY;
    }


    private void setAudio() {

        try {

            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onDestroy() {

        super.onDestroy();

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        stopSelf();

    }

    @NonNull
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
