package com.example.fypchatbotapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
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
import android.widget.EditText;
import android.widget.Toast;

import com.example.fypchatbotapp.model.AddDeal;
import com.example.fypchatbotapp.model.Deals;
import com.example.fypchatbotapp.model.DealsFood;
import com.example.fypchatbotapp.model.Food;
import com.example.fypchatbotapp.viewholder.AdminFoodViewHolder;
import com.example.fypchatbotapp.viewholder.DealFoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminUpdateDeal extends AppCompatActivity {
    EditText etDealName, etDealPrice, etDealDescription, etItemName, etItemQty;
    AppCompatButton btnUpdateItem, btnUpdateDeal;

    FirebaseDatabase database;
    DatabaseReference dealRef, foodRef;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    FirebaseRecyclerAdapter<DealsFood, DealAdapter.DealAdapterViewHolder> fAdapter;
    FirebaseRecyclerOptions<DealsFood> options;

    String catId = "";
    String dealId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_deal);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
        Toolbar toolbar = findViewById(R.id.toolbarDealUp);
        toolbar.setTitle("Update Deal");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(getApplicationContext(),AdminDealActivity.class);
                startActivity(backIntent);
            }
        });
        etDealName = findViewById(R.id.etUpdateFDName);
        etDealPrice = findViewById(R.id.etUpdateFDPrice);
        etDealDescription = findViewById(R.id.etUpdateFDDesc);
        etItemName = findViewById(R.id.etAddFDItemU);
        etItemQty = findViewById(R.id.etAddFDQtyU);
        btnUpdateItem = findViewById(R.id.btnUpdateItem);
        btnUpdateDeal = findViewById(R.id.btnUpdateDeal);

        recyclerView = findViewById(R.id.rvArrayU);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<DealsFood> items = new ArrayList<DealsFood>();

        if (getIntent() != null)
        {
            catId = getIntent().getStringExtra("catId");
            dealId = getIntent().getStringExtra("dealId");
        }

        //Toast.makeText(this, dealId, Toast.LENGTH_SHORT).show();

        database = FirebaseDatabase.getInstance();
        dealRef = database.getReference("Deals");

        dealRef.child(dealId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Deals d = snapshot.getValue(Deals.class);
                etDealName.setText(d.getDeal_name());
                etDealPrice.setText(d.getDeal_price());
                etDealDescription.setText(d.getDeal_description());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        options = new FirebaseRecyclerOptions.Builder<DealsFood>().setQuery(dealRef.child(dealId).child("food_items"), DealsFood.class).build();
        fAdapter = new FirebaseRecyclerAdapter<DealsFood, DealAdapter.DealAdapterViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DealAdapter.DealAdapterViewHolder holder, int position, @NonNull DealsFood model) {
                holder.tvItemQty.setText(model.getQuantity());
                holder.tvItemName.setText(model.getName());
                DealsFood dealsFood = new DealsFood(model.getName(),model.getQuantity());
                items.add(dealsFood);
                holder.btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dealRef.child(dealId).child("food_items").child(fAdapter.getRef(position).getKey()).removeValue();
                        items.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, items.size());
                        //holder.itemView.setVisibility(View.GONE);
                    }
                });
            }

            @NonNull
            @Override
            public DealAdapter.DealAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deals_arraylist_view, parent, false);
                return new DealAdapter.DealAdapterViewHolder(view);
            }
        };
        fAdapter.startListening();
        recyclerView.setAdapter(fAdapter);

        btnUpdateDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDeal addDeal = new AddDeal();
                addDeal.setCat_id(catId);
                addDeal.setDeal_name(etDealName.getText().toString());
                addDeal.setDeal_price(etDealPrice.getText().toString());
                addDeal.setDeal_description(etDealDescription.getText().toString());
                addDeal.setFood_items(items);
                dealRef.child(dealId).setValue(addDeal);
                Toast.makeText(AdminUpdateDeal.this, addDeal.getDeal_name()+" updated", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminUpdateDeal.this, AdminDealCategoryActivity.class);
                startActivity(intent);
            }
        });

        btnUpdateItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DealsFood dealsFood = new DealsFood(etItemName.getText().toString(), etItemQty.getText().toString());
                items.add(dealsFood);
                etItemName.setText("");
                etItemQty.setText("");
                adapter = new DealAdapter(items);
                recyclerView.setAdapter(adapter);
            }
        });
    }
}