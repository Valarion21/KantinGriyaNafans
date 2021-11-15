package com.example.reza.kantingriyanafans;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    Hold dbCenter;
    ViewPager vPager;
    ImageView btKr,btAkun;
    FirebaseDatabase dtbs = FirebaseDatabase.getInstance();
    DatabaseReference database = dtbs.getReference("status");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dtbs.getReference("user").child(getIntent().getStringExtra("Username")).child("isi").setValue(0);
        setStatus();
        countKer();

        btKr = (ImageView)findViewById(R.id.keranjang);
        vPager=(ViewPager)findViewById(R.id.viewPager);
        btAkun = (ImageView)findViewById(R.id.akun);

        dbCenter = new Hold(getApplicationContext());
        SQLiteDatabase db = dbCenter.getWritableDatabase();
        db.execSQL("DELETE FROM pesan");
        db.close();
        vPager.setAdapter(new PageAdapter(getSupportFragmentManager()));

        btKr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query cekid = dtbs.getReference("count").child("countOrder");
                cekid.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Intent keranjang = new Intent (MainActivity.this,Keranjang.class);
                        String username = getIntent().getStringExtra("Username");
                        keranjang.putExtra("Username",username);
                        keranjang.putExtra("idnow",dataSnapshot.getValue(String.class));
                        startActivity(keranjang);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });

        btAkun.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String username = getIntent().getStringExtra("Username");
                Intent akun = new Intent(MainActivity.this,Akun.class);
                akun.putExtra("Username",username);
                startActivity(akun);
            }
        });
    }

    public void setStatus(){
        Query cek = database.child("valStat");
        cek.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView stat = (TextView)findViewById(R.id.status);
                if(dataSnapshot.getValue(String.class).equals("Buka")){
                    stat.setText("Kantin Buka");
                    stat.setTextColor(Color.GREEN);
                }else if(dataSnapshot.getValue(String.class).equals("Tutup")){
                    stat.setText("Kantin Tutup");
                    stat.setTextColor(Color.RED);
                }else{
                    Toast.makeText(MainActivity.this, "Connection Error", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void countKer(){
        Query count = dtbs.getReference("user").child(getIntent().getStringExtra("Username")).child("isi");
        count.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long val = (long)dataSnapshot.getValue();
                TextView count = (TextView)findViewById(R.id.countKer);
                count.setText(String.valueOf(val));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Apakah anda yakin ingin keluar?")
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
