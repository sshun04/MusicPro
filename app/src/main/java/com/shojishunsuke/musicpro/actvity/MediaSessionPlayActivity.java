package com.shojishunsuke.musicpro.actvity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.Player;
import com.shojishunsuke.musicpro.R;
import com.shojishunsuke.musicpro.Service.MediaSessionService;

import java.util.List;

public class MediaSessionPlayActivity extends AppCompatActivity {

    private MediaBrowserCompat mediaBrowser;
    private MediaControllerCompat mediaController;

    private TextView textView_title;
    private TextView textView_artist;
    private TextView textView_position;
    private TextView textView_duration;
    private FloatingActionButton button_plev;
    private FloatingActionButton button_next;
    private FloatingActionButton playButton;
    private ImageView artImageView;
    private SeekBar seekBar;
    private android.support.v7.widget.Toolbar toolbar;
    private ActionBar actionBar;
    private ImageView repeatButton;
    private int songPosition = -1;

    private static String POSITION_KEY = "key_position";


    public static void start(Context context, int position) {
        Intent intent = new Intent(context, MediaSessionPlayActivity.class);
        intent.putExtra(POSITION_KEY, position);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_session_play);

        Intent intent = getIntent();
        songPosition = intent.getIntExtra(POSITION_KEY, -1);

        toolbar = findViewById(R.id.mtoolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);


        textView_title = (TextView) findViewById(R.id.title);
        textView_artist = (TextView) findViewById(R.id.artistName);
        textView_position = (TextView) findViewById(R.id.duration_left);
        textView_duration = (TextView) findViewById(R.id.duration_right);
        button_plev = (FloatingActionButton) findViewById(R.id.button_prev);
        button_next = (FloatingActionButton) findViewById(R.id.button_next);
        playButton = (FloatingActionButton) findViewById(R.id.button_play);
        artImageView = (ImageView) findViewById(R.id.track_art);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        repeatButton = (ImageView) findViewById(R.id.repeatButton);


        button_next.setImageResource(R.drawable.skipnext);
        button_plev.setImageResource(R.drawable.skipprev);
        repeatButton.setImageResource(R.drawable.baseline_repeat_white_24dp);

        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mediaController.getRepeatMode()) {
                    case PlaybackStateCompat.REPEAT_MODE_NONE:
                        mediaController.getTransportControls().setRepeatMode(Player.REPEAT_MODE_ALL);
                        break;
                    case PlaybackStateCompat.REPEAT_MODE_ALL:
                        mediaController.getTransportControls().setRepeatMode(Player.REPEAT_MODE_ONE);
                        break;
                    case PlaybackStateCompat.REPEAT_MODE_ONE:
                        mediaController.getTransportControls().setRepeatMode(Player.REPEAT_MODE_OFF);
                        break;


                }
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mediaController.getPlaybackState().getState()){

                    case PlaybackStateCompat.STATE_PLAYING:
                        mediaController.getTransportControls().pause();
                        break;
                    case PlaybackStateCompat.STATE_STOPPED:
                        mediaController.getTransportControls().play();
                        break;
                    case PlaybackStateCompat.STATE_PAUSED:
                        mediaController.getTransportControls().play();
                        break;
                }
            }
        });


        button_plev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaController.getTransportControls().skipToPrevious();


            }
        });

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaController.getTransportControls().skipToNext();


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
                mediaController.getTransportControls().seekTo(seekBar.getProgress());


            }
        });


        mediaBrowser = new MediaBrowserCompat(this, new ComponentName(this, MediaSessionService.class), connectionCallback, null);
        mediaBrowser.connect();


    }


    private MediaBrowserCompat.ConnectionCallback connectionCallback = new MediaBrowserCompat.ConnectionCallback() {

        @Override
        public void onConnected() {

            try {

                mediaController = new MediaControllerCompat(MediaSessionPlayActivity.this, mediaBrowser.getSessionToken());

                mediaController.registerCallback(controllerCallback);

                if (mediaController.getPlaybackState() != null && mediaController.getPlaybackState().getState() == PlaybackStateCompat.STATE_PLAYING) {
                    controllerCallback.onMetadataChanged(mediaController.getMetadata());
                    controllerCallback.onPlaybackStateChanged(mediaController.getPlaybackState());
                }

            } catch (RemoteException ex) {
                ex.printStackTrace();
                Toast.makeText(MediaSessionPlayActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
            mediaBrowser.subscribe(mediaBrowser.getRoot(), subscriptionCallback);

        }
    };

    private MediaBrowserCompat.SubscriptionCallback subscriptionCallback = new MediaBrowserCompat.SubscriptionCallback() {
        @Override
        public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children) {

            play(children.get(songPosition).getMediaId());

        }



    };

    private MediaControllerCompat.Callback controllerCallback = new MediaControllerCompat.Callback() {
        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {

            textView_title.setText(metadata.getDescription().getTitle());
            artImageView.setImageBitmap(metadata.getDescription().getIconBitmap());
            textView_artist.setText(metadata.getDescription().getSubtitle());
            textView_duration.setText(long2TimeString(metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)));
            seekBar.setMax((int) metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION));
            actionBar.setTitle(metadata.getDescription().getTitle());


        }


        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {

            switch (state.getState()){
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
    };

    private void play(String id) {
        mediaController.getTransportControls().playFromMediaId(id, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaController.getPlaybackState().getState() != PlaybackStateCompat.STATE_PLAYING) {

        }
    }


    private String long2TimeString(long src) {

        long dm = src / 60000;
        long ds = (src - (dm * 60000)) / 1000;

        return String.format("%d:%02d", dm, ds);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }
}
