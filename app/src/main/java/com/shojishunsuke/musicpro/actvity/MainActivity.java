package com.shojishunsuke.musicpro.actvity;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.shojishunsuke.musicpro.R;
import com.shojishunsuke.musicpro.Service.TrackService;
import com.shojishunsuke.musicpro.adapter.PagerAdapter;
import com.shojishunsuke.musicpro.utils.CheckServiceUtils;
import com.shojishunsuke.musicpro.utils.RuntimePermissionUtils;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private boolean isStartService = false;

    private FloatingActionButton playButton;

    private final static String[] READ_EXTERNAL_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    private final static int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        playButton = (FloatingActionButton) findViewById(R.id.mainPlay);

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

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityManager activityManager = (ActivityManager) MainActivity.this.getSystemService(ACTIVITY_SERVICE);
                if (activityManager != null) {
                    isStartService = CheckServiceUtils.checkAudioService(activityManager);
                }

                AudioManager audioManager = (AudioManager)MainActivity.this.getSystemService(Context.AUDIO_SERVICE);

                if (audioManager.isMusicActive()&& isStartService) {

                    playButton.setImageResource(R.drawable.playarrow);
                    startService(new Intent(MainActivity.this, TrackService.class));


                } else if (isStartService) {
                    playButton.setImageResource(R.drawable.pause);
                    startService(new Intent(MainActivity.this, TrackService.class));

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
