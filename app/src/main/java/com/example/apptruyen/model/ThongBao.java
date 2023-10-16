package com.example.apptruyen.model;

public class ThongBao {
    String maThongBao, maTruyen, thoiGianThongBao;
    public ThongBao(){}

    public ThongBao(String maThongBao, String maTruyen, String thoiGianThongBao) {
        this.maThongBao = maThongBao;
        this.maTruyen = maTruyen;
        this.thoiGianThongBao = thoiGianThongBao;
    }

    public ThongBao(String maTruyen, String thoiGianThongBao) {
        this.maTruyen = maTruyen;
        this.thoiGianThongBao = thoiGianThongBao;
    }

    public String getMaThongBao() {
        return maThongBao;
    }

    public String getMaTruyen() {
        return maTruyen;
    }

    public String getThoiGianThongBao() {
        return thoiGianThongBao;
    }

    public void setMaThongBao(String maThongBao) {
        this.maThongBao = maThongBao;
    }

    public void setMaTruyen(String maTruyen) {
        this.maTruyen = maTruyen;
    }

    public void setThoiGianThongBao(String thoiGianThongBao) {
        this.thoiGianThongBao = thoiGianThongBao;
    }
}
