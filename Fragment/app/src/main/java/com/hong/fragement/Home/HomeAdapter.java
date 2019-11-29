package com.hong.fragement.Home;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.hong.fragement.MovieInfo;
import com.hong.fragement.R;

import java.util.ArrayList;



public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MovieViewHolder> {
    public String TAG = "=======================";
    private ArrayList<MovieInfo> movieData;
    Context context;

    public HomeAdapter(ArrayList<MovieInfo> movieData, Context context) {
        this.movieData = movieData;
        this.context = context;
    }

    // View Holder 생성, movie_list.xml와 holder를 연결
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "홀더생성중.");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list,parent,false);

        MovieViewHolder holder = new MovieViewHolder(itemView);
        return holder;

    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        Glide.with(holder.itemView.getContext())
                .load(movieData.get(position).getPoster())
                .error(R.drawable.common_full_open_on_phone)
                .into(holder.poster);

    }

    @Override
    public int getItemCount() {
        Log.d(TAG, movieData.size()+"입니다");
        return movieData.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;

        public MovieViewHolder(View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.poster);
        }
    }


}
