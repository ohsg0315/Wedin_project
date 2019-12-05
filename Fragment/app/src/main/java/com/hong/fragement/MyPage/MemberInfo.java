package com.hong.fragement.MyPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.hong.fragement.R;

public class MemberInfo extends AppCompatActivity implements View.OnClickListener {

    private Button updateConfirmBtn, confirmBtn;
    private EditText passwordEdit, repasswordEdit, nameEdit, yearEdit, monthEdit, dayEdit;
    private ImageView setImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_info);

        passwordEdit = findViewById(R.id.member_password_update);
        repasswordEdit = findViewById(R.id.member_re_password_update);
        nameEdit = findViewById(R.id.member_name_update);
        yearEdit = findViewById(R.id.member_birth_year);
        monthEdit = findViewById(R.id.member_birth_month);
        dayEdit = findViewById(R.id.member_birth_day);
        setImage = findViewById(R.id.set_image_sign_up);
        updateConfirmBtn = findViewById(R.id.member_update_finish);
        confirmBtn = findViewById(R.id.member_finish);

        updateConfirmBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
    }

    private void profileUpdate() {
        String name = nameEdit.getText().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (name.length() > 0) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();

            if (user != null) {
                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "회원정보 수정을 성공했습니다.", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        });
            }
        } else {
            Toast.makeText(getApplicationContext(), "회원정보 수정을 실패했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == updateConfirmBtn) profileUpdate();
        else if (v == confirmBtn) finish();
    }
}
