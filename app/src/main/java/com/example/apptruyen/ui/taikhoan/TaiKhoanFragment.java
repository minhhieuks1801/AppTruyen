package com.example.apptruyen.ui.taikhoan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.apptruyen.DoiMatKhauActivity;
import com.example.apptruyen.KiemTraDangNhapActivity;
import com.example.apptruyen.MainActivity;
import com.example.apptruyen.SuaThongTinTaiKhoanActivity;
import com.example.apptruyen.TacGiaActivity;
import com.example.apptruyen.databinding.FragmentTaikhoanBinding;
import com.google.firebase.auth.FirebaseAuth;

public class TaiKhoanFragment extends Fragment {
    private TextView txtSuaThongTin, txtDoiMatKhau, txtTacPham, txtDangXuat;

    private FragmentTaikhoanBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTaikhoanBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initUi();
        actionUI();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initUi(){
        txtDoiMatKhau = binding.txtDoiMatKhau;
        txtSuaThongTin = binding.txtSuaThongTin;
        txtTacPham = binding.txtTacPham;
        txtDangXuat = binding.txtDangXuat;
    }

    private void actionUI(){
        txtDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), KiemTraDangNhapActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        txtSuaThongTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SuaThongTinTaiKhoanActivity.class);
                startActivity(intent);
            }
        });

        txtDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DoiMatKhauActivity.class);
                startActivity(intent);
            }
        });
        txtTacPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TacGiaActivity.class);
                startActivity(intent);
            }
        });
    }


}