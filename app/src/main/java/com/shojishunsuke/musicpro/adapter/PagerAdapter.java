package com.shojishunsuke.musicpro.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.shojishunsuke.musicpro.fragment.AlbumTabFragment;
import com.shojishunsuke.musicpro.fragment.ArtistTabFragment;
import com.shojishunsuke.musicpro.fragment.TrackTabFragment;

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
                return TrackTabFragment.newInstance();
            case 1:
                return ArtistTabFragment.newInstance();
            case 2:
                return AlbumTabFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "TRACK";
            case 1:
                return "ARTIST";
            case 2:
                return "ALBUM";
            default:
                return null;
        }
    }
}
