package com.example.apptruyen.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.apptruyen.R;
import com.example.apptruyen.model.BinhLuan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BinhLuan_Adapter extends ArrayAdapter<BinhLuan> {
    private Activity context;
    private int iDLayout;
    private ArrayList<BinhLuan> mylist;
    private TextView txtTenNguoiBinhLuan, txtNoiDungBinhLuan, txtThoiGianBinhLuan;

    public BinhLuan_Adapter(Activity context, int iDLayout, ArrayList<BinhLuan> mylistBL) {
        super(context, iDLayout, mylistBL);
        this.context = context;
        this.iDLayout = iDLayout;
        this.mylist = mylistBL;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater myinflater = context.getLayoutInflater();
        convertView = myinflater.inflate(iDLayout, null);

        txtTenNguoiBinhLuan = convertView.findViewById(R.id.txtTenNguoiBinhLuan);
        txtNoiDungBinhLuan = convertView.findViewById(R.id.txtNoiDungBinhLuan);
        txtThoiGianBinhLuan = convertView.findViewById(R.id.txtThoiGianBinhLuan);

        txtTenNguoiBinhLuan.setText(mylist.get(position).getTenTaiKhoan());
        txtNoiDungBinhLuan.setText(mylist.get(position).getNoiDung());
        txtThoiGianBinhLuan.setText(tinhThoiGian(mylist.get(position).getThoiGianBinhLuan()));
        return convertView;
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
                a =khoangCach/86400 + " ngày trước";
            }
            else if (khoangCach >= 2592000 && (khoangCach/2592000) < 12) {
                a =khoangCach/2592000 + " tháng trước";
            }
            else if((khoangCach/2592000) >= 12){
                a = ((khoangCach/2592000)/12) + "năm trước";
            }
        } catch (ParseException e) {

        }
        return a;
    }
}
