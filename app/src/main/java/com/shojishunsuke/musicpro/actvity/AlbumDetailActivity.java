package com.shojishunsuke.musicpro.actvity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
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

import java.util.List;


public class AlbumDetailActivity extends AppCompatActivity {

    private static final String KEY_ALBUM = "kyc_album";
    public boolean isStartService = false;
    public boolean isAudioPause = false;

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

        final FloatingActionButton playButton = (FloatingActionButton) findViewById(R.id.mainPlay);


        album_title.setText(album.album);
        album_artist.setText(album.artist);
        album_tracks.setText(String.valueOf(album.tracks) + "tracks");


//        アルバムの画像をセットする。

        String path = album.albumArt;
        album_art.setImageResource(R.drawable.backsub);

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

        audioCheck();

        if (isStartService) {
            playButton.setImageResource(R.drawable.pause);
        } else {
            playButton.setImageResource(R.drawable.playarrow);
        }


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                audioCheck();

                if (isAudioPause && isStartService) {

                    playButton.setImageResource(R.drawable.pause);
                    startService(new Intent(AlbumDetailActivity.this, TrackService.class));
                    isAudioPause = false;


                } else if (isStartService) {
                    playButton.setImageResource(R.drawable.playarrow);
                    startService(new Intent(AlbumDetailActivity.this, TrackService.class));
                    isAudioPause = true;
                } else {
                    isAudioPause = false;
                    Toast.makeText(AlbumDetailActivity.this, "Choose Track", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    public void audioCheck() {
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> listSeriviceInfo = am.getRunningServices(Integer.MAX_VALUE);


        for (ActivityManager.RunningServiceInfo curr : listSeriviceInfo) {
            if (curr.service.getClassName().equals(TrackService.class.getName())) {
                isStartService = true;
                break;
            }
        }
    }
}
