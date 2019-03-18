package com.shojishunsuke.musicpro.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.shojishunsuke.musicpro.R;
import com.shojishunsuke.musicpro.actvity.MainActivity;
import com.shojishunsuke.musicpro.adapter.ListArtistAdapter;
import com.shojishunsuke.musicpro.model.Artist;

import java.util.List;


public class ArtistTabFragment extends Fragment  {

    public static ArtistTabFragment newInstance() {
        ArtistTabFragment fragment = new ArtistTabFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_artist_tab, container, false);

        MainActivity activity = (MainActivity) getActivity();

        List artists = Artist.getItems(activity);
        ListView artistList = (ListView) rootView.findViewById(R.id.listArtist);
        ListArtistAdapter adapter = new ListArtistAdapter(activity, artists);
        artistList.setAdapter(adapter);


        // Inflate the layout for this fragment
        return rootView;
    }
}
