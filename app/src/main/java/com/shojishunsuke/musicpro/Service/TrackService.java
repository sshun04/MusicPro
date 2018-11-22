package com.shojishunsuke.musicpro.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
//import android.support.v4.media.app.NotificationCompat;
import android.support.v4.media.app.NotificationCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.shojishunsuke.musicpro.R;
import com.shojishunsuke.musicpro.actvity.AlbumDetailActivity;
import com.shojishunsuke.musicpro.actvity.MainActivity;
import com.shojishunsuke.musicpro.actvity.TrackDetailActivity;

import static com.shojishunsuke.musicpro.App.CHANNEL_ID;

import java.io.IOException;

public class TrackService extends Service {
    private static String EXTRA_SONG_PATH = "song_path";
    private static String EXTRA_SONG_TITLE = "song_title";

    public static void start(Context context, String path,String title) {
        Intent intent = createIntent(context);
        intent.putExtra(EXTRA_SONG_PATH, path);
        intent.putExtra(EXTRA_SONG_TITLE,title);
        context.startService(intent);
    }

    private static Intent createIntent(Context context) {
        return new Intent(context, TrackService.class);
    }

    private String trackPath;
    private String trackTitle;

    private boolean isAudioSet = false;

    private AudioManager audioManager;


    private final MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    public void onCreate() {
        super.onCreate();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        Log.d("debug", "onCreate()");


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        trackPath = intent.getStringExtra(EXTRA_SONG_PATH);
        trackTitle = intent.getStringExtra(EXTRA_SONG_TITLE);

        mediaPlayer.setLooping(true);

        if (!isAudioSet) {
            setAudio();

            isAudioSet = true;
        }

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            audioManager.abandonAudioFocus(afChangeListener);

        } else {
            audioManager.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            mediaPlayer.start();

        }

        createNotification();

        return START_STICKY;
    }


    private void setAudio() {

        try {

            mediaPlayer.setDataSource(trackPath);
            mediaPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onDestroy() {

        mediaPlayer.stop();
        mediaPlayer.release();

        audioManager.abandonAudioFocus(afChangeListener);

        stopSelf();

        super.onDestroy();

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
                    break;
            }

        }
    };

    public void createNotification() {

        Intent notificationIntent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);

        android.support.v4.app.NotificationCompat.Builder builder = new android.support.v4.app.NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle(trackTitle)
                .setContentText("YES")
                .setSmallIcon(R.drawable.track_icon)
                .setContentIntent(pendingIntent);

        if (audioManager.isMusicActive()){
            builder.addAction(new android.support.v4.app.NotificationCompat.Action(R.drawable.pause,"pause",
                    MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_PAUSE))
            );
        }else {
            builder.addAction(new android.support.v4.app.NotificationCompat.Action(R.drawable.playarrow,"play",
                    MediaButtonReceiver.buildMediaButtonPendingIntent(this,PlaybackStateCompat.ACTION_PLAY))
            );
        }

        startForeground(1,builder.build());

        if (!audioManager.isMusicActive()){
            stopForeground(false);
        }


    }


}
