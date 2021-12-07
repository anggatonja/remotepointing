package com.example.projekta;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Pengaturan extends AppCompatActivity {
    private TextView Pointing;
    private TextView BTSNearby;
    private TextView Pengaturan;
    private TextView statusDevices;
    private TextView statusDevicess;
    private DatabaseReference mDatabase;
    private String key;

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    Integer x = 1;

    private void cekDevices(){
        DatabaseReference databaseakun = database.getInstance().getReference("Device/"+Preference.getLoggedInUser(getBaseContext()));
        databaseakun.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String detik = snapshot.child("DetikBts").getValue(String.class);
                Log.i("detik",detik);
                statusDevices.setText("ON");
                statusDevices.setTextColor(Color.GREEN);
                if(x == 1){
                    statusDevices.setText("OFF");
                    statusDevices.setTextColor(Color.RED);
                    x=0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void resetJaringan(){
        dialog = new AlertDialog.Builder(Pengaturan.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_reset,null);
        dialog.setView(dialogView);
        dialog.setCancelable(false);
        dialog.setTitle("Reset SSID");

        dialog.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabase.child(key).child("reset").setValue("1");
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
        dialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan);
        Pointing = findViewById(R.id.pointing);
        BTSNearby = findViewById(R.id.btsterdekat);
        Pengaturan = findViewById(R.id.pengaturan);
        Button logout = (Button) findViewById(R.id.buttonlogout);
        Button pengaturanwifi = (Button) findViewById(R.id.buttonpengaturan);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        key = "Device/"+Preference.getLoggedInUser(getBaseContext());
        statusDevices = findViewById(R.id.status_device);
        statusDevicess = findViewById(R.id.status_devicee);
        TextView namadevices = (TextView) findViewById(R.id.status_device);
        TextView namadevicess = (TextView) findViewById(R.id.status_devicee);
        namadevices.setText(Preference.getLoggedInUser(getBaseContext()));
        cekDevices();



        pengaturanwifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetJaringan();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Preference.setLoggedInStatus(getBaseContext(),false);
                Preference.clearLoggedInUser(getBaseContext());
                Intent intent = new Intent(Pengaturan.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        Pointing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Pengaturan.this, com.example.projekta.Pointing.class);
                Pengaturan.this.startActivity(intent);
            }
        });

        BTSNearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Pengaturan.this, BTSNearby.class);
                Pengaturan.this.startActivity(intent);
            }
        });
    }

}