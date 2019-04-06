package com.shojishunsuke.musicpro.utils;

import android.content.ComponentName;
import android.content.Context;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import java.util.List;

public class MusicPlayer {
    private static MusicPlayer musicPlayer = new MusicPlayer();

    private MediaBrowserCompat mediaBrowserCompat;
    private MediaControllerCompat mediaControllerCompat;

    private UiCallback uiCallback = null;
    private ListUiCallback listUiCallback = null;
    private List<MediaBrowserCompat.MediaItem> children;

    private MediaControllerCompat.Callback controllerCallback;
    private MediaBrowserCompat.SubscriptionCallback subscriptionCallback;
    MediaBrowserCompat.ConnectionCallback connectionCallback;

    private int playingSongPosition = -1;

    private String TAG = "tag";

    private MusicPlayer() {
    }

    public static MusicPlayer getInstance() {
        return musicPlayer;
    }

    public void init(final Context context,
                     ComponentName componentName) {

        controllerCallback = new MediaControllerCompat.Callback() {
            @Override
            public void onPlaybackStateChanged(PlaybackStateCompat state) {
                if (uiCallback != null) {
                    uiCallback.onPlaybackStateChanged(state);
                } else {
                    Log.d("MusicPlayer", "uiCallbackがnull");
                }
                if (listUiCallback!= null){
                    listUiCallback.onPlaybackStateChanged(state);
                }
            }

            @Override
            public void onMetadataChanged(MediaMetadataCompat metadata) {
                if (uiCallback != null) {
                    uiCallback.onMetadataChanged(metadata);
                }
                if (listUiCallback != null){
                    listUiCallback.onMetadataChanged(metadata);
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
                if (uiCallback != null){

                    uiCallback.onRepeatModeChanged(repeatMode);

                }
            }
        };

        subscriptionCallback = new MediaBrowserCompat.SubscriptionCallback() {
            @Override
            public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children) {
                if (uiCallback != null) {
                    uiCallback.onChildrenLoaded(parentId, children);
                }

                if (listUiCallback != null) {
                    listUiCallback.onChildrenLoaded(parentId, children);
                }
            }
        };

        connectionCallback = new MediaBrowserCompat.ConnectionCallback() {
            @Override
            public void onConnected() {

                try {
                    mediaControllerCompat = new MediaControllerCompat(context, mediaBrowserCompat.getSessionToken());
                } catch (RemoteException re) {
                    re.printStackTrace();
                }

                mediaControllerCompat.registerCallback(controllerCallback);

                mediaBrowserCompat.subscribe(mediaBrowserCompat.getRoot(), subscriptionCallback);

            }
        };


        mediaBrowserCompat = new MediaBrowserCompat(context, componentName, connectionCallback, null);

    }

    public void setChildren(List<MediaBrowserCompat.MediaItem> children) {
        this.children = children;

    }

    public MediaMetadataCompat getMetaData() {
        return mediaControllerCompat.getMetadata();
    }

    public void setPlayingSongPosition(int playingSongPosition) {
        this.playingSongPosition = playingSongPosition;
    }

    public int getPlayingSongPosition() {
        return playingSongPosition;
    }

    public List<MediaBrowserCompat.MediaItem> getChildren() {
        return children;
    }

    public void setUiCallback(UiCallback uiCallback) {
        this.uiCallback = uiCallback;
    }

    public void setListUiCallback(ListUiCallback uiCallback) {
        this.listUiCallback = uiCallback;
    }

    public void play() {
        mediaControllerCompat.getTransportControls().play();
    }

    public void pause() {
        mediaControllerCompat.getTransportControls().pause();
    }

    public void playFromId(String id) {
        mediaControllerCompat.getTransportControls().playFromMediaId(id, null);
    }

    public int getRepeatMode() {
        return mediaControllerCompat.getRepeatMode();
    }

    public void setRepeatMode(int repeatMode) {
        mediaControllerCompat.getTransportControls().setRepeatMode(repeatMode);
    }

    public void skipToNext() {
        mediaControllerCompat.getTransportControls().skipToNext();
    }

    public int getState() {
        return mediaControllerCompat.getPlaybackState().getState();
    }

    public void skipToPrevious() {
        mediaControllerCompat.getTransportControls().skipToPrevious();
    }

    public void seekTo(int progress) {
        mediaControllerCompat.getTransportControls().seekTo(progress);
    }

    public void connectMediaBrowser() {
        mediaBrowserCompat.connect();
    }


    public interface UiCallback {
        void onPlaybackStateChanged(PlaybackStateCompat state);

        void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children);

        void onMetadataChanged(MediaMetadataCompat metadata);

        void onRepeatModeChanged(int repeatMode);

    }

    public interface ListUiCallback {
        void onPlaybackStateChanged(PlaybackStateCompat state);

        void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children);

        void onMetadataChanged(MediaMetadataCompat metadata);

    }


}