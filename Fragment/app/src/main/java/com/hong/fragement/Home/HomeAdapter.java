package com.hong.fragement.Home;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hong.fragement.MovieInfo;
import com.hong.fragement.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MovieViewHolder> {
    private ArrayList<MovieInfo> movieData;
    private Context context;
    private static View.OnClickListener onClickListener;

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView poster;
        private TextView title;
        private TextView story;
        private TextView price;
        private TextView genre1;
        private TextView genre2;
        private TextView genre3;

        public MovieViewHolder(View v) {
            super(v);
            poster = v.findViewById(R.id.poster);
            /*
            다른 영화 정보 findViewById
             */

            v.setClickable(true);
            v.setEnabled(true); // 객체 활성화
            v.setOnClickListener(onClickListener);

        }

    }

    public HomeAdapter(ArrayList<MovieInfo> movieData,Context context) {
        this.movieData = movieData;
        this.context = context;
    }

    // View Holder 생성, movie_list.xml와 holder를 연결
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list,parent,false);

        MovieViewHolder holder = new MovieViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder,final int position) {

        MovieInfo info = movieData.get(position);

        Picasso.get()
                .load(info.getPoster())
                .fit()
                .centerInside()
                .into(holder.poster);
        // Uri uri = Uri.parse(info.getPoster());
    }

    @Override
    public int getItemCount() {
        return movieData.size();}
}
