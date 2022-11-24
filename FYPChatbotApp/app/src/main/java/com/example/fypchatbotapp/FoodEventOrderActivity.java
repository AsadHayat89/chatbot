package com.example.fypchatbotapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fypchatbotapp.common.Common;
import com.example.fypchatbotapp.model.Catering;
import com.example.fypchatbotapp.model.EventBooking;
import com.example.fypchatbotapp.model.EventCatering;
import com.example.fypchatbotapp.model.Food;
import com.example.fypchatbotapp.model.FoodOrder;
import com.example.fypchatbotapp.viewholder.EventCateringFoodViewHolder;
import com.example.fypchatbotapp.viewholder.FoodOrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FoodEventOrderActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    TextView txt_hall_charges, txt_cake_price, txt_balloons_decor_price, txt_stage_decor_price, txt_songs_charges, txt_total_bill;

    FirebaseRecyclerAdapter<EventCatering, EventCateringFoodViewHolder> adapter;
    FirebaseRecyclerOptions<EventCatering> options;

    String orderId = "";
    String user = "";

    FirebaseDatabase database;
    DatabaseReference orders, foodOrders, food;

    Food f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_event_order);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
        Toolbar toolbar1 = findViewById(R.id.toolbarevent);
        toolbar1.setTitle("Event Booking Price Details");
        setSupportActionBar(toolbar1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (getIntent()!=null)
        {
            orderId = getIntent().getStringExtra("orderId");
            user = getIntent().getStringExtra("user");
        }
        toolbar1.setNavigationOnClickListener(new View.OnClickListener() {
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
        txt_hall_charges = findViewById(R.id.price_hall);
        txt_cake_price = findViewById(R.id.price_cake);
        txt_balloons_decor_price = findViewById(R.id.wall_price_decor);
        txt_stage_decor_price = findViewById(R.id.stage_price_decor);
        txt_songs_charges = findViewById(R.id.price_songs);
        txt_total_bill = findViewById(R.id.txt_total_bill_event);

        database = FirebaseDatabase.getInstance();
        orders = database.getReference("Event_Booking");
        foodOrders = orders.child(orderId).child("food_items");
        //Toast.makeText(this, foodOrders.getKey(), Toast.LENGTH_SHORT).show();

        recyclerView = findViewById(R.id.list_foods_event);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOders();

        orders.child(orderId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                EventBooking e = snapshot.getValue(EventBooking.class);
                txt_cake_price.setText("Rs. : "+e.getCake_price());
                txt_balloons_decor_price.setText("Rs. : "+e.getBalloons_decor_price());
                txt_stage_decor_price.setText("Rs. : "+e.getStage_decor_price());
                txt_hall_charges.setText("Rs. : "+e.getHall_price());
                txt_songs_charges.setText("Rs. : "+e.getSongs_price());
                txt_total_bill.setText("Total Bill : Rs. "+e.getTotal_price());
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