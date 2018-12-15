package com.shojishunsuke.musicpro.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.shojishunsuke.musicpro.R;
import com.shojishunsuke.musicpro.actvity.MainActivity;
import com.shojishunsuke.musicpro.adapter.ListTrackAdapter;
import com.shojishunsuke.musicpro.model.Track;

import java.util.List;


public class TrackTabFragment extends Fragment {

    public static TrackTabFragment newInstance() {
        TrackTabFragment fragment = new TrackTabFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track_tab, container, false);
        MainActivity activity = (MainActivity) getActivity();

        List tracks = Track.getItems(activity);
        ListView trackList = (ListView) view.findViewById(R.id.listTrack);
        ListTrackAdapter trackAdapter = new ListTrackAdapter(activity, tracks);
        trackList.setAdapter(trackAdapter);


        // Inflate the layout for this fragment
        return view;
    }
}


