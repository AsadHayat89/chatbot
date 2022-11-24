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

import com.example.fypchatbotapp.model.Event_Booking_Details;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventBookingDetails extends AppCompatActivity {
    AppCompatButton btnUpload;
    EditText event_adv_booking, event_max_people, balloons, stage_decor, extra_decor, hall_area, hall_price, song_price;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_booking_details);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
        Toolbar toolbar = findViewById(R.id.toolbarEBDetails);
        toolbar.setTitle("Event Booking Details");
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
        event_adv_booking = findViewById(R.id.event_advance);
        event_max_people = findViewById(R.id.event_max);
        balloons = findViewById(R.id.balloons_decor_price);
        stage_decor = findViewById(R.id.stage_decor_price);
        extra_decor = findViewById(R.id.extra_decor_price);
        hall_area = findViewById(R.id.hall_area);
        hall_price = findViewById(R.id.hall_price);
        song_price = findViewById(R.id.songs_rate);
        btnUpload = findViewById(R.id.event_det);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Restaurant_Details");

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event_Booking_Details e = new Event_Booking_Details();
                e.setAdv_booking_time(event_adv_booking.getText().toString());
                e.setMax_people(event_max_people.getText().toString());
                e.setDecor_rate_balloons(balloons.getText().toString());
                e.setStage_decor_price(stage_decor.getText().toString());
                e.setExtra_decor(extra_decor.getText().toString());
                e.setHall_area(hall_area.getText().toString());
                e.setHall_price_per_hour(hall_price.getText().toString());
                e.setSong_rate_per_hour(song_price.getText().toString());
                databaseReference.child("Event_Booking_Details").setValue(e);
                Toast.makeText(EventBookingDetails.this, "uploaded", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EventBookingDetails.this, CateringDetails.class);
                startActivity(intent);
            }
        });
        databaseReference.child("Event_Booking_Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    Event_Booking_Details event_booking_details = snapshot.getValue(Event_Booking_Details.class);
                    event_adv_booking.setText(event_booking_details.getAdv_booking_time());
                    event_max_people.setText(event_booking_details.getMax_people());
                    balloons.setText(event_booking_details.getDecor_rate_balloons());
                    extra_decor.setText(event_booking_details.getExtra_decor());
                    hall_area.setText(event_booking_details.getHall_area());
                    hall_price.setText(event_booking_details.getHall_price_per_hour());
                    song_price.setText(event_booking_details.getSong_rate_per_hour());
                    stage_decor.setText(event_booking_details.getStage_decor_price());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}