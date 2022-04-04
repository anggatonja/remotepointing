package com.example.projekta;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private String waktuBts = "1";
    private String waktuClient = "1";
    private LatLng LatLngBts;
    private LatLng LatLngClient;
    private LatLng myLatLng;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    Integer x = 1;

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int wBts = Integer.parseInt(waktuBts) + 1;
            waktuBts = String.valueOf(wBts);
            int wClient = Integer.parseInt(waktuClient) + 1;
            waktuClient = String.valueOf(wClient);
            cekDevices();
            cekDevicesbts();
            handler.postDelayed(this, 2000);
        }
    };

    private void cekDevices() {

        DatabaseReference databaseakun = database.getInstance().getReference("Device/" + Preference.getLoggedInUser(getBaseContext()) + "/DetikClient");
        databaseakun.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String detikClient = snapshot.getValue(String.class);
                Log.i("Detik", "Firebase " + detikClient);
                Log.i("Detik", "Detik " + waktuClient);
                if (detikClient.equals(waktuClient)) {
                    statusDevices.setTextColor(Color.GREEN);
                    waktuClient = detikClient;
                } else {
                    statusDevices.setTextColor(Color.LTGRAY);
                    waktuClient = detikClient;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void cekDevicesbts() {

        DatabaseReference databaseakun = database.getInstance().getReference("Device/" + Preference.getLoggedInUser(getBaseContext()) + "/DetikBts");
        databaseakun.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String detikBts = snapshot.getValue(String.class);

                if (detikBts.equals(waktuBts)) {
                    statusDevicess.setTextColor(Color.GREEN);
                    waktuBts = detikBts;
                } else {
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
        setContentView(R.layout.activity_btsnearby);
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Pointing = findViewById(R.id.pointing);
        BTSNearby = findViewById(R.id.btsterdekat);
        Pengaturan = findViewById(R.id.pengaturan);
        statusDevices = findViewById(R.id.status);
        statusDevicess = findViewById(R.id.statusbts);
        handler.postDelayed(runnable, 0);
//        cekDevices();
//        cekDevicesbts();


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
        mMap.getUiSettings().setCompassEnabled(true);

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            Log.i("Lokasi gps", String.valueOf(myLatLng));
                        }
                    }
                });

        DatabaseReference databaseakun = database.getInstance().getReference("Device/" + Preference.getLoggedInUser(getBaseContext()) + "/GPS/Client/");
        databaseakun.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Latitude").exists() && snapshot.child("Longitude").exists()) {
                    LatLngClient = new LatLng(snapshot.child("Latitude").getValue(Double.class), snapshot.child("Longitude").getValue(Double.class));
                    createMarker();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        DatabaseReference databaseakunn = database.getInstance().getReference("Device/" + Preference.getLoggedInUser(getBaseContext()) + "/GPS/BTS/");
        databaseakunn.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Latitude").exists() && snapshot.child("Longitude").exists()) {
                    LatLngBts = new LatLng(snapshot.child("Latitude").getValue(Double.class), snapshot.child("Longitude").getValue(Double.class));
                    createMarker();
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

    private void createMarker() {
        mMap.clear();

        try {
            if (LatLngClient != null) {
                mMap.addMarker(new MarkerOptions()
                        .position(LatLngClient)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        .title("Client"));
            }

            if (LatLngBts != null) {
                mMap.addMarker(new MarkerOptions()
                        .position(LatLngBts)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        .title("BTS"));
            }
            if (LatLngBts != null && LatLngClient != null) {
                if (LatLngBts.latitude > LatLngClient.latitude) {
                    LatLngBounds bounds = new LatLngBounds(LatLngClient, LatLngBts);
                    mMap.addPolyline((new PolylineOptions()).add(LatLngBts, LatLngClient).width(5)
                            .color(Color.RED)
                            .geodesic(true));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));
                } else {
                    LatLngBounds bounds = new LatLngBounds(LatLngBts, LatLngClient);
                    mMap.addPolyline((new PolylineOptions()).add(LatLngBts, LatLngClient).width(5)
                            .color(Color.RED)
                            .geodesic(true));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));
                }

            }
        }catch (Exception e){
            Toast.makeText(this, String.valueOf(e), Toast.LENGTH_SHORT).show();
        }


    }
}