package com.example.reza.kantingriyanafans;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Admin extends AppCompatActivity {
    Button stats,respass,btDel;
    TextView ktStats;
    FirebaseDatabase dtbs = FirebaseDatabase.getInstance();
    DatabaseReference database = dtbs.getReference("status");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        setListOrder();

        btDel = (Button)findViewById(R.id.clear);
        respass = (Button)findViewById(R.id.resetpass);
        stats = (Button)findViewById(R.id.changeSt);
        ktStats = (TextView)findViewById(R.id.txStat);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        btDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener confDelete = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                dtbs.getReference("order").removeValue();
                                dtbs.getReference("count").child("countOrder").setValue("0000");
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };
                builder.setMessage("Anda Yakin Ingin Menghapus List Order?").setPositiveButton("YA", confDelete)
                        .setNegativeButton("TIDAK", confDelete).show();
            }
        });

        respass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Admin.this, ResetPassword.class);
                startActivity(i);
            }
        });

        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String now = ktStats.getText().toString();
                if(now.equals("Buka")){
                    database.child("valStat").setValue("Tutup");
                    ktStats.setText("Tutup");
                    ktStats.setTextColor(Color.RED);
                    Toast.makeText(Admin.this, "Toko Berhasil Ditutup!", Toast.LENGTH_LONG).show();
                }else if(now.equals("Tutup")){
                    database.child("valStat").setValue("Buka");
                    ktStats.setText("Buka");
                    ktStats.setTextColor(Color.GREEN);
                    Toast.makeText(Admin.this, "Toko Berhasil Dibuka!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(Admin.this, "Connection Error", Toast.LENGTH_LONG).show();
                }
            }
        });

        Query cek = database.child("valStat");
        cek.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(String.class).equals("Buka")){
                    ktStats.setText("Buka");
                    ktStats.setTextColor(Color.GREEN);
                }else if(dataSnapshot.getValue().equals("Tutup")){
                    ktStats.setText("Tutup");
                    ktStats.setTextColor(Color.RED);
                }else{
                    Toast.makeText(Admin.this, "Connection Error", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void setListOrder(){
        final  ListView list = (ListView)findViewById(R.id.listOrder);
        Query query = dtbs.getReference("order");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<String> lsOrder = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String id = ds.getKey();
                    lsOrder.add(0,id);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,lsOrder);
                list.setAdapter(arrayAdapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selectedItem = (String) parent.getItemAtPosition(position);
                        Intent i = new Intent(Admin.this,ViewAdmin.class);
                        i.putExtra("id",selectedItem);
                        i.putExtra("order",selectedItem.substring(0,4));
                        i.putExtra("user",selectedItem.substring(36,selectedItem.length()));
                        startActivity(i);
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Anda yakin ingin keluar?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
