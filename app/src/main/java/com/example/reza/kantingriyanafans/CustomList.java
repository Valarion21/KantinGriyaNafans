package com.example.reza.kantingriyanafans;

import java.util.ArrayList;

public class CustomList extends ArrayList<String> {
    private String Nama;
    private String Harga;
    private String Gambar;

    public CustomList(String nama, String harga, String gambar) {
        this.Nama = nama;
        this.Harga = harga;
        this.Gambar = gambar;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        this.Nama = nama;
    }

    public String getHarga() {
        return Harga;
    }

    public void setHarga(String harga) {
        this.Harga = harga;
    }

    public String getGambar() {
        return Gambar;
    }

    public void setGambar(String gambar) {
        this.Gambar = gambar;
    }
}
