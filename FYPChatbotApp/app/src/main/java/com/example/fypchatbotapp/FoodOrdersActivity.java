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
import android.widget.Toast;

import com.example.fypchatbotapp.model.Food;
import com.example.fypchatbotapp.model.FoodOrder;
import com.example.fypchatbotapp.model.Orders;
import com.example.fypchatbotapp.viewholder.FoodOrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FoodOrdersActivity extends AppCompatActivity {
    public TextView tvTotalPrice, tvDeliveryPrice;
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<FoodOrder, FoodOrderViewHolder> adapter;
    FirebaseRecyclerOptions<FoodOrder> options;

    String orderId = "";
    String user = "";

    FirebaseDatabase database;
    DatabaseReference orders, foodOrders, food;

    Food f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_orders);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
        Toolbar toolbar = findViewById(R.id.toolbar17);
        toolbar.setTitle("Food Order Price Details");
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
                    backIntent = new Intent(getApplicationContext(), Simple_Food_Order.class);
                }
                else
                {
                    backIntent = new Intent(getApplicationContext(), AdminFoodOrders.class);
                }
                startActivity(backIntent);
            }
        });
        database = FirebaseDatabase.getInstance();
        orders = database.getReference("Orders");
        food = database.getReference("Food");
        foodOrders = orders.child(orderId).child("food_items");
       // Toast.makeText(this, foodOrders.getKey(), Toast.LENGTH_SHORT).show();

        tvDeliveryPrice = findViewById(R.id.txt_total_bill_del);
        tvTotalPrice = findViewById(R.id.order_total_price);
        recyclerView = findViewById(R.id.list_foods);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOders();

        orders.child(orderId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Orders o = snapshot.getValue(Orders.class);
                tvDeliveryPrice.setText("Delivery Charges : Rs. "+o.getDelivery_charges());
                tvTotalPrice.setText("Total Bill : Rs. "+o.getTotal_price());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadOders() {
        options = new FirebaseRecyclerOptions.Builder<FoodOrder>().setQuery(foodOrders,FoodOrder.class).build();
        adapter = new FirebaseRecyclerAdapter<FoodOrder, FoodOrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodOrderViewHolder holder, int position, @NonNull FoodOrder model) {
                //Toast.makeText(FoodOrdersActivity.this, "abc", Toast.LENGTH_SHORT).show();
                String name = cap(model.getName());
                holder.txtFoodName.setText(name);
                holder.txtFoodPrice.setText("Rs. "+model.getPrice());
                holder.txtFoodQuantity.setText(model.getQuantity());
                Query foodRef = food.orderByChild("name").equalTo(name);
                //Toast.makeText(FoodOrdersActivity.this, com.example.fypchatbotapp.model.getName()+name, Toast.LENGTH_SHORT).show();
                foodRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            //Toast.makeText(FoodOrdersActivity.this, "exists", Toast.LENGTH_SHORT).show();
                            f = snapshot.getValue(Food.class);
                            //Toast.makeText(FoodOrdersActivity.this, f.getImage(), Toast.LENGTH_LONG).show();
                        }
                        else
                        {

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(FoodOrdersActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });

            }

            @NonNull
            @Override
            public FoodOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_food_layout, parent,false);
                return new FoodOrderViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private String cap(String message) {
        char[] charArray = message.toCharArray();
        boolean foundSpace = true;

        for(int i = 0; i < charArray.length; i++) {

            // if the array element is a letter
            if(Character.isLetter(charArray[i])) {

                // check space is present before the letter
                if(foundSpace) {

                    // change the letter into uppercase
                    charArray[i] = Character.toUpperCase(charArray[i]);
                    foundSpace = false;
                }
            }

            else {
                // if the new character is not character
                foundSpace = true;
            }
        }

        // convert the char array to the string
        message = String.valueOf(charArray);
        return message;
    }


}