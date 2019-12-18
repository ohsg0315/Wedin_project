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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdapterForSearchResult extends RecyclerView.Adapter<AdapterForSearchResult.SearchResultHolder> {

    private List<MovieObj> list;
    Context context;
    private List<Integer> priceArray;
    int i;

    public AdapterForSearchResult(List<MovieObj> list, Context context)
    {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterForSearchResult.SearchResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.each_movie_list,parent,false);

        AdapterForSearchResult.SearchResultHolder searchResultHolder = new AdapterForSearchResult.SearchResultHolder(view);
        return searchResultHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultHolder holder, int position) {

        Glide.with(holder.itemView)
                .load(list.get(position).getImageUri())
                .into(holder.posterImage);
        holder.title.setText(list.get(position).getTitle());
        holder.summary.setText(list.get(position).getSummary());


        priceArray = new ArrayList<>();
        for (String key: list.get(position).getPrice().keySet())
        {

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

    public class SearchResultHolder extends RecyclerView.ViewHolder {

        protected TextView title;
        protected TextView summary;
        protected TextView price;
        protected ImageView posterImage;
        public SearchResultHolder(@NonNull View itemView) {
            super(itemView);

            title=(TextView)itemView.findViewById(R.id.title_each_movie_list);
            posterImage=(ImageView)itemView.findViewById(R.id.poster_each_movie_list);
            price= (TextView)itemView.findViewById(R.id.price_each_movie_list);
            summary=(TextView)itemView.findViewById(R.id.story_each_movie_list);
        }
    }
}
