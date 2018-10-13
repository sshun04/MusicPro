package com.example.shojishunsuke.musicpro.actvity;

import android.content.Context;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shojishunsuke.musicpro.model.Album;
import com.example.shojishunsuke.musicpro.fragment.AlbumTabFragment;
import com.example.shojishunsuke.musicpro.fragment.ArtistTabFragment;
import com.example.shojishunsuke.musicpro.adapter.ListTrackAdapter;
import com.example.shojishunsuke.musicpro.adapter.PagerAdapter;
import com.example.shojishunsuke.musicpro.R;
import com.example.shojishunsuke.musicpro.model.Track;
import com.example.shojishunsuke.musicpro.fragment.TrackTabFragment;

import java.io.Serializable;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TrackTabFragment.OnFragmentInteractionListener, ArtistTabFragment.OnFragmentInteractionListener,
        AlbumTabFragment.OnFragmentInteractionListener {
    Context mContext;

    enum FragType {fAlbum}
    private ListView trackList;

    private FragType fTop;
    private Album focusedAlbum;

    public void focusedAlbum(Album item, Context context) {
        this.mContext = context;
        if (item == null) {
            focusedAlbum = item;
        }
    }

    public Album getFocusedAlbum() {
        return focusedAlbum;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Tracks"));
        tabLayout.addTab(tabLayout.newTab().setText("Artist"));
        tabLayout.addTab(tabLayout.newTab().setText("Album"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }




    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void popBackFragment() {

        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();

    }

}
