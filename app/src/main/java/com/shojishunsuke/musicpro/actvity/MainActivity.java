package com.shojishunsuke.musicpro.actvity;

import android.Manifest;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;

import com.shojishunsuke.musicpro.R;
import com.shojishunsuke.musicpro.Service.MediaSessionService;
import com.shojishunsuke.musicpro.adapter.PagerAdapter;
import com.shojishunsuke.musicpro.fragment.TrackTabFragment;
import com.shojishunsuke.musicpro.utils.RuntimePermissionUtils;

public class MainActivity extends AppCompatActivity {


    private android.support.v7.widget.Toolbar toolbar;
    private ActionBar actionBar;


    private ImageView playButton;

    private final static String[] READ_EXTERNAL_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    private final static int PERMISSION_REQUEST_CODE = 1;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);

        playButton = (ImageView) findViewById(R.id.mainPlay);


        if (RuntimePermissionUtils.hasSelfPermissions(MainActivity.this, READ_EXTERNAL_STORAGE)) {


            replaceWithTrackTab();
            MediaSessionService.start(this);

        } else {
            Toast.makeText(this, "許可を選ぶと曲リストが表示されます", Toast.LENGTH_LONG).show();
            requestPermissions(READ_EXTERNAL_STORAGE, PERMISSION_REQUEST_CODE);

        }





    }

    @Override
    protected void onResume() {
        super.onResume();

        AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);


    }

    public void replaceWithTrackTab(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.mainBackground, TrackTabFragment.newInstance());
        fragmentTransaction.commit();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0) {

            if (RuntimePermissionUtils.checkGrantResults(grantResults)) {

                replaceWithTrackTab();

                MediaSessionService.start(this);
            }
        }
    }



}
