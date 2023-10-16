package com.example.apptruyen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apptruyen.model.Chuong;
import com.example.apptruyen.model.Truyen;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.nio.charset.Charset;
import java.util.ArrayList;

public class ChuongActivity extends AppCompatActivity {
    private TextView txtNoiDungChuong;
    private Button btnChuongTruoc, btnChuongSau;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuong);
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
        chuyenChuong(chuong, listChuong);
    }

    private void initUI(){
        txtNoiDungChuong = findViewById(R.id.txtNoiDungChuong);
        btnChuongTruoc = findViewById(R.id.btnChuongTruoc);
        btnChuongSau = findViewById(R.id.btnChuongSau);
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
                        txtNoiDungChuong.setText(txtContent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        txtNoiDungChuong.setText("Đang lỗi, báo admin ngay và luôn đê!");
                    }
                });
    }
    private void chuyenChuong(Chuong chuong, ArrayList<Chuong> listChuong){

        ArrayList<String> a = new ArrayList<>();
        for(int i = 0; i<listChuong.size(); i++){
            a.add(listChuong.get(i).getTenChuong());
        }
        int index = a.indexOf(chuong.getTenChuong());

        btnChuongSau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (index == 0){
                        Toast.makeText(ChuongActivity.this, "Đây là chương cuối", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Intent intent = getIntent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("chuong", listChuong.get(index - 1));
                        bundle.putSerializable("list_chuong", listChuong);
                        intent.putExtras(bundle);
                        finish();
                        startActivity(intent);

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        btnChuongTruoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (index == (listChuong.size() - 1)){
                        Toast.makeText(ChuongActivity.this, "Đây là chương đầu!", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Intent intent = getIntent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("chuong", listChuong.get(index + 1));
                        bundle.putSerializable("list_chuong", listChuong);
                        intent.putExtras(bundle);
                        finish();
                        startActivity(intent);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}