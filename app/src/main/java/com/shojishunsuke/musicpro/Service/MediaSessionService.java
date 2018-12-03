package com.shojishunsuke.musicpro.Service;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.MediaMetadataCompat;
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

import java.lang.annotation.Native;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class MediaSessionService extends MediaBrowserServiceCompat {

    final String TAG = MediaSessionService.class.getSimpleName();
    final String ROOT_ID = "root";

    private Handler handler;

    MediaSessionCompat mediaSession;
    AudioManager audioManager;

    int index = 0;

    ExoPlayer exoPlayer;

    List<MediaSessionCompat.QueueItem> queueItems = new ArrayList<>();

    public static void start(Context context){
        Intent intent = new Intent(context,MediaSessionService.class);
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
                super.onPlaybackStateChanged(state);


            }

            @Override
            public void onMetadataChanged(MediaMetadataCompat metadata) {
                super.onMetadataChanged(metadata);
            }
        });

//        キューにアイテムを追加

        int i = 0;
        for (MediaBrowserCompat.MediaItem mediaItem : MusicLibrary.getMediaItems()) {
            queueItems.add(new MediaSessionCompat.QueueItem(mediaItem.getDescription(), i));
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
                                 Bundle rootHints){
        Log.d(TAG,"connected from package:"+clientPackageName+"Uid:"+clientUid);
        return new BrowserRoot(ROOT_ID,null);
    }

    @Override
    public void onLoadChildren(@NonNull String parentId,
                               @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {

        if (parentId.equals(ROOT_ID)){
            result.sendResult(MusicLibrary.getMediaItems());
        }else {
            result.sendResult(new ArrayList<MediaBrowserCompat.MediaItem>());
        }

    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy");
        mediaSession.setActive(false);
        mediaSession.release();
        exoPlayer.stop();
        exoPlayer.release();

    }

    private MediaSessionCompat.Callback callback = new MediaSessionCompat.Callback() {
        @Override
        public void onPlayFromMediaId(String mediaId, Bundle extras) {

            com.google.android.exoplayer2.upstream.DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(),
                    Util.getUserAgent(getApplicationContext(),"MusicPro"));
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse("file:///android_asset/"+MusicLibrary.getMusicFileNames(mediaId)));

            for (MediaSessionCompat.QueueItem item : queueItems){
                if (item.getDescription().getMediaId().equals(mediaId)){
                    index =(int)item.getQueueId();
                }
            }

            exoPlayer.prepare(mediaSource);

            mediaSession.setActive(true);

            onPlay();

            mediaSession.setMetadata(MusicLibrary.getMetaData(getApplicationContext(),mediaId));



        }

        @Override
        public void onPlay() {
           if (audioManager.requestAudioFocus(audioFocusChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
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
           if (index>=queueItems.size()){
               index = 0;
           }
           onPlayFromMediaId(queueItems.get(index).getDescription().getMediaId(),null);
        }

        @Override
        public void onSkipToPrevious() {
           index--;
           if (index<0){
               index = queueItems.size() - 1;
           }

           onPlayFromMediaId(queueItems.get(index).getDescription().getMediaId(),null);
        }

        @Override
        public void onSkipToQueueItem(long id) {

            onPlayFromMediaId(queueItems.get((int)id).getDescription().getMediaId(),null);

        }

        @Override
        public boolean onMediaButtonEvent(Intent mediaButtonEvent) {

            KeyEvent keyEvent = mediaButtonEvent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            Log.d(TAG,String.valueOf(keyEvent.getKeyCode()));
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
                    if (focusChange  == AudioManager.AUDIOFOCUS_LOSS){
                        mediaSession.getController().getTransportControls().pause();
                    }else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT){
                        mediaSession.getController().getTransportControls().pause();
                    }else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){

                    }else if (focusChange == AudioManager.AUDIOFOCUS_GAIN){
                        mediaSession.getController().getTransportControls().play();
                    }
                }
            };
}



