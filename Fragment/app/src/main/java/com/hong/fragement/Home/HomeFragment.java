package com.hong.fragement.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hong.fragement.MovieInfo;
import com.hong.fragement.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener{


    private DatabaseReference mFirebaseDatabaseReference;

    private SearchView searchView;
    private Button btn1,bt2,btn3,btn4;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private HomeAdapter mAdapter;

    private ArrayList<MovieInfo> moiveData;
    private MovieInfo dto;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        mRecyclerView = v.findViewById(R.id.recyclerview1);
        mRecyclerView.setHasFixedSize(true); // 리사이클러뷰 안의 아이템 크기를 일정하게 고정
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);

        moiveData = new ArrayList<>();
        dto = new MovieInfo();

        searchView = (SearchView) v.findViewById(R.id.searchForm);

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mFirebaseDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String poster = data.child("image").getValue().toString();
                    dto.setPoster(poster);
                    moiveData.add(dto);
                }

                mAdapter = new HomeAdapter(moiveData, getActivity());
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;
    }



    @Override
    public void onClick(View view) {

        if(view == searchView) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
    }




}
