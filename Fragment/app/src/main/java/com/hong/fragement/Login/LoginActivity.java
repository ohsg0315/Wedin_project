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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hong.fragement.MainActivity;
import com.hong.fragement.MyPage.MemberObj;
import com.hong.fragement.R;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "시발";
    private FirebaseAuth auth; // 파이어 베이스 인증 객체

    private GoogleSignInClient mGoogleSignInClient;
    public static final int RC_SIGN_IN = 1;
    private static final String GOOGLE_CLIENT_ID = "281264414900-14qhjboc6vlpjj2vk8g6idj1iucrrs44.apps.googleusercontent.com";

    private EditText userId;
    private EditText userPassowrd;

    private ImageView logo;

    private Button login;
    private Button signUpBtn;
    private Button idFind;
    private SignInButton googleBtn; // 구글 로그인 버튼
    public CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        callbackManager = CallbackManager.Factory.create();

        LoginButton facebookBtn = findViewById(R.id.login_button);
        facebookBtn.setReadPermissions("email", "public_profile");
        facebookBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(GOOGLE_CLIENT_ID)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        auth = FirebaseAuth.getInstance();  // 파이어베이스 인증 객체 초기화

        initSettings();
    }

    private void initSettings() {
        googleBtn = findViewById(R.id.google_btn);
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
        googleBtn.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onClick(View view) {
        if (view == signUpBtn)
            signUp();
        else if (view == googleBtn)
            signInByGoogle();
        else if (view == login)
            signInByOriginal(userId.getText().toString(), userPassowrd.getText().toString());
        else if (view == idFind)
            startIdFind();
        else if (view == logo)
            myStartActivity(MainActivity.class);
    }

    // 구글 로그인
    private void signInByGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        signInIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // 앱 자체 회원 로그인
    private void signInByOriginal(String email, String password) {
        if (email.length() > 0 && password.length() > 5) {
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
        } else {
            Toast.makeText(getApplicationContext(), "이메일 or 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
        }
    }

    // 아이디 찾기
    private void startIdFind() {
        Intent idFindIntent = new Intent(LoginActivity.this, FindInfo.class);
        startActivity(idFindIntent);
    }

    //앱 자체 회원가입
    private void signUp() {
        Intent signUpIntent = new Intent(LoginActivity.this, SignUp.class);
        startActivity(signUpIntent);
    }

    // 인증 성공시 Intent
    public void updateUI(FirebaseUser user) {
        if (user != null) {
            Log.d(TAG, "가자가자");
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = auth.getCurrentUser();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            //기존 구글 아이디 DB가 존재하지 않는 경우 초기화하여 DB생성
                            String year = "1900";
                            String month = "1";
                            String day = "1";
                            ArrayList<String> pGenre = new ArrayList<>();

                            for (int i = 0; i < 3; i++) {
                                pGenre.add(i, "드라마");
                            }

                            MemberObj newMemberObj = new MemberObj(acct.getEmail(), acct.getDisplayName(), year, month, day, pGenre, "Google");
                            db.collection("Users").document(user.getUid()).set(newMemberObj);

                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //  Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        // ...
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
}

