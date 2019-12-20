package com.hong.fragement.MovieDetailPage;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hong.fragement.MovieObj;
import com.hong.fragement.R;

import java.util.ArrayList;
import java.util.Map;

public class DetailMovieActivity extends YouTubeBaseActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference ratingRef = db.collection("Rating");

    private YouTubePlayerView youTubePlayerView;

    private String youtubeUri;
    private ImageView poster;
    private TextView title;
    private TextView lowPrice;
    private TextView highPrice;
    private TextView playPrice;
    private TextView yesPrice;
    private TextView summary;
    private TextView ratingScoreView;
    private TextView genre;
    private Button reviewAddBtn;
    private Button summaryReadBtn;


    static ArrayList<RatingObj> dataList;
    private RatingObj data;
    private RatingBar ratingBar;

    private Intent intent;
    private RecyclerView ratingRecyclerVeiw;
    private DetailMoviewAdapter adapter;
    private CustomDialog customDialog;
    private String movieTitle;

    private int dataFlag;
    private String summayStory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_movie_activity);

        dataList = new ArrayList<RatingObj>();
        intent = getIntent();
        dataFlag = Integer.parseInt(intent.getStringExtra("dataFlag"));
        movieTitle = intent.getStringExtra("title");

        youTubePlayerView = findViewById(R.id.youtubeview);
        poster = findViewById(R.id.poster_detail_movie);
        title = findViewById(R.id.title_detail_movie);
        lowPrice = findViewById(R.id.movie_low_price);
        highPrice = findViewById(R.id.movie_high_price);
        playPrice = findViewById(R.id.movie_play_price);
        yesPrice = findViewById(R.id.movie_yes_price);
        summary = findViewById(R.id.summary_detail_movie);
        summaryReadBtn = findViewById(R.id.story_read);
        genre = findViewById(R.id.genre_detail_movie);

        ratingScoreView = findViewById(R.id.rating_detail_movie);
        ratingBar = findViewById(R.id.ratingBar_detail_movie);
        ratingRecyclerVeiw = findViewById(R.id.rating_recyclerview);
        reviewAddBtn = findViewById(R.id.review_add_btn);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);

        ratingRecyclerVeiw.setLayoutManager(layoutManager);
        ratingRecyclerVeiw.setHasFixedSize(true);
        ratingRecyclerVeiw.addItemDecoration(new DividerItemDecoration(this, 1));   // 댓글마다 구분선

        reviewAddBtn.setOnClickListener(reviewAddBtnListener);
        summaryReadBtn.setOnClickListener(summaryReadListener);

        setPageView(dataFlag);
        readRatingData();
    }

    // HomeeFragmet에서 intent 방식에 따라 DetailMovieActivity의 View를 세팅해줌
    private void setPageView(int flag) {

        switch(flag) {
            case 1:     // 검색 타이틑을  가져와 DB에서 검색해 View 세팅
                db.collection("Movie").document(movieTitle).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Map<String,Integer> moviePrice = documentSnapshot.toObject(MovieObj.class).getPrice();
                                int naverPrice = moviePrice.get("네이버");
                                int wavePrice = moviePrice.get("웨이브");
                                int yesPriceData = moviePrice.get("예스24");
                                int playPriceData = moviePrice.get("플레이스토어");

                                youtubeUri = documentSnapshot.toObject(MovieObj.class).getYoutubeUri();
                                youTubePlayerView.initialize(youtubeUri, onInitializedListener);

                                Glide.with(DetailMovieActivity.this)
                                        .load(documentSnapshot.toObject(MovieObj.class).getImageUri())
                                        .error(R.drawable.common_full_open_on_phone)
                                        .into(poster);

                                title.setText(movieTitle);
                                lowPrice.setText(Integer.toString(naverPrice));
                                highPrice.setText(Integer.toString(wavePrice));
                                yesPrice.setText(Integer.toString(yesPriceData));
                                playPrice.setText(Integer.toString(playPriceData));

                                genre.setText(documentSnapshot.toObject(MovieObj.class).getGenre());
                                summayStory = documentSnapshot.toObject(MovieObj.class).getSummary();
                                summary.setText(summayStory);
                            }
                        });
                break;

            case 2 :    // 선택한 포스터의 정보를 intent로 가져와 바로 세팅
                Glide.with(DetailMovieActivity.this)
                        .load(intent.getStringExtra("imageUri"))
                        .error(R.drawable.common_full_open_on_phone)
                        .into(poster);

                youtubeUri = intent.getStringExtra("youtubeUri");
                youTubePlayerView.initialize(youtubeUri, onInitializedListener);
                title.setText(movieTitle);
                lowPrice.setText(intent.getStringExtra("price1"));
                highPrice.setText(intent.getStringExtra("price2"));
                yesPrice.setText(intent.getStringExtra("price3"));
                playPrice.setText(intent.getStringExtra("price4"));

                genre.setText(intent.getStringExtra("genre"));
                summayStory = intent.getStringExtra("summary");
                summary.setText(summayStory);

                break;
        }
    }

    private void readRatingData() {

        ratingRef.document(movieTitle).collection("review").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : documentSnapshots) {
                            data = new RatingObj();
                            data.setId(documentSnapshot.toObject(RatingObj.class).getId());
                            data.setReview(documentSnapshot.toObject(RatingObj.class).getReview());
                            data.setScore(documentSnapshot.toObject(RatingObj.class).getScore());

                            dataList.add(data);
                        }

                        setRatingBarScore();

                        adapter = new DetailMoviewAdapter(dataList);
                        ratingRecyclerVeiw.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DetailMovieActivity.this, "리뷰 데이터가 없습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private View.OnClickListener summaryReadListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(DetailMovieActivity.this);
            builder.setTitle(movieTitle + " 줄거리").setMessage(summayStory);
            builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {}
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
    };



    private View.OnClickListener reviewAddBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            customDialog = new CustomDialog(DetailMovieActivity.this, movieTitle);
            customDialog.show();

            customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    readRatingData();
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

    private void setRatingBarScore() {
        float scoreSum = 0;

        for (int num= 0; num<dataList.size(); num++) {
            scoreSum += dataList.get(num).getScore();
        }
        scoreSum = (float) (Math.round(scoreSum / dataList.size() * 10) / 10.0);
        ratingScoreView.setText(Float.toString(scoreSum));
        ratingBar.setRating(scoreSum);;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        }

}