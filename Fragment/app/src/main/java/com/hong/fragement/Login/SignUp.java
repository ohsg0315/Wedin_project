package com.hong.fragement.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hong.fragement.MainActivity;
import com.hong.fragement.MyPage.MemberObj;
import com.hong.fragement.R;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class SignUp extends AppCompatActivity implements View.OnClickListener, Dialog.OnCancelListener {

    private FirebaseAuth mAuth;

    private Spinner[] preferenceGenre = new Spinner[3];
    private ArrayAdapter arrayAdapter;

    private String TAG = "SignUpActivity", authenticationString;

    private Button signUpConfirmBtn, idAuthBtn;
    private EditText emailEdit, passwordEdit, repasswordEdit, nameEdit, yearEdit, monthEdit, dayEdit;
    private ImageView setImage;

    // Dialog
    LayoutInflater dialog;
    View dialogLayout;
    Dialog authDialog;

    // 카운트 다운 타이머
    TextView time_counter; //시간을 보여주는 TextView
    EditText emailAuth_number; //인증 번호를 입력 하는 칸
    Button emailAuth_btn, idAuthCancelBtn; // 인증버튼
    CountDownTimer countDownTimer;
    private boolean isEmailAuthentication;

    final int MILLISINFUTURE = 300 * 1000; //총 시간 (300초 = 5분)
    final int COUNT_DOWN_INTERVAL = 1000; //onTick 메소드를 호출할 간격 (1초)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());


        setViewSpinner();

        isEmailAuthentication = false;
        signUpConfirmBtn = findViewById(R.id.sign_up_finish);
        idAuthBtn = findViewById(R.id.email_auth_btn);
        emailEdit = findViewById(R.id.email_sign_up);
        passwordEdit = findViewById(R.id.password_sign_up);
        repasswordEdit = findViewById(R.id.re_password_sign_up);
        nameEdit = findViewById(R.id.name_sign_up);
        yearEdit = findViewById(R.id.birth_year);
        monthEdit = findViewById(R.id.birth_month);
        dayEdit = findViewById(R.id.birth_day);
        setImage = (ImageView) findViewById(R.id.set_image_sign_up);

        signUpConfirmBtn.setOnClickListener(this);
        idAuthBtn.setOnClickListener(this);

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
        // 인증 메일 보내기 버튼
        if (view == idAuthBtn) {
            emailAuth();
        }
        // Dialog 내 인증 버튼을 눌렀을 경우
        else if (view == emailAuth_btn) {
            if (emailAuth_number.getText().toString().length() > 0) {

                if (emailAuth_number.getText().toString().equals(authenticationString)) {
                    Toast.makeText(this, "이메일 인증 성공", Toast.LENGTH_SHORT).show();

                    isEmailAuthentication = true;
                    idAuthBtn.setText("인증이 완료되었습니다.");
                    idAuthBtn.setEnabled(false);

                    authDialog.cancel();
                } else {
                    Toast.makeText(this, "이메일 인증 실패", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "인증 번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        }
        // Dialog 내 취소 번튼을 눌렀을 경우
        else if (view == idAuthCancelBtn) {
            authDialog.cancel();
        }

        // 회원가입 완료 버튼
        else if (view == signUpConfirmBtn) {
            if (isInput()) {
                newSignUp(emailEdit.getText().toString(), passwordEdit.getText().toString());
            }
        }
    }

    // 이메일 인증 함수
    private void emailAuth() {
        if (emailEdit.getText().toString().length() > 3) {
            try {
                authenticationString = makeAuthenticationString();

                GMailSender gMailSender = new GMailSender("ohsg0315@gmail.com", "Aiden0088151!");
                gMailSender.sendMail("Wedin 인증 메일입니다.", " Wedin 어플을 이용해주셔서 감하삽니다. \n회원가입을 위한 인증 번호는 >>  " + authenticationString + "  << 입니다.\n Wedin 인증을 위해 입력해주세요.\n", emailEdit.getText().toString());
                Toast.makeText(getApplicationContext(), "이메일 보내기 성공~!~!", Toast.LENGTH_SHORT).show();
            } catch (SendFailedException e) {
                Toast.makeText(getApplicationContext(), "이메일 형식이 잘못됨~!~!", Toast.LENGTH_SHORT).show();
            } catch (MessagingException e) {
                Toast.makeText(getApplicationContext(), "인터넷 연결확인~!~!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog = LayoutInflater.from(this);
            dialogLayout = dialog.inflate(R.layout.auth_dialog, null); // LayoutInflater를 통해 XML에 정의된 Resource들을 View의 형태로 반환 시켜 줌
            authDialog = new Dialog(this); //Dialog 객체 생성
            authDialog.setContentView(dialogLayout); //Dialog에 inflate한 View를 탑재 하여줌
            authDialog.setCanceledOnTouchOutside(false); //Dialog 바깥 부분을 선택해도 닫히지 않게 설정함.
            authDialog.setOnCancelListener(this); // 다이얼로그 닫을 때
            authDialog.show(); //Dialog를 나타내어 준다.
            countDownTimer();
        } else {
            Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    // 카운트 다운 메소드
    public void countDownTimer() {
        //줄어드는 시간을 나타내는 TextView
        time_counter = dialogLayout.findViewById(R.id.emailAuth_time_counter);

        //사용자 인증 번호 입력창
        emailAuth_number = dialogLayout.findViewById(R.id.emailAuth_number);

        //인증하기 버튼
        emailAuth_btn = dialogLayout.findViewById(R.id.emailAuth_btn);

        //취소 버튼
        idAuthCancelBtn = dialogLayout.findViewById(R.id.emailAuth_cancel_btn);

        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) { //(300초에서 1초 마다 계속 줄어듬)
                long emailAuthCount = millisUntilFinished / 1000;
                Log.d("Alex", emailAuthCount + "");

                if ((emailAuthCount - ((emailAuthCount / 60) * 60)) >= 10) { //초가 10보다 크면 그냥 출력
                    time_counter.setText((emailAuthCount / 60) + " : " + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                } else { //초가 10보다 작으면 앞에 '0' 붙여서 같이 출력. ex) 02,03,04...
                    time_counter.setText((emailAuthCount / 60) + " : 0" + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                }
            }

            //시간이 다 되면 다이얼로그 종료
            @Override
            public void onFinish() {
                authDialog.cancel();
            }
        }.start();

        emailAuth_btn.setOnClickListener(this);
        idAuthCancelBtn.setOnClickListener(this);
    }

    // 회원가입 함수
    private void newSignUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            // 회원가입시 정보 저장
                            String email = emailEdit.getText().toString();
                            String name = nameEdit.getText().toString();
                            String year = yearEdit.getText().toString();
                            String month = monthEdit.getText().toString();
                            String day = dayEdit.getText().toString();
                            ArrayList<String> pGenre = new ArrayList<>();
                            String type = "App";

                            for (int i = 0; i < 3; i++) {
                                pGenre.add(i, preferenceGenre[i].getSelectedItem().toString());
                            }

                            MemberObj newMemberObj = new MemberObj(email, name, year, month, day, pGenre, type);
                            db.collection("Users").document(mAuth.getUid()).set(newMemberObj);
                            Toast.makeText(getApplicationContext(), "회원가입에 성공하였습니다.", Toast.LENGTH_LONG).show();
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
    }


    private void makeDialog(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("< 알림 >").setMessage(title + "을/를 확인해주세요.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    // 회원정보 입력 확인
    private boolean isInput() {
        if (emailEdit.length() <= 0) {
            makeDialog("이메일");
            return false;
        } else if ((passwordEdit.length() <= 5) || !(passwordEdit.getText().toString().equals(repasswordEdit.getText().toString()))) {
            makeDialog("비밀번호");
            return false;
        } else if (nameEdit.length() <= 0) {
            makeDialog("이름");
            return false;
        } else if (yearEdit.length() <= 0) {
            makeDialog("생년월일");
            return false;
        } else if (monthEdit.length() <= 0) {
            makeDialog("생년월일");
            return false;
        } else if (dayEdit.length() <= 0) {
            makeDialog("생년월일");
            return false;
        } else if (!isEmailAuthentication) {
            makeDialog("이메일 인증");
            return false;
        } else
            return true;
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
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    //다이얼로그 닫을 때 카운트 다운 타이머의 cancel()메소드 호출
    @Override
    public void onCancel(DialogInterface dialog) {
        countDownTimer.cancel();
    }

    private String makeAuthenticationString() {
        int ten = 1, randomValue = 0;
        double value;
        for (int i = 0; i < 10; i++) {
            value = Math.random();
            ten *= 10;
            value *= ten;
            randomValue += (int) value;
        }
        String res = Integer.toHexString(randomValue);
        Log.d("시발", res);
        return res;
    }

}

