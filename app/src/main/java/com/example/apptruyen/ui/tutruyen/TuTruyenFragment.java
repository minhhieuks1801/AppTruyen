package com.example.apptruyen.ui.tutruyen;

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

import com.example.apptruyen.adapter.TruyenTimKiemAdapter;
import com.example.apptruyen.adapter.TruyenTrangChuAdapter;
import com.example.apptruyen.databinding.FragmentTutruyenBinding;
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
import java.util.List;

public class TuTruyenFragment extends Fragment {
    private RecyclerView rcvTuTruyen;
    private List<Truyen> listTuTruyen;
    private List<User> listUser;
    private TruyenTimKiemAdapter trangChuAdapter;
    private FragmentTutruyenBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTutruyenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listTuTruyen = new ArrayList<>();
        listUser = new ArrayList<>();

        rcvTuTruyen = binding.rcvTuTruyen;
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcvTuTruyen.setLayoutManager(linearLayoutManager1);
        trangChuAdapter = new TruyenTimKiemAdapter(listTuTruyen);
        rcvTuTruyen.setAdapter(trangChuAdapter);
        getListTruyen();

        return root;
    }
    private void getListTruyen(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference myRefTruyen = database.getReference("Truyen");
        DatabaseReference myRefUser = database.getReference("User");

        Query query1 = myRefTruyen.orderByChild("thoiGianViet");

        myRefUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listUser != null){
                    listUser.clear();
                }
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    user.setKey(postSnapshot.getKey());
                    if(user.getId().equals(user1.getUid())){
                        query1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(listTuTruyen != null){
                                    listTuTruyen.clear();
                                }
                                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                                    Truyen truyen = postSnapshot.getValue(Truyen.class);
                                    truyen.setMaTruyen(postSnapshot.getKey());
                                    if(user.getDanhSachTheoDoi().contains(truyen.getMaTruyen())){
                                        listTuTruyen.add(0, truyen);
                                    }
                                }
                                trangChuAdapter.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                        break;
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