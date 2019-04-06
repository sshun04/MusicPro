package com.shojishunsuke.musicpro.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shojishunsuke.musicpro.DisplayManager;
import com.shojishunsuke.musicpro.R;
import com.shojishunsuke.musicpro.Service.MediaSessionService;
import com.shojishunsuke.musicpro.actvity.MainActivity;
import com.shojishunsuke.musicpro.adapter.ListTrackAdapter;
import com.shojishunsuke.musicpro.utils.MusicPlayer;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;


public class TrackTabFragment extends Fragment implements MusicPlayer.ListUiCallback {

    public static TrackTabFragment newInstance() {
        TrackTabFragment fragment = new TrackTabFragment();
        return fragment;
    }

    private MusicPlayer musicPlayer;


    private ListTrackAdapter mBrowserAdapter;

    private ConstraintLayout songBox;
    private ImageView playButton;
    private ImageView icon;
    private TextView titleTextView;
    private TextView artistTextView;
    private  DisplayManager displayManager;



    @Override
    public void onAttach(Context context) {


        super.onAttach(context);

        musicPlayer = MusicPlayer.getInstance();
        musicPlayer.setListUiCallback(this);
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
        songBox = view.findViewById(R.id.footerTitleBox);
        titleTextView = view.findViewById(R.id.footerTitleText);
        artistTextView = view.findViewById(R.id.footerArtistTextView);
        icon = view.findViewById(R.id.footerIcon);
        playButton = view.findViewById(R.id.footerPlay);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (musicPlayer.getState()){
                    case PlaybackStateCompat.STATE_PLAYING:
                        musicPlayer.pause();
                        break;
                    case PlaybackStateCompat.STATE_STOPPED:
                        musicPlayer.play();
                        break;

                    case PlaybackStateCompat.STATE_PAUSED:
                        musicPlayer.play();
                        break;
                }

            }
        });

        displayManager = DisplayManager.getInstance();



        songBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (displayManager.isPlayed()){
                displayManager.showPlayTab();
                displayManager.hideList();}
            }
        });




        mBrowserAdapter = new ListTrackAdapter(activity,getFragmentManager());
        trackList.setAdapter(mBrowserAdapter);

        // Inflate the layout for this fragment
        return view;
    }



    @Override
    public void onPlaybackStateChanged(PlaybackStateCompat state) {
        switch (state.getState()){
            case PlaybackStateCompat.STATE_PLAYING:
                playButton.setImageResource(R.drawable.baseline_pause_white_24dp);
                break;
            case PlaybackStateCompat.STATE_STOPPED:
                playButton.setImageResource(R.drawable.baseline_play_arrow_white_36dp);
                break;
            case PlaybackStateCompat.STATE_PAUSED:
                playButton.setImageResource(R.drawable.baseline_play_arrow_white_36dp);
                break;
        }


    }

    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children) {

        mBrowserAdapter.addAll(children);
        musicPlayer.setChildren(children);

    }

    @Override
    public void onMetadataChanged(MediaMetadataCompat metadata) {
        titleTextView.setText(metadata.getDescription().getTitle());
        artistTextView.setText(metadata.getDescription().getSubtitle());
        icon.setImageBitmap(metadata.getDescription().getIconBitmap());


    }
}


