package com.example.shojishunsuke.musicpro;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

public class Artist {

    public long id;
    public String artist;
    public String artistKey;
    public int albums;
    public int tracks;

    public static final String[] FIELD_PROJECTION = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ARTIST_KEY,
            MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
            MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
    };

    public Artist(Cursor cursor) {

        id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Artists._ID));
        artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
        artistKey = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST_KEY));
        albums = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS));
        tracks = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS));

    }

    public static List getItems(Context activity) {
        List artists = new ArrayList();

        ContentResolver resolver = activity.getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                Artist.FIELD_PROJECTION,
                null,
                null,
                "ARTIST ASC"
        );

        while (cursor.moveToNext()) {
            artists.add(new Artist(cursor));
        }

        cursor.close();
        return artists;
    }
}
