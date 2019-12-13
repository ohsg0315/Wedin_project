package com.hong.fragement.MovieDetailPage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hong.fragement.R;

import java.util.ArrayList;

public class DetailMoviewAdapter extends RecyclerView.Adapter<DetailMoviewAdapter.MovieViewHolder> {

    private ArrayList<RatingObj> dataList;

    public DetailMoviewAdapter(ArrayList<RatingObj> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_list,parent,false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.id.setText(dataList.get(position).getId());
        holder.review.setText(dataList.get(position).getReview());
        holder.score.setRating(dataList.get(position).getScore());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        TextView review;
        RatingBar score;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.rating_id);
            review = itemView.findViewById(R.id.rating_review);
            score = itemView.findViewById(R.id.rating_score);
        }
    }

}
