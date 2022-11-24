package com.example.fypchatbotapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.fypchatbotapp.common.Common;
import com.example.fypchatbotapp.model.Catering;
import com.example.fypchatbotapp.model.EventBooking;
import com.example.fypchatbotapp.model.EventCatering;
import com.example.fypchatbotapp.viewholder.EventBookingViewHolder;
import com.example.fypchatbotapp.viewholder.EventCateringFoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FoodCateringOrders extends AppCompatActivity {
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    TextView txt_total_bill, delivery_charges;

    FirebaseRecyclerAdapter<EventCatering, EventCateringFoodViewHolder> adapter;
    FirebaseRecyclerOptions<EventCatering> options;

    String orderId = "";
    String user = "";

//    FirebaseRecyclerAdapter<FoodOrder, FoodOrderViewHolder> foodAdapter;
//    FirebaseRecyclerOptions<FoodOrder> foodOptions;

    FirebaseDatabase database;
    DatabaseReference orders, foodOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_catering_orders);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
        Toolbar toolbar = findViewById(R.id.toolbarCateringFood);
        toolbar.setTitle("Catering Price Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (getIntent()!=null)
        {
            orderId = getIntent().getStringExtra("orderId");
            user = getIntent().getStringExtra("user");
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent;
                if (user.equals("user"))
                {
                    backIntent = new Intent(getApplicationContext(), EventBookingOrders.class);
                }
                else
                {
                    backIntent = new Intent(getApplicationContext(), AdminEventBookingOrders.class);
                }
                startActivity(backIntent);
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(backIntent);
            }
        });
        database = FirebaseDatabase.getInstance();
        orders = database.getReference("Catering_Orders");
        foodOrders = orders.child(orderId).child("food_items");

        delivery_charges = findViewById(R.id.catering_order_delivery);
        txt_total_bill = findViewById(R.id.catering_order_total_price);
        recyclerView = findViewById(R.id.list_catering_foods);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOders();

        orders.child(orderId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Catering c = snapshot.getValue(Catering.class);
                delivery_charges.setText("Delivery Charges : "+c.getDelivery_charges());
                txt_total_bill.setText("Total Bill : "+c.getTotal_bill());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void loadOders() {
        options = new FirebaseRecyclerOptions.Builder<EventCatering>().setQuery(foodOrders,EventCatering.class).build();
        adapter = new FirebaseRecyclerAdapter<EventCatering, EventCateringFoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull EventCateringFoodViewHolder holder, int position, @NonNull EventCatering model) {
                //Toast.makeText(FoodOrdersActivity.this, "abc", Toast.LENGTH_SHORT).show();
                holder.txtFoodName.setText(model.getName());
                holder.txtFoodPrice.setText("Rs. "+model.getPrice());
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });

            }

            @NonNull
            @Override
            public EventCateringFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_and_catering_food_order_layout, parent,false);
                return new EventCateringFoodViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}