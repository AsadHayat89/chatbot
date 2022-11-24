package com.example.fypchatbotapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fypchatbotapp.common.Common;
import com.example.fypchatbotapp.model.Category;
import com.example.fypchatbotapp.viewholder.AdminMenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdminMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ActionBarDrawerToggle actionBarDrawerToggle;
    FirebaseDatabase database;
    DatabaseReference categories, foods;
    TextView txtFullName;
    RecyclerView recycler_menu;
    FirebaseRecyclerAdapter<Category, AdminMenuViewHolder> adapter;
    FirebaseRecyclerOptions<Category> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
        Toolbar toolbar = findViewById(R.id.toolbar4);
        toolbar.setTitle("Dish Categories");
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name);
        drawer.addDrawerListener(actionBarDrawerToggle);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        txtFullName = headerView.findViewById(R.id.txtFullName);
        txtFullName.setText(Common.currentUser.getName());

        database = FirebaseDatabase.getInstance();
        categories = database.getReference("Category");
        foods = database.getReference("Food");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMenuActivity.this, AdminAddCategory.class);
                startActivity(intent);
            }
        });
        recycler_menu = findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(this);
//        recycler_menu.setLayoutManager(layoutManager);
        recycler_menu.setLayoutManager(new GridLayoutManager(this, 2));
        loadMenu();
    }

    private void loadMenu() {
        options = new FirebaseRecyclerOptions.Builder<Category>().setQuery(categories, Category.class).build();
        adapter = new FirebaseRecyclerAdapter<Category, AdminMenuViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminMenuViewHolder holder, int position, @NonNull Category model) {
                holder.txtMenuName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(holder.imageView);

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent foodListIntent = new Intent(AdminMenuActivity.this, AdminFoodListActivity.class);
                        foodListIntent.putExtra("CategoryId", adapter.getRef(position).getKey());
                        startActivity(foodListIntent);
                    }
                });
                holder.btnFCUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent updateIntent = new Intent(AdminMenuActivity.this,AdminUpdateCategory.class);
                        updateIntent.putExtra("key", adapter.getRef(position).getKey());
                        startActivity(updateIntent);
                    }
                });
                holder.btnFCDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        categories.child(adapter.getRef(position).getKey()).removeValue();
                        Query delQuery = foods.orderByChild("menuId").equalTo(adapter.getRef(position).getKey());
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
                        Toast.makeText(AdminMenuActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public AdminMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_food_category, parent, false);
                return new AdminMenuViewHolder(view);
            }
        };
        adapter.startListening();
        recycler_menu.setAdapter(adapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat, menu);
        return true;
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_cat) {

        } else if (id == R.id.nav_deal) {
            Intent dealsIntent = new Intent(AdminMenuActivity.this, AdminDealCategoryActivity.class);
            startActivity(dealsIntent);
        }
            else if (id == R.id.res_det) {
                Intent resIntent = new Intent(AdminMenuActivity.this, RestaurantDetailsActivity.class);
                startActivity(resIntent);
        } else if (id == R.id.nav_food_orders) {
            Intent orderIntent = new Intent(AdminMenuActivity.this, AdminFoodOrders.class);
            startActivity(orderIntent);
        } else if (id == R.id.nav_table_orders) {
            Intent tOrderIntent = new Intent(AdminMenuActivity.this, AdminTableBookingOrders.class);
            startActivity(tOrderIntent);
        }
        else if (id == R.id.nav_event_orders) {
            Intent eOrderIntent = new Intent(AdminMenuActivity.this, AdminEventBookingOrders.class);
            startActivity(eOrderIntent);
        }
        else if (id == R.id.nav_catering_orders) {
            Intent cOrderIntent = new Intent(AdminMenuActivity.this, AdminCateringOrders.class);
            startActivity(cOrderIntent);
        }else if (id == R.id.nav_logout) {
            Intent signIntent = new Intent(AdminMenuActivity.this, SignIn.class);
            signIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(signIntent);
        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


}