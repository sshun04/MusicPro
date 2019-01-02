package com.shojishunsuke.musicpro.fragment;

import android.content.Context;
import android.os.Bundle;
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

import com.shojishunsuke.musicpro.Interface.MediaBrowserProvider;
import com.shojishunsuke.musicpro.Interface.MediaFragmentListener;
import com.shojishunsuke.musicpro.R;
import com.shojishunsuke.musicpro.adapter.ListTrackAdapter;

import java.util.List;

import static android.support.constraint.Constraints.TAG;


public class TrackTabFragment extends Fragment {

    public static TrackTabFragment newInstance() {
        TrackTabFragment fragment = new TrackTabFragment();
        return fragment;
    }

    private static final String ARG_MEDIA_ID = "media_id";

    private ListTrackAdapter mBrowserAdapter;
    private String mMediaId;
    private MediaFragmentListener mediaFragmentListener;
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

    private MediaBrowserCompat.SubscriptionCallback subscriptionCallback =
            new MediaBrowserCompat.SubscriptionCallback() {
                @Override
                public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children) {
                    try {
                            for (MediaBrowserCompat.MediaItem item : children) {
                               songList.add(item);
                            }
                        } catch (Exception e) {

                    }

                }

                @Override
                public void onError(@NonNull String parentId) {
                    Log.d(TAG, "Error");

                }
            };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track_tab, container, false);


        mBrowserAdapter = new ListTrackAdapter(getActivity(),songList);

        ListView trackList = (ListView) view.findViewById(R.id.listTrack);
        trackList.setAdapter(mBrowserAdapter);
        trackList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MediaBrowserCompat.MediaItem item = mBrowserAdapter.getItem(position);
                mediaFragmentListener.onMediaItemSelcted(item);
            }
        });


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        MediaBrowserCompat mediaBrowserCompat = mediaFragmentListener.getMediaBrowser();
        if (mediaBrowserCompat.isConnected()) {

            onConnected();

        }
    }

//    public void setMediaId(String mediaId) {
//        Bundle args = new Bundle(1);
//        args.putString(TrackTabFragment.ARG_MEDIA_ID, mediaId);
//        setArguments(args);
//    }

    public String getMediaId() {
        Bundle args = getArguments();
        if (args != null) {

            return args.getString(ARG_MEDIA_ID);
        }

        return null;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mediaFragmentListener = null;
    }

    private void onConnected() {

        if (isDetached()) {
            return;
        }
        mMediaId = getMediaId();

        if (mMediaId == null) {
            mMediaId = mediaFragmentListener.getMediaBrowser().getRoot();
        }

        mediaFragmentListener.getMediaBrowser().unsubscribe(mMediaId);


        mediaFragmentListener.getMediaBrowser().subscribe(mMediaId,subscriptionCallback);

    }


}


