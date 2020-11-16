package com.game.onecricket.activity;

import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.game.onecricket.R;
import com.potyvideo.library.AndExoPlayerView;
import com.potyvideo.library.globalEnums.EnumResizeMode;

public class VideoViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(view -> finish());
        VideoView videoView = findViewById(R.id.videoView);
        final MediaController mediacontroller = new MediaController(this);
        mediacontroller.setAnchorView(videoView);
        AndExoPlayerView andExoPlayerView = findViewById(R.id.andExoPlayerView);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String url = extras.getString("Video_Url");
            andExoPlayerView.setSource(url);
        }

        andExoPlayerView.setShowController(true);
        andExoPlayerView.setResizeMode(EnumResizeMode.ZOOM);
        andExoPlayerView.setPlayWhenReady(true);
    }
}
