package com.example.apptruyen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.apptruyen.adapter.TruyenTimKiemAdapter;
import com.example.apptruyen.adapter.TruyenTrangChuAdapter;
import com.example.apptruyen.model.Truyen;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TimKiemActivity extends AppCompatActivity {
    private TruyenTimKiemAdapter truyenTimKiemAdapter;
    private ArrayList<Truyen> listTruyenTimKiem, listTruyen;
    private RecyclerView rcvTruyenTimKiem;
    private SearchView SearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timkiem);

        inItUI();

        listTruyenTimKiem = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TimKiemActivity.this, RecyclerView.VERTICAL, false);
        rcvTruyenTimKiem.setLayoutManager(linearLayoutManager);
        truyenTimKiemAdapter = new TruyenTimKiemAdapter(listTruyenTimKiem);
        rcvTruyenTimKiem.setAdapter(truyenTimKiemAdapter);
        getList();

        SearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) { //Click vào nút
                filterData(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) { //Thay đổi từ
                filterData(s);
                return false;
            }
        });
    }
    private void inItUI(){
        rcvTruyenTimKiem = findViewById(R.id.rcvTruyenTimKiem);
        SearchView = findViewById(R.id.SreachView);
    }

    private void getList(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Truyen");

        Query query1 = myRef.orderByChild("thoiGianViet");
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listTruyenTimKiem != null){
                    listTruyenTimKiem.clear();
                }
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Truyen truyen = postSnapshot.getValue(Truyen.class);
                    truyen.setMaTruyen(postSnapshot.getKey());
                    listTruyenTimKiem.add(0, truyen);
                }

                truyenTimKiemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void filterData(String query) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Truyen");

        Query query1 = myRef.orderByChild("thoiGianViet");
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listTruyenTimKiem != null){
                    listTruyenTimKiem.clear();
                }
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Truyen truyen = postSnapshot.getValue(Truyen.class);
                    truyen.setMaTruyen(postSnapshot.getKey());
                    listTruyenTimKiem.add(0, truyen);
                }
                ArrayList<Truyen> filteredData = new ArrayList<>();
                if(query.isEmpty()){
                    listTruyen = listTruyenTimKiem;
                    truyenTimKiemAdapter.clear();
                    truyenTimKiemAdapter.addAll(listTruyenTimKiem);
                    truyenTimKiemAdapter.notifyDataSetChanged();
                }
                else {
                    listTruyen = listTruyenTimKiem;
                    for (Truyen item : listTruyenTimKiem) {
                        if (item.getTenTruyen().toLowerCase().contains(query.toLowerCase())) {
                            filteredData.add(item);
                        }
                    }
                    truyenTimKiemAdapter.clear();
                    truyenTimKiemAdapter.addAll(filteredData);
                    truyenTimKiemAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}