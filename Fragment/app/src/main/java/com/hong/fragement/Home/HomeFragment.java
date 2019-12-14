    package com.hong.fragement.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

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
import com.hong.fragement.MovieDetailPage.DetailMovieActivity;
import com.hong.fragement.MovieObj;
import com.hong.fragement.R;

import java.util.ArrayList;
import java.util.Map;

public class HomeFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private AutoCompleteTextView movieSearchBar;
    private Button searchBtn;

    private RecyclerView recyclerView1;

    private MovieObj data;
    private HomeAdapter adapter;
    private ArrayList<String> titleList;
    private ArrayList<MovieObj> datList;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // searchBtn = view.findViewById(R.id.)
        recyclerView1 = view.findViewById(R.id.recyclerview1);
        movieSearchBar = view.findViewById(R.id.movie_search_bar);

        /*
        movieSearchBar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == keyEvent.KEYCODE_SPACE) return true;
                else return false;
            }
        });

         */

        recyclerView1.setHasFixedSize(true); // 리사이클러뷰 안의 아이템 크기를 일정하게 고정
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));
        // recyclerView 나열방식 : Horizontal

        readMovieData(); // DB에서 데이터를 빼와 리스트에 담고 adapter를 생성, 세팅하는 메서드

        // 서치바에 띄어줄 데이터를 담을 리스트를 생성하고 연결
        titleList = new ArrayList<String>();
        ArrayAdapter<String> titleListAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,titleList);
        movieSearchBar.setAdapter(titleListAdapter);

        return view;
    }

    // DB에서 데이터를 가져와 리사이클러뷰에 세팅해주는 메서드
    private void readMovieData() {
        datList = new ArrayList<MovieObj>();

        db.collection("Movie").get()    // DB의 "Movie" 컬렉션에서 데이터 가져옴
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

                                titleList.add(document.toObject(MovieObj.class).getTitle());
                                datList.add(data);
                            }
                            adapter = new HomeAdapter(datList, getActivity(),listener); // 데이터 생성. Context : getActivity (프래그먼트 이용)
                            recyclerView1.setAdapter(adapter);

                        }
                    }
                });
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

            intent.putExtra("imageUri",selectedMovie.getImageUri());
            intent.putExtra("title",selectedMovie.getTitle());
            intent.putExtra("price1",naverPrice);
            intent.putExtra("price2",wavePrice);
            intent.putExtra("summary",selectedMovie.getSummary());
            intent.putExtra("title",selectedMovie.getTitle());
            intent.putExtra("youtubeUri",selectedMovie.getYoutubeUri());
            intent.putExtra("dataFlag","2");

            startActivity(intent);
        }
    };

    /*
    // 영화 입력 후 검색 버튼 클릭시 상세 페이지 전환 이벤트
    private View.OnClickListener btnClcikListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(getContext(),DetailMovieActivity.class);

            intent.putExtra("title",movieSearchBar.getText().toString().trim());
            intent.putExtra("dataFlag","1");

            startActivity(intent);
        }
    };

     */

    public interface OnItemClick {
        void onMovieSelected(MovieObj selectedMovie);
    }

}
