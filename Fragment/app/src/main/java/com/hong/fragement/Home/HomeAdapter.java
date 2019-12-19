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

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MovieViewHolder> {

    private HomeFragment.OnItemClick listener;
    private ArrayList<MovieObj> movieData;
    Context context;

    public HomeAdapter(ArrayList<MovieObj> movieData, Context context, HomeFragment.OnItemClick listener) {
        this.movieData = movieData;
        this.context = context;
        this.listener = listener;
    }

    // View Holder 생성시, movie_list.xml와 holder를 연결
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list, parent, false);

        MovieViewHolder holder = new MovieViewHolder(itemView);
        return holder;

    }

    // holder item과 movieData에서 얻은 ImageUri를 바인드
    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {

        Glide.with(holder.itemView.getContext())
                .load(movieData.get(position).getImageUri())
                .error(R.drawable.common_full_open_on_phone)
                .into(holder.poster);
    }


    @Override
    public int getItemCount() {
        return movieData.size();
    }

    // MovieViewHolder 생성
    public class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;

        public MovieViewHolder(View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.poster);


        poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null) {
                    listener.onMovieSelected(movieData.get(getAdapterPosition()));
                }
            }
        });

        }

    }
}
