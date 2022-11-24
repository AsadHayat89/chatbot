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
import com.example.fypchatbotapp.model.Orders;
import com.example.fypchatbotapp.viewholder.AdminFoodOrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminFoodOrders extends AppCompatActivity {
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Orders, AdminFoodOrderViewHolder> adapter;
    FirebaseRecyclerOptions<Orders> options;

    String user = "";

    FirebaseDatabase database;
    DatabaseReference orders, foodOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_food_orders);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
        Toolbar toolbar = findViewById(R.id.toolbar13);
        toolbar.setTitle("Food Delivery Orders");
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
        orders = database.getReference("Orders");

        recyclerView = findViewById(R.id.list_orders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOders();
    }
    private void loadOders() {
        options = new FirebaseRecyclerOptions.Builder<Orders>().setQuery(orders, Orders.class).build();
        adapter = new FirebaseRecyclerAdapter<Orders, AdminFoodOrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminFoodOrderViewHolder holder, int position, @NonNull Orders model) {
                holder.txtOrderName.setText("Name : "+model.getUsername());
                holder.txtOrderPhone.setText("Phone : "+model.getUserphone());
                holder.txtOrderAddress.setText("Address : "+model.getUseraddress());
                holder.txtOrderTotal.setText("Total Price : Rs. "+model.getTotal_price());
                holder.btnDelOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        orders.child(adapter.getRef(position).getKey()).removeValue();
                        Toast.makeText(AdminFoodOrders.this, "Order deleted!", Toast.LENGTH_SHORT).show();
                    }
                });
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(AdminFoodOrders.this, FoodOrdersActivity.class);
                        intent.putExtra("user", "admin");
                        intent.putExtra("orderId", adapter.getRef(position).getKey());
                        startActivity(intent);

                    }
                });
            }

            @NonNull
            @Override
            public AdminFoodOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_order_layout, parent,false);
                return new AdminFoodOrderViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}