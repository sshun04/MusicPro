package com.example.shojishunsuke.musicpro.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.shojishunsuke.musicpro.fragment.AlbumTabFragment;
import com.example.shojishunsuke.musicpro.fragment.ArtistTabFragment;
import com.example.shojishunsuke.musicpro.fragment.TrackTabFragment;

public class PagerAdapter extends FragmentPagerAdapter {

    int mNoOfTabs;

    public PagerAdapter(FragmentManager fm, int NumberOfTabs) {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;

    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:

                TrackTabFragment trackTabFragment = new TrackTabFragment();
                return trackTabFragment;

            case 1:
               ArtistTabFragment artistTabFragment = new ArtistTabFragment();
                return artistTabFragment;

            case 2:
                AlbumTabFragment albumTabFragment = new AlbumTabFragment();
                 return albumTabFragment;

          default:
              return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}
