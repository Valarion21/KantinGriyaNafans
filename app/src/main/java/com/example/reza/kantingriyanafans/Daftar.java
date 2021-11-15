package com.example.reza.kantingriyanafans;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Daftar extends AppCompatActivity {
    Button Daftar ;
    EditText Email, Username , Password , NoHp , NoKmr;
    String UsernameHolder , EmailHolder, PasswordHolder , HpHolder , KamarHolder;
    Boolean CheckEditText,CekMin,CekUsrName;
    FirebaseDatabase dtbs = FirebaseDatabase.getInstance();
    DatabaseReference database = dtbs.getReference("user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        Email = (EditText) findViewById(R.id.email);
        Username = (EditText) findViewById(R.id.username);
        Password = (EditText) findViewById(R.id.password);
        NoHp = (EditText) findViewById(R.id.nohp);
        NoKmr = (EditText) findViewById(R.id.nokm);
        Daftar = (Button) findViewById(R.id.daftar1);

        Daftar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                CheckEditTextIsEmptyOrNot();
                CekMin();
                if (CheckEditText && CekMin){
                    inputDaftar();
                    Intent login = new Intent(Daftar.this, Login.class);
                    startActivity(login);
                    finish();
                }
            }
        });
    }
    public void CekMin(){
        Boolean PassMin,HpMin;
        if(Password.getText().toString().length()>=6){
            PassMin = true;
        }else{
            PassMin = false;
            Toast.makeText(Daftar.this, "Kata Sandi minimal 6 karakter", Toast.LENGTH_SHORT).show();
        }

        if(NoHp.getText().toString().length()>=11){
            HpMin = true;
        }else{
            HpMin = false;
            Toast.makeText(Daftar.this, "No Handphone minimal 11 karakter", Toast.LENGTH_SHORT).show();
        }
        if(PassMin && HpMin){
            CekMin = true;
        }else{
            CekMin = false;
        }
    }

    public void CheckEditTextIsEmptyOrNot(){
        UsernameHolder = Username.getText().toString();
        EmailHolder = Email.getText().toString();
        PasswordHolder = Password.getText().toString();
        HpHolder = NoHp.getText().toString();
        KamarHolder = NoKmr.getText().toString();
        if (TextUtils.isEmpty(UsernameHolder) || TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder) || TextUtils.isEmpty(HpHolder) || TextUtils.isEmpty(KamarHolder))
        {
            CheckEditText = false;
            Toast.makeText(Daftar.this, "Kolom Belum Terisi", Toast.LENGTH_LONG).show();
        }
        else{
            if(UsernameHolder.indexOf(" ")>-1){
                Toast.makeText(Daftar.this, "Nama pengguna tidak boleh mengandung spasi", Toast.LENGTH_LONG).show();
                CheckEditText=false;
            }else{
                CheckEditText=true;
            }
        }
    }

    public void inputDaftar(){
        Query query = database.orderByKey().equalTo(UsernameHolder);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Toast.makeText(Daftar.this, "Nama Pengguna Sudah Ada!", Toast.LENGTH_LONG).show();
                } else {
                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("email",EmailHolder);
                    hashMap.put("no_hp",HpHolder);
                    hashMap.put("no_kmr",KamarHolder);
                    hashMap.put("password",PasswordHolder);
                    database.child(UsernameHolder).setValue(hashMap);
                    Toast.makeText(Daftar.this, "Daftar Berhasil!", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Daftar.this, "Database Offline", Toast.LENGTH_LONG).show();
            }
        });
    }
}
