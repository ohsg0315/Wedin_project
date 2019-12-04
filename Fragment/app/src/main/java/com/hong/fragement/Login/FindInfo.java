package com.hong.fragement.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.hong.fragement.R;

public class FindInfo extends FragmentActivity implements View.OnClickListener, FindPassword.OnInputListener{

    private static final String TAG = "infoActivity";

    private Button FindIdBtn, FindPasswordBtn, confirmBtn;
    private FragmentTransaction transaction;
    private Fragment newFragment;

    private String pName, pEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_info);

        FindIdBtn = findViewById(R.id.find_id);
        FindPasswordBtn = findViewById(R.id.find_password);

        FindIdBtn.setOnClickListener(this);
        FindPasswordBtn.setOnClickListener(this);

        // 프래그먼트 초기화
        newFragment = new FindId();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_findid, newFragment);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();;
        transaction = fragmentManager.beginTransaction();

        if(v == FindIdBtn) {
            newFragment = new FindId();
            transaction.replace(R.id.fragment_findid, newFragment);
        }
        else if(v == FindPasswordBtn){
            newFragment = new FindPassword();
            transaction.replace(R.id.fragment_findid, newFragment);
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void sendInput(String name, String email){
        pName = name; pEmail = email;
        Log.d(TAG, "asvnlkdlsdnvlksd" + pName + pEmail);
    }
}
