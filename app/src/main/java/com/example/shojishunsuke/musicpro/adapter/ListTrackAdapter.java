package com.example.shojishunsuke.musicpro.adapter;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shojishunsuke.musicpro.R;
import com.example.shojishunsuke.musicpro.model.Track;

import java.io.IOException;
import java.util.List;

public class ListTrackAdapter extends ArrayAdapter<Track> {

    LayoutInflater mInflater;
    Context mContext;
    private MediaPlayer mediaPlayer;

    public ListTrackAdapter(Context context, List item) {
        super(context, 0, item);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Track item = getItem(position);
        final ViewHolder holder;

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.item_track, null);
            holder = new ViewHolder();
            holder.trackTextView = (TextView) convertView.findViewById(R.id.title);
            holder.artistTextView = (TextView) convertView.findViewById(R.id.artist);
            holder.durationTextView = (TextView) convertView.findViewById(R.id.duration);
//            holder.trackartImageView= (ImageView)convertView.findViewById(R.id.artWork);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        long dm = item.duration / 60000;
        long ds = (item.duration - (dm * 60000)) / 1000;

        holder.artistTextView.setText(item.artist);
        holder.trackTextView.setText(item.title);
        holder.durationTextView.setText(String.format("%d:%02d", dm, ds));


        final MediaPlayer mediaPlayer = new MediaPlayer();
//


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    mediaPlayer.setDataSource(item.path);
                    mediaPlayer.prepare();

                } catch (IOException e) {
                    e.printStackTrace();
                }
//

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();

                } else {
                    mediaPlayer.start();
                    Toast.makeText(mContext, "PLAY", Toast.LENGTH_SHORT).show();
                    holder.trackTextView.setTextColor(Color.BLUE);
                }


            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                mediaPlayer.stop();
                mediaPlayer.reset();

                Toast.makeText(mContext, "STOP", Toast.LENGTH_SHORT).show();
                holder.trackTextView.setTextColor(Color.BLACK);


                return true;


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
