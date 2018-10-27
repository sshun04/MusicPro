package com.shojishunsuke.musicpro.actvity;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.shojishunsuke.musicpro.R;
import com.shojishunsuke.musicpro.RuntimePermissionUtils;
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

    private ListView trackList;
    private TabLayout tabLayout;
    private ViewPager viewPager;

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


    private final static String[] READ_EXTERNAL_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    private final static int PERMISSION_REQUEST_CODE = 1;

    public Album getFocusedAlbum() {
        return focusedAlbum;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        final FloatingActionButton playButton = (FloatingActionButton) findViewById(R.id.mainPlay);

        tabLayout.addTab(tabLayout.newTab().setText("Tracks"));
        tabLayout.addTab(tabLayout.newTab().setText("Artist"));
        tabLayout.addTab(tabLayout.newTab().setText("Album"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager);


        if (RuntimePermissionUtils.hasSelfPermissions(MainActivity.this, READ_EXTERNAL_STORAGE)) {

            final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(adapter);

        } else {
            Toast.makeText(this,"許可を選ぶと曲リストが表示されます",Toast.LENGTH_LONG).show();
            requestPermissions(READ_EXTERNAL_STORAGE, PERMISSION_REQUEST_CODE);

        }


        tabLayout.setupWithViewPager(viewPager);

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


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0) {

            if (!RuntimePermissionUtils.checkGrantResults(grantResults)) {

                ;

            }else {
                final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
                viewPager.setAdapter(adapter);
                tabLayout.setupWithViewPager(viewPager);

            }
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