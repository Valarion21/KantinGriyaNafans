package com.example.reza.kantingriyanafans;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class Makanan extends ListFragment {
    String[] makanan;
    String nm,hrg,gbr;
    List<String> menu = new ArrayList<>();
    ArrayList<CustomList> menuMakanan = new ArrayList<>();
    ArrayAdapter<String> item;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setMenu();
        for(int x=0; x<menuMakanan.size(); x++){
            menu.add(menuMakanan.get(x).getNama());
        }
        makanan = menu.toArray(new String[0]);
        item = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,makanan);
        setListAdapter(item);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void onStart(){
        super.onStart();
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getContext(),ViewItem.class);
                nm = menuMakanan.get(position).getNama();
                hrg = menuMakanan.get(position).getHarga();
                gbr = menuMakanan.get(position).getGambar();
                String uname = getActivity().getIntent().getStringExtra("Username");
                i.putExtra("Username",uname);
                i.putExtra("nama",nm);
                i.putExtra("harga",hrg);
                i.putExtra("gambar",gbr);
                startActivity(i);
            }
        });
    }

    public void setMenu(){
        menuMakanan.add(new CustomList("Ayam Penyet Sambal Hijau","13000","ayampenyetijo"));
        menuMakanan.add(new CustomList("Ayam Penyet Sambal Merah","12000","ayampenyet"));
        menuMakanan.add(new CustomList("Ayam Plecing Kangkung","11000","ayamplecing"));
        menuMakanan.add(new CustomList("Ayam Rica-Rica","12000","ayamricarica"));
        menuMakanan.add(new CustomList("Ayam Saos Mentega","13000","ayamsaosmentega"));
        menuMakanan.add(new CustomList("Bayam Bening","3000","bayem"));
        menuMakanan.add(new CustomList("Cah Kangkung","3000","cahkangkung"));
        menuMakanan.add(new CustomList("Capcay Kuah","7000","capcay"));
        menuMakanan.add(new CustomList("Fuyunghai","6000","fuyunghai"));
        menuMakanan.add(new CustomList("Indomie Goreng","12000","miegoreng"));
        menuMakanan.add(new CustomList("Indomie Rebus","12000","mierebus"));
        menuMakanan.add(new CustomList("Kentang Goreng","8000","kentanggoreng"));
        menuMakanan.add(new CustomList("Mie Godok Tek-Tek","11000","miegodok"));
        menuMakanan.add(new CustomList("Mie Goreng Cina","11000","miegorengcina"));
        menuMakanan.add(new CustomList("Nasi","4000","nasi"));
        menuMakanan.add(new CustomList("Nasi Goreng Ayam","11000","nasigorengayam"));
        menuMakanan.add(new CustomList("Nasi Goreng Bakso","12000","nasigorengbakso"));
        menuMakanan.add(new CustomList("Nasi Goreng Cina","11000","nasigorengcina"));
        menuMakanan.add(new CustomList("Nasi Goreng Kornet","11000","nasigorengcornet"));
        menuMakanan.add(new CustomList("Nasi Goreng Jawa","13000","nasigorengjawa"));
        menuMakanan.add(new CustomList("Nuget Goreng","6000","nuget"));
        menuMakanan.add(new CustomList("Sop Ayam","9000","sopayam"));
        menuMakanan.add(new CustomList("Sosis Goreng","6000","sosis"));
        menuMakanan.add(new CustomList("Tempe Goreng","6000","tempegoreng"));

    }
}

