package com.hong.fragement.Top100Page;

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
import com.hong.fragement.AdapterForMovieList;
import com.hong.fragement.MovieObj;
import com.hong.fragement.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Top100Page extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RecyclerView recyclerView;
    private AdapterForTop100 mAdapterForTop100;

    private List<MovieObj> movieObjList;
    private MovieObj data;
    private Context context;
    private String parse;
    private int count=1;


    private String TAG = "-----TOP100!-----";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top100_page);

        movieObjList = new ArrayList<MovieObj>();

        this.context = this;

        recyclerView = findViewById(R.id.recyclerview_top100);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));


        readDataForTop100();
    }

    private void readDataForTop100() {

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
                        data.setRank(queryDocumentSnapshot.toObject(MovieObj.class).getRank());

                        movieObjList.add(data);
                        count++;




                }
                mAdapterForTop100 = new AdapterForTop100(movieObjList, context);
                recyclerView.setAdapter(mAdapterForTop100);

            }
            else
            {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }


        }
    });

}





}
