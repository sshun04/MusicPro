package com.shojishunsuke.musicpro.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.shojishunsuke.musicpro.R;
import com.shojishunsuke.musicpro.Service.MediaSessionService;
import com.shojishunsuke.musicpro.adapter.ListTrackAdapter;

import java.util.List;

import static android.support.constraint.Constraints.TAG;


public class TrackTabFragment extends Fragment {

    public static TrackTabFragment newInstance() {
        TrackTabFragment fragment = new TrackTabFragment();
        return fragment;
    }


    private ListTrackAdapter mBrowserAdapter;
    private MediaBrowserCompat mediaBrowser;
    private Context context;
    private MediaControllerCompat mediaController;
    private List<MediaBrowserCompat.MediaItem> songList;

    private MediaControllerCompat.Callback mediaControllerCallback = new MediaControllerCompat.Callback() {
        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            super.onMetadataChanged(metadata);
            if (metadata == null) {
                return;
            }

        }

        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            super.onPlaybackStateChanged(state);
        }
    };




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track_tab, container, false);


        ListView trackList = (ListView) view.findViewById(R.id.listTrack);
        mBrowserAdapter = new ListTrackAdapter(getActivity(),songList);
        trackList.setAdapter(mBrowserAdapter);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mediaBrowser = new MediaBrowserCompat(context, new ComponentName(context, MediaSessionService.class), connectionCallback, null);
        mediaBrowser.connect();

    }

    private MediaBrowserCompat.ConnectionCallback connectionCallback = new MediaBrowserCompat.ConnectionCallback() {
        @Override
        public void onConnected() {
            try {
                mediaController = new MediaControllerCompat(context, mediaBrowser.getSessionToken());

                mediaController.registerCallback(mediaControllerCallback);
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }

            mediaBrowser.subscribe(mediaBrowser.getRoot(),subscriptionCallback);
        }
    };

    private MediaBrowserCompat.SubscriptionCallback subscriptionCallback =
            new MediaBrowserCompat.SubscriptionCallback() {


                @Override
                public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children) {
                    try {

                        for (MediaBrowserCompat.MediaItem item : children) {
                            songList.add(item);
                        }
                    } catch (Exception e) {
                        e.fillInStackTrace();

                    }

                }

                @Override
                public void onError(@NonNull String parentId) {
                    Log.d(TAG, "Error");

                }
            };



}


