package com.shojishunsuke.musicpro.Service;



import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import java.util.logging.Handler;
import java.util.logging.LogRecord;


//public class MediaSessionService extends MediaBrowserServiceCompat {
//
//    final String TAG = MediaSessionService.class.getSimpleName();
////    クライアントにかえすId
//    final String Root_ID = "root";
//
//
//    MediaSessionCompat mSession;
////    AudioFocusを使うため
//    AudioManager audioManager;
//
//    android.os.Handler handler;
//
//    int index = 0;
//
//    MediaPlayer mediaPlayer;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//
//        mSession = new MediaSessionCompat(getApplicationContext(), TAG);
//
////　　　再生、停止、スキップのコントロールを提供
//        mSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
//
//        mSession.setCallback(callback);
//
//        setSessionToken(mSession.getSessionToken());
//
//
////  通知の作　あとで実装
//        mSession.getController().registerCallback(new MediaControllerCompat.Callback() {
//            @Override
//            public void onPlaybackStateChanged(PlaybackStateCompat state) {
////             todo   CreateNotification();
//            }
//
//            @Override
//            public void onMetadataChanged(MediaMetadataCompat metadata) {
////             todo   CreateNotification();
//            }
//        });
//
//         mediaPlayer = new MediaPlayer();
//
//        final android.os.Handler handler =new android.os.Handler();
//
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (mediaPlayer.isPlaying()){
////                   曲の状態のアップデート　あとで実装
////            todo        UpdatePlayBackState();
//                }
//                handler.postDelayed(this,500);
//
//            }
//        },500);
//
//    }
//
//    @Override
//    public  BrowserRoot onGetRoot(@NonNull String clientPackageName,
//                                  int clientUid,
//                                  Bundle rootHints){
//
//        return new BrowserRoot(Root_ID,null);
//
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//
//        Log.d(TAG,"OnDestroy");
//        mSession.setActive(false);
//        mSession.release();
//        mediaPlayer.stop();
//        mediaPlayer.release();
//    }
//
//}
