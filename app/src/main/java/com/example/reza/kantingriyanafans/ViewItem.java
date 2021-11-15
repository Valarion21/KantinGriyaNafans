package com.example.reza.kantingriyanafans;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ViewItem extends AppCompatActivity {
    Hold dbHold;
    TextView tvHarga,tvItem;
    ImageView imItem;
    FirebaseDatabase dtbs = FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);

        imItem = (ImageView)findViewById(R.id.imItem);
        tvItem = (TextView)findViewById(R.id.tvItem);
        tvHarga = (TextView)findViewById(R.id.tvHarga);

        dbHold = new Hold(this);
        final String nm = getIntent().getStringExtra("nama");
        final String hrg = getIntent().getStringExtra("harga");
        final String gbr = getIntent().getStringExtra("gambar");
        tvItem.setText(nm);
        tvHarga.setText("Rp."+hrg);

        int res = getResources().getIdentifier(gbr,"drawable",getPackageName());
        imItem.setImageResource(res);
        Button pesan = (Button)findViewById(R.id.btPesan);
        pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Query count = dtbs.getReference("user").child(getIntent().getStringExtra("Username")).child("isi");
                count.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long val = (long)dataSnapshot.getValue();
                        val = val+1;
                        dtbs.getReference("user").child(getIntent().getStringExtra("Username")).child("isi").setValue(val);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                SQLiteDatabase db = dbHold.getWritableDatabase();
                db.execSQL("insert into pesan values('"+nm+"','"+hrg+"','"+gbr+"')");
                Toast.makeText(getApplicationContext(), "Tambah Keranjang Berhasil", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
