package com.shojishunsuke.musicpro.actvity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.shojishunsuke.musicpro.R;
import com.shojishunsuke.musicpro.model.Artist;
import com.shojishunsuke.musicpro.model.Track;

import java.util.List;

public class ArtistDetailActivity extends AppCompatActivity {

    public static final String KEY_ARTIST = "kec_artist";

    public static void start(Context context, Artist artist) {
        Intent intent = new Intent(context, ArtistDetailActivity.class);
        intent.putExtra(KEY_ARTIST, artist);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_detail);

        Intent intent = getIntent();
        Artist artist = (Artist) intent.getSerializableExtra(KEY_ARTIST);

        Toolbar toolbar = findViewById(R.id.background);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(artist.artist);

        TextView numberOfTracks = (TextView) findViewById(R.id.tracks);
        TextView numberOfAlbums = (TextView) findViewById(R.id.albums);

        numberOfTracks.setText(String.valueOf(artist.tracks) + "tracks");
        numberOfAlbums.setText(String.valueOf(artist.albums) + "albums");

        findViewById(R.id.artist_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        List<Track> tracks = Track.getItemsByAlbum(this, artist.albumId);

        ListView listView = (ListView) findViewById(R.id.list);
//        ListTrackAdapter adapter = new ListTrackAdapter(this);
//        listView.setAdapter(adapter);
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
