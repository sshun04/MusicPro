package com.shojishunsuke.musicpro.Interface;

import android.support.v4.media.MediaBrowserCompat;

public interface MediaFragmentListener extends MediaBrowserProvider {

    void onMediaItemSelcted(MediaBrowserCompat.MediaItem item);

    void setToolbarTitle(CharSequence title);
}

