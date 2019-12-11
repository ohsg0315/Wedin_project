package com.hong.fragement.MovieDetailPage;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hong.fragement.R;

import java.util.ArrayList;

public class DetailMovieActivity extends YouTubeBaseActivity {

    private YouTubePlayerView youTubePlayerView;
    private String youtubeUri;

    private ImageView poster;
    private TextView title;
    private TextView lowPrice;
    private TextView highPrice;
    private TextView summary;
    private Button reviewAddBtn;

    private Intent intent;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference ratingRef = db.collection("Rating");

    private RatingObj data;
    private RatingBar ratingBar;

    private RecyclerView ratingRecyclerVeiw;
    private DetailMoviewAdapter adapter;
    private CustomDialog customDialog;
    private String movieTitle;
    static ArrayList<RatingObj> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_movie_activity);

        intent = getIntent();
        dataList = new ArrayList<RatingObj>();

        youTubePlayerView= findViewById(R.id.youtubeview);
        poster = findViewById(R.id.poster_detail_movie);
        title = findViewById(R.id.title_detail_movie);
        lowPrice = findViewById(R.id.movie_low_price);
        highPrice = findViewById(R.id.movie_high_price);
        summary = findViewById(R.id.summary_detail_movie);
        ratingBar = findViewById(R.id.ratingBar_detail_movie);
        ratingRecyclerVeiw = findViewById(R.id.rating_recyclerview);
        reviewAddBtn = findViewById(R.id.review_add_btn);

        Glide.with(this)
                .load(intent.getStringExtra("imageUri"))
                .error(R.drawable.common_full_open_on_phone)
                .into(poster);

        title.setText(intent.getStringExtra("title"));
        lowPrice.setText(intent.getStringExtra("price1"));
        highPrice.setText(intent.getStringExtra("price2"));
        summary.setText(intent.getStringExtra("summary"));
        youtubeUri= intent.getStringExtra("youtubeUri");

        youTubePlayerView.initialize(youtubeUri, onInitializedListener);
        reviewAddBtn.setOnClickListener(reviewAddBtnListener);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);

        ratingRecyclerVeiw.setLayoutManager(layoutManager);
        ratingRecyclerVeiw.setHasFixedSize(true);

        readRatingData();

    }

    private void readRatingData() {
        movieTitle =  intent.getStringExtra("title");

        ratingRef.document(movieTitle).collection("review").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    float scoreSum = 0;
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : documentSnapshots) {
                            data = new RatingObj();
                            data.setId(documentSnapshot.toObject(RatingObj.class).getId());
                            data.setReview(documentSnapshot.toObject(RatingObj.class).getReview());
                            data.setScore(documentSnapshot.toObject(RatingObj.class).getScore());

                            dataList.add(data);
                            scoreSum += data.getScore();
                        }

                        scoreSum = (float) (Math.round(scoreSum / dataList.size() * 100) / 100.0);
                        ratingBar.setRating(scoreSum);

                        adapter = new DetailMoviewAdapter(dataList);
                        ratingRecyclerVeiw.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DetailMovieActivity.this,"리뷰 데이터가 없습니다",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private View.OnClickListener reviewAddBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            customDialog = new CustomDialog(DetailMovieActivity.this, movieTitle);
            customDialog.show();

            customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // 다이얼로그 종료시 데이터 변화 적용 감지 리스너
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    };

    private YouTubePlayer.OnInitializedListener onInitializedListener = new YouTubePlayer.OnInitializedListener() {
        @Override
        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
            youTubePlayer.loadVideo(youtubeUri);
        }

        @Override
        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

        }
    };

}