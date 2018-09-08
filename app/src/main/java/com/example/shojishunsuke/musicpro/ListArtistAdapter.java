package com.example.shojishunsuke.musicpro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListArtistAdapter extends ArrayAdapter<Artist> {

    LayoutInflater mInflater;

    public ListArtistAdapter(Context context, List item){

        super(context,0,item);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        Artist item = getItem(position);
        ViewHolder holder;

        if (convertView==null){
            convertView = mInflater.inflate(R.layout.item_artist,null);
            holder = new ViewHolder();
            holder.artistNameTextView = (TextView)convertView.findViewById(R.id.artist);
            holder.albumsTextView = (TextView)convertView.findViewById(R.id.albums);
            holder.tracksTextView = (TextView)convertView.findViewById(R.id.tracks);
            convertView.setTag(holder);

        }else{
            holder= (ViewHolder)convertView.getTag();
        }

        holder.artistNameTextView.setText(item.artist);
        holder.albumsTextView.setText(String.format("%d Albums",item.albums));
        holder.tracksTextView.setText(String.format("%d Tracks",item.tracks));



        return convertView;
    }

    static class ViewHolder{
        TextView artistNameTextView;
        TextView albumsTextView;
        TextView tracksTextView;
    }



}
