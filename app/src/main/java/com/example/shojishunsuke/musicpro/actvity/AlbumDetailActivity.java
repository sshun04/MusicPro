package com.example.shojishunsuke.musicpro.actvity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shojishunsuke.musicpro.ImageGetTask;
import com.example.shojishunsuke.musicpro.R;
import com.example.shojishunsuke.musicpro.adapter.ListTrackAdapter;
import com.example.shojishunsuke.musicpro.model.Album;
import com.example.shojishunsuke.musicpro.model.Track;

import java.util.List;

//import static com.example.shojishunsuke.musicpro.AlbumMenu.album_item;

public class AlbumDetailActivity extends AppCompatActivity {

    private static final String KEY_ALBUM = "kyc_album";
    Context context;

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

        TextView album_title = (TextView) findViewById(R.id.title);
        TextView album_artist = (TextView) findViewById(R.id.artist);
        TextView album_tracks = (TextView) findViewById(R.id.tracks);
        ImageView album_art = (ImageView) findViewById(R.id.albumart);

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
    }

}
