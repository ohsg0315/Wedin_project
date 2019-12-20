package com.hong.fragement.Home;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hong.fragement.MovieObj;
import com.hong.fragement.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Top100Adapter extends RecyclerView.Adapter<Top100Adapter.Top100ViewHolder> {

    Context context;
    private ArrayList<MovieObj> list;
    private HomeFragment.OnItemClick listner;

    public Top100Adapter(ArrayList<MovieObj> list, Context context, HomeFragment.OnItemClick listner) {
        this.list = list;
        this.context = context;
        this.listner = listner;
    }

    @NonNull
    @Override
    public Top100ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list,parent,false);

        Top100ViewHolder freeMovieViewHolder = new Top100ViewHolder(view);
        Collections.sort(list);


        return freeMovieViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Top100ViewHolder holder, int position) {

        Glide.with(holder.itemView)
                .load(list.get(position).getImageUri())
                .into(holder.poster);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class Top100ViewHolder extends RecyclerView.ViewHolder {

        ImageView poster;

        public Top100ViewHolder(@NonNull View itemView) {

            super(itemView);
            poster = itemView.findViewById(R.id.poster);
            poster.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    listner.onMovieSelected(list.get(getAdapterPosition()));
                }
            });
        }
    }

}
