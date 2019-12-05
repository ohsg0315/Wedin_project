package com.hong.fragement.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hong.fragement.MainActivity;
import com.hong.fragement.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private Spinner[] preferenceGenre = new Spinner[3];
    private ArrayAdapter arrayAdapter;

    private String TAG = "SignUpActivity";

    private Button signUpConfirmBtn;
    private EditText emailEdit, passwordEdit, repasswordEdit, nameEdit, yearEdit, monthEdit, dayEdit;
    private ImageView setImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setViewSpinner();

        signUpConfirmBtn = (Button) findViewById(R.id.sign_up_finish);

        emailEdit = findViewById(R.id.email_sign_up);
        passwordEdit = findViewById(R.id.password_sign_up);
        repasswordEdit = findViewById(R.id.re_password_sign_up);
        nameEdit = findViewById(R.id.name_sign_up);
        yearEdit = findViewById(R.id.birth_year);
        monthEdit = findViewById(R.id.birth_month);
        dayEdit = findViewById(R.id.birth_day);
        setImage = (ImageView) findViewById(R.id.set_image_sign_up);

        signUpConfirmBtn.setOnClickListener(this);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // 비밀번호 재확인 & 최소 6자리 만족
        repasswordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (passwordEdit.getText().toString().equals(repasswordEdit.getText().toString()) && passwordEdit.getText().toString().length() > 5) {
                    setImage.setImageResource(R.drawable.sign_up_password_true);
                } else {
                    setImage.setImageResource(R.drawable.sign_up_password_false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View view) {

        // 회원가입 완료 버튼
        if (view == signUpConfirmBtn) {

            // 성공
            if (passwordEdit.getText().toString().equals(repasswordEdit.getText().toString())) {
                Toast.makeText(getApplicationContext(), "회원가입에 성공하였습니다.", Toast.LENGTH_LONG).show();
                newSignUp(emailEdit.getText().toString(), passwordEdit.getText().toString());
            }

            //실패
            else {
                Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }


    // 회원가입 함수
    private void newSignUp(String email, String password) {
        if (email.length() > 0 && password.length() > 5) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                if (task.getException() != null)
                                    Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                                //Toast.makeText(EmailPasswordActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }

                            // ...
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "이메일 또는 비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    // 스피너 세팅
    private void setViewSpinner() {
        preferenceGenre[0] = (Spinner) findViewById(R.id.preference_genre1);
        preferenceGenre[1] = (Spinner) findViewById(R.id.preference_genre2);
        preferenceGenre[2] = (Spinner) findViewById(R.id.preference_genre3);

        arrayAdapter = ArrayAdapter.createFromResource(this, R.array.movie_genre, android.R.layout.simple_spinner_dropdown_item);

        for (int i = 0; i < 3; i++)
            preferenceGenre[i].setAdapter(arrayAdapter);
    }

    public void updateUI(FirebaseUser user) {
        if (user != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}

