package com.shojishunsuke.musicpro.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;

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

    private Context context;
    private String id;
    private String path;
    private boolean flag = true;

    AudioManager audioManager;


    final MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    public void onCreate() {
        super.onCreate();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        Log.d("debug", "onCreate()");


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        他のトラックがタッチされたらそっちが再生されるようにしたい

        path = intent.getStringExtra(EXTRA_SONG_PATH);

        mediaPlayer.setLooping(true);

        if (flag) {
            setAudio();


            flag = false;
        }

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            audioManager.abandonAudioFocus(afChangeListener);

        } else {
            audioManager.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
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

        audioManager.abandonAudioFocus(afChangeListener);

        stopSelf();

    }


    @NonNull
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {

            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS:

                    mediaPlayer.pause();
                    break;

                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:

                    mediaPlayer.pause();
                    break;

                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:

                    break;

                case AudioManager.AUDIOFOCUS_GAIN:

                    mediaPlayer.start();
            }

        }
    };


}
