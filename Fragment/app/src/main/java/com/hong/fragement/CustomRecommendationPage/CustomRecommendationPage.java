package com.hong.fragement.CustomRecommendationPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hong.fragement.MovieObj;
import com.hong.fragement.NewMoviePage.AdapterForNewMovie;
import com.hong.fragement.R;
import com.hong.fragement.SearchResult.AdapterForSearchResult;

import java.util.ArrayList;
import java.util.List;

public class CustomRecommendationPage extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RecyclerView recyclerView;
    private AdapterForCustomRecommendationPage mAdapterForCustomRecommendationPage;

    private List<MovieObj> movieObjList;
    private MovieObj data;
    private Context context;


    private String TAG = "-----검색결과-----";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_recommendation_page);

        movieObjList = new ArrayList<MovieObj>();

        this.context = this;

        recyclerView = findViewById(R.id.recyclerview_custom_recommendation);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));


        readDataForAdapterForCustomRecommendation();
    }

    private void readDataForAdapterForCustomRecommendation() {
        data = new MovieObj();
        CollectionReference collectionReference
                = db.collection("Movie");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                    {
                        data = queryDocumentSnapshot.toObject(MovieObj.class);



                        data.setTitle(queryDocumentSnapshot.toObject(MovieObj.class).getTitle());
                        data.setImageUri(queryDocumentSnapshot.get("imageUri").toString());
                        data.setPrice(queryDocumentSnapshot.toObject(MovieObj.class).getPrice());
                        data.setSummary(queryDocumentSnapshot.toObject(MovieObj.class).getSummary());

                        movieObjList.add(data);


                    }
                    mAdapterForCustomRecommendationPage = new AdapterForCustomRecommendationPage(movieObjList, context  );
                    recyclerView.setAdapter(mAdapterForCustomRecommendationPage);

                }
                else
                {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }


            }
        });
    }
}
