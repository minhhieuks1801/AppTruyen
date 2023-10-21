package com.example.apptruyen;

import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.apptruyen.adapter.BinhLuan_Adapter;
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

public class ChiTietTruyenActivity extends AppCompatActivity {
    private TabHost mytab;
    private TextView txtTenTruyen, txtTacGia, txtTheLoai, txtGioiThieu;
    private ImageView imgTruyen;
    private EditText edtCmt;
    private Button btnGuiCmt, btnTheoDoi;
    private ListView lvBinhLuan, lvChuong;
    private BinhLuan_Adapter binhLuanAdapter;
    private Context mcontext;
    private ArrayList<BinhLuan> listBinhLuan;
    private ArrayList<User> listUsers;
    private ArrayList<Chuong> listChuong;
    private ArrayAdapter<String> chuongAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_truyen);
        addControl();
        inItUI();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefBinhLuan = database.getReference("BinhLuan");
        DatabaseReference myRefUser = database.getReference("User");
        DatabaseReference myRefChuong = database.getReference("Chuong");

        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }
        Truyen truyen = (Truyen) bundle.get("truyen");
        XulyBinhLuan(truyen, myRefBinhLuan, myRefUser);
        ActionUI(truyen,myRefUser);
        XuLyChuong(truyen, myRefChuong);
        XyLybtnTheoDoi(truyen, myRefUser);

    }
    private void inItUI(){
        imgTruyen = findViewById(R.id.imgBiaSach);
        txtTenTruyen = findViewById(R.id.txtTenTruyen);
        txtTacGia = findViewById(R.id.txtTenTacGia);
        txtTheLoai = findViewById(R.id.txtTheLoai);
        txtGioiThieu = findViewById(R.id.txtGioiThieu);
        lvBinhLuan = findViewById(R.id.lvCmt);
        edtCmt = findViewById(R.id.edtCmt);
        btnGuiCmt = findViewById(R.id.btnGuiCmt);
        lvChuong = findViewById(R.id.lvChuong);
        btnTheoDoi = findViewById(R.id.btnTheoDoi);
    }
    private void ActionUI(Truyen truyen, DatabaseReference myRefUser){
        txtTenTruyen.setText(truyen.getTenTruyen());
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
                Glide.with(ChiTietTruyenActivity.this)
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
        txtRef.getBytes(1024 * 1024) // Giới hạn tối đa là 1 MB
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {

                        String txtContent = new String(bytes, Charset.forName("UTF-8"));
                        txtGioiThieu.setText(txtContent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        txtGioiThieu.setText("Đang lỗi, báo admin ngay và luôn đê!");
                    }
                });
        listBinhLuan = new ArrayList<>();
        binhLuanAdapter = new BinhLuan_Adapter(ChiTietTruyenActivity.this, R.layout.layout_chi_tiet_sach_binhluan_item, listBinhLuan);
        lvBinhLuan.setAdapter(binhLuanAdapter);

    }
    private void XulyBinhLuan(Truyen truyen, DatabaseReference myRefBinhLuan, DatabaseReference myRefUser){
        listUsers = new ArrayList<>();
        myRefUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listUsers != null){
                    listUsers.clear();
                }
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    listUsers.add(0, user);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        Query query1 = myRefBinhLuan.orderByChild("thoiGianBinhLuan");
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listBinhLuan != null){
                    listBinhLuan.clear();
                }
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    BinhLuan binhLuan = postSnapshot.getValue(BinhLuan.class);
                    binhLuan.setMaBinhLuan(postSnapshot.getKey());
                    if(binhLuan.getMaTruyen().equals(truyen.getMaTruyen())){
                        for(int i = 0; i <listUsers.size(); i++){
                            if(listUsers.get(i).getId().equals(binhLuan.getMaNguoiBinhLuan())){
                                binhLuan.setTenTaiKhoan(listUsers.get(i).getTen());
                            }
                        }
                        listBinhLuan.add(0, binhLuan);
                    }
                }
                binhLuanAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference addBinhLuan = myRefBinhLuan.push();
        btnGuiCmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date currentDate = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = dateFormat.format(currentDate);
                String ten;
                if(user.getDisplayName() == null){
                    ten = "Ẩn danh";
                }
                else {
                    ten = user.getDisplayName();
                }
                BinhLuan binhLuan = new BinhLuan(user.getUid(), truyen.getMaTruyen(), edtCmt.getText().toString(), dateString);
                addBinhLuan.setValue(binhLuan);
                binhLuan.setTenTaiKhoan(ten);
                listBinhLuan.add(0, binhLuan);
                binhLuanAdapter.notifyDataSetChanged();
                edtCmt.setText("");
            }
        });
    }
    private void XuLyChuong(Truyen truyen, DatabaseReference myRefChuong){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefTruyen = database.getReference("Truyen");
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
        chuongAdapter = new ArrayAdapter<>(ChiTietTruyenActivity.this, android.R.layout.simple_list_item_1, tenChuongs);
        lvChuong.setAdapter(chuongAdapter);
        lvChuong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ChiTietTruyenActivity.this, ChuongActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("chuong", listChuong.get(i));
                bundle.putSerializable("list_chuong", listChuong);
                intent.putExtras(bundle);
                startActivity(intent);
                Truyen truyen1 = truyen;
                truyen1.setLuotDoc(truyen.getLuotDoc()+ 1);
                myRefTruyen.child(truyen.getMaTruyen()).setValue(truyen1.toMap());
            }
        });
    }
    private void XyLybtnTheoDoi(Truyen truyen, DatabaseReference myRefUser){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefTruyen = database.getReference("Truyen");
        myRefUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listUsers != null){
                    listUsers.clear();
                }
                final boolean[] checkTheoDoi = {false};
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    User user1 = postSnapshot.getValue(User.class);
                    if (user1.getId().equals(user.getUid()) && user1.getDanhSachTheoDoi().contains(truyen.getMaTruyen())) {
                        checkTheoDoi[0] = true; // Nếu người dùng đang theo dõi truyện, đặt biến này thành true
                        break;
                    }
                }
                if (checkTheoDoi[0]) {
                    // Nếu người dùng đang theo dõi truyện
                    btnTheoDoi.setText("Bỏ theo dõi");
                } else {
                    // Nếu người dùng chưa theo dõi truyện
                    btnTheoDoi.setText("Theo dõi");
                }
                btnTheoDoi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(checkTheoDoi[0] == true){
                            btnTheoDoi.setText("Theo dõi");
                            checkTheoDoi[0] = !checkTheoDoi[0];
                            for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                                User user1 = postSnapshot.getValue(User.class);
                                String thaydoi = "";
                                if(user1 == null){
                                    return;
                                }
                                if(user1.getId().equals(user.getUid())){
                                    thaydoi = user1.getDanhSachTheoDoi().replace("," + truyen.getMaTruyen(), "");
                                    user1.setDanhSachTheoDoi(thaydoi);
                                    user1.setKey(postSnapshot.getKey());
                                    myRefUser.child(user1.getKey()).updateChildren(user1.toMap());
                                }
                            }
                            Truyen truyen1 = truyen;
                            truyen1.setLuotTheoDoi(truyen.getLuotTheoDoi()- 1);
                            myRefTruyen.child(truyen.getMaTruyen()).setValue(truyen1.toMap());
                        }
                        else if(checkTheoDoi[0] == false) {
                            btnTheoDoi.setText("Bỏ theo dõi");
                            checkTheoDoi[0] = !checkTheoDoi[0];
                            for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                                User user1 = postSnapshot.getValue(User.class);
                                String thaydoi = "";
                                if(user1 == null){
                                    return;
                                }
                                if(user1.getId().equals(user.getUid())){
                                    thaydoi = user1.getDanhSachTheoDoi().concat("," + truyen.getMaTruyen());
                                    user1.setDanhSachTheoDoi(thaydoi);
                                    user1.setKey(postSnapshot.getKey());
                                    myRefUser.child(user1.getKey()).updateChildren(user1.toMap());
                                }
                            }
                            Truyen truyen1 = truyen;
                            truyen1.setLuotTheoDoi(truyen.getLuotTheoDoi()+ 1);
                            myRefTruyen.child(truyen.getMaTruyen()).setValue(truyen1.toMap());
                        }
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void addControl() {
        mytab = findViewById(R.id.tarSach1);
        mytab.setup();
        TabHost.TabSpec spec_GioiThieu, spec_Chuong, spec_BinhLuan;
        //tab Trang chủ
        spec_GioiThieu = mytab.newTabSpec("tab_gioithieu");
        spec_GioiThieu.setContent(R.id.tabGioiThieu);
        spec_GioiThieu.setIndicator("Giới thiệu", null);
        mytab.addTab(spec_GioiThieu);
        //tab Thông báo
        spec_Chuong = mytab.newTabSpec("tab_chuong");
        spec_Chuong.setContent(R.id.tabChuong);
        spec_Chuong.setIndicator("DS.Chương", null);
        mytab.addTab(spec_Chuong);
        //tab Setting
        spec_BinhLuan = mytab.newTabSpec("tab_binhluan");
        spec_BinhLuan.setContent(R.id.tabBinhLuan);
        spec_BinhLuan.setIndicator("Bình Luận", null);
        mytab.addTab(spec_BinhLuan);
        //tab Tài Khoản
    }
}