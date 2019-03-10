package com.shojishunsuke.musicpro.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaBrowserCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.shojishunsuke.musicpro.R;
import com.shojishunsuke.musicpro.actvity.MainActivity;
import com.shojishunsuke.musicpro.adapter.ListAlbumAdapter;
import com.shojishunsuke.musicpro.model.Album;

import java.util.List;


public class AlbumTabFragment extends Fragment {

    public static AlbumTabFragment newInstance() {
        AlbumTabFragment fragment = new AlbumTabFragment();
        return fragment;
    }

    private ListAlbumAdapter albumAdapter;
    private MediaBrowserCompat mediaBrowserCompat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_album_tab, container, false);

        MainActivity activity = (MainActivity) getActivity();

        List albums = Album.getItems(activity);
        ListView albumlist = (ListView) rootView.findViewById(R.id.listAlbum);
        ListAlbumAdapter adapter = new ListAlbumAdapter(activity, albums);
        albumlist.setAdapter(adapter);


        // Inflate the layout for this fragment
        return rootView;
    }
}
