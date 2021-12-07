package com.example.projekta;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BTSNearby extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private Double latitude;
    private Double longitude;
    private TextView statusDevices;
    private TextView statusDevicess;
    private TextView Pointing;
    private TextView BTSNearby;
    private TextView Pengaturan;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    Integer x = 1;

    private void cekDevices(){
        DatabaseReference databaseakun = database.getInstance().getReference("Device/"+Preference.getLoggedInUser(getBaseContext()));;
        databaseakun.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String detikBts = snapshot.child("DetikBts").getValue(String.class);
                String detikClient = snapshot.child("DetikClient").getValue(String.class);
                String currentTime = new SimpleDateFormat("ss", Locale.getDefault()).format(new Date());

                if (detikBts.equals(currentTime)){
                    statusDevices.setText("ON");
                    statusDevices.setTextColor(Color.GREEN);
                }
                if (detikClient.equals(currentTime)){
                    statusDevicess.setText("ON");
                    statusDevicess.setTextColor(Color.GREEN);
                }
                if(x == 1){
                    statusDevices.setText("OFF");
                    statusDevices.setTextColor(Color.RED);
                    statusDevicess.setText("OFF");
                    statusDevicess.setTextColor(Color.RED);
                    x=0;
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
        setContentView(R.layout.activity_btsnearby);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Pointing = findViewById(R.id.pointing);
        BTSNearby = findViewById(R.id.btsterdekat);
        Pengaturan = findViewById(R.id.pengaturan);
        statusDevices = findViewById(R.id.status_device);
        statusDevicess = findViewById(R.id.status_devicee);
        cekDevices();


        Pointing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BTSNearby.this, com.example.projekta.Pointing.class);
                BTSNearby.this.startActivity(intent);
                finish();
            }
        });
        Pengaturan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BTSNearby.this, Pengaturan.class);
                BTSNearby.this.startActivity(intent);
                finish();
            }
        });

        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        Toast.makeText(this, getMacAddr(), Toast.LENGTH_LONG).show();

    }


    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mMap.setMyLocationEnabled(true);
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.

        for(int x = 0; x <=1; x++){
            String ctgr;
            if (x == 0){
                ctgr = "BTS";
            }else {
                ctgr = "Client";
            }
            DatabaseReference databaseakun = database.getInstance().getReference("Device/");
            Query query = databaseakun.orderByChild("Cgr").equalTo(ctgr);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // snapshot is the "issue" node with all children with id 0

                        for (DataSnapshot user : snapshot.getChildren()) {
                            // do something with the individual "issues"
                            DataSnapshot devices = snapshot.child(user.getKey());
                            LatLng location = new LatLng(devices.child("Latitude").getValue(Double.class),devices.child("Longitude").getValue(Double.class));
                            Log.i("LatLongmaps",String.valueOf(location));
                            if(ctgr == "BTS"){
                                googleMap.addMarker(new MarkerOptions()
                                        .position(location)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                                        .title("BTS"));
                            }else if(ctgr == "Client"){
                                googleMap.addMarker(new MarkerOptions()
                                        .position(location)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                        .title("Client"));
                            }

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
        }
        DatabaseReference databaseakun = database.getInstance().getReference("Device/");
        Query query = databaseakun.orderByChild("Cgr").equalTo("BTS");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // snapshot is the "issue" node with all children with id 0

                    for (DataSnapshot user : snapshot.getChildren()) {
                        // do something with the individual "issues"
                        DataSnapshot devices = snapshot.child(user.getKey());
                        Double X = devices.child("Longitude").getValue(Double.class);
                        LatLng location = new LatLng(devices.child("Latitude").getValue(Double.class),devices.child("Longitude").getValue(Double.class));
                        Log.i("LatLongmaps",String.valueOf(location));
                        Log.i("LatLongmaps","disini");
                        googleMap.addMarker(new MarkerOptions()
                                .position(location)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                                .title("BTS"));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
//                            Toast.makeText(BTSNearby.this,String.valueOf(latitude)+String.valueOf(longitude), Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }
}