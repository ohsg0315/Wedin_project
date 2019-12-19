package com.hong.fragement.SearchResult;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hong.fragement.MovieDetailPage.DetailMovieActivity;
import com.hong.fragement.MovieObj;
import com.hong.fragement.R;
import com.hong.fragement.Top100Page.Top100Page;

import java.util.ArrayList;
import java.util.List;

public class SearchResultPage extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RecyclerView recyclerView;
    private SearchResultPageAdapter mSearchResultPageAdapter;

    private List<MovieObj> movieObjList;
    private MovieObj data;
    private Context context;
    private String searchContent;

    private String TAG = "-----검색결과-----";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_page);

        Intent intent = getIntent();
        searchContent = intent.getStringExtra("title");

        movieObjList = new ArrayList<MovieObj>();

        this.context = this;

        recyclerView = findViewById(R.id.recyclerview_search_result);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        readDataForSearchResult();
    }

    private void readDataForSearchResult() {

        db.collection("Movie")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                String compareTitle = queryDocumentSnapshot.toObject(MovieObj.class).getTitle();
                                if (compareTitle.contains(searchContent)) {
                                    data = queryDocumentSnapshot.toObject(MovieObj.class);

                                    data.setTitle(queryDocumentSnapshot.toObject(MovieObj.class).getTitle());
                                    data.setImageUri(queryDocumentSnapshot.get("imageUri").toString());
                                    data.setPrice(queryDocumentSnapshot.toObject(MovieObj.class).getPrice());
                                    data.setSummary(queryDocumentSnapshot.toObject(MovieObj.class).getSummary());

                                    movieObjList.add(data);
                                }
                            }
                            mSearchResultPageAdapter = new  SearchResultPageAdapter(movieObjList,context,listener);
                            recyclerView.setAdapter(mSearchResultPageAdapter);
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
            intent.setClass(SearchResultPage.this, DetailMovieActivity.class);

            intent.putExtra("title",selectedMovie.getTitle());
            intent.putExtra("dataFlag","1");

            startActivity(intent);
        }
    };




}
