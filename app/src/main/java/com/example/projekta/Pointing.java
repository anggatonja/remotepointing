package com.example.projekta;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Pointing extends AppCompatActivity {
    private TextView Pointing;
    private TextView BTSNearby;
    private TextView Pengaturan;
    private TextView statusDevices;
    private TextView statusDevicess;
    private TextView tv_DerajatH;
    private TextView tv_DerajatV;
    private TextView tv_DerajatHH;
    private TextView tv_DerajatVV;
    private DatabaseReference mDatabase;
    private String key;
    private String child;
    private int derajatH;
    private int derajatV;
    private int derajatHH;
    private int derajatVV;
    private String waktuBts = "1";
    private String waktuClient = "1";
    private Handler handler = new Handler();

    Integer x = 1;
    FirebaseDatabase database = FirebaseDatabase.getInstance();


    private void cekDataDerajat(){
        DatabaseReference databaseakun = database.getInstance().getReference("Device/"+Preference.getLoggedInUser(getBaseContext())+"/Derajat/Client");
        databaseakun.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    derajatH = snapshot.child("Horizontal").getValue(int.class);
                    derajatV = snapshot.child("Vertical").getValue(int.class);
                    tv_DerajatH.setText(String.valueOf(derajatH));
                    tv_DerajatV.setText(String.valueOf(derajatV));

                }else {
                    mDatabase.child(child).child("/Derajat/Client/Horizontal").setValue(0);
                    mDatabase.child(child).child("/Derajat/Client/Vertical").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void cekDataDerajatt(){
        Log.i("cekData1", Preference.getLoggedInUser(getBaseContext()));
        DatabaseReference databaseakunn = database.getInstance().getReference("Device/"+Preference.getLoggedInUser(getBaseContext())+"/Derajat/BTS");
        databaseakunn.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    derajatHH = snapshot.child("Horizontal").getValue(int.class);
                    derajatVV = snapshot.child("Vertical").getValue(int.class);
                    tv_DerajatHH.setText(String.valueOf(derajatHH));
                    tv_DerajatVV.setText(String.valueOf(derajatVV));

                }else {
                    mDatabase.child(child).child("/Derajat/BTS/Horizontal").setValue(0);
                    mDatabase.child(child).child("/Derajat/BTS/Vertical").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int wBts = Integer.parseInt(waktuBts)+1;
            waktuBts = String.valueOf(wBts);
            int wClient = Integer.parseInt(waktuClient)+1;
            waktuClient = String.valueOf(wClient);
            cekDevices();
            cekDevicesbts();
            handler.postDelayed(this, 2000);
        }
    };

    private void cekDevices(){

        DatabaseReference databaseakun = database.getInstance().getReference("Device/"+Preference.getLoggedInUser(getBaseContext())+"/DetikClient");
        databaseakun.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String detikClient = snapshot.getValue(String.class);
                Log.i("Detik","Firebase "+detikClient);
                Log.i("Detik","Detik "+waktuClient);
                if (detikClient.equals(waktuClient)){
                    statusDevices.setTextColor(Color.GREEN);
                    waktuClient = detikClient;
                }else{
                    statusDevices.setTextColor(Color.LTGRAY);
                    waktuClient = detikClient;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void cekDevicesbts(){

        DatabaseReference databaseakun = database.getInstance().getReference("Device/"+Preference.getLoggedInUser(getBaseContext())+"/DetikBts");
        databaseakun.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String detikBts = snapshot.getValue(String.class);

                if (detikBts.equals(waktuBts)){
                    statusDevicess.setTextColor(Color.GREEN);
                    waktuBts = detikBts;
                }
                else{
                    statusDevicess.setTextColor(Color.LTGRAY);
                    waktuBts = detikBts;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pointing);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }else{
            // Write you code here if permission already given.
        }

        key = "Device/"+Preference.getLoggedInUser(getBaseContext())+"/Tombol";
        child = "Device/"+Preference.getLoggedInUser(getBaseContext());

        ImageView btnatas = (ImageView) findViewById(R.id.button_atas);
        ImageView btnbawah = (ImageView) findViewById(R.id.button_bawah);
        ImageView btnkiri = (ImageView) findViewById(R.id.button_kiri);
        ImageView btnkanan = (ImageView) findViewById(R.id.button_kanan);
        ImageView btnreset = (ImageView) findViewById(R.id.button_reset);
        ImageView btnatass = (ImageView) findViewById(R.id.button_atass);
        ImageView btnbawahh = (ImageView) findViewById(R.id.button_bawahh);
        ImageView btnkirii = (ImageView) findViewById(R.id.button_kirii);
        ImageView btnkanann = (ImageView) findViewById(R.id.button_kanann);
        ImageView btnresett = (ImageView) findViewById(R.id.button_resett);
        Pointing = findViewById(R.id.pointing);
        BTSNearby = findViewById(R.id.btsterdekat);
        Pengaturan = findViewById(R.id.pengaturan);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        statusDevices = findViewById(R.id.status);
        statusDevicess = findViewById(R.id.statusbts);
        tv_DerajatH = findViewById(R.id.nilaiderajath);
        tv_DerajatV = findViewById(R.id.nilaiderajatv);
        tv_DerajatHH = findViewById(R.id.nilaiderajath1);
        tv_DerajatVV = findViewById(R.id.nilaiderajatv1);
        handler.postDelayed(runnable, 0);
//        cekDevices();
//        cekDevicesbts();
        cekDataDerajat();
        cekDataDerajatt();
        Log.i("derajath", String.valueOf(derajatH));
        Log.i("derajatv", String.valueOf(derajatV));
        Log.i("derajathh", String.valueOf(derajatHH));
        Log.i("derajatvv", String.valueOf(derajatVV));

        BTSNearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( Pointing.this, BTSNearby.class);
                Pointing.this.startActivity(intent);
                finish();

            }
        });
        Pengaturan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( Pointing.this, Pengaturan.class);
                Pointing.this.startActivity(intent);
                finish();
            }
        });

        btnatas.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    btnatas.setImageResource(R.drawable.atas2);
                    if (derajatV < 180){
                        mDatabase.child(child).child("/Derajat/Client/Vertical").setValue(derajatV+10);
                        cekDataDerajat();
                    }
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    btnatas.setImageResource(R.drawable.atas);
                }
                return true;
            }
        });

        btnbawah.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    btnbawah.setImageResource(R.drawable.bawah2);
                    if (derajatV > 0){
                        mDatabase.child(child).child("/Derajat/Client/Vertical").setValue(derajatV-10);
                        cekDataDerajat();
                    }
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    btnbawah.setImageResource(R.drawable.bawah);
                }
                return true;
            }
        });

        btnkanan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    btnkanan.setImageResource(R.drawable.kanan2);
                    if (derajatH > 0){
                        mDatabase.child(child).child("/Derajat/Client/Horizontal").setValue(derajatH-10);
                        cekDataDerajat();
                    }
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    btnkanan.setImageResource(R.drawable.kanan);
                }
                return true;
            }
        });

        btnkiri.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    btnkiri.setImageResource(R.drawable.kiri2);
                    if (derajatH < 180){
                        mDatabase.child(child).child("/Derajat/Client/Horizontal").setValue(derajatH+10);
                        cekDataDerajat();
                    }
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    btnkiri.setImageResource(R.drawable.kiri);
                }
                return true;
            }
        });

        btnreset.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    btnreset.setImageResource(R.drawable.reset2);
                    mDatabase.child(child).child("/Derajat/Client/Horizontal").setValue(0);
                    mDatabase.child(child).child("/Derajat/Client/Vertical").setValue(0);
                    cekDataDerajat();
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    btnreset.setImageResource(R.drawable.reset);
                }
                return true;
            }
        });

        btnatass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    btnatass.setImageResource(R.drawable.atas2);
                    if (derajatVV < 180){
                        mDatabase.child(child).child("/Derajat/BTS/Vertical").setValue(derajatVV+10);
                        cekDataDerajatt();
                    }
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    btnatass.setImageResource(R.drawable.atas);
                }
                return true;
            }
        });

        btnbawahh.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    btnbawahh.setImageResource(R.drawable.bawah2);
                    if (derajatVV > 0){
                        mDatabase.child(child).child("/Derajat/BTS/Vertical").setValue(derajatVV-10);
                        cekDataDerajatt();
                    }
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    btnbawahh.setImageResource(R.drawable.bawah);
                }
                return true;
            }
        });

        btnkanann.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    btnkanann.setImageResource(R.drawable.kanan2);
                    if (derajatHH > 0){
                        mDatabase.child(child).child("/Derajat/BTS/Horizontal").setValue(derajatHH-10);
                        cekDataDerajatt();
                    }
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    btnkanann.setImageResource(R.drawable.kanan);
                }
                return true;
            }
        });

        btnkirii.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    btnkirii.setImageResource(R.drawable.kiri2);
                    if (derajatHH < 180){
                        mDatabase.child(child).child("/Derajat/BTS/Horizontal").setValue(derajatHH+10);
                        cekDataDerajatt();
                    }
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    btnkirii.setImageResource(R.drawable.kiri);
                }
                return true;
            }
        });

        btnresett.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    btnresett.setImageResource(R.drawable.reset2);
                    mDatabase.child(child).child("/Derajat/BTS/Horizontal").setValue(0);
                    mDatabase.child(child).child("/Derajat/BTS/Vertical").setValue(0);
                    cekDataDerajatt();
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    btnresett.setImageResource(R.drawable.reset);
                }
                return true;
            }
        });

    }

}