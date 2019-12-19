package com.hong.fragement.MyPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hong.fragement.MainActivity;
import com.hong.fragement.R;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Arrays;

public class MemberInfo extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "시발";
    private String email, uid;
    private Button updateConfirmBtn, confirmBtn;
    private EditText emailEdit, passwordEdit, repasswordEdit, nameEdit, yearEdit, monthEdit, dayEdit;
    private ImageView setImage;
    private MemberObj memberObj;
    private Spinner[] genre = new Spinner[3];
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_info);

        emailEdit = findViewById(R.id. member_email_update);
        passwordEdit = findViewById(R.id.member_password_update);
        repasswordEdit = findViewById(R.id.member_re_password_update);
        nameEdit = findViewById(R.id.member_name_update);
        yearEdit = findViewById(R.id.member_birth_year);
        monthEdit = findViewById(R.id.member_birth_month);
        dayEdit = findViewById(R.id.member_birth_day);
        setImage = findViewById(R.id.member_set_image_update);
        updateConfirmBtn = findViewById(R.id.member_update_finish);
        confirmBtn = findViewById(R.id.member_finish);

        updateConfirmBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);

        setViewSpinner();

        Intent intent = getIntent();
        memberObj = (MemberObj) intent.getSerializableExtra("member");

        initProfile();

        // 비밀번호 관련
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

    // 읽어온 객체를 입력
    private void initProfile() {
        emailEdit.setText(memberObj.getEmail());
        nameEdit.setText(memberObj.getName());
        yearEdit.setText(memberObj.getYear());
        monthEdit.setText(memberObj.getMonth());
        dayEdit.setText(memberObj.getDay());
        setSpinnerData(memberObj.getGenre());
    }


    private void profileUpdate() {

        String email = emailEdit.getText().toString();
        String name = nameEdit.getText().toString();
        String year = yearEdit.getText().toString();
        String month = monthEdit.getText().toString();
        String day = dayEdit.getText().toString();
        ArrayList<String> pGenre = new ArrayList<>();
        String type = null;

        for (int i = 0; i < 3; i++) {
            pGenre.add(i, genre[i].getSelectedItem().toString());
        }

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // 비밀번호 변경
        if(passwordEdit.getText().toString().equals(repasswordEdit.getText().toString()) && passwordEdit.getText().toString().length() > 5){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String newPassword = passwordEdit.getText().toString();

            user.updatePassword(newPassword)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("쓰벌", "User password updated.");
                            }
                        }
                    });
        }

        MemberObj newMemberObj = new MemberObj(email, name, year, month, day, pGenre, type);
        db.collection("Users").document(firebaseUser.getUid()).set(newMemberObj)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "내 정보를 수정했습니다.", Toast.LENGTH_SHORT).show();
                        myStartActivity(MainActivity.class);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "내 정보 수정을 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v == updateConfirmBtn) profileUpdate();
        else if (v == confirmBtn) backToMain();
    }

    private void backToMain(){
        finish();
    }
    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    // 스피너 세팅
    private void setViewSpinner() {
        genre[0] = (Spinner) findViewById(R.id.member_preference_genre1);
        genre[1] = (Spinner) findViewById(R.id.member_preference_genre2);
        genre[2] = (Spinner) findViewById(R.id.member_preference_genre3);

        arrayAdapter = ArrayAdapter.createFromResource(this, R.array.movie_genre, android.R.layout.simple_spinner_dropdown_item);

        for (int i = 0; i < 3; i++)
            genre[i].setAdapter(arrayAdapter);
    }


    // 읽어온 객체로부터 스피너값 설정
    private void setSpinnerData(ArrayList<String> arrayList) {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.movie_genre, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < 3; i++) {
            genre[i].setAdapter(adapter);

            if (arrayList.get(i) != null) {
                int spinnerPosition = adapter.getPosition(arrayList.get(i));
                genre[i].setSelection(spinnerPosition);
            }
        }

        for (int i = 0; i < 3; i++) {
            ArrayAdapter myAdap = (ArrayAdapter) genre[i].getAdapter();
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
