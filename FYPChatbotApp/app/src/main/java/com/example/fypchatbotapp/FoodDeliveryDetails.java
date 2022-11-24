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

import com.example.fypchatbotapp.model.Food_Delivery_Details;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FoodDeliveryDetails extends AppCompatActivity {
    AppCompatButton btnUpload;
    EditText del_area, del_price, del_time_max, del_time_min;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_delivery_details);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
        Toolbar toolbar = findViewById(R.id.toolbarFDDetails);
        toolbar.setTitle("Food Delivery Details");
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
        del_area = findViewById(R.id.del_area);
        del_price = findViewById(R.id.del_price);
        del_time_max = findViewById(R.id.del_time_max);
        del_time_min = findViewById(R.id.del_time_min);
        btnUpload = findViewById(R.id.food_del_det);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Restaurant_Details");

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Food_Delivery_Details f = new Food_Delivery_Details();
                f.setDelivery_area_limit(del_area.getText().toString());
                f.setDelivery_price(del_price.getText().toString());
                f.setDelivery_time_max(del_time_max.getText().toString());
                f.setDelivery_time_min(del_time_min.getText().toString());
                databaseReference.child("Food_Delivery_Details").setValue(f);
                Toast.makeText(FoodDeliveryDetails.this, "uploaded", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FoodDeliveryDetails.this, TableBookingDetails.class);
                startActivity(intent);
            }
        });
        databaseReference.child("Food_Delivery_Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    Food_Delivery_Details food_delivery_details = snapshot.getValue(Food_Delivery_Details.class);
                    del_area.setText(food_delivery_details.getDelivery_area_limit());
                    del_price.setText(food_delivery_details.getDelivery_price());
                    del_time_max.setText(food_delivery_details.getDelivery_time_max());
                    del_time_min.setText(food_delivery_details.getDelivery_time_min());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}