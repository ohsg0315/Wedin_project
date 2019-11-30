package com.hong.fragement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterForFreeMovie extends RecyclerView.Adapter<AdapterForFreeMovie.FreeMovieViewHolder> {


    private List<MovieObj> list;
    Context context;

    public AdapterForFreeMovie(List<MovieObj> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public FreeMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.each_movie_list,parent,false);

        FreeMovieViewHolder freeMovieViewHolder = new FreeMovieViewHolder(view);

        return freeMovieViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FreeMovieViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class FreeMovieViewHolder extends RecyclerView.ViewHolder {

        
        public FreeMovieViewHolder(@NonNull View itemView) {


            super(itemView);
        }
    }
}
