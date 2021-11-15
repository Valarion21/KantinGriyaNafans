package com.example.reza.kantingriyanafans;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewAdmin extends AppCompatActivity {
    FirebaseDatabase dtbs = FirebaseDatabase.getInstance();
    TextView userodr;
    Context mcon = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_admin);

        userodr = (TextView)findViewById(R.id.userOdr);
        userodr.setText(getIntent().getStringExtra("user"));
        Query query1 = dtbs.getReference("user").child(getIntent().getStringExtra("user"));
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView kmrodr = (TextView) findViewById(R.id.kmrOdr);
                TextView hpodr = (TextView)findViewById(R.id.hpOdr);
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    if(ds.getKey().equals("no_kmr")){
                        kmrodr.setText(ds.getValue(String.class));
                    }else if(ds.getKey().equals("no_hp")){
                        hpodr.setText(ds.getValue(String.class));
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        final ArrayList<CustomList> orderItem = new ArrayList<>();
        Query query2 = dtbs.getReference("order");
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView jmlodr = (TextView)findViewById(R.id.jmlOdr);
                TextView totodr = (TextView)findViewById(R.id.totOdr);
                int x=0;
                String namaMn = "";
                String kdMn = "";
                String Hrg = "";
                int totalHrg = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    if(ds.getKey().equals(getIntent().getStringExtra("id"))){
                        for(DataSnapshot ds1:ds.getChildren()){
                            for (DataSnapshot ds2:ds1.getChildren()){
                                if(ds2.getKey().equals("nama_menu")){
                                    namaMn=ds2.getValue(String.class);
                                }else if(ds2.getKey().equals("id_menu")){
                                    kdMn=ds2.getValue(String.class);
                                }else if(ds2.getKey().equals("harga")){
                                    Hrg=ds2.getValue(String.class);
                                }
                            }
                            orderItem.add(new CustomList(namaMn,Hrg,kdMn));
                            x=x+1;
                            totalHrg = totalHrg+Integer.parseInt(Hrg);
                        }
                    }
                }
                totodr.setText(String.valueOf(totalHrg));
                jmlodr.setText(String.valueOf(x));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        AdapterKeranjang adapter = new AdapterKeranjang(mcon,R.layout.item_order,orderItem);
        ListView mListView = (ListView) findViewById(R.id.vwOrder);
        mListView.setAdapter(adapter);
    }
}
