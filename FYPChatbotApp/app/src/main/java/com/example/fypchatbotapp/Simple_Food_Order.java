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
import android.widget.Toast;

import com.example.fypchatbotapp.common.Common;
import com.example.fypchatbotapp.model.FoodOrder;
import com.example.fypchatbotapp.model.Orders;
import com.example.fypchatbotapp.viewholder.FoodOrderViewHolder;
import com.example.fypchatbotapp.viewholder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Simple_Food_Order extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Orders, OrderViewHolder> adapter;
    FirebaseRecyclerOptions<Orders> options;

    FirebaseRecyclerAdapter<FoodOrder, FoodOrderViewHolder> foodAdapter;
    FirebaseRecyclerOptions<FoodOrder> foodOptions;

    FirebaseDatabase database;
    DatabaseReference orders, foodOrders;

    Orders fOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_food_order);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
        Toolbar toolbar = findViewById(R.id.toolbar2);
        toolbar.setTitle("Food Delivery Order");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(getApplicationContext(),ChatActivity.class);
                startActivity(backIntent);
            }
        });

        database = FirebaseDatabase.getInstance();
        orders = database.getReference("Orders");

        recyclerView = findViewById(R.id.list_orders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOders(Common.currentUser.getPhone());
    }

    private void loadOders(String phone) {
        options = new FirebaseRecyclerOptions.Builder<Orders>().setQuery(orders.orderByChild("userphone").equalTo(phone), Orders.class).build();
        adapter = new FirebaseRecyclerAdapter<Orders, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull Orders model) {
                String key = adapter.getRef(position).getKey();
                holder.txtOrderName.setText("Name : "+model.getUsername());
                holder.txtOrderPhone.setText("Phone : "+model.getUserphone());
                holder.txtOrderAddress.setText("Address : "+model.getUseraddress());
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(Simple_Food_Order.this, FoodOrdersActivity.class);
                        intent.putExtra("user", "user");
                        intent.putExtra("orderId", key);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout, parent,false);
                return new OrderViewHolder(view);
            }

        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}