package com.hong.fragement.MovieDetailPage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hong.fragement.R;

import java.util.Map;

public class DetailMovieActivity extends AppCompatActivity {

    private String TAG = "Read Data from FireStore";

    private ImageView poster;
    private TextView title;
    private TextView lowPrice;
    private TextView highPrice;
    private TextView summary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        poster = findViewById(R.id.poster_detail_movie);
        title = findViewById(R.id.title_detail_movie);
        lowPrice = findViewById(R.id.movie_low_price);
        highPrice = findViewById(R.id.movie_high_price);
        summary = findViewById(R.id.summary_detail_movie);

        Intent intent = getIntent();

        Glide.with(this)
                .load(intent.getStringExtra("imageUri"))
                .error(R.drawable.common_full_open_on_phone)
                .into(poster);

        title.setText(intent.getStringExtra("title"));
        lowPrice.setText(intent.getStringExtra("price1"));
        highPrice.setText(intent.getStringExtra("price2"));
        summary.setText(intent.getStringExtra("summary"));


    }

}


