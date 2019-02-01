package com.shojishunsuke.musicpro.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.shojishunsuke.musicpro.R;
import com.shojishunsuke.musicpro.Service.MediaSessionService;
import com.shojishunsuke.musicpro.actvity.MainActivity;
import com.shojishunsuke.musicpro.adapter.ListTrackAdapter;
import com.shojishunsuke.musicpro.utils.MusicPlayer;

import java.util.ArrayList;
import java.util.List;


public class TrackTabFragment extends Fragment implements MusicPlayer.UiCallback {

    public static TrackTabFragment newInstance() {
        TrackTabFragment fragment = new TrackTabFragment();
        return fragment;
    }


    private ListTrackAdapter mBrowserAdapter;
//    private MediaBrowserCompat mediaBrowser;
//    private MediaControllerCompat mediaController;
    private List<MediaBrowserCompat.MediaItem> songList = new ArrayList<>();

    private MusicPlayer hoge;

//    private MediaControllerCompat.Callback mediaControllerCallback = new MediaControllerCompat.Callback() {
//        @Override
//        public void onMetadataChanged(MediaMetadataCompat metadata) {
//            if (metadata == null) {
//                return;
//            }
//
//        }
//
//        @Override
//        public void onPlaybackStateChanged(PlaybackStateCompat state) {
//            super.onPlaybackStateChanged(state);
//        }
//    };

    @Override
    public void onAttach(Context context) {


        super.onAttach(context);


//        mediaBrowser = new MediaBrowserCompat(context, new ComponentName(context, MediaSessionService.class), connectionCallback, null);
//        mediaBrowser.connect();

        hoge = MusicPlayer.getInstance();
        hoge.setUiCallback(this);

        hoge.connectMediaBrowser();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track_tab, container, false);

        MainActivity activity = (MainActivity) getActivity();


        ListView trackList = (ListView) view.findViewById(R.id.listTrack);
        mBrowserAdapter = new ListTrackAdapter(activity);
        trackList.setAdapter(mBrowserAdapter);

        // Inflate the layout for this fragment
        return view;
    }


//    private MediaBrowserCompat.ConnectionCallback connectionCallback = new MediaBrowserCompat.ConnectionCallback() {
//        @Override
//        public void onConnected() {
//            try {
//                mediaController = new MediaControllerCompat(getContext(), mediaBrowser.getSessionToken());
//
//                mediaController.registerCallback(mediaControllerCallback);
//            } catch (RemoteException ex) {
//                ex.printStackTrace();
//                Toast.makeText(getContext(),ex.getMessage(),Toast.LENGTH_SHORT).show();
//            }
//
//            mediaBrowser.subscribe(mediaBrowser.getRoot(), subscriptionCallback);
//        }
//    };

//    private MediaBrowserCompat.SubscriptionCallback subscriptionCallback =
//            new MediaBrowserCompat.SubscriptionCallback() {


//                @Override
//                public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children) {
//                    try {
//
//                            mBrowserAdapter.addAll(children);
//
//
//
//                    } catch (IllegalStateException ex) {
//                        ex.printStackTrace();
//                        Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }

//                @Override
//                public void onError(@NonNull String parentId) {
//                    Log.d(TAG, "Error");
//
//                }
//            };


    @Override
    public void onPlaybackStateChanged(PlaybackStateCompat state) {

    }

    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children) {

        try {

            mBrowserAdapter.addAll(children);



        } catch (IllegalStateException ex) {
            ex.printStackTrace();
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMetadataChanged(MediaMetadataCompat metadata) {
        if (metadata == null) {
            return;
        }
    }
}


