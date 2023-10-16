package com.example.apptruyen.adapter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apptruyen.ChiTietTruyenActivity;
import com.example.apptruyen.R;
import com.example.apptruyen.model.Truyen;
import com.example.apptruyen.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.List;

public class TruyenTrangChuAdapter extends RecyclerView.Adapter<TruyenTrangChuAdapter.ViewHolder> {

    private List<Truyen> truyenList;

    public TruyenTrangChuAdapter(List<Truyen> truyenList) {
        this.truyenList = truyenList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_truyentrangchu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Truyen truyen = truyenList.get(position);
        if(truyen == null){
            return;
        }

        holder.txtItemTenTruyenTT.setText(truyen.getTenTruyen());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefUser = database.getReference("User");
        myRefUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    if(user.getId().equals(truyen.getTacGia())){
                        holder.txtItemTacGiaTT.setText(user.getTen());
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
                // Sử dụng Glide để tải và hiển thị hình ảnh từ Uri
                Glide.with(holder.itemView.getContext()) // Sử dụng ApplicationContext hoặc Activity.this
                        .load(uri)
                        .error(R.drawable.default_book_cover_2)
                        .into(holder.imgItemBiaTruyenTT);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                holder.imgItemBiaTruyenTT.setImageResource(R.drawable.default_book_cover_2);
                // Xử lý lỗi tải hình ảnh (nếu cần)
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), ChiTietTruyenActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("truyen", truyen);
                intent.putExtras(bundle);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(truyenList != null){
            return truyenList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgItemBiaTruyenTT;
        private TextView txtItemTenTruyenTT, txtItemTacGiaTT;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtItemTenTruyenTT = itemView.findViewById(R.id.txtItemTenTruyenTT);
            txtItemTacGiaTT = itemView.findViewById(R.id.txtItemTacGiaTT);
            imgItemBiaTruyenTT = itemView.findViewById(R.id.imgItemBiaTruyenTT);
        }
    }
}
