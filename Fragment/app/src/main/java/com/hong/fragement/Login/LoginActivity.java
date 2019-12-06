package com.hong.fragement.Login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.hong.fragement.MainActivity;
import com.hong.fragement.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private FirebaseAuth auth; // 파이어 베이스 인증 객체
    private GoogleApiClient googleApiClient;  // 구글 API 클라이언트 객체
    private static final int REQ_SIGN_GOOGLE = 100; // 구글 로그인 결과 코드
    private static final String GOOGLE_WEB_CLIENT_ID = "281264414900-14qhjboc6vlpjj2vk8g6idj1iucrrs44.apps.googleusercontent.com";
    private EditText userId;
    private EditText userPassowrd;

    private ImageView logo;

    private Button login;
    private Button signUpBtn;
    private Button idFind;
    private SignInButton googleBtn; // 구글 로그인 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();


        // 구글 SignIn 버튼 클릭 시 기본적인 사항 연동
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))  // 웹 클라이언트 ID
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        auth = FirebaseAuth.getInstance();  // 파이어베이스 인증 객체 초기화

        googleBtn = findViewById(R.id.google_btn);
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, REQ_SIGN_GOOGLE);
            }
        });

        userId = findViewById(R.id.user_id);
        userPassowrd = findViewById(R.id.user_password);

        logo = findViewById(R.id.login_wedin_logo);
        login = findViewById(R.id.sign_in_btn);
        idFind = findViewById(R.id.id_find_btn);
        signUpBtn = findViewById(R.id.sign_up_btn);

        logo.setOnClickListener(this);
        userId.setOnClickListener(this);
        idFind.setOnClickListener(this);
        userPassowrd.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);
        login.setOnClickListener(this);

        // 로그아웃을 하지 않았다면, 자동 로그인
        /*
        if(auth.getCurrentUser() != null) {
            signIn();
        }
        */
    }


    // 구글 로그인 요청 시 결과값 받는 함수
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_SIGN_GOOGLE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            // 인증 결과가 성공
            if(result.isSuccess()){
                Log.d("시발", "인증 성공했어!!!");
                GoogleSignInAccount account = result.getSignInAccount();  // account에 구글 로그인 정보가 모두 담김
                resultLogin(account);  // 로그인 결과값 출력 메소드
            }
            else {
                Log.d("시발", "인증 실패했어!!!");
            }
        }
    }

    private void resultLogin(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    // 로그인이 성공했는지
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show();
                            Log.d("시발", "성공했어!!!");
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
                            Log.d("시발", "실패했어");
                        }
                    }
                });
    }

    // 앱 자체 회원 로그인
    private void signInByOriginal(String email, String password){
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = auth.getCurrentUser();
                            updateUI(user);
                            Toast.makeText(getApplicationContext(), "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            updateUI(null);
                            Toast.makeText(getApplicationContext(), "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }


    @Override
    public void onClick(View view) {
        if(view == signUpBtn) signUp();
        else if(view == login) signInByOriginal(userId.getText().toString(), userPassowrd.getText().toString());
        else if(view == idFind) startIdFind();
        else if(view == logo) myStartActivity(MainActivity.class);
    }

    private void startIdFind(){
        Intent idFindIntent = new Intent(LoginActivity.this, FindInfo.class);
        startActivity(idFindIntent);
    }
    private void signUp(){
        Intent signUpIntent = new Intent(LoginActivity.this, SignUp.class);
        startActivity(signUpIntent);
    }
    private void signIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQ_SIGN_GOOGLE);
    }

    // 인증 성공시 Intent
    public void updateUI(FirebaseUser user) {
        if (user != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("시발", "함수 실패했어!!!");
    }
}

