package com.hong.fragement.MyPage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.hong.fragement.MovieObj;
import com.hong.fragement.R;

import java.util.ArrayList;

public class MyMovieList extends AppCompatActivity {

    ArrayList<MovieObj> movieDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_movie_list);

        this.InitializeMovieData();

        ListView listView = (ListView)findViewById(R.id.mml_listView);
        final MyMovieAdapter myMovieAdapter = new MyMovieAdapter(this,movieDataList);

        listView.setAdapter(myMovieAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
                Log.d("테스트", "영화 누름");
            }
        });
    }

    public void InitializeMovieData()
    {
        movieDataList = new ArrayList<MovieObj>();
        /*
        movieDataList.add(new MovieObj(R.drawable.sign_up_password_false, "미션임파서블","15세 이상관람가"));
        movieDataList.add(new MovieObj(R.drawable.sign_up_password_true, "아저씨","19세 이상관람가"));
        movieDataList.add(new MovieObj(R.drawable.movie_icon, "어벤져스","12세 이상관람가"));
        */
    }
}
