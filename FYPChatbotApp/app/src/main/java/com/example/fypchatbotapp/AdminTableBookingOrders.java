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
import com.example.fypchatbotapp.model.TableBooking;
import com.example.fypchatbotapp.viewholder.AdminTableOrderViewHolder;
import com.example.fypchatbotapp.viewholder.TableBookingViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminTableBookingOrders extends AppCompatActivity {
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<TableBooking, AdminTableOrderViewHolder> adapter;
    FirebaseRecyclerOptions<TableBooking> options;

//    FirebaseRecyclerAdapter<FoodOrder, FoodOrderViewHolder> foodAdapter;
//    FirebaseRecyclerOptions<FoodOrder> foodOptions;

    FirebaseDatabase database;
    DatabaseReference orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_table_booking_orders);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
        Toolbar toolbar = findViewById(R.id.toolbar14);
        toolbar.setTitle("Table Booking Orders");
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
        orders = database.getReference("Table_Booking");

        recyclerView = findViewById(R.id.list_admin_orders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOders();
    }
    private void loadOders() {
        options = new FirebaseRecyclerOptions.Builder<TableBooking>().setQuery(orders, TableBooking.class).build();
        adapter = new FirebaseRecyclerAdapter<TableBooking, AdminTableOrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminTableOrderViewHolder holder, int position, @NonNull TableBooking model) {
                String key = adapter.getRef(position).getKey();
                holder.txtOrderName.setText("Name : " + model.getUsername());
                holder.txtOrderPhone.setText("Phone : " + model.getUserphone());
                holder.txtOrderDate.setText("Booking date : " + model.getDate());
                holder.txtOrderTime.setText("Booking time : " + model.getTime());
                holder.txtOrderPeople.setText("No. of People : " + model.getPeople());
                holder.btnDeltableOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        orders.child(adapter.getRef(position).getKey()).removeValue();
                        Toast.makeText(AdminTableBookingOrders.this, "Order deleted!", Toast.LENGTH_SHORT).show();
                    }
                });
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(AdminTableBookingOrders.this, FoodTableOrdersActivity.class);
                        intent.putExtra("user", "user");
                        intent.putExtra("orderId", key);
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public AdminTableOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_table_order_layout, parent,false);
                return new AdminTableOrderViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}