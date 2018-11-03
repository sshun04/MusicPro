package com.shojishunsuke.musicpro.actvity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.shojishunsuke.musicpro.R;
import com.shojishunsuke.musicpro.Service.TrackService;
import com.shojishunsuke.musicpro.adapter.PagerAdapter;
import com.shojishunsuke.musicpro.fragment.AlbumTabFragment;
import com.shojishunsuke.musicpro.fragment.ArtistTabFragment;
import com.shojishunsuke.musicpro.fragment.TrackTabFragment;
import com.shojishunsuke.musicpro.model.Album;

import java.util.List;

public class MainActivity extends AppCompatActivity implements TrackTabFragment.OnFragmentInteractionListener, ArtistTabFragment.OnFragmentInteractionListener,
        AlbumTabFragment.OnFragmentInteractionListener {
    Context mContext;

    enum FragType {fAlbum}
    private FloatingActionButton playButton;

    private ListView trackList;

    private FragType fTop;
    private Album focusedAlbum;

    public void focusedAlbum(Album item, Context context) {
        this.mContext = context;
        if (item == null) {
            focusedAlbum = item;
        }
    }

    public boolean check = false;
    public boolean flag = false;

    public Album getFocusedAlbum() {
        return focusedAlbum;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        playButton = (FloatingActionButton) findViewById(R.id.mainPlay);
        FloatingActionButton stopButton = (FloatingActionButton) findViewById(R.id.mainStop);

        tabLayout.addTab(tabLayout.newTab().setText("Tracks"));
        tabLayout.addTab(tabLayout.newTab().setText("Artist"));
        tabLayout.addTab(tabLayout.newTab().setText("Album"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        stopButton.setImageResource(R.drawable.round_stop_white);
        playButton.setImageResource(R.drawable.pause);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());

            }
        });

//        TODO TrackDetailActivityでPauseしてから戻るとボタンの画像がずれてしまう


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                audioCheck();

                if (flag && check) {

                    playButton.setImageResource(R.drawable.pause);
                    startService(new Intent(MainActivity.this, TrackService.class));
                    flag = false;


                } else if (check) {
                    playButton.setImageResource(R.drawable.playarrow);
                    startService(new Intent(MainActivity.this, TrackService.class));
                    flag = true;
                } else {
                    flag = false;
                    Toast.makeText(MainActivity.this, "Choose Track", Toast.LENGTH_SHORT).show();
                }
            }
        });


        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                audioCheck();

                if (check) {


                    stopService(new Intent(MainActivity.this, TrackService.class));
                    playButton.setImageResource(R.drawable.pause);
                    check = false;
                    flag = false;
                } else {
                    Toast.makeText(MainActivity.this, "Choose Track", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            if (audioManager.isMusicActive()) {
                playButton.setImageResource(R.drawable.pause);
            } else {
                playButton.setImageResource(R.drawable.playarrow);
            }
        } else {
            playButton.setImageResource(R.drawable.playarrow);
        }
    }

    public void audioCheck() {
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> listSeriviceInfo = am.getRunningServices(Integer.MAX_VALUE);


        for (ActivityManager.RunningServiceInfo curr : listSeriviceInfo) {
            if (curr.service.getClassName().equals(TrackService.class.getName())) {
                check = true;
                break;
            }
        }
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void popBackFragment() {

        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();

    }



}
