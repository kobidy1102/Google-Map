package com.example.nguyenhuy.googlemap;

public class ThongTinThanhVien {
   public  String ten;
    public double viDo;
    public double kinhDo;
    public int stt;
    public String linkAnh;


    public ThongTinThanhVien(){

    }

    public ThongTinThanhVien(String ten, double viDo, double kinhDo, int stt, String linkAnh) {
        this.ten = ten;
        this.viDo = viDo;
        this.kinhDo = kinhDo;
        this.stt = stt;
        this.linkAnh = linkAnh;
    }
}
