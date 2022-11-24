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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.fypchatbotapp.common.Common;
import com.example.fypchatbotapp.model.EventBooking;
import com.example.fypchatbotapp.viewholder.AdminEventOrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminEventBookingOrders extends AppCompatActivity {
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<EventBooking, AdminEventOrderViewHolder> adapter;
    FirebaseRecyclerOptions<EventBooking> options;

//    FirebaseRecyclerAdapter<FoodOrder, FoodOrderViewHolder> foodAdapter;
//    FirebaseRecyclerOptions<FoodOrder> foodOptions;

    FirebaseDatabase database;
    DatabaseReference orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_event_booking_orders);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
        Toolbar toolbar = findViewById(R.id.toolbar15);
        toolbar.setTitle("Event Booking Orders");
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
        database = FirebaseDatabase.getInstance();
        orders = database.getReference("Event_Booking");

        recyclerView = findViewById(R.id.list_orders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOders();
    }
    private void loadOders() {
        options = new FirebaseRecyclerOptions.Builder<EventBooking>().setQuery(orders, EventBooking.class).build();
        adapter = new FirebaseRecyclerAdapter<EventBooking, AdminEventOrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminEventOrderViewHolder holder, int position, @NonNull EventBooking model) {
                holder.txtOrderName.setText("Name : " + model.getUsername());
                holder.txtOrderPhone.setText("Phone : " + model.getUserphone());
                holder.txtOrderDate.setText("Booking date : " + model.getDate());
                holder.txtOrderTime.setText("Booking time : " + model.getTime());
                holder.txtOrderPeople.setText("No. of People : " + model.getPeople());
                holder.txtOrderType.setText("Event Type : "+model.getEvent_type());
                holder.txtOrderCakeWriting.setText("Cake Writing : "+model.getCake_writing());
                holder.txtBalloonsDecor.setText("Balloons Color : "+model.getBalloons_decor());
                holder.txtStageDecor.setText("Stage Decor : "+model.getStage_decor());
                holder.txtStageWritingDecor.setText("Stage Writing : "+model.getStage_writing());
                holder.txtEventHours.setText("Event Hours : "+model.getEvent_hours());
                holder.txtSongs.setText("Event Songs : "+model.getEvent_songs());
                holder.txtCakeType.setText("Cake : "+model.getCake_type());
                holder.btnDelEventOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        orders.child(adapter.getRef(position).getKey()).removeValue();
                        Toast.makeText(AdminEventBookingOrders.this, "Order deleted!", Toast.LENGTH_SHORT).show();
                    }
                });
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(AdminEventBookingOrders.this, FoodEventOrderActivity.class);
                        intent.putExtra("user", "admin");
                        intent.putExtra("orderId", adapter.getRef(position).getKey());
                        startActivity(intent);

                    }
                });
            }

            @NonNull
            @Override
            public AdminEventOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_event_order_layout, parent,false);
                return new AdminEventOrderViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}