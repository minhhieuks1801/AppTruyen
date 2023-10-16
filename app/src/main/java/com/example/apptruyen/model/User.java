package com.example.apptruyen.model;

import java.util.HashMap;
import java.util.Map;

public class User {
    String key, id, ten, danhSachTheoDoi;
    public User(){}

    public User(String key, String id, String ten, String danhSachTheoDoi) {
        this.key = key;
        this.id = id;
        this.ten = ten;
        this.danhSachTheoDoi = danhSachTheoDoi;
    }

    public User(String id, String ten, String danhSachTheoDoi) {
        this.id = id;
        this.ten = ten;
        this.danhSachTheoDoi = danhSachTheoDoi;
    }

    public String getId() {
        return id;
    }

    public String getTen() {
        return ten;
    }

    public String getDanhSachTheoDoi() {
        return danhSachTheoDoi;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public void setDanhSachTheoDoi(String danhSachTheoDoi) {
        this.danhSachTheoDoi = danhSachTheoDoi;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("ten", ten);
        result.put("danhSachTheoDoi", danhSachTheoDoi);
        return result;
    }
}
