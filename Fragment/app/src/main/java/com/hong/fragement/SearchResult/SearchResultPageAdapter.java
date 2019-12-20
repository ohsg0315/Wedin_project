package com.hong.fragement.SearchResult;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hong.fragement.MovieObj;
import com.hong.fragement.R;
import com.hong.fragement.Top100Page.Top100Page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchResultPageAdapter extends RecyclerView.Adapter<SearchResultPageAdapter.FreeMovieViewHolder> {

    Context context;
    private List<MovieObj> list;
    private List<Integer> priceArray;
    private SearchResultPage.OnItemClick listener;
    int i;

    public SearchResultPageAdapter(List<MovieObj> list, Context context, SearchResultPage.OnItemClick listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FreeMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.each_movie_list, parent, false);

        FreeMovieViewHolder freeMovieViewHolder = new FreeMovieViewHolder(view);

        return freeMovieViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FreeMovieViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(list.get(position).getImageUri())
                .into(holder.posterImage);

        holder.title.setText(list.get(position).getTitle());
        holder.summary.setText(list.get(position).getSummary());

        priceArray = new ArrayList<>();
        for (String key : list.get(position).getPrice().keySet()) {
            priceArray.add((int) list.get(position).getPrice().get(key));
        }
        Collections.sort(priceArray);
        holder.price.setText(priceArray.get(0).toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class FreeMovieViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView summary;
        TextView price;
        ImageView posterImage;

        public FreeMovieViewHolder(@NonNull View itemView) {

            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_each_movie_list);
            posterImage = (ImageView) itemView.findViewById(R.id.poster_each_movie_list);
            price = (TextView) itemView.findViewById(R.id.price_each_movie_list);
            summary = (TextView) itemView.findViewById(R.id.story_each_movie_list);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onMovieSelected(list.get(getAdapterPosition()));
                }
            });
        }
    }
}