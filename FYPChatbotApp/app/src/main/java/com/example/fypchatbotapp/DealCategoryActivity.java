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

import com.example.fypchatbotapp.common.Common;
import com.example.fypchatbotapp.model.Deal_Category;
import com.example.fypchatbotapp.model.EventBooking;
import com.example.fypchatbotapp.viewholder.DealCategoryViewHolder;
import com.example.fypchatbotapp.viewholder.EventBookingViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class DealCategoryActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Deal_Category, DealCategoryViewHolder> adapter;
    FirebaseRecyclerOptions<Deal_Category> options;

    FirebaseDatabase database;
    DatabaseReference deals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_category);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
        Toolbar toolbar = findViewById(R.id.toolbar22);
        toolbar.setTitle("Deal Categories");
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
        deals = database.getReference("Deal_Category");

        recyclerView = findViewById(R.id.list_deals_cat);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadDealCategories();
    }
    private void loadDealCategories() {
        options = new FirebaseRecyclerOptions.Builder<Deal_Category>().setQuery(deals, Deal_Category.class).build();
        adapter = new FirebaseRecyclerAdapter<Deal_Category, DealCategoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DealCategoryViewHolder holder, int position, @NonNull Deal_Category model) {
                holder.txtCatName.setText(model.getName());
                //Picasso.with(getBaseContext()).load(model.getImage()).into(holder.imageView);
                Picasso.with(getBaseContext()).load(model.getImage()).into(holder.iv);
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(DealCategoryActivity.this, DealsActivity.class);
                        intent.putExtra("catId", adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public DealCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deal_category_layout, parent,false);
                return new DealCategoryViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}