package com.example.projekta;

import android.Manifest;
import android.annotation.SuppressLint;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private TextView tv_altclient;
    private TextView tv_altbts;
    private DatabaseReference mDatabase;
    private String key;
    private String child;
    private Integer derajatH;
    private Integer derajatV;
    private Integer derajatHH;
    private Integer derajatVV;
    private double altclient;
    private double altbts;
    private String waktuBts = "1";
    private String waktuClient = "1";
    private Handler handler = new Handler();
    private CardView cardClient;
    private CardView cardBts;
    private int waktuDevicesBts, waktuDevicesClient;
    private boolean firstStartClient = true;
    private boolean firstStartBts = true;

    Integer x = 1;
    private String childFire = "/Derajat/Client/";
    private Integer derajatVertikal, derajatHorizon;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    boolean isBtsClick = false;

    public void cekDataDerajat(){
        DatabaseReference databaseakun = database.getInstance().getReference("Device/"+Preference.getLoggedInUser(getBaseContext())+"/Derajat/Client");
        databaseakun.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    derajatH = snapshot.child("Horizontal").getValue(Integer.class);
                    derajatV = snapshot.child("Vertical").getValue(Integer.class);
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

    public void cekDataDerajatt(){
        Log.i("cekData1", Preference.getLoggedInUser(getBaseContext()));
        DatabaseReference databaseakunn = database.getInstance().getReference("Device/"+Preference.getLoggedInUser(getBaseContext())+"/Derajat/BTS");
        databaseakunn.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    derajatHH = snapshot.child("Horizontal").getValue(Integer.class);
                    derajatVV = snapshot.child("Vertical").getValue(Integer.class);
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

    private void cekaltitudeclient(){
        Log.i("cekData1", Preference.getLoggedInUser(getBaseContext()));
        DatabaseReference databaseakunn = database.getInstance().getReference("Device/"+Preference.getLoggedInUser(getBaseContext())+"/GPS/Client");
        databaseakunn.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    altclient = snapshot.child("Altitude").getValue(double.class);
                    tv_altclient.setText(String.valueOf(altclient));
                }else{
                    mDatabase.child(child).child("/GPS/Client/Altitude").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void cekaltitudebts(){
        Log.i("cekData1", Preference.getLoggedInUser(getBaseContext()));
        DatabaseReference databaseakunn = database.getInstance().getReference("Device/"+Preference.getLoggedInUser(getBaseContext())+"/GPS/BTS");
        databaseakunn.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    altbts = snapshot.child("Altitude").getValue(double.class);
                    tv_altbts.setText(String.valueOf(altbts));
                }else{
                    mDatabase.child(child).child("/GPS/BTS/Altitude").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private Runnable runnableClient = new Runnable() {
        @Override
        public void run() {


            if (waktuDevicesClient == 0){
                statusDevices.setTextColor(Color.LTGRAY);
            }else{
                waktuDevicesClient = waktuDevicesClient - 1000;
                statusDevices.setTextColor(Color.GREEN);
            }

            handler.postDelayed(this, 1000);
        }
    };

    private Runnable runnableBts = new Runnable() {
        @Override
        public void run() {

            if (waktuDevicesBts == 0){
                statusDevicess.setTextColor(Color.LTGRAY);
            }else{
                waktuDevicesBts = waktuDevicesBts - 1000;
                statusDevicess.setTextColor(Color.GREEN);
            }

            handler.postDelayed(this, 1000);
        }
    };

    private void cekDevices(){

        DatabaseReference databaseakun = database.getInstance().getReference("Device/"+Preference.getLoggedInUser(getBaseContext())+"/DetikClient");
        databaseakun.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String detikClient = snapshot.getValue(String.class);
                    Log.i("Detik","Firebase "+detikClient);
                    Log.i("Detik","Detik "+waktuClient);

                    if (firstStartClient) {
                        statusDevices.setTextColor(Color.LTGRAY);
                        firstStartClient = false;
                        handler.postDelayed(runnableClient, 0);
                    } else {
                        waktuDevicesClient = 10000;
                    }

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
                if (snapshot.exists()){
                    String detikBts = snapshot.getValue(String.class);

                    if (firstStartBts) {
                        statusDevicess.setTextColor(Color.LTGRAY);
                        firstStartBts = false;
                        handler.postDelayed(runnableBts, 0);
                    } else {
                        waktuDevicesBts = 10000;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @SuppressLint("ResourceAsColor")
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
        tv_altclient = findViewById(R.id.altvalue);
        tv_altbts = findViewById(R.id.altvaluebts);
        cardBts = findViewById(R.id.cardBts);
        cardClient = findViewById(R.id.cardClient);
        cekDevices();
        cekDevicesbts();
        cekDataDerajat();
        cekDataDerajatt();
        cekaltitudeclient();
        cekaltitudebts();

//        DatabaseReference databaseakun = database.getInstance().getReference("Device/"+Preference.getLoggedInUser(getBaseContext())+"/Derajat");
//        databaseakun.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    derajatH = snapshot.child("Client").child("Horizontal").getValue(Integer.class);
//                    derajatV = snapshot.child("Client").child("Vertical").getValue(Integer.class);
//                    derajatHH = snapshot.child("BTS").child("Horizontal").getValue(Integer.class);
//                    derajatVV = snapshot.child("BTS").child("Vertical").getValue(Integer.class);
//                    tv_DerajatH.setText(String.valueOf(derajatH));
//                    tv_DerajatV.setText(String.valueOf(derajatV));
//                    tv_DerajatHH.setText(String.valueOf(derajatHH));
//                    tv_DerajatVV.setText(String.valueOf(derajatVV));
//
//                }else {
//                    mDatabase.child(child).child("/Derajat/Client/Horizontal").setValue(0);
//                    mDatabase.child(child).child("/Derajat/Client/Vertical").setValue(0);
//                    mDatabase.child(child).child("/Derajat/BTS/Horizontal").setValue(0);
//                    mDatabase.child(child).child("/Derajat/BTS/Vertical").setValue(0);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

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

        cardClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cekDataDerajat();
                cekDataDerajatt();
                cardClient.setCardBackgroundColor(Color.parseColor("#548CFF"));
                cardBts.setCardBackgroundColor(Color.parseColor("#ACA9A9"));
                childFire = "/Derajat/Client/";
                derajatVertikal = derajatV;
                derajatHorizon = derajatH;
                isBtsClick = false;

            }
        });

        cardBts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cekDataDerajat();
                cekDataDerajatt();
                cardBts.setCardBackgroundColor(Color.parseColor("#548CFF"));
                cardClient.setCardBackgroundColor(Color.parseColor("#ACA9A9"));
                childFire = "/Derajat/BTS/";
                derajatVertikal = derajatVV;
                derajatHorizon = derajatHH;
                isBtsClick = true;
            }
        });

        btnatas.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    btnatas.setImageResource(R.drawable.atasblue2);
                    if (isBtsClick){
                        derajatVertikal  = derajatVV;
                    }else{
                        derajatVertikal  = derajatV;
                    }
                    if (derajatVertikal < 180){
                        mDatabase.child(child).child(childFire+"Vertical").setValue(derajatVertikal+10);
                        cekDataDerajat();
                        cekDataDerajatt();
                    }
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    btnatas.setImageResource(R.drawable.atasblue);
                }
                return true;
            }
        });

        btnbawah.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    btnbawah.setImageResource(R.drawable.bawahblue2);
                    if (isBtsClick){
                        derajatVertikal  = derajatVV;
                    }else{
                        derajatVertikal  = derajatV;
                    }
                    if (derajatVertikal > 0){
                        mDatabase.child(child).child(childFire+"Vertical").setValue(derajatVertikal-10);
                        cekDataDerajat();
                        cekDataDerajatt();
                    }
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    btnbawah.setImageResource(R.drawable.bawahblue);
                }
                return true;
            }
        });

        btnkanan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    btnkanan.setImageResource(R.drawable.kananblue2);
                    if (isBtsClick){
                        derajatHorizon  = derajatHH;
                    }else{
                        derajatHorizon  = derajatH;
                    }
                    if (derajatHorizon > 0){
                        mDatabase.child(child).child(childFire+"Horizontal").setValue(derajatHorizon-10);
                        cekDataDerajat();
                        cekDataDerajatt();
                    }
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    btnkanan.setImageResource(R.drawable.kananblue);
                }
                return true;
            }
        });

        btnkiri.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    btnkiri.setImageResource(R.drawable.kiriblue2);
                    if (isBtsClick){
                        derajatHorizon  = derajatHH;
                    }else{
                        derajatHorizon  = derajatH;
                    }
                    if (derajatHorizon < 180){
                        mDatabase.child(child).child(childFire+"Horizontal").setValue(derajatHorizon+10);
                        cekDataDerajat();
                        cekDataDerajatt();
                    }
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    btnkiri.setImageResource(R.drawable.kiriblue);
                }
                return true;
            }
        });

        btnreset.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    btnreset.setImageResource(R.drawable.resetblue2);
                    mDatabase.child(child).child(childFire+"Horizontal").setValue(0);
                    mDatabase.child(child).child(childFire+"Vertical").setValue(0);
                    cekDataDerajat();
                    cekDataDerajatt();
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    btnreset.setImageResource(R.drawable.resetblue);
                }
                return true;
            }
        });

    }

}