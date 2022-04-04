package com.example.projekta;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {

    private String Password;
    private String Username;
    FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextInputLayout username = (TextInputLayout) findViewById(R.id.username);
        TextInputLayout password = (TextInputLayout) findViewById(R.id.pasword);
        Button login = (Button) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Username = username.getEditText().getText().toString();
                Password = password.getEditText().getText().toString();
                Log.i("Infoo",Username+Password);
                checkDevices();
            }
        });


    }
    private void checkDevices(){
        DatabaseReference databaseakun = database.getInstance().getReference("Device/");
        Query query = databaseakun.orderByChild("devicename").equalTo(Username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0

                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                        // do something with the individual "issues"
                        DataSnapshot devices = dataSnapshot.child(user.getKey());
                        String dbPassword = devices.child("password").getValue(Long.class).toString();
                        String key = devices.getKey();

                        if (Password.equals(dbPassword)) {
                            Preference.setLoggedInStatus(getBaseContext(),true);
                            Preference.setLoggedInUser(getBaseContext(),key);
                            Toast.makeText(LoginActivity.this, "Berhasil Login", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, Pointing.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(LoginActivity.this, "Password Salah", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Tidak Terdaftar", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}