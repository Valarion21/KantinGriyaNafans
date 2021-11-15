package com.example.reza.kantingriyanafans;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    EditText Username, Password;
    TextView btHelp;
    Button LogIn, SignIn;
    String PasswordHolder, UsernameHolder;
    Boolean CheckEditText;
    FirebaseDatabase dtbs = FirebaseDatabase.getInstance();
    DatabaseReference database = dtbs.getReference("user");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Username = (EditText) findViewById(R.id.uname);
        Password = (EditText) findViewById(R.id.pass);
        LogIn = (Button) findViewById(R.id.Tlogin);
        SignIn = (Button) findViewById(R.id.Tdaftar);
        btHelp = (TextView)findViewById(R.id.btHelp);

        btHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hlp = new Intent(Login.this,Help1.class);
                startActivity(hlp);
            }
        });

        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEditTextIsEmptyOrNot();
                if (CheckEditText) {
                    cekLogin();
                } else {
                    Toast.makeText(Login.this, "Kolom Belum Terisi", Toast.LENGTH_LONG).show();
                }
            }
        });

        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent daftr = new Intent(Login.this, Daftar.class);
                startActivity(daftr);
            }
        });
    }

    public void CheckEditTextIsEmptyOrNot() {
        if (TextUtils.isEmpty(Username.getText().toString()) || TextUtils.isEmpty(Password.getText().toString())) {
            CheckEditText = false;
        } else {
            CheckEditText = true;
        }
    }

    public void cekLogin() {
        UsernameHolder = Username.getText().toString();
        PasswordHolder = Password.getText().toString();
        Query query = database.child(UsernameHolder).child("password");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String pass = dataSnapshot.getValue(String.class);
                    if(pass.equals(PasswordHolder)){
                        if(UsernameHolder.equals("admin")){
                            Intent intent = new Intent(Login.this, Admin.class);
                            intent.putExtra("Username", UsernameHolder);
                            startActivity(intent);
                            finish();
                        }else {
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            intent.putExtra("Username", UsernameHolder);
                            startActivity(intent);
                            finish();
                        }
                    }else{
                        Toast.makeText(Login.this, "Password Salah!", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(Login.this, "Username Tidak Terdaftar!", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
}
