package com.shojishunsuke.musicpro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.shojishunsuke.musicpro.R;
import com.shojishunsuke.musicpro.actvity.MediaSessionPlayActivity;
import com.shojishunsuke.musicpro.actvity.TrackDetailActivity;
import com.shojishunsuke.musicpro.model.Track;

import java.util.List;

public class ListTrackAdapter extends ArrayAdapter<Track> {

    private LayoutInflater mInflater;
    Context context;


    public ListTrackAdapter(Context context, List item) {
        super(context, 0, item);

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Track item = getItem(position);
        final ViewHolder holder;


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
        long dm = item.duration / 60000;
        long ds = (item.duration - (dm * 60000)) / 1000;

        holder.artistTextView.setText(item.artist);
        holder.trackTextView.setText(item.title);
        holder.durationTextView.setText(String.format("%d:%02d", dm, ds));


//


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                TrackDetailActivity.start(context, item);

               MediaSessionPlayActivity.start(context);


            }
        });


        return convertView;
    }

    static class ViewHolder {

        TextView trackTextView;
        TextView artistTextView;
        TextView durationTextView;

    }
}
