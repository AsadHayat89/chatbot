package com.example.fypchatbotapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fypchatbotapp.model.Catering_Details;
import com.example.fypchatbotapp.model.Event_Booking_Details;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CateringDetails extends AppCompatActivity {
    AppCompatButton btnUpload;
    EditText adv_booking, del_charges;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catering_details);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
        Toolbar toolbar = findViewById(R.id.toolbarCatDetails);
        toolbar.setTitle("Catering Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(getApplicationContext(),AdminMenuActivity.class);
                startActivity(backIntent);
            }
        });

        adv_booking = findViewById(R.id.cat_advance);
        del_charges = findViewById(R.id.cat_del_price);
        btnUpload = findViewById(R.id.cat_det);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Restaurant_Details");

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Catering_Details c = new Catering_Details();
                c.setAdv_booking_time(adv_booking.getText().toString());
                c.setDelivery_charges(del_charges.getText().toString());
                databaseReference.child("Catering_Details").setValue(c);
                Toast.makeText(CateringDetails.this, "uploaded", Toast.LENGTH_SHORT).show();
            }
        });
        databaseReference.child("Catering_Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    Catering_Details catering_details = snapshot.getValue(Catering_Details.class);
                    adv_booking.setText(catering_details.getAdv_booking_time());
                    del_charges.setText(catering_details.getDelivery_charges());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}