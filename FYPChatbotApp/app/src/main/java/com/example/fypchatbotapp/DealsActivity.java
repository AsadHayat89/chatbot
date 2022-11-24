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

import com.example.fypchatbotapp.model.Deal_Category;
import com.example.fypchatbotapp.model.Deals;
import com.example.fypchatbotapp.model.DealsFood;
import com.example.fypchatbotapp.viewholder.DealFoodViewHolder;
import com.example.fypchatbotapp.viewholder.DealViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DealsActivity extends AppCompatActivity {
    public RecyclerView recyclerDeal;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<DealsFood, DealFoodViewHolder> fAdapter;
    FirebaseRecyclerOptions<DealsFood> fOptions;
    FirebaseRecyclerAdapter<Deals, DealViewHolder> adapter;
    FirebaseRecyclerOptions<Deals> options;

    FirebaseDatabase database;
    DatabaseReference deals, dealCategory;
    String dealId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deals);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(getApplicationContext(),DealCategoryActivity.class);
                startActivity(backIntent);
            }
        });
        if (getIntent() != null)
        {
            dealId = getIntent().getStringExtra("catId");
        }
        database = FirebaseDatabase.getInstance();
        deals = database.getReference("Deals");
        dealCategory = database.getReference("Deal_Category");
        dealCategory.child(dealId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Deal_Category deal_category = snapshot.getValue(Deal_Category.class);
                toolbar.setTitle(deal_category.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerDeal = findViewById(R.id.list_deals);
        recyclerDeal.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerDeal.setLayoutManager(layoutManager);

//        recyclerFood = findViewById(R.id.deals_foods);
//        recyclerFood.setHasFixedSize(true);
//        fLayoutManager = new LinearLayoutManager(this);
//        recyclerFood.setLayoutManager(fLayoutManager);

        loadDeals();
    }

    private void loadDeals() {
        options = new FirebaseRecyclerOptions.Builder<Deals>().setQuery(deals.orderByChild("cat_id").equalTo(dealId),Deals.class).build();
        adapter = new FirebaseRecyclerAdapter<Deals, DealViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DealViewHolder holder, int position, @NonNull Deals model) {
                String key = adapter.getRef(position).getKey();
                holder.txtDealName.setText(model.getDeal_name());
                holder.txtDealPrice.setText("Rs. "+model.getDeal_price());
                holder.txtDealDescription.setText(model.getDeal_description());
                fOptions = new FirebaseRecyclerOptions.Builder<DealsFood>().setQuery(deals.child(key).child("food_items"),DealsFood.class).build();
                fAdapter = new FirebaseRecyclerAdapter<DealsFood, DealFoodViewHolder>(fOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull DealFoodViewHolder holder, int position, @NonNull DealsFood model) {
                        //Toast.makeText(DealsActivity.this, com.example.fypchatbotapp.model.getName(), Toast.LENGTH_SHORT).show();
                        holder.txtFoodName.setText(model.getName());
                        holder.txtFoodQuantity.setText(model.getQuantity());
                        holder.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onClick(View view, int position, boolean isLongClick) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public DealFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deals_food_layout, parent,false);
                        return new DealFoodViewHolder(view);
                    }
                };
                fAdapter.startListening();
                fAdapter.notifyDataSetChanged();
                holder.recyclerFood.setAdapter(fAdapter);

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
            }

            @NonNull
            @Override
            public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deals_layout, parent,false);
                return new DealViewHolder(view);
            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerDeal.setAdapter(adapter);
    }
}