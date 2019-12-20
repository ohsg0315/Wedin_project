package com.hong.fragement.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hong.fragement.MainActivity;
import com.hong.fragement.R;

public class FindPassword extends Fragment {

    private static final String TAG = "FindPassword_Fragment";

    private FirebaseAuth auth;
    private String name, email;
    private EditText editName, editEmail;
    private Button confirmBtn;

    public FindPassword() {

    }

    public interface OnInputListener {
        void sendInput(String name, String email);
    }

    public OnInputListener mOnInputListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_find_password, container, false);

        editName = view.findViewById(R.id.edit_find_password_name);
        editEmail = view.findViewById(R.id.edit_find_password);
        confirmBtn = view.findViewById(R.id.passsword_confirm_btn);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = editName.getText().toString();
                email = editEmail.getText().toString();
                /*
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email)) {
                    mOnInputListener.sendInput(name, email);
                }    */
                resetPassword(email);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnInputListener = (OnInputListener) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException" + e.getMessage());
        }
    }

    private void resetPassword(String email){
        auth.setLanguageCode("fr");
        auth.useAppLanguage();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = email;

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "해당 이메일로 전송되었습니다.", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }else{
                            Toast.makeText(getContext(), "해당 이메일이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}