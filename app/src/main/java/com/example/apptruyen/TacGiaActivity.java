package com.example.apptruyen;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apptruyen.adapter.BinhLuan_Adapter;
import com.example.apptruyen.adapter.TruyenTacGiaAdapter;
import com.example.apptruyen.adapter.TruyenTrangChuAdapter;
import com.example.apptruyen.model.BinhLuan;
import com.example.apptruyen.model.Chuong;
import com.example.apptruyen.model.Truyen;
import com.example.apptruyen.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TacGiaActivity extends AppCompatActivity {
    private RecyclerView rcvTruyenSangTac;
    private List<Truyen> listTruyenSangTac;
    private TextView txtThongBaoST;
    private Button btnThemTruyen;
    private TruyenTacGiaAdapter truyenTacGiaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tacgia);

        InitUI();
        listTruyenSangTac = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TacGiaActivity.this, RecyclerView.VERTICAL, false);
        rcvTruyenSangTac.setLayoutManager(linearLayoutManager);
        truyenTacGiaAdapter = new TruyenTacGiaAdapter(listTruyenSangTac, new TruyenTacGiaAdapter.IClickListener() {
            @Override
            public void onClickSuaItem(Truyen truyen) {
                Intent intent = new Intent(TacGiaActivity.this, ChinhSuaTruyenActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("truyen", truyen);
                intent.putExtras(bundle);
                startActivity(intent);
            }
            @Override
            public void onClickXoaItem(Truyen truyen) {
                onClickDeleteData(truyen);
            }
        });
        rcvTruyenSangTac.setAdapter(truyenTacGiaAdapter);

        getListTruyen();

        btnThemTruyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TacGiaActivity.this, ThemTruyenActivity.class);
                startActivity(intent);
            }
        });
    }
    private void InitUI(){
        rcvTruyenSangTac = findViewById(R.id.rcvTruyenSangTac);
        btnThemTruyen = findViewById(R.id.btnThemTruyen);
        txtThongBaoST = findViewById(R.id.txtThongBaoST);
    }

    private void getListTruyen(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference myRefTruyen = database.getReference("Truyen");

        Query query1 = myRefTruyen.orderByChild("thoiGianViet");

        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listTruyenSangTac != null){
                    listTruyenSangTac.clear();
                }
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Truyen truyen = postSnapshot.getValue(Truyen.class);
                    truyen.setMaTruyen(postSnapshot.getKey());
                    if(user.getUid().equals(truyen.getTacGia())){
                        listTruyenSangTac.add(0, truyen);
                    }
                }
                if(listTruyenSangTac != null){
                    truyenTacGiaAdapter.notifyDataSetChanged();
                }
                else {
                    txtThongBaoST.setText("Bạn chưa sáng tác truyện nào");
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void onClickDeleteData(Truyen truyen) {
        new AlertDialog.Builder(this).setTitle("Title").setMessage("Bạn có chắc muốn xóa không?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("Truyen");
                        myRef.child(truyen.getTenTruyen()).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(TacGiaActivity.this, "Delete data success", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .show();
    }
}