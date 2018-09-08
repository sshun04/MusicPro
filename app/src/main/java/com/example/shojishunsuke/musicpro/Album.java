package com.example.shojishunsuke.musicpro;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

public class Album {
    public long id;
    public String album;
    public String albumArt;
    public long albumId;
    public String albumKey;
    public String artist;
    public int tracks;

    public static final String[] FILLED_PROJECTION = {
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ALBUM_ART,
            MediaStore.Audio.Albums.ALBUM_KEY,
            MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.NUMBER_OF_SONGS,
    };

    public Album(Cursor cursor) {
        id        = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Albums._ID));
        album     = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
        albumArt  = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
        albumId   = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
        albumKey  = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_KEY));
        artist    = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST));
        tracks    = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS));
    }

    public static List getItems(Context activity) {

        List albums = new ArrayList();

        ContentResolver resolver = activity.getContentResolver();
        Cursor cursor = resolver.query(
                        MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                        Album.FILLED_PROJECTION,
                null,
                null,
                "ALBUM ASC"

        );

        while (cursor.moveToNext()){
            albums.add(new Album(cursor));
        }
        cursor.close();
        return albums;
    }
}
