package com.example.reza.kantingriyanafans;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Akun extends AppCompatActivity {
    TextView Vuser , Vemail, Vhp ,Vkamar;
    Button Aknreset,AknHelp;
    ImageView btKr,btHome;
    final Context context = this;
    FirebaseDatabase dtbs = FirebaseDatabase.getInstance();
    DatabaseReference database = dtbs.getReference("user");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akun);

        countKer();

        btKr = (ImageView) findViewById(R.id.keranjang);
        btHome = (ImageView)findViewById(R.id.home);
        Aknreset = (Button) findViewById(R.id.Aknreset);
        AknHelp = (Button)findViewById(R.id.AknHelp);

        final String akun = getIntent().getStringExtra("Username");
        AkunInfo(akun);

        btKr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query cekid = dtbs.getReference("count").child("countOrder");
                cekid.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Intent keranjang = new Intent(Akun.this,Keranjang.class);
                        String username = getIntent().getStringExtra("Username");
                        keranjang.putExtra("Username",username);
                        keranjang.putExtra("idnow",dataSnapshot.getValue(String.class));
                        startActivity(keranjang);
                        finish();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = getIntent().getStringExtra("Username");
                Intent home = new Intent(Akun.this,MainActivity.class);
                home.putExtra("Username",username);
                home.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(home, 0);
                finish();
            }
        });

        AknHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hlp = new Intent(Akun.this,Help2.class);
                startActivity(hlp);
            }
        });

        Aknreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.ubahpassword, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptsView);
                final EditText oldPass = (EditText) promptsView.findViewById(R.id.oldpass);
                final EditText newPass = (EditText) promptsView.findViewById(R.id.newpass);
                final EditText confPass = (EditText) promptsView.findViewById(R.id.confpass);
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String lama = oldPass.getText().toString();
                                String baru = newPass.getText().toString();
                                String conf = confPass.getText().toString();
                                if (baru.length()>=6){
                                    if(conf.equals(baru)){
                                        UbahPassword(akun,lama,baru);
                                    }else{
                                        Toast.makeText(Akun.this, "Konfirmasi Kata Sandi Tidak Sesuai", Toast.LENGTH_LONG).show();
                                    }
                                }else{
                                    dialog.cancel();
                                    Toast.makeText(Akun.this, "Kata Sandi Baru Minimal 6 Karakter", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    public void UbahPassword(final String akun, final String lama , final String baru){
        Query chnge = database.child(akun).child("password");
        chnge.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String plama = dataSnapshot.getValue(String.class);
                if(plama.equals(lama)){
                    database.child(akun).child("password").setValue(baru);
                    Toast.makeText(Akun.this, "Kata Sandi Berhasil Diganti", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(Akun.this, "Kata Sandi Tidak Sesuai", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void AkunInfo(String user){
        Vuser = (TextView)findViewById(R.id.Aknnama);
        Vemail = (TextView)findViewById(R.id.Aknemail);
        Vhp = (TextView)findViewById(R.id.Aknhp);
        Vkamar = (TextView)findViewById(R.id.Aknkmr);
        Vuser.setText(user);
        Query email = database.child(user).child("email");
        Query hp = database.child(user).child("no_hp");
        Query kmr = database.child(user).child("no_kmr");
        email.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Vemail.setText(dataSnapshot.getValue(String.class));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        hp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Vhp.setText(dataSnapshot.getValue(String.class));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        kmr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Vkamar.setText(dataSnapshot.getValue(String.class));
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
}
