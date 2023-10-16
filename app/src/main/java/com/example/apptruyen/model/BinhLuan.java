package com.example.apptruyen.model;

public class BinhLuan {
    private String maBinhLuan;
    private String maNguoiBinhLuan;
    private String maTruyen;
    private String noiDung;
    private String thoiGianBinhLuan;
    private String tenTaiKhoan;

    public BinhLuan(){

    }

    public BinhLuan(String maBinhLuan, String maNguoiBinhLuan, String maTruyen, String noiDung, String thoiGianBinhLuan, String tenTaiKhoan) {
        this.maBinhLuan = maBinhLuan;
        this.maNguoiBinhLuan = maNguoiBinhLuan;
        this.maTruyen = maTruyen;
        this.noiDung = noiDung;
        this.thoiGianBinhLuan = thoiGianBinhLuan;
        this.tenTaiKhoan = tenTaiKhoan;
    }

    public BinhLuan(String maNguoiBinhLuan, String maTruyen, String noiDung, String thoiGianBinhLuan) {
        this.maNguoiBinhLuan = maNguoiBinhLuan;
        this.maTruyen = maTruyen;
        this.noiDung = noiDung;
        this.thoiGianBinhLuan = thoiGianBinhLuan;
    }

    public BinhLuan(String maNguoiBinhLuan, String maTruyen, String noiDung, String thoiGianBinhLuan, String tenTaiKhoan) {
        this.maNguoiBinhLuan = maNguoiBinhLuan;
        this.maTruyen = maTruyen;
        this.noiDung = noiDung;
        this.thoiGianBinhLuan = thoiGianBinhLuan;
        this.tenTaiKhoan = tenTaiKhoan;
    }

    public String getMaBinhLuan() {
        return maBinhLuan;
    }

    public String getMaNguoiBinhLuan() {
        return maNguoiBinhLuan;
    }

    public String getMaTruyen() {
        return maTruyen;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public String getThoiGianBinhLuan() {
        return thoiGianBinhLuan;
    }

    public String getTenTaiKhoan() {
        return tenTaiKhoan;
    }

    public void setMaBinhLuan(String maBinhLuan) {
        this.maBinhLuan = maBinhLuan;
    }

    public void setMaNguoiBinhLuan(String maNguoiBinhLuan) {
        this.maNguoiBinhLuan = maNguoiBinhLuan;
    }

    public void setMaTruyen(String maTruyen) {
        this.maTruyen = maTruyen;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public void setThoiGianBinhLuan(String thoiGianBinhLuan) {
        this.thoiGianBinhLuan = thoiGianBinhLuan;
    }

    public void setTenTaiKhoan(String tenTaiKhoan) {
        this.tenTaiKhoan = tenTaiKhoan;
    }
}
