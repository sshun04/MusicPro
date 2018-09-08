package com.example.shojishunsuke.musicpro;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import static com.example.shojishunsuke.musicpro.R.string.title_section1;

public class RootMenu extends android.support.v4.app.Fragment {

    SectionsPagerAdapter mSectionsPagerAdapter;
   ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater ,ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_main, container, false);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        return rootView;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }



        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            android.support.v4.app.Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new HomeSectionFragment();
                    break;
                case 1:
                    fragment = new TrackSectionFragment();
                    break;
                case 2:
                    fragment = new AlbumSectionFragment();
                    break;
                case 3:
                    fragment = new ArtistSectionFragment();
                    break;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

//
    }

    public static class HomeSectionFragment extends android.support.v4.app.Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater ,ViewGroup container,Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            View v = inflater.inflate(R.layout.menu_home, container, false);
            return v;

        }
    }

    public static class TrackSectionFragment extends android.support.v4.app.Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            MainActivity activity = (MainActivity) getActivity();
            List tracks = Track.getItems(activity);

            View v = inflater.inflate(R.layout.item_track, container, false);
            ListView trackList = (ListView) v.findViewById(R.id.list);
            ListTrackAdapter adapter = new ListTrackAdapter(activity, tracks);
            trackList.setAdapter(adapter);

            return v;

        }
    }

    public static class AlbumSectionFragment extends android.support.v4.app.Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

           MainActivity activity = (MainActivity) getActivity();
            List album = Album.getItems(activity);

            View v = inflater.inflate(R.layout.item_album, container, false);
            ListView albumList = (ListView) v.findViewById(R.id.list);
            ListAlbumAdapter adapter = new ListAlbumAdapter(activity, album);
            albumList.setAdapter(adapter);

            return v;

        }

    }

    public static class ArtistSectionFragment extends android.support.v4.app.Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            MainActivity activity = (MainActivity) getActivity();
            List artist = Artist.getItems(activity);

            View v = inflater.inflate(R.layout.item_artist, container, false);
            ListView artistList = (ListView) v.findViewById(R.id.list);
            ListArtistAdapter adapter = new ListArtistAdapter(activity, artist);
            artistList.setAdapter(adapter);

            return v;

        }

    }


}
