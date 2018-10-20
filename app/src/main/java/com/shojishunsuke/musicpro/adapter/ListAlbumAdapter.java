package com.shojishunsuke.musicpro.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shojishunsuke.musicpro.R;
import com.shojishunsuke.musicpro.ImageCache;
import com.shojishunsuke.musicpro.ImageGetTask;
import com.shojishunsuke.musicpro.actvity.AlbumDetailActivity;
import com.shojishunsuke.musicpro.model.Album;

import java.util.List;

public class ListAlbumAdapter extends ArrayAdapter<Album> {

    Context context;
    LayoutInflater mInflater;

    public ListAlbumAdapter(Context context, List<Album> item) {
        super(context, 0, item);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Album item = getItem(position);
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_album, null);
            holder = new ViewHolder();

            holder.albumTextView = (TextView) convertView.findViewById(R.id.title);
            holder.artistTextView = (TextView) convertView.findViewById(R.id.artist);
            holder.tracksTextView = (TextView) convertView.findViewById(R.id.tracks);
            holder.artworkImageView = (ImageView) convertView.findViewById(R.id.albumart);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.albumTextView.setText(item.album);
        holder.artistTextView.setText(item.artist);
        holder.tracksTextView.setText(String.valueOf(item.tracks) + "tracks");

        String path = item.albumArt;
        holder.artworkImageView.setImageResource(R.drawable.backsub);

        if (path == null) {
            path = String.valueOf(R.drawable.record);
            Bitmap bitmap = ImageCache.getImage(path);

            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.record);
                ImageCache.setImage(path, bitmap);
            }
        }

        holder.artworkImageView.setTag(path);
        ImageGetTask task = new ImageGetTask(holder.artworkImageView);
        task.execute(path);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlbumDetailActivity.start(context, item);

            }
        });

        return convertView;

    }



    static class ViewHolder {
        TextView albumTextView;
        TextView artistTextView;
        TextView tracksTextView;
        ImageView artworkImageView;
    }


}
