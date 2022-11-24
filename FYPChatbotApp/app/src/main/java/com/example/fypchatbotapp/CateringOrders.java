package com.example.fypchatbotapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fypchatbotapp.common.Common;
import com.example.fypchatbotapp.model.Catering;
import com.example.fypchatbotapp.viewholder.CateringOrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CateringOrders extends AppCompatActivity {
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Catering, CateringOrderViewHolder> adapter;
    FirebaseRecyclerOptions<Catering> options;

//    FirebaseRecyclerAdapter<FoodOrder, FoodOrderViewHolder> foodAdapter;
//    FirebaseRecyclerOptions<FoodOrder> foodOptions;

    FirebaseDatabase database;
    DatabaseReference orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catering_orders);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
        Toolbar toolbar = findViewById(R.id.toolbar12);
        toolbar.setTitle("Catering Orders");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(backIntent);
            }
        });
        database = FirebaseDatabase.getInstance();
        orders = database.getReference("Catering_Orders");

        recyclerView = findViewById(R.id.list_orders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOders(Common.currentUser.getPhone());
    }
    private void loadOders(String phone) {
        options = new FirebaseRecyclerOptions.Builder<Catering>().setQuery(orders.orderByChild("userphone").equalTo(phone), Catering.class).build();
        adapter = new FirebaseRecyclerAdapter<Catering, CateringOrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CateringOrderViewHolder holder, int position, @NonNull Catering model) {
                holder.txtOrderName.setText("Name : " + model.getUsername());
                holder.txtOrderPhone.setText("Phone : " + model.getUserphone());
                holder.txtOrderDate.setText("Booking date : " + model.getDate());
                holder.txtOrderTime.setText("Booking time : " + model.getTime());
                holder.txtOrderPeople.setText("No. of People : " + model.getPeople());
                holder.txtOrderLocation.setText("Event Location : " + model.getEvent_location());
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(CateringOrders.this, FoodCateringOrders.class);
                        intent.putExtra("user", "user");
                        intent.putExtra("orderId", adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public CateringOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.catering_order_layout, parent,false);
                return new CateringOrderViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}