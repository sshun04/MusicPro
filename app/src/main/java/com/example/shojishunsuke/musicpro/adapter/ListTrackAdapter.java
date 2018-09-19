package com.example.shojishunsuke.musicpro.adapter;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shojishunsuke.musicpro.R;
import com.example.shojishunsuke.musicpro.Service.TrackService;
import com.example.shojishunsuke.musicpro.actvity.AlbumDetailActivity;
import com.example.shojishunsuke.musicpro.actvity.MainActivity;
import com.example.shojishunsuke.musicpro.model.Track;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class ListTrackAdapter extends ArrayAdapter<Track>{

    LayoutInflater mInflater;
    Context mContext;
    private MediaPlayer mediaPlayer;
    TrackService service;


    public ListTrackAdapter(Context context, List item) {
        super(context, 0, item);

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;

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
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

//                TODO SERVICE
//

                TrackService service = new TrackService();
                Intent intent = new Intent(service.getApplication(), TrackService.class);
                intent.putExtra("REQUEST_CODE",1);
//
//
                service.startForegroundService(intent);




//        if (mediaPlayer.isPlaying()) {
//
//
//                    mediaPlayer.stop();
//                    mediaPlayer.reset();
//
//                    Toast.makeText(mContext, "STOP", Toast.LENGTH_SHORT).show();
//
//                    holder.trackTextView.setTextColor(Color.BLACK);
//
//                } else {
//                    try {
//                        mediaPlayer.setDataSource(item.path);
//                        mediaPlayer.prepare();
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    mediaPlayer.start();
//                    Toast.makeText(mContext, "PLAY", Toast.LENGTH_SHORT).show();
//                    holder.trackTextView.setTextColor(Color.BLUE);
//                }
//
//                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mp) {
//                        Log.d("debug","end of audio");
//                        mediaPlayer.stop();
//                        mediaPlayer.reset();
//
//                        holder.trackTextView.setTextColor(Color.BLACK);
//                        Toast.makeText(mContext,"End",Toast.LENGTH_SHORT).show();
//                    }
//                });





            }
        });

//

        return convertView;
    }



    static class ViewHolder {

        TextView trackTextView;
        TextView artistTextView;
        TextView durationTextView;

    }
}
