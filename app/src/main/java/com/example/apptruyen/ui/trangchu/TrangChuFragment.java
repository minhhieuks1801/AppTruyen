package com.example.apptruyen.ui.trangchu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apptruyen.ChiTietTruyenActivity;
import com.example.apptruyen.R;
import com.example.apptruyen.adapter.TruyenTrangChuAdapter;
import com.example.apptruyen.databinding.FragmentTrangchuBinding;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrangChuFragment extends Fragment {
    private List<Truyen> listTruyenMoi, listTruyenLuotDoc, listTruyenTheoDoi;
    private ArrayList<User> listUsers;
    private RecyclerView rcvTruyenMoiNhat, rcvTruyenDocNhieu, rcvTruyenTheoDoiNhieu;
    private TextView txtTenTruyenTrangChu, txtGioiThieuTruyenTrangChu, txtLuotDocTruyenTrangChu;
    private Button btnDocTruyenTrangChu, btnTheoDoiTruyenTrangChu;
    private ImageView imgBiaTruyenTrangChu;
    private FragmentTrangchuBinding binding;
    private TruyenTrangChuAdapter trangChuAdapter1, trangChuAdapter2, trangChuAdapter3;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTrangchuBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initUI();
        ListTT();
        getListTruyen();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initUI(){
        rcvTruyenMoiNhat = binding.rcvTruyenMoiNhat;
        rcvTruyenDocNhieu = binding.rcvTruyenDocNhieu;
        rcvTruyenTheoDoiNhieu = binding.rcvTruyenTheoDoiNhieu;
        txtTenTruyenTrangChu = binding.txtTenTruyenTrangChu;
        txtGioiThieuTruyenTrangChu = binding.txtGioiThieuTruyenTrangChu;
        txtLuotDocTruyenTrangChu = binding.txtLuotDocTruyenTrangChu;
        btnDocTruyenTrangChu = binding.btnDocTruyenTrangChu;
        btnTheoDoiTruyenTrangChu = binding.btnTheoDoiTruyenTrangChu;
        imgBiaTruyenTrangChu = binding.imgBiaTruyenTrangChu;
    }

    private void ListTT(){
        listTruyenMoi = new ArrayList<>();
        listTruyenLuotDoc = new ArrayList<>();
        listTruyenTheoDoi = new ArrayList<>();

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        rcvTruyenMoiNhat.setLayoutManager(linearLayoutManager1);
        trangChuAdapter1 = new TruyenTrangChuAdapter(listTruyenMoi);
        rcvTruyenMoiNhat.setAdapter(trangChuAdapter1);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        rcvTruyenDocNhieu.setLayoutManager(linearLayoutManager2);
        trangChuAdapter2 = new TruyenTrangChuAdapter(listTruyenLuotDoc);
        rcvTruyenDocNhieu.setAdapter(trangChuAdapter2);

        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        rcvTruyenTheoDoiNhieu.setLayoutManager(linearLayoutManager3);
        trangChuAdapter3 = new TruyenTrangChuAdapter(listTruyenTheoDoi);
        rcvTruyenTheoDoiNhieu.setAdapter(trangChuAdapter3);
    }

    private void getListTruyen(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Truyen");

        Query query1 = myRef.orderByChild("thoiGianViet");
        query1.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(listTruyenMoi != null){
                   listTruyenMoi.clear();
               }
               for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                   Truyen truyen = postSnapshot.getValue(Truyen.class);
                   truyen.setMaTruyen(postSnapshot.getKey());
                   listTruyenMoi.add(0, truyen);
               }
               getTruyenTrangChu(listTruyenMoi);
               trangChuAdapter1.notifyDataSetChanged();
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
        });

        Query query2 = myRef.orderByChild("luotDoc");
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listTruyenLuotDoc != null){
                    listTruyenLuotDoc.clear();
                }
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Truyen truyen = postSnapshot.getValue(Truyen.class);
                    truyen.setMaTruyen(postSnapshot.getKey());
                    listTruyenLuotDoc.add(0, truyen);
                }
               trangChuAdapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Query query3 = myRef.orderByChild("luotTheoDoi");
        query3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listTruyenTheoDoi != null){
                    listTruyenTheoDoi.clear();
                }
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Truyen truyen = postSnapshot.getValue(Truyen.class);
                    truyen.setMaTruyen(postSnapshot.getKey());
                    listTruyenTheoDoi.add(0, truyen);
                }
                trangChuAdapter3.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getTruyenTrangChu(List<Truyen> list){
        Truyen truyen = list.get(0);
        txtTenTruyenTrangChu.setText(truyen.getTenTruyen()+ "");
        txtLuotDocTruyenTrangChu.setText(String.valueOf(truyen.getLuotDoc())+ "");
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
                    // Sử dụng Glide để tải và hiển thị hình ảnh từ Uri
                    Glide.with(getActivity()) // Sử dụng ApplicationContext hoặc Activity.this
                            .load(uri)
                            .error(R.drawable.default_book_cover_2)
                            .into(imgBiaTruyenTrangChu);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    imgBiaTruyenTrangChu.setImageResource(R.drawable.default_book_cover_2);
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
        txtRef.getBytes(1024 * 1024) // Giới hạn tối đa là 1 MB (điều này có thể được điều chỉnh)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {

                        String txtContent = new String(bytes, Charset.forName("UTF-8"));
                        txtGioiThieuTruyenTrangChu.setText(txtContent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        txtGioiThieuTruyenTrangChu.setText("Đang lỗi, báo admin ngay và luôn đê!");
                    }
                });
        btnDocTruyenTrangChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChiTietTruyenActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("truyen", truyen);
                intent.putExtras(bundle);
                getContext().startActivity(intent);
            }
        });
        XyLybtnTheoDoi(truyen);
    }

    private void XyLybtnTheoDoi(Truyen truyen){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefTruyen = database.getReference("Truyen");
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
                    if (user1.getId().equals(user.getUid()) && user1.getDanhSachTheoDoi().contains(truyen.getMaTruyen())) {
                        checkTheoDoi[0] = true; // Nếu người dùng đang theo dõi truyện, đặt biến này thành true
                        break;
                    }
                }
                if (checkTheoDoi[0]) {
                    // Nếu người dùng đang theo dõi truyện
                    btnTheoDoiTruyenTrangChu.setText("-");
                } else {
                    // Nếu người dùng chưa theo dõi truyện
                    btnTheoDoiTruyenTrangChu.setText("+");
                }
                btnTheoDoiTruyenTrangChu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(checkTheoDoi[0] == true){
                            btnTheoDoiTruyenTrangChu.setText("+");
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
                            btnTheoDoiTruyenTrangChu.setText("Bỏ theo dõi");
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

    private void ActionUI(){

    }
}