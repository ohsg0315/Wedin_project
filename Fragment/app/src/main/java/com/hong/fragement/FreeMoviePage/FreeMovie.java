package com.hong.fragement.FreeMoviePage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.hong.fragement.Top100Page.Top100Page;

import java.util.ArrayList;
import java.util.List;

public class FreeMovie extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RecyclerView recyclerView;
    private FreeAdapter mAFreeAdapter;

    private List<MovieObj> movieObjList;
    private MovieObj data;

    private String TAG = "-----무료영화-----";
    public FreeMovie() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.free_movie,container,false);
        movieObjList = new ArrayList<MovieObj>();

        recyclerView = view.findViewById(R.id.recyclerview_freemovie);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));

        readDataForFreeMovie();



        return view;
    }

    private void readDataForFreeMovie()
    {
        data = new MovieObj();
        CollectionReference collectionReference
                = db.collection("Movie");
        collectionReference.whereEqualTo("free",true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                    {
                        data = queryDocumentSnapshot.toObject(MovieObj.class);

                        data.setTitle(queryDocumentSnapshot.toObject(MovieObj.class).getTitle());
                        data.setImageUri(queryDocumentSnapshot.get("imageUri").toString());
                        //data.setPrice(queryDocumentSnapshot.toObject(MovieObj.class).getPrice());
                        data.setSummary(queryDocumentSnapshot.toObject(MovieObj.class).getSummary());
                        data.setGenre(queryDocumentSnapshot.toObject(MovieObj.class).getGenre());

                        movieObjList.add(data);
                    }
                    mAFreeAdapter = new FreeAdapter(movieObjList, getActivity(),listener);
                    recyclerView.setAdapter(mAFreeAdapter);

                }
                else
                {
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
            intent.setClass(getContext(), DetailMovieActivity.class);

            intent.putExtra("title",selectedMovie.getTitle());
            intent.putExtra("dataFlag","1");

            startActivity(intent);
        }
    };


}
