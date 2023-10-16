package com.example.apptruyen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.apptruyen.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DangKyActivity extends AppCompatActivity {

    private EditText edtEmailSU, edtPassSU1, edtPassSU2;
    private Button btnSignUp;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangky);

        initUi();
        initListener();

    }

    private void initUi(){
        edtEmailSU = findViewById(R.id.edtEmailSU);
        edtPassSU1 = findViewById(R.id.edtPassSU1);
        edtPassSU2 = findViewById(R.id.edtPassSU2);
        btnSignUp = findViewById(R.id.btn_sign_up);
        dialog = new ProgressDialog(this);
    }

    private void initListener() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtPassSU1.getText().toString().equals(edtPassSU2.getText().toString())){
                    onClickSignUp();
                }
                else {
                    Toast.makeText(DangKyActivity.this, "2 mật khẩu chưa trùng khớp",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void onClickSignUp() {
        String email = edtEmailSU.getText().toString().trim();
        String password = edtPassSU1.getText().toString().trim();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        dialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.dismiss();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent = new Intent(DangKyActivity.this, MainActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(DangKyActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}