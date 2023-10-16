package com.example.apptruyen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.apptruyen.model.Chuong;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class ChinhSuaChuongActivity extends AppCompatActivity {
    private EditText edtNoiDungChuongChinhSua;
    private Button btnLuuChinhSuaChuong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinh_sua_chuong);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }
        Chuong chuong = (Chuong) bundle.get("chuong");
        ArrayList<Chuong> listChuong = (ArrayList<Chuong>) bundle.get("list_chuong");

        ArrayList<String> a = new ArrayList<>();
        for(int i = 0; i<listChuong.size(); i++){
            a.add(listChuong.get(i).getTenChuong());
        }
        int index = a.indexOf(chuong.getTenChuong());
        String b = a.get(index);
        String k = chuong.getTenChuong();

        initUI();
        xuLyChuong(chuong.getNoiDungChuong());
        LuuChuong(chuong);
    }

    private void initUI(){
        edtNoiDungChuongChinhSua = findViewById(R.id.edtNoiDungChuongChinhSua);
        btnLuuChinhSuaChuong = findViewById(R.id.btnLuuChinhSuaChuong);
    }
    private void xuLyChuong(String noiDUngChuong){

        String noiDung = "erro.txt";
        if(noiDUngChuong.equals("")){
            noiDung = "erro.txt";
        }
        else {
            noiDung = noiDUngChuong;
        }

        StorageReference txtRef = FirebaseStorage.getInstance().getReference().child(noiDung);
        txtRef.getBytes(1024 * 1024) // Giới hạn tối đa là 1 MB
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {

                        String txtContent = new String(bytes, Charset.forName("UTF-8"));
                        edtNoiDungChuongChinhSua.setText(txtContent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        edtNoiDungChuongChinhSua.setText("Đang lỗi, báo admin ngay và luôn đê!");
                    }
                });
    }

    private void LuuChuong(Chuong chuong){
        btnLuuChinhSuaChuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editedContent = edtNoiDungChuongChinhSua.getText().toString();
                try {
                    File tempFile = File.createTempFile("edited_content", ".txt");
                    FileOutputStream fos = new FileOutputStream(tempFile);
                    fos.write(editedContent.getBytes());
                    fos.close();

                    String storagePath = chuong.getNoiDungChuong();  // Đặt đường dẫn trong Firebase Storage
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(storagePath);

                    Uri fileUri = Uri.fromFile(tempFile);
                    UploadTask uploadTask = storageRef.putFile(fileUri);

                    uploadTask.addOnFailureListener(exception -> {

                    }).addOnSuccessListener(taskSnapshot -> {

                    });
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        });
    }
}