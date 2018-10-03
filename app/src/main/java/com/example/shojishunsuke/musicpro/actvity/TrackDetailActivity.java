package com.example.shojishunsuke.musicpro.actvity;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shojishunsuke.musicpro.R;
import com.example.shojishunsuke.musicpro.Service.TrackService;
import com.example.shojishunsuke.musicpro.model.Album;
import com.example.shojishunsuke.musicpro.model.Track;

import java.io.IOException;
import java.net.URI;

public class TrackDetailActivity extends AppCompatActivity {

    public static final String KEY_TRACK = "kyc_track";
    Context context;
    private Track track;
    private Uri uri;
    private String path;

    public static void start(Context context, Track track) {

        Intent intent = new Intent(context, TrackDetailActivity.class);
        intent.putExtra(KEY_TRACK, track);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_detail);


        final Intent intent = getIntent();
        track = (Track) intent.getSerializableExtra(KEY_TRACK);
        uri = track.uri;


        TextView titleTextView = (TextView) findViewById(R.id.title);
        TextView artistTextView = (TextView) findViewById(R.id.artist);
        TextView positionTextView = (TextView) findViewById(R.id.textView_position);
        TextView durationTextView = (TextView) findViewById(R.id.textView_duration);
        ImageView artImageView = (ImageView) findViewById(R.id.trackart);
        FloatingActionButton prevButton = (FloatingActionButton) findViewById(R.id.button_prev);
        FloatingActionButton playButton = (FloatingActionButton) findViewById(R.id.button_play);
        final FloatingActionButton nextButton = (FloatingActionButton) findViewById(R.id.button_next);


        titleTextView.setText(track.title);
        artistTextView.setText(track.artist);

        long dm = track.duration / 60000;
        long ds = (track.duration - (dm * 60000)) / 1000;
        durationTextView.setText(String.format("%d:%02d", dm, ds));

        artImageView.setImageResource(R.drawable.musicicon);
        playButton.setImageResource(R.drawable.play);
        prevButton.setImageResource(R.drawable.skipprev);
        nextButton.setImageResource(R.drawable.skipnext);


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TrackService.start(TrackDetailActivity.this,uri);


            }
        });


    }


}
