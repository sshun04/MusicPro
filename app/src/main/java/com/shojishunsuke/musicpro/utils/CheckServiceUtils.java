package com.shojishunsuke.musicpro.utils;

import android.app.ActivityManager;

import com.shojishunsuke.musicpro.Service.TrackService;

import java.util.List;


public class CheckServiceUtils {
    private CheckServiceUtils() {

    }

    public static boolean checkAudioService(ActivityManager activityManager) {

        List<ActivityManager.RunningServiceInfo> listServiceInfo = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo curr : listServiceInfo) {
            if (curr.service.getClassName().equals(TrackService.class.getName())) {
                return true;
            }
        }

        return false;
    }
}
