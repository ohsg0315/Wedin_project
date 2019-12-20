package com.hong.fragement.Home;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hong.fragement.CustomRecommendationPage.CustomRecommendationPage;
import com.hong.fragement.MovieDetailPage.DetailMovieActivity;
import com.hong.fragement.MovieObj;
import com.hong.fragement.NewMoviePage.NewMoviePage;
import com.hong.fragement.R;
import com.hong.fragement.SearchResult.SearchResultPage;
import com.hong.fragement.Top100Page.AdapterForTop100;
import com.hong.fragement.Top100Page.Top100Page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class HomeFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private AutoCompleteTextView movieSearchBar;
    private ImageButton searchBtn;
    private Button btn_more_new;
    private Button btn_more_top100;

    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;

    private MovieObj data, data2;
    private HomeAdapter adapter;
    private Top100Adapter adapter2;
    private ArrayList<String> titleList;
    private ArrayList<MovieObj> datList;
    private ArrayList<MovieObj> datList2;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView1 = view.findViewById(R.id.recyclerview1);
        recyclerView2 = view.findViewById(R.id.recyclerview2);
        movieSearchBar = view.findViewById(R.id.movie_search_bar);
        searchBtn = view.findViewById(R.id.search_btn);

        btn_more_new = view.findViewById(R.id.more_new);
        btn_more_top100 = view.findViewById(R.id.more_top100);

        btn_more_new.setOnClickListener(btnPageIntentListener);
        btn_more_top100.setOnClickListener(btnPageIntentListener);

        recyclerView1.setHasFixedSize(true); // 리사이클러뷰 안의 아이템 크기를 일정하게 고정
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));
        // recyclerView 나열방식 : Horizontal

        recyclerView2.setHasFixedSize(true); // 리사이클러뷰 안의 아이템 크기를 일정하게 고정
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));

        readMovieData();    // 최신영화 : DB에서 데이터를 빼와 리스트에 담고 adapter를 생성, 세팅하는 메서드
        readMovieData2();   // Top100영화

        // 서치바에 띄어줄 데이터를 담을 리스트를 생성하고 연결
        titleList = new ArrayList<String>();
        ArrayAdapter<String> titleListAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,titleList);
        movieSearchBar.setAdapter(titleListAdapter);
        movieSearchBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent();
                intent.setClass(getActivity(), DetailMovieActivity.class);

                intent.putExtra("title",movieSearchBar.getText().toString());
                intent.putExtra("dataFlag","1");

                startActivity(intent);
            }
        });
        searchBtn.setOnClickListener(btnClcikListener);

        return view;
    }

    // 최신영화 : DB에서 데이터를 가져와 리사이클러뷰에 세팅해주는 메서드
    private void readMovieData() {
        datList = new ArrayList<MovieObj>();

        db.collection("Movie").limit(10).get()    // DB의 "Movie" 컬렉션에서 데이터 가져옴
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {   // 스냅샷을 떠서 MovieObj 타입으로 변환 후 리스트에 담음
                                data = new MovieObj();
                                data.setTitle(document.toObject(MovieObj.class).getTitle());
                                data.setImageUri(document.toObject(MovieObj.class).getImageUri());
                                data.setPrice(document.toObject(MovieObj.class).getPrice());
                                data.setSummary(document.toObject(MovieObj.class).getSummary());
                                data.setYoutubeUri(document.toObject(MovieObj.class).getYoutubeUri());
                                data.setGenre(document.toObject(MovieObj.class).getGenre());

                                titleList.add(document.toObject(MovieObj.class).getTitle());
                                datList.add(data);
                            }
                            adapter = new HomeAdapter(datList, getActivity(),listener); // 데이터 생성. Context : getActivity (프래그먼트 이용)
                            recyclerView1.setAdapter(adapter);
                        }
                    }
                });
    }

    // 최신영화 : DB에서 데이터를 가져와 리사이클러뷰에 세팅해주는 메서드
    private void readMovieData2() {
        datList2 = new ArrayList<MovieObj>();

        db.collection("Movie").get()    // DB의 "Movie" 컬렉션에서 데이터 가져옴
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {   // 스냅샷을 떠서 MovieObj 타입으로 변환 후 리스트에 담음
                                data2 = new MovieObj();
                                data2.setTitle(document.toObject(MovieObj.class).getTitle());
                                data2.setImageUri(document.toObject(MovieObj.class).getImageUri());
                                data2.setPrice(document.toObject(MovieObj.class).getPrice());
                                data2.setSummary(document.toObject(MovieObj.class).getSummary());
                                data2.setYoutubeUri(document.toObject(MovieObj.class).getYoutubeUri());
                                data2.setGenre(document.toObject(MovieObj.class).getGenre());
                                data2.setRank(document.toObject(MovieObj.class).getRank());
                                datList2.add(data2);
                            }
                            adapter2 = new Top100Adapter(datList2, getActivity(),listener); // 데이터 생성. Context : getActivity (프래그먼트 이용)
                            recyclerView2.setAdapter(adapter2);
                        }
                    }
                });
    }

    public interface OnItemClick {
        void onMovieSelected(MovieObj selectedMovie);
    }

    // 영화 포스터 클릭시 상세페이지 전환 이벤트
    private OnItemClick listener = new OnItemClick() {
        @Override
        public void onMovieSelected(MovieObj selectedMovie) {
            Intent intent = new Intent();
            intent.setClass(getContext(),DetailMovieActivity.class);

            Map<String, Integer> moviePrice = selectedMovie.getPrice();
            String naverPrice = moviePrice.get("네이버").toString();
            String wavePrice = moviePrice.get("웨이브").toString();
            String yesPrice = moviePrice.get("예스24").toString();
            String playPrice = moviePrice.get("플레이스토어").toString();

            intent.putExtra("imageUri",selectedMovie.getImageUri());
            intent.putExtra("title",selectedMovie.getTitle());
            intent.putExtra("price1",naverPrice);
            intent.putExtra("price2",wavePrice);
            intent.putExtra("price3",yesPrice);
            intent.putExtra("price4",playPrice);
            intent.putExtra("summary",selectedMovie.getSummary());
            intent.putExtra("title",selectedMovie.getTitle());
            intent.putExtra("youtubeUri",selectedMovie.getYoutubeUri());
            intent.putExtra("genre",selectedMovie.getGenre());
            intent.putExtra("dataFlag","2");

            startActivity(intent);
        }
    };

    // 영화 입력 후 검색 버튼 클릭시 상세 페이지 전환 이벤트
    private View.OnClickListener btnClcikListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (TextUtils.isEmpty(movieSearchBar.getText().toString())) {
                Toast.makeText(getActivity(),"글자를 입력해주세요!",Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent();
                intent.setClass(getContext(), SearchResultPage.class);

                intent.putExtra("title", movieSearchBar.getText().toString().trim());
                intent.putExtra("dataFlag", "1");

                startActivity(intent);
            }
        }
    };

    private View.OnClickListener btnPageIntentListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();

            switch (view.getId()) {
                case R.id.more_new :
                    intent.setClass(getActivity(), NewMoviePage.class);
                    break;
                case R.id.more_top100 :
                    intent.setClass(getActivity(), Top100Page.class);
                    break;
            }
            startActivity(intent);
        }
    };

}