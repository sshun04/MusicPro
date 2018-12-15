package com.shojishunsuke.musicpro.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.KeyEvent;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.shojishunsuke.musicpro.Library.MusicLibrary;
import com.shojishunsuke.musicpro.R;
import com.shojishunsuke.musicpro.actvity.MediaSessionPlayActivity;

import java.util.ArrayList;
import java.util.List;

public class MediaSessionService extends MediaBrowserServiceCompat {

    final String TAG = MediaSessionService.class.getSimpleName();
    final String ROOT_ID = "root";
    private Context context;

    private Handler handler;

    MediaSessionCompat mediaSession;
    AudioManager audioManager;

    MusicLibrary musicLibrary;

    int index = 0;

    ExoPlayer exoPlayer;

    List<MediaSessionCompat.QueueItem> queueItems = new ArrayList<>();

    public static void start(Context context) {
        Intent intent = new Intent(context, MediaSessionService.class);
        context.startService(intent);
    }


    @Override
    public void onCreate() {
        super.onCreate();

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        mediaSession = new MediaSessionCompat(getApplicationContext(), TAG);

        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mediaSession.setCallback(callback);

        setSessionToken(mediaSession.getSessionToken());

//通知の作成、更新
        mediaSession.getController().registerCallback(new MediaControllerCompat.Callback() {
            @Override
            public void onPlaybackStateChanged(PlaybackStateCompat state) {
                CreateNotification();

            }

            @Override
            public void onMetadataChanged(MediaMetadataCompat metadata) {
                CreateNotification();
            }
        });

//        キューにアイテムを追加
        musicLibrary = new MusicLibrary(this);

        int i = 0;

        for (MediaBrowserCompat.MediaItem mediaItem : musicLibrary.getMediaItems()) {
            queueItems.add(new MediaSessionCompat.QueueItem(mediaItem.getDescription(), i));

            i++;
        }
        mediaSession.setQueue(queueItems);


//        ExoPlayerの初期化
        exoPlayer = ExoPlayerFactory.newSimpleInstance(getApplicationContext(), new DefaultTrackSelector());

        exoPlayer.addListener(eventListener);

        handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (exoPlayer.getPlaybackState() == Player.STATE_READY && exoPlayer.getPlayWhenReady()) {
                    UpdatePlayBackState();
                }

                handler.postDelayed(this, 500);
            }
        }, 500);


    }

    @Override
    public BrowserRoot onGetRoot(@Nullable String clientPackageName,
                                 int clientUid,
                                 Bundle rootHints) {
        Log.d(TAG, "connected from package:" + clientPackageName + "Uid:" + clientUid);
        return new BrowserRoot(ROOT_ID, null);
    }

    @Override
    public void onLoadChildren(@NonNull String parentId,
                               @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {

        if (parentId.equals(ROOT_ID)) {
            result.sendResult(musicLibrary.getMediaItems());
        } else {
            result.sendResult(new ArrayList<MediaBrowserCompat.MediaItem>());
        }

    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        mediaSession.setActive(false);
        mediaSession.release();
        exoPlayer.stop();
        exoPlayer.release();

    }

    private MediaSessionCompat.Callback callback = new MediaSessionCompat.Callback() {
        @Override
        public void onPlayFromMediaId(String mediaId, Bundle extras) {

            com.google.android.exoplayer2.upstream.DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(),
                    Util.getUserAgent(getApplicationContext(), "MusicPro"));
//            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse("file:///android_asset/" + MusicLibrary.getMusicFileNames(mediaId)));
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(musicLibrary.getMusicFileNames(mediaId));

            for (MediaSessionCompat.QueueItem item : queueItems) {
                if (item.getDescription().getMediaId().equals(mediaId)) {
                    index = (int) item.getQueueId();
                }
            }

            exoPlayer.prepare(mediaSource);

            mediaSession.setActive(true);

            onPlay();

            mediaSession.setMetadata(musicLibrary.getMetaData(getApplicationContext(), mediaId));


        }

        @Override
        public void onPlay() {
            if (audioManager.requestAudioFocus(audioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                mediaSession.setActive(true);
                exoPlayer.setPlayWhenReady(true);
            }
        }

        @Override
        public void onPause() {
            exoPlayer.setPlayWhenReady(false);
            audioManager.abandonAudioFocus(audioFocusChangeListener);
        }

        @Override
        public void onStop() {
            onPause();
            mediaSession.setActive(false);

            audioManager.abandonAudioFocus(audioFocusChangeListener);
        }

        @Override
        public void onSeekTo(long pos) {
            exoPlayer.seekTo(pos);
        }


        @Override
        public void onSkipToNext() {
            index++;
            if (index >= musicLibrary.getMediaItems().size())
                index = 0;

            onPlayFromMediaId(queueItems.get(index).getDescription().getMediaId(), null);
        }

        @Override
        public void onSkipToPrevious() {
            index--;
            if (index < 0)
                index = queueItems.size() - 1;


            onPlayFromMediaId(queueItems.get(index).getDescription().getMediaId(), null);
        }

        @Override
        public void onSkipToQueueItem(long id) {

            onPlayFromMediaId(queueItems.get((int) id).getDescription().getMediaId(), null);

        }

        @Override
        public boolean onMediaButtonEvent(Intent mediaButtonEvent) {

            KeyEvent keyEvent = mediaButtonEvent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            Log.d(TAG, String.valueOf(keyEvent.getKeyCode()));
            return super.onMediaButtonEvent(mediaButtonEvent);
        }
    };

    private Player.EventListener eventListener = new Player.DefaultEventListener() {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            UpdatePlayBackState();
        }
    };


    private void UpdatePlayBackState() {
        int state = PlaybackStateCompat.STATE_NONE;

        switch (exoPlayer.getPlaybackState()) {
            case Player.STATE_IDLE:
                state = PlaybackStateCompat.STATE_NONE;
                break;
            case Player.STATE_BUFFERING:
                state = PlaybackStateCompat.STATE_BUFFERING;
                break;
            case Player.STATE_READY:
                if (exoPlayer.getPlayWhenReady()) {
                    state = PlaybackStateCompat.STATE_PLAYING;
                } else {
                    state = PlaybackStateCompat.STATE_PAUSED;
                }
                break;
            case Player.STATE_ENDED:
                state = PlaybackStateCompat.STATE_STOPPED;
                break;

        }


        mediaSession.setPlaybackState(new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PAUSE | PlaybackStateCompat.ACTION_SKIP_TO_NEXT |
                        PlaybackStateCompat.ACTION_STOP).setState(state, exoPlayer.getCurrentPosition(), exoPlayer.getPlaybackParameters().speed)
                .build());
    }

    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        mediaSession.getController().getTransportControls().pause();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                        mediaSession.getController().getTransportControls().pause();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {

                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        mediaSession.getController().getTransportControls().play();
                    }
                }
            };

    private void CreateNotification() {
        MediaControllerCompat mediaController = mediaSession.getController();
        MediaMetadataCompat mediaMetadata = mediaController.getMetadata();

        if (mediaMetadata == null && !mediaSession.isActive()) return;

        MediaDescriptionCompat description = mediaMetadata.getDescription();

        String NOTIFICATION_CHANNEL_ID = "com.shojishunsuke.musicpro";
        String channelName = "My Background Service";

        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName,
                NotificationManager.IMPORTANCE_NONE);

        channel.setLightColor(Color.BLUE);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.createNotificationChannel(channel);


        android.support.v4.app.NotificationCompat.Builder builder = new android.support.v4.app.NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        builder.setOngoing(true)
                .setContentTitle(description.getTitle())
                .setContentText(description.getSubtitle())
                .setSubText(description.getDescription())
                .setLargeIcon(description.getIconBitmap())

                .setContentIntent(createContentIntent())
                .setDeleteIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(this,
                        PlaybackStateCompat.ACTION_STOP))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

                .setColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSession.getSessionToken())
                        .setShowActionsInCompactView(1));

        if (mediaController.getPlaybackState().getState() == PlaybackStateCompat.STATE_PLAYING) {
            builder.addAction(new NotificationCompat.Action(
                    R.drawable.pause, "pause",
                    MediaButtonReceiver.buildMediaButtonPendingIntent(this,
                            PlaybackStateCompat.ACTION_PAUSE)
            ));
        } else {
            builder.addAction(new NotificationCompat.Action(
                    R.drawable.playarrow, "play",
                    MediaButtonReceiver.buildMediaButtonPendingIntent(this,
                            PlaybackStateCompat.ACTION_PLAY)
            ));
        }

        builder.addAction(new NotificationCompat.Action(
                R.drawable.skipnext, "next",
                MediaButtonReceiver.buildMediaButtonPendingIntent(this,
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT)));

        startForeground(1, builder.build());

        if (mediaController.getPlaybackState().getState() != PlaybackStateCompat.STATE_PLAYING)
            stopForeground(false);
    }

    private PendingIntent createContentIntent() {
        Intent openUI = new Intent(this, MediaSessionPlayActivity.class);
        openUI.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(
                this, 1, openUI, PendingIntent.FLAG_CANCEL_CURRENT
        );
    }
}




