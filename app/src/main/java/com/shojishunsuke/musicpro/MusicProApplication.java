package com.shojishunsuke.musicpro;

import android.app.Application;
import android.content.ComponentName;

import com.shojishunsuke.musicpro.Service.MediaSessionService;
import com.shojishunsuke.musicpro.utils.MusicPlayer;

public class MusicProApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MusicPlayer musicPlayer = MusicPlayer.getInstance();
        musicPlayer.init(this, new ComponentName(this, MediaSessionService.class));


    }
}
