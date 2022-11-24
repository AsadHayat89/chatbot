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
import com.example.fypchatbotapp.model.Deal_Category;
import com.example.fypchatbotapp.viewholder.AdminDealCategoryViewHolder;
import com.example.fypchatbotapp.viewholder.DealCategoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdminDealCategoryActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Deal_Category, AdminDealCategoryViewHolder> adapter;
    FirebaseRecyclerOptions<Deal_Category> options;

    FirebaseDatabase database;
    DatabaseReference deals, df;

    FloatingActionButton fabAddDealCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_deal_category);
        Toolbar toolbar = findViewById(R.id.toolbar32);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
        toolbar.setTitle("Deal Categories");
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
        deals = database.getReference("Deal_Category");
        df = database.getReference("Deals");

        recyclerView = findViewById(R.id.list_deals_cat_admin);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        fabAddDealCat = findViewById(R.id.fabAddDealCat);
        fabAddDealCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(AdminDealCategoryActivity.this, AdminAddDealCategory.class);
                startActivity(addIntent);
            }
        });

        loadDealCategories();
    }
    private void loadDealCategories() {
        options = new FirebaseRecyclerOptions.Builder<Deal_Category>().setQuery(deals, Deal_Category.class).build();
        adapter = new FirebaseRecyclerAdapter<Deal_Category, AdminDealCategoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminDealCategoryViewHolder holder, int position, @NonNull Deal_Category model) {
                holder.txtCatName.setText(model.getName());
                //Picasso.with(getBaseContext()).load(model.getImage()).into(holder.imageView);
                Picasso.with(getBaseContext()).load(model.getImage()).into(holder.iv);
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(AdminDealCategoryActivity.this, AdminDealActivity.class);
                        intent.putExtra("catId", adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
                holder.btnDealCatUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent upIntent = new Intent(AdminDealCategoryActivity.this, AdminUpdateDealCategory.class);
                        upIntent.putExtra("dealCatId", adapter.getRef(position).getKey());
                        startActivity(upIntent);
                    }
                });
                holder.btnDealCatDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deals.child(adapter.getRef(position).getKey()).removeValue();
                        Query delQuery = df.orderByChild("cat_id").equalTo(adapter.getRef(position).getKey());
                        delQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot i: snapshot.getChildren()) {
                                    i.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        Toast.makeText(AdminDealCategoryActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @NonNull
            @Override
            public AdminDealCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_deal_category_layout, parent,false);
                return new AdminDealCategoryViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals(com.example.fypchatbotapp.common.Common.UPDATE)) {


        } else if (item.getTitle().equals(Common.DELETE)) {

        }
        return super.onContextItemSelected(item);
    }
}