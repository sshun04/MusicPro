package com.shojishunsuke.musicpro.actvity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.shojishunsuke.musicpro.R;
import com.shojishunsuke.musicpro.Service.TrackService;
import com.shojishunsuke.musicpro.model.Track;

import java.util.List;

public class TrackDetailActivity extends AppCompatActivity {

    private static final String KEY_TRACK = "kyc_track";
    private Track track;
    private String trackPath;
    private String trackTitle;
    private String trackArtist;
    private boolean flag = false;
    private boolean isStartService = false;
    private Intent intent;
    private Uri songUri;


    public static void start(Context context, Track track) {
        Intent intent = new Intent(context, TrackDetailActivity.class);
        intent.putExtra(KEY_TRACK, track);
        context.startActivity(intent);


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_detail);

        if (savedInstanceState ==null){


        intent = getIntent();


        track = (Track) intent.getSerializableExtra(KEY_TRACK);

        trackPath = track.path;
        trackTitle = track.title;
        trackArtist = track.artist;
        songUri =track.uri;
        }





        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(trackTitle);


        TextView titleTextView = (TextView) findViewById(R.id.title);
        TextView artistTextView = (TextView) findViewById(R.id.artist);
        TextView positionTextView = (TextView) findViewById(R.id.textView_position);
        TextView durationTextView = (TextView) findViewById(R.id.textView_duration);
        ImageView artImageView = (ImageView) findViewById(R.id.trackart);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        FloatingActionButton prevButton = (FloatingActionButton) findViewById(R.id.button_prev);
        final FloatingActionButton playButton = (FloatingActionButton) findViewById(R.id.button_play);
        FloatingActionButton nextButton = (FloatingActionButton) findViewById(R.id.button_next);


        titleTextView.setText(trackTitle);
        artistTextView.setText(trackArtist);


//        long dm = track.duration / 60000;
//        long ds = (track.duration - (dm * 60000)) / 1000;
//        durationTextView.setText(String.format("%d:%02d", dm, ds));

        playButton.setImageResource(R.drawable.pause);
        prevButton.setImageResource(R.drawable.skipprev);
        nextButton.setImageResource(R.drawable.skipnext);

        artImageView.setImageResource(R.drawable.track_icon);


        checkService();


        if (isStartService && savedInstanceState ==null) {

            stopService(new Intent(TrackDetailActivity.this, TrackService.class));

            TrackService.start(TrackDetailActivity.this, trackPath, trackTitle, trackArtist);

            flag = true;

            playButton.setImageResource(R.drawable.pause);


        } else if (savedInstanceState == null){


            TrackService.start(TrackDetailActivity.this, trackPath, trackTitle, trackArtist);

            flag = true;

        }


//　　　　UIでは曲の一時停止、再生を行う

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag) {


                    TrackService.start(TrackDetailActivity.this, trackPath, trackTitle, trackArtist);

                    playButton.setImageResource(R.drawable.playarrow);

                    isStartService = false;
                    flag = false;

                } else {

                    TrackService.start(TrackDetailActivity.this, trackPath, trackTitle, trackArtist);

                    playButton.setImageResource(R.drawable.pause);

                    flag = true;

                }

            }
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("extra_song_path", trackPath);
        savedInstanceState.putString("extra_song_title", trackTitle);
        savedInstanceState.putString("extra_song_artist", trackArtist);
        savedInstanceState.putParcelable("extra_song_uri",songUri);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        trackPath = savedInstanceState.getString("extra_song_path");
        trackTitle = savedInstanceState.getString("extra_song_title");
        trackArtist = savedInstanceState.getString("extra_song_artist");
        songUri  = savedInstanceState.getParcelable("extra_song_uri");


    }

    public void checkService() {

        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> listSeriviceInfo = am.getRunningServices(Integer.MAX_VALUE);


        for (ActivityManager.RunningServiceInfo curr : listSeriviceInfo) {
            if (curr.service.getClassName().equals(TrackService.class.getName())) {
                isStartService = true;
                break;
            }
        }
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
