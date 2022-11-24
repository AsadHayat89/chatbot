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

import com.example.fypchatbotapp.model.Table_Booking_Details;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TableBookingDetails extends AppCompatActivity {
    AppCompatButton btnUpload;
    EditText table_adv_booking, table_max;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_booking_details);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
        Toolbar toolbar = findViewById(R.id.toolbarTBDetails);
        toolbar.setTitle("Table Booking Details");
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
        table_adv_booking = findViewById(R.id.table_advance);
        table_max = findViewById(R.id.table_max);
        btnUpload = findViewById(R.id.table_det);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Restaurant_Details");

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Table_Booking_Details t = new Table_Booking_Details();
                t.setAdv_booking_time(table_adv_booking.getText().toString());
                t.setMax_people(table_max.getText().toString());
                databaseReference.child("Table_Booking_Details").setValue(t);
                Toast.makeText(TableBookingDetails.this, "uploaded", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TableBookingDetails.this, EventBookingDetails.class);
                startActivity(intent);
            }
        });
        databaseReference.child("Table_Booking_Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Table_Booking_Details table_booking_details = snapshot.getValue(Table_Booking_Details.class);
                    table_adv_booking.setText(table_booking_details.getAdv_booking_time());
                    table_max.setText(table_booking_details.getMax_people());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}