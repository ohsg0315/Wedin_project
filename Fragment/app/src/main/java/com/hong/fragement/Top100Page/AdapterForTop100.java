package com.hong.fragement.Top100Page;


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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdapterForTop100 extends RecyclerView.Adapter<AdapterForTop100.AdapterForTop100ViewHolder> {

    Context context;
    private List<MovieObj> list;
    private List<Integer> priceArray;
    private Top100Page.OnItemClick listner;
    int i;

    public AdapterForTop100(List<MovieObj> list, Context context, Top100Page.OnItemClick listner) {
        this.list = list;
        this.context = context;
        this.listner = listner;
    }

    @NonNull
    @Override
    public AdapterForTop100ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.each_movie_list,parent,false);

        AdapterForTop100ViewHolder freeMovieViewHolder = new AdapterForTop100ViewHolder(view);
        Collections.sort(list);

        return freeMovieViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterForTop100ViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(list.get(position).getImageUri())
                .into(holder.posterImage);

        holder.title.setText(list.get(position).getTitle());
        holder.summary.setText(list.get(position).getSummary());
        holder.genre.setText(list.get(position).getGenre());
        priceArray = new ArrayList<>();
        for (String key: list.get(position).getPrice().keySet()) {
            priceArray.add((int)list.get(position).getPrice().get(key));
            //Log.d("-----가격배열-----",list.get(position).getPrice().get(key).toString());
        }
        Collections.sort(priceArray);
        holder.price.setText(priceArray.get(0).toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AdapterForTop100ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView summary;
        TextView price;
        ImageView posterImage;
        TextView genre;

        public AdapterForTop100ViewHolder(@NonNull View itemView) {

            super(itemView);
            title=(TextView)itemView.findViewById(R.id.title_each_movie_list);
            summary=(TextView)itemView.findViewById(R.id.story_each_movie_list);
            price= (TextView)itemView.findViewById(R.id.price_each_movie_list);
            posterImage=(ImageView)itemView.findViewById(R.id.poster_each_movie_list);
            genre =(TextView) itemView.findViewById(R.id.genre1_each_movie_list);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listner.onMovieSelected(list.get(getAdapterPosition()));
                }
            });
        }
    }


}
