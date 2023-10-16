package com.example.apptruyen.ui.thongbao;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptruyen.adapter.ThongBaoAdapter;
import com.example.apptruyen.adapter.TruyenTrangChuAdapter;
import com.example.apptruyen.databinding.FragmentThongbaoBinding;
import com.example.apptruyen.model.ThongBao;
import com.example.apptruyen.model.Truyen;
import com.example.apptruyen.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ThongBaoFragment extends Fragment {
    private RecyclerView rcvThongBao;
    private ArrayList<ThongBao> listThongBao;
    private ThongBaoAdapter thongBaoAdapter;
    private FragmentThongbaoBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentThongbaoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        rcvThongBao = binding.rcvThongBao;
        listThongBao = new ArrayList<>();
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcvThongBao.setLayoutManager(linearLayoutManager1);
        thongBaoAdapter = new ThongBaoAdapter(listThongBao);
        rcvThongBao.setAdapter(thongBaoAdapter);
        getListThongBao();
        return root;
    }

    private void getListThongBao(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference myRefThongBao = database.getReference("ThongBao");
        DatabaseReference myRefUser = database.getReference("User");
        Query query = myRefThongBao.orderByChild("thoiGianThongBao");

        myRefUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()){
                    User user = postSnapshot.getValue(User.class);
                    user.setKey(postSnapshot.getKey());
                    if(user.getId().equals(user1.getUid())){
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(listThongBao != null){
                                    listThongBao.clear();
                                }
                                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                                    ThongBao thongBao = postSnapshot.getValue(ThongBao.class);
                                    thongBao.setMaThongBao(postSnapshot.getKey());
                                    if(user.getDanhSachTheoDoi().contains(thongBao.getMaTruyen())){
                                        listThongBao.add(0, thongBao);
                                    }
                                }
                                listThongBao.size();
                                thongBaoAdapter.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}