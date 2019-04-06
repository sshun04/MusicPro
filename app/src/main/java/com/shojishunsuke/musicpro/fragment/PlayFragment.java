package com.shojishunsuke.musicpro.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.exoplayer2.Player;
import com.shojishunsuke.musicpro.R;
import com.shojishunsuke.musicpro.utils.MusicPlayer;

import java.util.List;

public class PlayFragment extends Fragment implements MusicPlayer.UiCallback {

    private int songPosition = -1;

    private MusicPlayer musicPlayer;

    private TextView textView_title;
    private TextView textView_artist;
    private TextView textView_position;
    private TextView textView_duration;
    private FloatingActionButton button_plev;
    private FloatingActionButton button_next;
    private FloatingActionButton playButton;
    private ImageView artImageView;
    private SeekBar seekBar;
    private ImageView repeatButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_play, container, false);
        if (getArguments() != null)
            songPosition = getArguments().getInt("songPosition");

        textView_title = mView.findViewById(R.id.title);
        textView_artist = mView.findViewById(R.id.artistName);
        textView_position = mView.findViewById(R.id.duration_left);
        textView_duration = mView.findViewById(R.id.duration_right);
        button_plev = mView.findViewById(R.id.button_prev);
        button_next = mView.findViewById(R.id.button_next);
        playButton = mView.findViewById(R.id.button_play);
        artImageView = mView.findViewById(R.id.track_art);
        seekBar = mView.findViewById(R.id.seekBar);
        repeatButton = mView.findViewById(R.id.repeatButton);

        textView_title.setFocusable(true);
        textView_title.setHorizontallyScrolling(true);


        button_next.setImageResource(R.drawable.skipnext);
        button_plev.setImageResource(R.drawable.skipprev);
        repeatButton.setImageResource(R.drawable.baseline_repeat_white_24dp);


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (musicPlayer.getState()) {
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

        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (musicPlayer.getRepeatMode()) {
                    case PlaybackStateCompat.REPEAT_MODE_NONE:
                        musicPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
                        break;
                    case PlaybackStateCompat.REPEAT_MODE_ALL:
                        musicPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
                        break;

                    case PlaybackStateCompat.REPEAT_MODE_ONE:
                        musicPlayer.setRepeatMode(Player.REPEAT_MODE_OFF);
                        break;
                }
            }
        });

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayer.skipToNext();
            }
        });


        button_plev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayer.skipToPrevious();

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                musicPlayer.seekTo(seekBar.getProgress());

            }
        });


        musicPlayer = MusicPlayer.getInstance();
        musicPlayer.setUiCallback(this);
//        musicPlayer.init(getContext(),new ComponentName(getContext(),MediaSessionService.class));
        MediaBrowserCompat.MediaItem song = musicPlayer.getChildren().get(songPosition);
        musicPlayer.playFromId(song.getMediaId());


        return mView;
    }

    @Override
    public void onPlaybackStateChanged(PlaybackStateCompat state) {

        switch (state.getState()) {
            case PlaybackStateCompat.STATE_PLAYING:
                playButton.setImageResource(R.drawable.pause);
                break;
            case PlaybackStateCompat.STATE_STOPPED:
                playButton.setImageResource(R.drawable.playarrow);
                break;
            case PlaybackStateCompat.STATE_PAUSED:
                playButton.setImageResource(R.drawable.playarrow);
                break;
        }

        textView_position.setText(long2TimeString(state.getPosition()));
        seekBar.setProgress((int) state.getPosition());


    }


    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children) {

    }

    @Override
    public void onMetadataChanged(MediaMetadataCompat metadata) {


        textView_title.setText(metadata.getDescription().getTitle());
        artImageView.setImageBitmap(metadata.getDescription().getIconBitmap());
        textView_artist.setText(metadata.getDescription().getSubtitle());
        textView_duration.setText(long2TimeString(metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)));
        seekBar.setMax((int) metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION));


    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

        switch (repeatMode) {
            case Player.REPEAT_MODE_OFF:
                repeatButton.setImageResource(R.drawable.baseline_repeat_white_24dp);
                break;
            case Player.REPEAT_MODE_ONE:
                repeatButton.setImageResource(R.drawable.repeat_one);
                break;
            case Player.REPEAT_MODE_ALL:
                repeatButton.setImageResource(R.drawable.baseline_repeat_blue);
                break;
        }

    }

    private String long2TimeString(long src) {

        long dm = src / 60000;
        long ds = (src - (dm * 60000)) / 1000;

        return String.format("%d:%02d", dm, ds);

    }
}
