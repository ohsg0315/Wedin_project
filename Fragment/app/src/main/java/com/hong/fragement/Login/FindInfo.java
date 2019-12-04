package com.hong.fragement.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hong.fragement.R;

public class FindInfo extends FragmentActivity implements View.OnClickListener{

    private Button FindIdBtn, FindPasswordBtn;

    private FindId findId;
    private FindPassword findPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_info);

        FindIdBtn = findViewById(R.id.find_id);
        FindPasswordBtn = findViewById(R.id.find_password);

        FindIdBtn.setOnClickListener(this);
        FindPasswordBtn.setOnClickListener(this);

        initFragment();
    }

    private void initFragment(){
        Fragment newFragment = new FindId();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_findid, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    @Override
    public void onClick(View v) {
        if(v == FindIdBtn) {
            Fragment newFragment = new FindId();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_findid, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else if(v == FindPasswordBtn){
            Fragment newFragment = new FindPassword();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_findid, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        };
    }
}
