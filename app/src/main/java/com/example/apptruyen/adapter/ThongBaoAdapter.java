package com.example.apptruyen.adapter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apptruyen.ChiTietTruyenActivity;
import com.example.apptruyen.R;
import com.example.apptruyen.model.ThongBao;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ThongBaoAdapter extends RecyclerView.Adapter<ThongBaoAdapter.ViewHolder>{
    private ArrayList<ThongBao> listThongBao;

    public ThongBaoAdapter(ArrayList<ThongBao> listThongBao) {
        this.listThongBao = listThongBao;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_thongbao, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ThongBao thongBao = listThongBao.get(position);
        if(thongBao == null){
            return;
        }
        holder.txtThoiGianThongBao.setText(tinhThoiGian(thongBao.getThoiGianThongBao()));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefTruyen = database.getReference("Truyen");
        myRefTruyen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Truyen truyen = postSnapshot.getValue(Truyen.class);
                    truyen.setMaTruyen(postSnapshot.getKey());
                    if(truyen.getMaTruyen().equals(thongBao.getMaTruyen())){
                        holder.txtnoiDungThongBao.setText("Truyện "+ truyen.getTenTruyen()+" đã thêm 1 chương mới");
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
                                        .into(holder.imgThongBao);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                holder.imgThongBao.setImageResource(R.drawable.default_book_cover_2);
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
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listThongBao != null){
            return listThongBao.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgThongBao;
        private TextView txtnoiDungThongBao, txtThoiGianThongBao;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThongBao = itemView.findViewById(R.id.imgThongBao);
            txtnoiDungThongBao = itemView.findViewById(R.id.txtnoiDungThongBao);
            txtThoiGianThongBao = itemView.findViewById(R.id.txtThoiGianThongBao);
        }
    }

    public String tinhThoiGian(String pastTime){
        String a = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = formatter.parse(pastTime);
            Date now = new Date();
            long khoangCach = (now.getTime() - date.getTime()) / 1000;

            if(khoangCach < 60){
                a = khoangCach + " giây trước";
            }
            else if(khoangCach >= 60 && khoangCach < 3600){
                a = khoangCach/60 + " phút trước";
            }
            else if(khoangCach >= 3600 && khoangCach < 86400){
                a = khoangCach/3600 + " giờ " + (khoangCach%3600)/60 + " phút trước";
            }
            else if (khoangCach >= 86400 && khoangCach < 2592000) {
                a =khoangCach/86400 + "ngày trước";
            }
            else if (khoangCach >= 2592000 && (khoangCach/2592000) < 12) {
                a =khoangCach/2592000 + "tháng trước";
            }
            else if((khoangCach/2592000) >= 12){
                a = ((khoangCach/2592000)/12) + "năm trước";
            }
        } catch (ParseException e) {

        }
        return a;
    }
}
