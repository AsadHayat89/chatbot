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

import com.example.fypchatbotapp.model.GeneralDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RestaurantDetailsActivity extends AppCompatActivity {
    AppCompatButton btnUpload;
    EditText restr_name, restr_address, restr_opening, restr_closing, restr_sitting_area, restr_tables, food_delivery, table_booking, event_booking, catering;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
        Toolbar toolbar = findViewById(R.id.toolbarRestDetails);
        toolbar.setTitle("Restaurant Details");
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
        btnUpload = findViewById(R.id.gen_det);
        restr_name = findViewById(R.id.res_name);
        restr_address = findViewById(R.id.res_address);
        restr_opening = findViewById(R.id.res_open);
        restr_closing = findViewById(R.id.res_close);
        restr_sitting_area = findViewById(R.id.res_sitting_area);
        restr_tables = findViewById(R.id.res_tables);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Restaurant_Details");

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralDetails g = new GeneralDetails();
                g.setRestr_name(restr_name.getText().toString());
                g.setRestr_address(restr_address.getText().toString());
                g.setRestr_opening_time(restr_opening.getText().toString());
                g.setRestr_closing_time(restr_closing.getText().toString());
                g.setRestr_sitting_area(restr_sitting_area.getText().toString());
                g.setTables(restr_tables.getText().toString());

                databaseReference.child("General_Details").setValue(g);
                Toast.makeText(RestaurantDetailsActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(RestaurantDetailsActivity.this, FoodDeliveryDetails.class);
                startActivity(intent);
            }
        });

        databaseReference.child("General_Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    GeneralDetails generalDetails = snapshot.getValue(GeneralDetails.class);
                    restr_name.setText(generalDetails.getRestr_name());
                    restr_address.setText(generalDetails.getRestr_address());
                    restr_opening.setText(generalDetails.getRestr_opening_time());
                    restr_closing.setText(generalDetails.getRestr_closing_time());
                    restr_sitting_area.setText(generalDetails.getRestr_sitting_area());
                    restr_tables.setText(generalDetails.getTables());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}