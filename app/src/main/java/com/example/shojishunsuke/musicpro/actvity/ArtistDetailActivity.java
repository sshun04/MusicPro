package com.example.shojishunsuke.musicpro.actvity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shojishunsuke.musicpro.R;
import com.example.shojishunsuke.musicpro.adapter.ListTrackAdapter;
import com.example.shojishunsuke.musicpro.model.Artist;
import com.example.shojishunsuke.musicpro.model.Track;

import java.util.List;

public class ArtistDetailActivity extends AppCompatActivity {

    public static final String KEY_ARTIST = "kec_artist";

    public static void start(Context context, Artist artist){
        Intent intent = new Intent(context,ArtistDetailActivity.class);
        intent.putExtra(KEY_ARTIST,artist);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_detail);

        Intent intent = getIntent();
        Artist artist = (Artist)intent.getSerializableExtra(KEY_ARTIST);

        TextView artistName = (TextView)findViewById(R.id.artist);
        TextView numberOfTracks = (TextView)findViewById(R.id.tracks);
        TextView numberOfAlbums = (TextView)findViewById(R.id.albums);

        artistName.setText(artist.artist);
        numberOfTracks.setText(String.valueOf(artist.tracks)+"tracks");
        numberOfAlbums.setText(String.valueOf(artist.albums)+"albums");

        findViewById(R.id.artist_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

     List<Track> tracks = Track.getItemsByAlbum(this,artist.albumId);

        ListView listView = (ListView)findViewById(R.id.list);
        ListTrackAdapter adapter = new ListTrackAdapter(this,tracks);
        listView.setAdapter(adapter);
    }
}
