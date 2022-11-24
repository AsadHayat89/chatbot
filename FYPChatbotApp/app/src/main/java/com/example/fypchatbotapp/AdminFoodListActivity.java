package com.example.fypchatbotapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.example.fypchatbotapp.model.Category;
import com.example.fypchatbotapp.model.Food;
import com.example.fypchatbotapp.viewholder.AdminFoodViewHolder;
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

public class AdminFoodListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference foodList, catName;
    String categoryId = "";
    FirebaseRecyclerAdapter<Food, AdminFoodViewHolder> adapter;
    FirebaseRecyclerOptions<Food> options;
    Category categName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_food_list);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }

        Toolbar toolbar = findViewById(R.id.toolbar5);

        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Food");

        if (getIntent() != null) {
            categoryId = getIntent().getStringExtra("CategoryId");
        }

        catName = database.getReference("Category");
        catName.child(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categName = snapshot.getValue(Category.class);
                toolbar.setTitle(categName.getName()+" Management");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
        FloatingActionButton fab = findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminFoodListActivity.this, AdminAddFoodActivity.class);
                intent.putExtra("CatId", categoryId);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recycler_food1);
        recyclerView.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        if (!categoryId.isEmpty()) {
            loadListFood(categoryId);
        }
    }
    private void loadListFood(String categoryId) {
        options = new FirebaseRecyclerOptions.Builder<Food>().setQuery(foodList.orderByChild("menuId").equalTo(categoryId), Food.class).build();
        adapter = new FirebaseRecyclerAdapter<Food, AdminFoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminFoodViewHolder holder, int position, @NonNull Food model) {
                holder.food_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(holder.food_image);

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent footDetailsIntent = new Intent(AdminFoodListActivity.this,FoodDetails.class);
                        footDetailsIntent.putExtra("FoodId",adapter.getRef(position).getKey());
                        startActivity(footDetailsIntent);
                    }
                });
                holder.btnFUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent updateIntent = new Intent(AdminFoodListActivity.this, AdminUpdateFoodActivity.class);
                        updateIntent.putExtra("foodKey", adapter.getRef(position).getKey());
                        startActivity(updateIntent);
                    }
                });
                holder.btnFDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        foodList.child(adapter.getRef(position).getKey()).removeValue();
                        Toast.makeText(AdminFoodListActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @NonNull
            @Override
            public AdminFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_food_layout, parent,false);
                return new AdminFoodViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}