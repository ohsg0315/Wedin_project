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
import com.hong.fragement.MovieObj;
import com.hong.fragement.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private String TAG = "Read Data from FireStore";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Button btn1,bt2,btn3,btn4;

    private RecyclerView recyclerView;
    private HomeAdapter mAdapter;
    private ArrayList<MovieObj> moiveData;
    private MovieObj dto, data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        moiveData = new ArrayList<MovieObj>();  // MovieObj Type의 data ArrayList에 담기위해 객체 생성

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 안의 아이템 크기를 일정하게 고정
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));
        // recyclerView 나열방식 : Horizontal

        readImageUri(); // DB에서 데이터를 빼와 리스트에 담고 adapter를 생성, 세팅하는 메서드

        return view;
    }


    private void readImageUri() {

        data = new MovieObj();  // DB에서 빼온 imageUri를 MovieObj 객체에 넣기위해 생성

        db.collection("Movie").get()    // DB의 "Movie" 컬렉션에서 데이터 가져옴
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {   // 스냅샷을 떠서 MovieObj 타입으로 변환 후 리스트에 담음
                                dto = new MovieObj();
                                data = document.toObject(MovieObj.class);
                                dto.setImageUri(data.getImageUri());
                                moiveData.add(dto);
                            }

                            mAdapter = new HomeAdapter(moiveData, getActivity());   // 데이터 생성. Context : getActivity (프래그먼트 이용)
                            recyclerView.setAdapter(mAdapter);
                        }
                    }
                });

    }


}
