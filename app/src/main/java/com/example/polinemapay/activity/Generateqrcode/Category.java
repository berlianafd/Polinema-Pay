package com.example.polinemapay.activity.Generateqrcode;

public class Category {
    private int idKategori;
    private String namaKategori;

    public Category(){}

    public Category(int id, String name){
        this.idKategori = id;
        this.namaKategori = name;
    }

    public void setId(int id){
        this.idKategori = id;
    }

    public void setName(String name){
        this.namaKategori = name;
    }

    public int getId(){
        return this.idKategori;
    }

    public String getName(){
        return this.namaKategori;
    }
}
