package com.hong.fragement.Home;

import android.graphics.Movie;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hong.fragement.MovieInfo;
import com.hong.fragement.MovieObj;
import com.hong.fragement.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Button btn1,bt2,btn3,btn4;

    private String TAG = "Read Data from FireStore";
    private RecyclerView recyclerView;
    private HomeAdapter mAdapter;
    private ArrayList<MovieInfo> moiveData;
    private MovieInfo dto;
    private MovieObj data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        moiveData = new ArrayList<MovieInfo>();

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 안의 아이템 크기를 일정하게 고정
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));
        readImageUri();



        return view;
    }

    private void readImageUri() {

        data = new MovieObj();


        db.collection("Movie").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                dto = new MovieInfo();
                                data = document.toObject(MovieObj.class);
                                String imageUri = data.getImageUri();
                                Log.d(TAG, imageUri+"이미지입니다.");
                                dto.setPoster(imageUri);
                                moiveData.add(dto);
                                Log.d(TAG, moiveData.size()+"임다");
                            }

                            mAdapter = new HomeAdapter(moiveData, getActivity());
                            recyclerView.setAdapter(mAdapter);
                        }
                    }
                });

    }


}
