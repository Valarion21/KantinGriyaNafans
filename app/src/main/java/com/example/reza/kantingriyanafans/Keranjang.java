package com.example.reza.kantingriyanafans;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Keranjang extends AppCompatActivity {
    Cursor cursor;
    Button kosong,pesan;
    Hold dbcenter;
    FirebaseDatabase dtbs = FirebaseDatabase.getInstance();
    DatabaseReference database = dtbs.getReference("order");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang);

        pesan = (Button)findViewById(R.id.btPesan);
        kosong =(Button)findViewById(R.id.btKosong);

        dbcenter=new Hold(this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        setList();

        kosong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener confDelete = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                final SQLiteDatabase db = dbcenter.getReadableDatabase();
                                db.execSQL("DELETE FROM pesan");
                                db.close();
                                dtbs.getReference("user").child(getIntent().getStringExtra("Username")).child("isi").setValue(0);
                                setList();
                                Toast.makeText(getApplicationContext(), "Hapus Berhasil!", Toast.LENGTH_LONG).show();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };
                builder.setMessage("Yakin Ingin Menghapus Item Dikeranjang?").setPositiveButton("YA", confDelete)
                        .setNegativeButton("TIDAK", confDelete).show();
            }
        });

        pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query cekStat = dtbs.getReference("status").child("valStat");
                cekStat.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue(String.class).equals("Tutup")){
                            Toast.makeText(Keranjang.this, "Maaf Kantin Tutup!", Toast.LENGTH_LONG).show();
                            return;
                        }else{
                            sendPesan();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    public void sendPesan(){
        dbcenter = new Hold(this);
        final String akun = getIntent().getStringExtra("Username");
        String space = "                                ";
        String idbefore = getIntent().getStringExtra("idnow");
        int idnw = Integer.parseInt(idbefore)+1;
        dtbs.getReference("count").child("countOrder").setValue(String.format("%04d",idnw));
        String id_order = String.format("%04d", idnw).concat(space).concat(akun);
        //final String id_order = new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime()).concat(String.format("%04d", r.nextInt(10000))).concat(space).concat(akun);
        final String timeStamp = new SimpleDateFormat("HH:mm:ss yyy-MM-dd").format(Calendar.getInstance().getTime());
        final SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM pesan", null);
        cursor.moveToFirst();
        for (int x=0;x<cursor.getCount();x++){
            cursor.moveToPosition(x);
            OrderFunction(id_order,akun,String.valueOf(x+1),cursor.getString(0),cursor.getString(2),cursor.getString(1),timeStamp);
        }
        final SQLiteDatabase db1 = dbcenter.getReadableDatabase();
        db1.execSQL("DELETE FROM pesan");
        db1.close();
        setList();
        dtbs.getReference("user").child(getIntent().getStringExtra("Username")).child("isi").setValue(0);
        finish();
    }

    public void setList(){
        ArrayList<CustomList> orderan = new ArrayList<>();
        dbcenter = new Hold(this);
        final SQLiteDatabase db = dbcenter.getReadableDatabase();
        int Tharga =0;
        TextView total = (TextView)findViewById(R.id.totalHrg);
        cursor = db.rawQuery("SELECT * FROM pesan", null);
        cursor.moveToFirst();
        for (int x=0;x<cursor.getCount();x++){
            cursor.moveToPosition(x);
            orderan.add(new CustomList(cursor.getString(0),cursor.getString(1),cursor.getString(2)));
            Tharga = Tharga + Integer.parseInt(cursor.getString(1));
        }
        total.setText(String.valueOf(Tharga));
        AdapterKeranjang adapter = new AdapterKeranjang(this,R.layout.item_order,orderan);
        ListView mListView = (ListView) findViewById(R.id.lsOrder);
        mListView.setAdapter(adapter);
    }

    public void OrderFunction(String id, String akun, String item, String menu,String idmenu,String harga,String waktu){

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("nama_usr",akun);
        hashMap.put("nama_menu",menu);
        hashMap.put("id_menu",idmenu);
        hashMap.put("harga",harga);
        hashMap.put("waktu",waktu);
        database.child(id).child(item).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>(){
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Keranjang.this, "Order Sukses!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
