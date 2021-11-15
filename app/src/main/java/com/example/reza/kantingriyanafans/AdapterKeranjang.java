package com.example.reza.kantingriyanafans;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterKeranjang extends ArrayAdapter<CustomList> {
    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    private static  class ViewHolder {
        TextView nama;
        TextView harga;
        ImageView gambar;
    }

    public AdapterKeranjang(Context context, int resource, ArrayList<CustomList> objects){
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String nama = getItem(position).getNama();
        String harga = getItem(position).getHarga();
        String gambar = getItem(position).getGambar();

        CustomList barang = new CustomList(nama,harga,gambar);

        final View result;
        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.nama = (TextView) convertView.findViewById(R.id.tvItemOrder);
            holder.harga = (TextView) convertView.findViewById(R.id.tvHargaOrder);
            holder.gambar = (ImageView) convertView.findViewById(R.id.imItemOrder);
            result = convertView;
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        lastPosition = position;
        holder.nama.setText(barang.getNama());
        holder.harga.setText(barang.getHarga());
        int res = mContext.getResources().getIdentifier(barang.getGambar(),"drawable",mContext.getPackageName());
        holder.gambar.setImageResource(res);
        return  convertView;
    }
}
