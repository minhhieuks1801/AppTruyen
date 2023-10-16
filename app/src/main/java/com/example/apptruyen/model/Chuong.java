package com.example.apptruyen.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Chuong implements Serializable {
    String maChuong, maTruyen, noiDungChuong, tenChuong, thoiGianDang;

    public Chuong(){}

    public Chuong(String maChuong, String maTruyen, String noiDungChuong, String tenChuong, String thoiGianDang) {
        this.maChuong = maChuong;
        this.maTruyen = maTruyen;
        this.noiDungChuong = noiDungChuong;
        this.tenChuong = tenChuong;
        this.thoiGianDang = thoiGianDang;
    }

    public Chuong(String maTruyen, String noiDungChuong, String tenChuong, String thoiGianDang) {
        this.maTruyen = maTruyen;
        this.noiDungChuong = noiDungChuong;
        this.tenChuong = tenChuong;
        this.thoiGianDang = thoiGianDang;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("maChuong", maChuong);
        result.put("maTruyen", maTruyen);
        result.put("noiDungChuong", noiDungChuong);
        result.put("tenChuong", tenChuong);
        result.put("thoiGianDang", thoiGianDang);
        return result;
    }

    public String getMaTruyen() {
        return maTruyen;
    }

    public String getNoiDungChuong() {
        return noiDungChuong;
    }

    public String getTenChuong() {
        return tenChuong;
    }

    public void setMaTruyen(String maTruyen) {
        this.maTruyen = maTruyen;
    }

    public void setNoiDungChuong(String noiDungChuong) {
        this.noiDungChuong = noiDungChuong;
    }

    public void setTenChuong(String tenChuong) {
        this.tenChuong = tenChuong;
    }

    public String getThoiGianDang() {
        return thoiGianDang;
    }

    public void setThoiGianDang(String thoiGianDang) {
        this.thoiGianDang = thoiGianDang;
    }

    public String getMaChuong() {
        return maChuong;
    }

    public void setMaChuong(String maChuong) {
        this.maChuong = maChuong;
    }
}
