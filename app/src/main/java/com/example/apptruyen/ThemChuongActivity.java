package com.example.apptruyen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apptruyen.model.Chuong;
import com.example.apptruyen.model.ThongBao;
import com.example.apptruyen.model.Truyen;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ThemChuongActivity extends AppCompatActivity {
    private EditText edtThemTenChuong, edtThemNoiDUngChuong;
    private Button btnLuuThemChuong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_chuong);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }
        Truyen truyen = (Truyen) bundle.get("truyen");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefChuong = database.getReference("Chuong");
        DatabaseReference myRefTruyen = database.getReference("Truyen");
        DatabaseReference myRefThongBao = database.getReference("ThongBao");
        DatabaseReference addChuong = myRefChuong.push();
        DatabaseReference addThongBao = myRefThongBao.push();

        edtThemTenChuong = findViewById(R.id.edtThemTenChuong);
        edtThemNoiDUngChuong = findViewById(R.id.edtThemNoiDUngChuong);
        btnLuuThemChuong = findViewById(R.id.btnLuuThemChuong);

        btnLuuThemChuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String k = edtThemTenChuong.getText().toString();
                Date currentDate = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = dateFormat.format(currentDate);

                Chuong chuong = new Chuong(truyen.getMaTruyen(),
                        edtThemTenChuong.getText().toString() +".txt",
                        edtThemTenChuong.getText().toString(),
                        dateString);
                getTxt();
                addChuong.setValue(chuong, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(ThemChuongActivity.this, "Thêm chương thành công", Toast.LENGTH_SHORT).show();
                    }
                });
                Truyen truyen1 = truyen;
                truyen1.setSoChuong(truyen.getSoChuong()+1);
                myRefTruyen.child(truyen1.getMaTruyen()).updateChildren(truyen1.toMap());

                ThongBao thongBao = new ThongBao(truyen.getMaTruyen(), dateString);
                addThongBao.setValue(thongBao);
            }
        });
    }

    private void getTxt(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference textFileRef = storageRef.child(edtThemTenChuong.getText().toString() +".txt");

        String textData = edtThemNoiDUngChuong.getText().toString();

        byte[] bytes = textData.getBytes(StandardCharsets.UTF_8);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

        textFileRef.putStream(inputStream)
                .addOnSuccessListener(taskSnapshot -> {
                })
                .addOnFailureListener(exception -> {
                });
    }
}