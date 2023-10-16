package com.example.apptruyen.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Truyen implements Serializable {
    private String maTruyen, gioiThieu, tacGia, tenTruyen, tinhTrang, thoiGianViet, biaTruyen, theLoai;
    private int luotDoc, soChuong, luotTheoDoi;
    public Truyen(){

    }

    public Truyen(String maTruyen, String gioiThieu, String tacGia, String tenTruyen, String tinhTrang, String thoiGianViet, String biaTruyen, String theLoai, int luotDoc, int soChuong, int luotTheoDoi) {
        this.maTruyen = maTruyen;
        this.gioiThieu = gioiThieu;
        this.tacGia = tacGia;
        this.tenTruyen = tenTruyen;
        this.tinhTrang = tinhTrang;
        this.thoiGianViet = thoiGianViet;
        this.biaTruyen = biaTruyen;
        this.theLoai = theLoai;
        this.luotDoc = luotDoc;
        this.soChuong = soChuong;
        this.luotTheoDoi = luotTheoDoi;
    }

    public Truyen(String gioiThieu, String tacGia, String tenTruyen, String tinhTrang, String thoiGianViet, String biaTruyen, String theLoai, int luotDoc, int soChuong, int luotTheoDoi) {
        this.gioiThieu = gioiThieu;
        this.tacGia = tacGia;
        this.tenTruyen = tenTruyen;
        this.tinhTrang = tinhTrang;
        this.thoiGianViet = thoiGianViet;
        this.biaTruyen = biaTruyen;
        this.theLoai = theLoai;
        this.luotDoc = luotDoc;
        this.soChuong = soChuong;
        this.luotTheoDoi = luotTheoDoi;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("maTruyen", maTruyen);
        result.put("gioiThieu", gioiThieu);
        result.put("tacGia", tacGia);
        result.put("tenTruyen", tenTruyen);
        result.put("tinhTrang", tinhTrang);
        result.put("thoiGianViet", thoiGianViet);
        result.put("biaTruyen", biaTruyen);
        result.put("theLoai", theLoai);
        result.put("luotDoc", luotDoc);
        result.put("soChuong", soChuong);
        result.put("luotTheoDoi", luotTheoDoi);
        return result;
    }

    public String getMaTruyen() {
        return maTruyen;
    }

    public String getGioiThieu() {
        return gioiThieu;
    }

    public String getTacGia() {
        return tacGia;
    }

    public String getTenTruyen() {
        return tenTruyen;
    }

    public String getTinhTrang() {
        return tinhTrang;
    }

    public String getThoiGianViet() {
        return thoiGianViet;
    }

    public String getBiaTruyen() {
        return biaTruyen;
    }

    public String getTheLoai() {
        return theLoai;
    }

    public int getLuotDoc() {
        return luotDoc;
    }

    public int getSoChuong() {
        return soChuong;
    }

    public int getLuotTheoDoi() {
        return luotTheoDoi;
    }

    public void setMaTruyen(String maTruyen) {
        this.maTruyen = maTruyen;
    }

    public void setGioiThieu(String gioiThieu) {
        this.gioiThieu = gioiThieu;
    }

    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
    }

    public void setTenTruyen(String tenTruyen) {
        this.tenTruyen = tenTruyen;
    }

    public void setTinhTrang(String tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public void setThoiGianViet(String thoiGianViet) {
        this.thoiGianViet = thoiGianViet;
    }

    public void setBiaTruyen(String biaTruyen) {
        this.biaTruyen = biaTruyen;
    }

    public void setTheLoai(String theLoai) {
        this.theLoai = theLoai;
    }

    public void setLuotDoc(int luotDoc) {
        this.luotDoc = luotDoc;
    }

    public void setSoChuong(int soChuong) {
        this.soChuong = soChuong;
    }

    public void setLuotTheoDoi(int luotTheoDoi) {
        this.luotTheoDoi = luotTheoDoi;
    }

}
