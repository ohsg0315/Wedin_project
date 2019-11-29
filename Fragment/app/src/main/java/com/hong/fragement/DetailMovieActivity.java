package com.hong.fragement;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.hong.fragement.R;

public class DetailMovieActivity extends AppCompatActivity {

    private VideoView videoView;
    private MediaController mediaController;
    private String videoURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        videoView = findViewById(R.id.video);
        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        Uri uri = Uri.parse(videoURL);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
    }
}
