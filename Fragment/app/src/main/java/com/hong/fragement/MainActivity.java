package com.hong.fragement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hong.fragement.Event.EventPage;
import com.hong.fragement.Home.HomeFragment;
import com.hong.fragement.Login.LoginActivity;
import com.hong.fragement.MyPage.MemberInfo;
import com.hong.fragement.MyPage.MemberObj;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String TAG = "Read Data from FireStore";
    private Map<String, Object> uriLink;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FragmentTransaction fragmentTransaction;

    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mFirebaseAuth;
    private String mUsername;
    private String mPhotoUrl;

    private MemberObj memberObj;

    // 프래그먼트 클래스
    private HomeFragment homeFragment;
    private FreeMovie freeMovie;
    private EventPage eventPage;

    private BottomNavigationView navigation;
    private MenuItem navLogInOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        // 로그인 후 화면에 default fragment 설정
        homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, homeFragment).commit();

        // 하단 네비게이션
        navigation = findViewById(R.id.navigation);     // activity_main BottomNavigationView id
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home); // fragment default

        // 오른쪽 네비게이션
        drawerLayout = findViewById(R.id.drawer_layout);    // activity_main DrawerLayout id
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); // 잠금 : 손가락으로 밀어도 안열림
        navigationView = findViewById(R.id.navigation_view);    // activity_main NavigationView id
        navigationView.setNavigationItemSelectedListener(this);

        // 로그인 상태일 경우 회원 정보를 받아온다.
        if (mFirebaseUser != null) {
            ReadUserData();
        }
    }


    private void ReadUserData() {
        DocumentReference docRef = db.collection("Users").document(mFirebaseUser.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                memberObj = documentSnapshot.toObject(MemberObj.class);
            }
        });
    }

    /* 영화 데이터 베이스 불러오기용 코드 */
    private void ReadData() {
        DocumentReference docRef = db.collection("Movie").document("조커");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                MovieObj movieObj = documentSnapshot.toObject(MovieObj.class);

                String movieTitle = movieObj.getTitle();
                String movieSummary = movieObj.getSummary();
                String movieImageUri = movieObj.getImageUri();
                Map<String, Integer> moviePrice = movieObj.getPrice();
                int naverPrice = moviePrice.get("네이버");
                int wavePrice = moviePrice.get("웨이브");

                Log.d(TAG, movieTitle);
                Log.d(TAG, movieSummary);
                Log.d(TAG, movieImageUri);
                Log.d(TAG, "네이버 = " + moviePrice.get("네이버") + ", 웨이브 = " + moviePrice.get("웨이브"));
            }
        });

    }

    /* 코드 끝 */

    // 하단 네비게이션바 버튼 클릭 이벤트
    public BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();

            // 하단바 아이템에 따른 프래그먼트
            switch (menuItem.getItemId()) {
                case R.id.navigation_home:
                    homeFragment = new HomeFragment();
                    fragmentTransaction.replace(R.id.frame, homeFragment);
                    break;
                case R.id.navigation_freemovie:
                    freeMovie = new FreeMovie();
                    fragmentTransaction.replace(R.id.frame, freeMovie);
                    break;
                case R.id.navigation_eventpage:
                    eventPage = new EventPage();
                    fragmentTransaction.replace(R.id.frame, eventPage);
                    break;
                case R.id.navigation_mypage:
                    drawerLayout.openDrawer(navigationView);    // 네이게이션 오픈
                    drawerLayout.addDrawerListener(listener);
                    return true;
            }

            fragmentTransaction.addToBackStack(null); // 백스택에 저장 x → 백키 눌러도 안나타남
            fragmentTransaction.commit();
            return true;
        }
    };

    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            changeOptionMenuItem();
        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
        }

        @Override
        public void onDrawerStateChanged(int newState) {
        }
    };

    // 우측 네비게이션 아이템 이벤트핸들러
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (menuItem.getItemId()) {
            case R.id.nav_loginout:
                if (navLogInOut.getTitle().equals("로그아웃")) {
                    LogoutAlertDialog();
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                }
                break;
            case R.id.navigation_right_mypage:
                if (mFirebaseUser != null) {

                    //내 정보를 객체로 전달하여 MemberInfo로 들어감.
                    Intent intent = new Intent(getApplicationContext(), MemberInfo.class);
                    intent.putExtra("member", memberObj);
                    myStartActivity(MemberInfo.class);
                    startActivity(intent);

                    //String email = profile.getEmail();
                    //Uri photoUrl = profile.getPhotoUrl();

                }
                break;
        }

        transaction.addToBackStack(null);
        transaction.commit();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    private void changeOptionMenuItem() {
        Menu nav_menu = navigationView.getMenu();
        navLogInOut = nav_menu.findItem(R.id.nav_loginout);

        if (mFirebaseUser == null) {
            navLogInOut.setTitle("로그인"); // default : 로그아웃
            nav_menu.findItem(R.id.nav_reservation_information).setVisible(false);
            nav_menu.findItem(R.id.navigation_right_mypage).setVisible(false);
        } else navLogInOut.setTitle("로그아웃");
    }

    // 로그아웃 다이얼로그
    private void LogoutAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mFirebaseAuth.signOut();

                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
}




