package com.example.apptruyen;

import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static com.example.apptruyen.MainActivity.MY_REQUEST_CODE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.apptruyen.model.Truyen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ThemTruyenActivity extends AppCompatActivity {
    private EditText edtTenTruyen, edtGioiThieu;
    private Button btnThemTruyen;
    private ImageView imgAnhBiaThemTruyen;
    private CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6, checkBox7, checkBox8, checkBox9, checkBox10, checkBox11, checkBox12;
    private String theLoai = "";
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themtruyen);

        InitUI();
        ActionUI();
    }
    private void InitUI(){
        checkBox1 = findViewById(R.id.checkBox1);
        checkBox2 = findViewById(R.id.checkBox2);
        checkBox3 = findViewById(R.id.checkBox3);
        checkBox4 = findViewById(R.id.checkBox4);
        checkBox5 = findViewById(R.id.checkBox5);
        checkBox6 = findViewById(R.id.checkBox6);
        checkBox7 = findViewById(R.id.checkBox7);
        checkBox8 = findViewById(R.id.checkBox8);
        checkBox9 = findViewById(R.id.checkBox9);
        checkBox10 = findViewById(R.id.checkBox10);
        checkBox11 = findViewById(R.id.checkBox11);
        checkBox12 = findViewById(R.id.checkBox12);
        edtTenTruyen= findViewById(R.id.edtTenTruyen);
        edtGioiThieu = findViewById(R.id.edtGioiThieu);
        imgAnhBiaThemTruyen = findViewById(R.id.imgAnhBiaThemTruyen);
        btnThemTruyen = findViewById(R.id.btnLuuTruyenMoi);
    }
    private void ActionUI(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefTruyen = database.getReference("Truyen");
        DatabaseReference addTruyen = myRefTruyen.push();
        List<CheckBox> checkBoxList = new ArrayList<>();
        checkBoxList.add(checkBox1);
        checkBoxList.add(checkBox2);
        checkBoxList.add(checkBox3);
        checkBoxList.add(checkBox4);
        checkBoxList.add(checkBox5);
        checkBoxList.add(checkBox6);
        checkBoxList.add(checkBox7);
        checkBoxList.add(checkBox8);
        checkBoxList.add(checkBox9);
        checkBoxList.add(checkBox10);
        checkBoxList.add(checkBox11);
        checkBoxList.add(checkBox12);
        for (CheckBox checkBox : checkBoxList) {
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    String checkBoxText = checkBox.getText().toString();
                    if (b) {
                        theLoai = theLoai + ", " + checkBoxText;
                    } else {
                        theLoai = theLoai.replace(", " + checkBoxText, "");
                    }
                }
            });
        }
        imgAnhBiaThemTruyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission();
            }
        });
        btnThemTruyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date currentDate = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = dateFormat.format(currentDate);
                getTxt();
                getImg();
                Truyen truyen = new Truyen("gioithieu"+ edtTenTruyen.getText().toString() +".txt",
                        user.getUid(),
                        edtTenTruyen.getText().toString(),
                        "Đang viết",
                        dateString,
                        edtTenTruyen.getText().toString()+ ".png",
                        theLoai.substring(1), 0, 0, 0);
                addTruyen.setValue(truyen, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(ThemTruyenActivity.this, "Thêm truyện thành công", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


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
    public void setmUri(Uri mUri) {
        this.mUri = mUri;
    }
    public void setBitmapImageView(Bitmap bitmap){
        imgAnhBiaThemTruyen.setImageBitmap(bitmap);

    }
    private void getImg(){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        BitmapDrawable drawable = (BitmapDrawable) imgAnhBiaThemTruyen.getDrawable();
        if (drawable != null) {
            Bitmap bitmap = drawable.getBitmap();

            // Convert the bitmap to a PNG byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();

            StorageReference imageRef = storageReference.child(edtTenTruyen.getText().toString()+ ".png");

            imageRef.putBytes(data)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                //Toast.makeText(ThemTruyenActivity.this, "Image uploaded to Firebase Storage", Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(ThemTruyenActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void getTxt(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference textFileRef = storageRef.child("gioithieu"+ edtTenTruyen.getText().toString() +".txt");

        String textData = edtGioiThieu.getText().toString();

        byte[] bytes = textData.getBytes(StandardCharsets.UTF_8);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

        textFileRef.putStream(inputStream)
                .addOnSuccessListener(taskSnapshot -> {
                })
                .addOnFailureListener(exception -> {
                });
    }
}
