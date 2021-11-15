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

public class Minuman extends ListFragment {
    String[] minuman;
    String nm,hrg,gbr;
    List<String> menu = new ArrayList<>();
    ArrayList<CustomList> menuMinuman = new ArrayList<>();
    ArrayAdapter<String> item;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setMenu();
        for(int x=0; x<menuMinuman.size(); x++){
            menu.add(menuMinuman.get(x).getNama());
        }
        minuman = menu.toArray(new String[0]);
        item = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,minuman);
        setListAdapter(item);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void onStart(){
        super.onStart();
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getContext(),ViewItem.class);
                nm = menuMinuman.get(position).getNama();
                hrg = menuMinuman.get(position).getHarga();
                gbr = menuMinuman.get(position).getGambar();
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
        menuMinuman.add(new CustomList("Es Cappucino","4000","escapucino"));
        menuMinuman.add(new CustomList("Es Milo","4000","esmilo"));
        menuMinuman.add(new CustomList("Es Susu Coklat","3000","essusucoklat"));
        menuMinuman.add(new CustomList("Es Susu Putih","3000","essusuputih"));
        menuMinuman.add(new CustomList("Es Teh","3000","esteh"));
        menuMinuman.add(new CustomList("Es Nutrisari","4000","nutrisari"));
        menuMinuman.add(new CustomList("Pop Ice Coklat","4000","popicecoklat"));
        menuMinuman.add(new CustomList("Pop Ice Strawberry","4000","popicestrawberry"));
        menuMinuman.add(new CustomList("Susu Coklat Hangat","3000","susucoklathangat"));
        menuMinuman.add(new CustomList("Susu Putih Hangat","3000","susuputihhangat"));
        menuMinuman.add(new CustomList("Teh Manis Hangat","2500","tehhangat"));

    }
}
