package com.example.apptruyen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class KiemTraDangNhapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kiemtradangnhap);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextActivity();
            }
        }, 2000);
    }

    private void nextActivity() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            //Chưa login
            Intent intent = new Intent(this, DangNhapActivity.class);
            startActivity(intent);
        }
        else {
            //Đã login
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}