package com.shojishunsuke.musicpro.adapter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.media.session.MediaController;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.shojishunsuke.musicpro.Library.MusicLibrary;
import com.shojishunsuke.musicpro.R;
import com.shojishunsuke.musicpro.Service.MediaSessionService;
import com.shojishunsuke.musicpro.actvity.MediaSessionPlayActivity;
import com.shojishunsuke.musicpro.actvity.TrackDetailActivity;
import com.shojishunsuke.musicpro.model.Track;

import java.util.ArrayList;
import java.util.List;

public class ListTrackAdapter extends ArrayAdapter<MediaBrowserCompat.MediaItem> {

    private LayoutInflater mInflater;
    private  Context context;



    public ListTrackAdapter(Context context,List<MediaBrowserCompat.MediaItem> items) {
        super(context, 0, items);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        final ViewHolder holder;
        MediaBrowserCompat.MediaItem mediaItem = getItem(position);

//        MediaController mediaController = ((Activity)getContext()).getMediaController();

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.item_track, null);
            holder = new ViewHolder();
            holder.trackTextView = (TextView) convertView.findViewById(R.id.title);
            holder.artistTextView = (TextView) convertView.findViewById(R.id.artist);
            holder.durationTextView = (TextView) convertView.findViewById(R.id.duration);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mediaItem.isPlayable()){
            holder.trackTextView.setText(mediaItem.getDescription().getTitle());
            holder.artistTextView.setText(mediaItem.getDescription().getSubtitle());
        }

        return convertView;
    }





    static class ViewHolder {

        TextView trackTextView;
        TextView artistTextView;
        TextView durationTextView;

    }

}
