package com.example.apptruyen;

import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static com.example.apptruyen.MainActivity.MY_REQUEST_CODE;

import androidx.annotation.NonNull;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.apptruyen.adapter.BinhLuan_Adapter;
import com.example.apptruyen.model.Chuong;
import com.example.apptruyen.model.Truyen;
import com.example.apptruyen.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class ChinhSuaTruyenActivity extends AppCompatActivity {
    private TabHost mytab;
    private TextView  txtTacGia, txtTheLoai;
    private ImageView imgTruyen;
    private EditText edtTenTruyen, edtGioiThieu;
    private Button  btnLuu, btnThemChuong;
    private ListView  lvChuong;
    private ArrayList<Chuong> listChuong;
    private ArrayAdapter<String> chuongAdapter;
    private Uri mUri;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinh_sua_truyen);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefChuong = database.getReference("Chuong");
        DatabaseReference myRefTruyen = database.getReference("Truyen");
        DatabaseReference myRefUser = database.getReference("User");

        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }
        Truyen truyen = (Truyen) bundle.get("truyen");

        addControl();
        inItUI();
        ActionUI(truyen,myRefUser);
        XuLyChuong(truyen, myRefChuong);
        LuuTruyen(truyen, myRefTruyen);
        ThemChuong(truyen);
    }

    private void inItUI(){
        imgTruyen = findViewById(R.id.imgBiaSachChinhSua);
        edtTenTruyen = findViewById(R.id.edtTenTruyenChinhSua);
        txtTacGia = findViewById(R.id.txtTenTacGiaChinhSua);
        txtTheLoai = findViewById(R.id.txtTheLoaiSua);
        edtGioiThieu = findViewById(R.id.edtGioiThieuSua);
        lvChuong = findViewById(R.id.lvChuongSua);
        btnLuu = findViewById(R.id.btnLuu);
        btnThemChuong = findViewById(R.id.btnThemChuong);
    }

    private void ActionUI(Truyen truyen, DatabaseReference myRefUser){
        edtTenTruyen.setText(truyen.getTenTruyen());
        txtTheLoai.setText(truyen.getTheLoai());
        myRefUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    if(user.getId().equals(truyen.getTacGia())){
                        txtTacGia.setText(user.getTen());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        String uri = "default_book_cover_2.PNG";
        if(truyen.getBiaTruyen().equals("")){
            uri = "default_book_cover_2.PNG";
        }
        else {
            uri = truyen.getBiaTruyen();
        }
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(uri);
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ChinhSuaTruyenActivity.this)
                        .load(uri)
                        .into(imgTruyen);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                imgTruyen.setImageResource(R.drawable.default_book_cover_2);
            }
        });

        String noiDung = "erro.txt";
        if(truyen.getGioiThieu().equals("")){
            noiDung = "erro.txt";
        }
        else {
            noiDung = truyen.getGioiThieu();
        }
        StorageReference txtRef = FirebaseStorage.getInstance().getReference().child(noiDung);
        txtRef.getBytes(1024 * 1024) //
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {

                        String txtContent = new String(bytes, Charset.forName("UTF-8"));
                        edtGioiThieu.setText(txtContent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        edtGioiThieu.setText("Đang lỗi, báo admin ngay và luôn đê!");
                    }
                });
    }
    private void XuLyChuong(Truyen truyen, DatabaseReference myRefChuong){
        Query query1 = myRefChuong.orderByChild("thoiGianDang");
        ArrayList<String> tenChuongs = new ArrayList<>();
        listChuong = new ArrayList<>();
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listChuong != null){
                    listChuong.clear();
                }
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Chuong chuong = postSnapshot.getValue(Chuong.class);
                    chuong.setMaChuong(postSnapshot.getKey());
                    if(chuong.getMaTruyen().equals(truyen.getMaTruyen())){
                        listChuong.add(0, chuong);
                        tenChuongs.add(0, chuong.getTenChuong());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        chuongAdapter = new ArrayAdapter<>(ChinhSuaTruyenActivity.this, android.R.layout.simple_list_item_1, tenChuongs);
        lvChuong.setAdapter(chuongAdapter);
        lvChuong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ChinhSuaTruyenActivity.this, ChinhSuaChuongActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("chuong", listChuong.get(i));
                bundle.putSerializable("list_chuong", listChuong);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    private void LuuTruyen(Truyen truyen, DatabaseReference myRefTruyen){

        imgTruyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission();
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                truyen.setTenTruyen(edtTenTruyen.getText().toString());
                myRefTruyen.child(truyen.getMaTruyen()).updateChildren(truyen.toMap());
                //Giới thiệu
                String editedContent = edtGioiThieu.getText().toString();
                try {
                    File tempFile = File.createTempFile("edited_content", ".txt");
                    FileOutputStream fos = new FileOutputStream(tempFile);
                    fos.write(editedContent.getBytes());
                    fos.close();

                    String storagePath = truyen.getGioiThieu();  // Đặt đường dẫn trong Firebase Storage
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(storagePath);

                    Uri fileUri = Uri.fromFile(tempFile);
                    UploadTask uploadTask = storageRef.putFile(fileUri);

                    uploadTask.addOnFailureListener(exception -> {

                    }).addOnSuccessListener(taskSnapshot -> {

                    });
                } catch (IOException e) {
                    e.printStackTrace();

                }
                //Ảnh bìa
                String storagePath = truyen.getBiaTruyen();
                StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(storagePath);
                try {
                    File localFile = File.createTempFile("edited_image", "png");
                    storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap editedBitmap = ((BitmapDrawable) imgTruyen.getDrawable()).getBitmap();


                            FileOutputStream fos = null;
                            try {
                                fos = new FileOutputStream(localFile);
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                            editedBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                            try {
                                fos.close();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            StorageReference editedImageRef = FirebaseStorage.getInstance().getReference().child(storagePath);
                            Uri editedImageUri = Uri.fromFile(localFile);

                            UploadTask uploadTask = editedImageRef.putFile(editedImageUri);

                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    private void ThemChuong(Truyen truyen){
        btnThemChuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChinhSuaTruyenActivity.this, ThemChuongActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("truyen", truyen);
                intent.putExtras(bundle);
                startActivity(intent);
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
        imgTruyen.setImageBitmap(bitmap);

    }
    private void addControl() {
        mytab = findViewById(R.id.tarSachChinhSua);
        mytab.setup();
        TabHost.TabSpec spec_GioiThieu, spec_Chuong;

        spec_GioiThieu = mytab.newTabSpec("tab_gioithieu");
        spec_GioiThieu.setContent(R.id.tabGioiThieuSua);
        spec_GioiThieu.setIndicator("Giới thiệu", null);
        mytab.addTab(spec_GioiThieu);

        spec_Chuong = mytab.newTabSpec("tab_chuong");
        spec_Chuong.setContent(R.id.tabChuongSua);
        spec_Chuong.setIndicator("DS.Chương", null);
        mytab.addTab(spec_Chuong);

    }
}