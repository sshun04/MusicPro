package com.shojishunsuke.musicpro.actvity;

import android.Manifest;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;

import com.shojishunsuke.musicpro.R;
import com.shojishunsuke.musicpro.adapter.PagerAdapter;
import com.shojishunsuke.musicpro.utils.RuntimePermissionUtils;

public class MainActivity extends AppCompatActivity  {

    private TabLayout tabLayout;
    private ViewPager viewPager;
//    private android.support.v7.widget.Toolbar toolbar;
    private ActionBar actionBar;


    private ImageView playButton;

    private final static String[] READ_EXTERNAL_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    private final static int PERMISSION_REQUEST_CODE = 1;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        toolbar = (Toolbar) findViewById(R.id.background);
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setHomeButtonEnabled(true);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        playButton = (ImageView) findViewById(R.id.mainPlay);


        tabLayout.addTab(tabLayout.newTab().setText("Tracks"));
        tabLayout.addTab(tabLayout.newTab().setText("Album"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager);

        if (RuntimePermissionUtils.hasSelfPermissions(MainActivity.this, READ_EXTERNAL_STORAGE)) {

            final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(adapter);

        } else {
            Toast.makeText(this, "許可を選ぶと曲リストが表示されます", Toast.LENGTH_LONG).show();
            requestPermissions(READ_EXTERNAL_STORAGE, PERMISSION_REQUEST_CODE);

        }

        tabLayout.setupWithViewPager(viewPager);

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




    }

    @Override
    protected void onResume() {
        super.onResume();

        AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0) {

            if (RuntimePermissionUtils.checkGrantResults(grantResults)) {
                final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
                viewPager.setAdapter(adapter);
                tabLayout.setupWithViewPager(viewPager);
            }
        }
    }



}
