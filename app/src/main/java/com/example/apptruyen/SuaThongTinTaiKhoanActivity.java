package com.example.apptruyen;

import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static com.example.apptruyen.MainActivity.MY_REQUEST_CODE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.apptruyen.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

public class SuaThongTinTaiKhoanActivity extends AppCompatActivity {
    private EditText edtTen;
    private TextView txtEmail;
    private Button btnUpdate;
    private ImageView imgAvatar;
    private ProgressDialog dialog;
    private MainActivity mainActivity;
    private Uri mUri;
    private ArrayList<User> listUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suathongtin);
        initUi();
        ActionUI();
        setUserInformation();

    }

    private void initUi(){
        dialog = new ProgressDialog(SuaThongTinTaiKhoanActivity.this);
        edtTen = findViewById(R.id.edtUpdateTen);
        txtEmail = findViewById(R.id.txtUpdateEmail);
        imgAvatar = findViewById(R.id.img_avatar);
        btnUpdate = findViewById(R.id.btn_UpdateProfile);
    }

    private void ActionUI(){
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickUpdateProfile();
            }
        });
    }

    private void setUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }

        edtTen.setText(user.getDisplayName());
        txtEmail.setText(user.getEmail());
        Glide.with(SuaThongTinTaiKhoanActivity.this).load(user.getPhotoUrl()).error(R.drawable.ic_avatar).into(imgAvatar);
    }

    private void onClickRequestPermission() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            openGllery();
            return;
        }
        if(checkSelfPermission(READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED){
            openGllery();
        }
        else {
            String [] permission = {READ_MEDIA_IMAGES};
            requestPermissions(permission, MY_REQUEST_CODE);
        }
    }
    public void openGllery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), MY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            setmUri(uri);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                setBitmapImageView(bitmap);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void setBitmapImageView(Bitmap bitmap){
        imgAvatar.setImageBitmap(bitmap);

    }

    public void setmUri(Uri mUri) {
        this.mUri = mUri;
    }

    private void onClickUpdateProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return;
        }
        String name = edtTen.getText().toString().trim();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(mUri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SuaThongTinTaiKhoanActivity.this, "Update profile success", Toast.LENGTH_SHORT).show();
                            //mainActivity.showUserInformation();
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefUser = database.getReference("User");

        myRefUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listUsers != null){
                    listUsers.clear();
                }
                final boolean[] checkTheoDoi = {false};
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    User user1 = postSnapshot.getValue(User.class);
                    if (user1.getId().equals(user.getUid())) {
                        user1.setTen(edtTen.getText().toString());
                    }
                    user1.getTen();
                    myRefUser.child(user1.getId()).updateChildren(user1.toMap());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
