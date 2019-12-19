package com.hong.fragement.MyPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hong.fragement.MovieObj;
import com.hong.fragement.R;

import java.util.ArrayList;

public class MyMovieAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<MovieObj> movieObj;

    public MyMovieAdapter(Context context, ArrayList<MovieObj> data){
        mContext = context;
        movieObj = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return movieObj.size();
    }

    @Override
    public Object getItem(int position) {
        return movieObj.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.activity_my_movie_list, null);

        ImageView imageView = (ImageView)view.findViewById(R.id.mml_poster);
        TextView movieName = (TextView)view.findViewById(R.id.mml_title);
        TextView summary = (TextView)view.findViewById(R.id.mml_price);

        //imageView.setImageResource(movieObj.get(position).);
        movieName.setText(movieObj.get(position).getTitle());
        summary.setText(movieObj.get(position).getSummary());

        return view;
    }
}
