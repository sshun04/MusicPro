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
import android.widget.ListView;
import android.widget.Toast;

import com.shojishunsuke.musicpro.R;
import com.shojishunsuke.musicpro.Service.MediaSessionService;
import com.shojishunsuke.musicpro.actvity.MainActivity;
import com.shojishunsuke.musicpro.adapter.ListTrackAdapter;
import com.shojishunsuke.musicpro.utils.MusicPlayer;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;


public class TrackTabFragment extends Fragment implements MusicPlayer.UiCallback {

    public static TrackTabFragment newInstance() {
        TrackTabFragment fragment = new TrackTabFragment();
        return fragment;
    }

    private MusicPlayer musicPlayer;


    private ListTrackAdapter mBrowserAdapter;



    @Override
    public void onAttach(Context context) {


        super.onAttach(context);

        musicPlayer = MusicPlayer.getInstance();
        musicPlayer.setUiCallback(this);
        musicPlayer.init(context,new ComponentName(context,MediaSessionService.class));
        musicPlayer.connectMediaBrowser();
        Log.d("TrackTab","onAttach");




    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track_tab, container, false);

        MainActivity activity = (MainActivity) getActivity();


        ListView trackList = (ListView) view.findViewById(R.id.listTrack);
        mBrowserAdapter = new ListTrackAdapter(activity,getFragmentManager());
        trackList.setAdapter(mBrowserAdapter);

        // Inflate the layout for this fragment
        return view;
    }






    @Override
    public void onPlaybackStateChanged(PlaybackStateCompat state) {


    }

    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children) {

        mBrowserAdapter.addAll(children);
        musicPlayer.setChildren(children);

    }

    @Override
    public void onMetadataChanged(MediaMetadataCompat metadata) {

    }
}


