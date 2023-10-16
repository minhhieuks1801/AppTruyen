package com.example.apptruyen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DoiMatKhauActivity extends AppCompatActivity {
    private EditText edtPassword, edtOldPassword;
    private Button btnChangePassword;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_mat_khau);

        initUi();
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Re_authenticate();
            }
        });
    }

    private void onClickChangePassword() {
        String newPass = edtPassword.getText().toString().trim();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updatePassword(newPass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(DoiMatKhauActivity.this, "User password update", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(DoiMatKhauActivity.this, "Erro! User can't password update", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void initUi(){
        dialog = new ProgressDialog(DoiMatKhauActivity.this);
        edtPassword = findViewById(R.id.edtChangePassword);
        btnChangePassword = findViewById(R.id.btn_Change_Password);
        edtOldPassword = findViewById(R.id.edtOldPassword);
    }

    private void Re_authenticate(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), edtOldPassword.getText().toString().trim());
        assert user != null;
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            onClickChangePassword();
                        }
                    }
                });
    }
}