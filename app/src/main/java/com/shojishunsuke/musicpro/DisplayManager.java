package com.shojishunsuke.musicpro;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.shojishunsuke.musicpro.fragment.PlayFragment;
import com.shojishunsuke.musicpro.fragment.TrackTabFragment;

public class DisplayManager  {

    private  static DisplayManager displayManager = new DisplayManager();
    private android.support.v4.app.FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private PlayFragment playFragment = new PlayFragment();
    private TrackTabFragment trackTabFragment = TrackTabFragment.newInstance();


    private DisplayManager(){
    }


    public static DisplayManager getInstance(){
        return displayManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        fragmentTransaction =fragmentManager.beginTransaction();
//        addFragments();
    }

    public void replaceWithTrackTab(){

        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.mainBackground,trackTabFragment);
        fragmentTransaction.commit();

    }

    public void replaceWithNewPlayTab(Bundle args){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        playFragment.setArguments(args);
        fragmentTransaction.add(R.id.mainBackground,playFragment);
        fragmentTransaction.commit();
    }

    public void hidePlay(){
        FragmentTransaction  fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.hide(playFragment);
        fragmentTransaction.commit();
    }

    public void hideList(){

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(trackTabFragment);
        fragmentTransaction.commit();

    }

    public void showList(){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.show(trackTabFragment);
        fragmentTransaction.commit();
    }

    public void showPlay(){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.show(playFragment);
        fragmentTransaction.commit();
    }


}
