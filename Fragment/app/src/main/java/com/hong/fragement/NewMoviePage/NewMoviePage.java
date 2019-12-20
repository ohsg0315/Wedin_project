package com.hong.fragement.NewMoviePage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hong.fragement.AdapterForMovieList;
import com.hong.fragement.MovieDetailPage.DetailMovieActivity;
import com.hong.fragement.MovieObj;
import com.hong.fragement.R;

import java.util.ArrayList;
import java.util.List;

public class NewMoviePage extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RecyclerView recyclerView;
    private NewMoviePageAdapter mNewMoviePageAdapter;

    private List<MovieObj> movieObjList;
    private MovieObj data;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_movie_page);

        movieObjList = new ArrayList<MovieObj>();

        this.context = this;

        recyclerView = findViewById(R.id.recyclerview_newmovie);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        readDataForNewMovie();

    }

    public void readDataForNewMovie()
    {
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
                        data.setGenre(queryDocumentSnapshot.toObject(MovieObj.class).getGenre());
                        movieObjList.add(data);
                    }
                    mNewMoviePageAdapter = new NewMoviePageAdapter(movieObjList, context, listener);
                    recyclerView.setAdapter(mNewMoviePageAdapter);
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
            intent.setClass(NewMoviePage.this, DetailMovieActivity.class);

            intent.putExtra("title",selectedMovie.getTitle());
            intent.putExtra("dataFlag","1");

            startActivity(intent);
        }
    };
}
