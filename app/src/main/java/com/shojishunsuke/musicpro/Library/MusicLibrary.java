package com.shojishunsuke.musicpro.Library;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;

import com.shojishunsuke.musicpro.BuildConfig;
import com.shojishunsuke.musicpro.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class MusicLibrary {

    private final TreeMap<String, MediaMetadataCompat> music = new TreeMap<>();
    private final HashMap<String, Integer> albumRes = new HashMap<>();
    private final HashMap<String, Uri> musicFileName = new HashMap<>();

    public static final String[] COLUMNS = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.TRACK,
    };

    public MusicLibrary(Context context) {

        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                MusicLibrary.COLUMNS,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            if (cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)) < 3000) {
                continue;
            }

            Long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));

            createMetaDateCompat(
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)),
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)),
                    "pops",
                    cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)),
                    ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id),
                    R.drawable.track_icon,
                    "album_jazz_blues"
            );

        }

        cursor.close();


    }


    public String getRoot() {
        return "root";
    }

    private String getAlbumArtUri(String albumArtResName) {
        return ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                BuildConfig.APPLICATION_ID + "/drawable/" + albumArtResName;
    }

    public Uri getMusicFileNames(String mediaId) {
        return musicFileName.containsKey(mediaId) ? musicFileName.get(mediaId) : null;
    }



    private int getAlbumRes(String mediaId) {
        return albumRes.containsKey(mediaId) ? albumRes.get(mediaId) : 0;
    }

    public Bitmap getAlbumBitmap(Context context, String mediaId) {
        return BitmapFactory.decodeResource(context.getResources(),
                getAlbumRes(mediaId));
    }

    public List<MediaBrowserCompat.MediaItem> getMediaItems() {
        List<MediaBrowserCompat.MediaItem> result = new ArrayList<>();
        for (MediaMetadataCompat metadata : music.values()) {
            result.add(
                    new MediaBrowserCompat.MediaItem(
                            metadata.getDescription(),
                            MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
                    )
            );
        }

        return result;
    }

    public MediaMetadataCompat getMetaData(Context context, String mediaId) {
        MediaMetadataCompat metadataWithoutBitmap = music.get(mediaId);
        Bitmap albumArt = getAlbumBitmap(context, mediaId);

        MediaMetadataCompat.Builder builder = new MediaMetadataCompat.Builder();
        for (String key :
                new String[]{
                        MediaMetadataCompat.METADATA_KEY_MEDIA_ID,
                        MediaMetadataCompat.METADATA_KEY_ALBUM,
                        MediaMetadataCompat.METADATA_KEY_ARTIST,
                        MediaMetadataCompat.METADATA_KEY_GENRE,
                        MediaMetadataCompat.METADATA_KEY_TITLE,

                }) {
            builder.putString(key, metadataWithoutBitmap.getString(key));
        }

        builder.putLong(
                MediaMetadataCompat.METADATA_KEY_DURATION,
                metadataWithoutBitmap.getLong(MediaMetadataCompat.METADATA_KEY_DURATION));
        builder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumArt);
        return builder.build();

    }


    private void createMetaDateCompat(
            String mediaid,
            String title,
            String artist,
            String album,
            String genre,
            long duration,
            Uri uri,
            int albumArtResId,
            String albumArtResName) {

        music.put(
                mediaid,
                new MediaMetadataCompat.Builder()
                        .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, mediaid)
                        .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, album)
                        .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist)
                        .putLong(MediaMetadataCompat.METADATA_KEY_DURATION,
                                duration)
                        .putString(MediaMetadataCompat.METADATA_KEY_GENRE, genre)
                        .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI,
                                getAlbumArtUri(albumArtResName))
                        .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI,
                                getAlbumArtUri(albumArtResName))
                        .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
                        .build());

        albumRes.put(mediaid, albumArtResId);
        musicFileName.put(mediaid, uri);

    }
}
