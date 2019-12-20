package com.hong.fragement.CustomRecommendationPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hong.fragement.AdapterForMovieList;
import com.hong.fragement.MovieDetailPage.DetailMovieActivity;
import com.hong.fragement.MovieObj;
import com.hong.fragement.MyPage.MemberObj;
import com.hong.fragement.R;

import java.util.ArrayList;
import java.util.List;

public class CustomRecommendationPage extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mFirebaseAuth;

    private RecyclerView recyclerView;
    private CustomRecommendAdapter mCustomRecommendAdapter;

    private List<MovieObj> movieObjList;
    private MovieObj data;
    private MemberObj member;
    private Context context;
    private ArrayList<String> genre;
    private TextView mainGenre;
    private String TAG = "-----검색결과-----";
    private int preNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_recommendation_page);

        movieObjList = new ArrayList<MovieObj>();

        this.context = this;

        recyclerView = findViewById(R.id.recyclerview_custom_recommendation);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        Intent intent = getIntent();


        // 유저의 선호 영화장르 3개.
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser != null) {
            ReadUserData();

        }



    }

    private void ReadUserData() {
        DocumentReference docRef = db.collection("Users").document(mFirebaseUser.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                member = documentSnapshot.toObject(MemberObj.class);

                genre = new ArrayList<>();
                for (int i = 0; i < 3; i++)
                    genre.add(member.getGenre().get(i));

                readDataForAdapterForCustomRecommendation();
            }
        });
    }


    private void readDataForAdapterForCustomRecommendation() {
        data = new MovieObj();
        preNum = (int) ((Math.random() * 10) % 3);
        Log.e("쓰벌", "선호 장르" + preNum + " = " + genre.get(preNum));

        CollectionReference collectionReference
                = db.collection("Movie");
        collectionReference
                .whereEqualTo("genre", genre.get(preNum))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                data = queryDocumentSnapshot.toObject(MovieObj.class);

                                data.setTitle(queryDocumentSnapshot.toObject(MovieObj.class).getTitle());
                                data.setImageUri(queryDocumentSnapshot.get("imageUri").toString());
                                data.setPrice(queryDocumentSnapshot.toObject(MovieObj.class).getPrice());
                                data.setSummary(queryDocumentSnapshot.toObject(MovieObj.class).getSummary());
                                data.setGenre(queryDocumentSnapshot.toObject(MovieObj.class).getGenre());

                                movieObjList.add(data);


                            }
                            mCustomRecommendAdapter = new CustomRecommendAdapter(movieObjList, context, listener);
                            recyclerView.setAdapter(mCustomRecommendAdapter);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }


                    }
                });
    }
    public interface OnItemClick {
        void onMovieSelected(MovieObj selectedMovie);
    }

    private OnItemClick listener = new OnItemClick() {
        @Override
        public void onMovieSelected(MovieObj selectedMovie) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), DetailMovieActivity.class);

            intent.putExtra("title",selectedMovie.getTitle());
            intent.putExtra("dataFlag","1");

            startActivity(intent);
        }
    };
}
