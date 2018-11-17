package com.shojishunsuke.musicpro.actvity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shojishunsuke.musicpro.ImageGetTask;
import com.shojishunsuke.musicpro.R;
import com.shojishunsuke.musicpro.Service.TrackService;
import com.shojishunsuke.musicpro.adapter.ListTrackAdapter;
import com.shojishunsuke.musicpro.model.Album;
import com.shojishunsuke.musicpro.model.Track;
import com.shojishunsuke.musicpro.utils.CheckServiceUtils;

import java.util.List;


public class AlbumDetailActivity extends AppCompatActivity {

    private static final String KEY_ALBUM = "kyc_album";
    private boolean isStartService = false;

    private FloatingActionButton playButton;

    public static void start(Context context, Album album) {
        Intent intent = new Intent(context, AlbumDetailActivity.class);
        intent.putExtra(KEY_ALBUM, album);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        Intent intent = getIntent();
        Album album = (Album) intent.getSerializableExtra(KEY_ALBUM);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(album.album);


        TextView album_title = (TextView) findViewById(R.id.title);
        TextView album_artist = (TextView) findViewById(R.id.artist);
        TextView album_tracks = (TextView) findViewById(R.id.tracks);
        ImageView album_art = (ImageView) findViewById(R.id.albumart);
        ImageView devide_view_top = (ImageView) findViewById(R.id.devider1);
        ImageView devide_view_bottom = (ImageView) findViewById(R.id.devider2);

        playButton = (FloatingActionButton) findViewById(R.id.mainPlay);


        album_title.setText(album.album);
        album_artist.setText(album.artist);
        album_tracks.setText(String.valueOf(album.tracks) + "tracks");


//        アルバムの画像をセットする。

        String path = album.albumArt;
        album_art.setImageResource(R.drawable.album_blue_brack);

        if (path != null) {
            album_art.setTag(path);
            ImageGetTask task = new ImageGetTask(album_art);
            task.execute(path);
        }

        findViewById(R.id.album_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        findViewById(R.id.tracktitle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        List<Track> tracks = Track.getItemsByAlbum(this, album.albumId);

        ListView trackList = (ListView) findViewById(R.id.list);
        ListTrackAdapter adapter = new ListTrackAdapter(this, tracks);
        trackList.setAdapter(adapter);

        ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        if (activityManager != null) {
            isStartService = CheckServiceUtils.checkAudioService(activityManager);
        }

        if (isStartService) {
            playButton.setImageResource(R.drawable.pause);
        } else {
            playButton.setImageResource(R.drawable.playarrow);
        }


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityManager activityManager = (ActivityManager) AlbumDetailActivity.this.getSystemService(ACTIVITY_SERVICE);
                if (activityManager != null) {
                    isStartService = CheckServiceUtils.checkAudioService(activityManager);
                }

                AudioManager audioManager = (AudioManager)AlbumDetailActivity.this.getSystemService(AUDIO_SERVICE);

                if (audioManager.isMusicActive() && isStartService) {

                    playButton.setImageResource(R.drawable.playarrow);
                    startService(new Intent(AlbumDetailActivity.this, TrackService.class));



                } else if (isStartService) {
                    playButton.setImageResource(R.drawable.pause);
                    startService(new Intent(AlbumDetailActivity.this, TrackService.class));

                } else {

                    Toast.makeText(AlbumDetailActivity.this, "Choose Track", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        AudioManager audioManager = (AudioManager) this.getSystemService(AUDIO_SERVICE);
        if (audioManager != null) {
            if (audioManager.isMusicActive()) {
                playButton.setImageResource(R.drawable.pause);

            } else {
                playButton.setImageResource(R.drawable.playarrow);
            }
        } else {
            playButton.setImageResource(R.drawable.playarrow);
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
