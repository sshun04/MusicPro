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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private int songPosition = 0;

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

        textView_title = (TextView) findViewById(R.id.title);
        textView_artist = (TextView) findViewById(R.id.artistName);
        textView_position = (TextView) findViewById(R.id.duration_left);
        textView_duration = (TextView) findViewById(R.id.duration_right);
        button_plev = (FloatingActionButton) findViewById(R.id.button_prev);
        button_next = (FloatingActionButton) findViewById(R.id.button_next);
        playButton = (FloatingActionButton) findViewById(R.id.button_play);
        artImageView = (ImageView) findViewById(R.id.trackart);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        button_next.setImageResource(R.drawable.skipnext);
        button_plev.setImageResource(R.drawable.skipprev);

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

        }


        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {

            if (state.getState() == PlaybackStateCompat.STATE_PLAYING) {
                playButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mediaController.getTransportControls().pause();
                    }
                });

                playButton.setImageResource(R.drawable.pause);
            } else {
                playButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mediaController.getTransportControls().play();
                    }
                });


                playButton.setImageResource(R.drawable.playarrow);
            }

            textView_position.setText(long2TimeString(state.getPosition()));
            seekBar.setProgress((int) state.getPosition());
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
}
