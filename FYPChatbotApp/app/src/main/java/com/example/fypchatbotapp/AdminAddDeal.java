package com.example.fypchatbotapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fypchatbotapp.model.AddDeal;
import com.example.fypchatbotapp.model.Deals;
import com.example.fypchatbotapp.model.DealsFood;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdminAddDeal extends AppCompatActivity {
    EditText etDealName, etDealPrice, etDealDescription, etItemName, etItemQty;
    AppCompatButton btnAddItem, btnAddDeal;

    FirebaseDatabase database;
    DatabaseReference dealRef;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    String catId = "";
    AddDeal addDeal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_deal);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
        Toolbar toolbar = findViewById(R.id.toolbarDealAdd);
        toolbar.setTitle("Add Deal");
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
        etDealName = findViewById(R.id.etAddFDName);
        etDealPrice = findViewById(R.id.etAddFDPrice);
        etDealDescription = findViewById(R.id.etAddFDDescription);
        etItemName = findViewById(R.id.etAddFDItem);
        etItemQty = findViewById(R.id.etAddFDQty);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnAddDeal = findViewById(R.id.btnAddDeal);

        recyclerView = findViewById(R.id.rvArray);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<DealsFood> items = new ArrayList<DealsFood>();

        if (getIntent() != null)
        {
            catId = getIntent().getStringExtra("dCatId");
        }

        database = FirebaseDatabase.getInstance();
        dealRef = database.getReference("Deals");

        btnAddDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deal_name = etDealName.getText().toString();
                String deal_price = etDealPrice.getText().toString();
                String deal_description = etDealDescription.getText().toString();
                if (deal_name.isEmpty())
                {
                    etDealName.setError("Deal name is empty");
                    etDealName.requestFocus();
                    return;
                }
                if (deal_price.isEmpty())
                {
                    etDealPrice.setError("Deal price is empty");
                    etDealPrice.requestFocus();
                    return;
                }
                if (deal_description.isEmpty())
                {
                    etDealDescription.setError("Deal decsription is empty");
                    etDealDescription.requestFocus();
                    return;
                }
                addDeal = new AddDeal();
                addDeal.setCat_id(catId);
                addDeal.setDeal_name(deal_name);
                addDeal.setDeal_price(deal_price);
                addDeal.setDeal_description(deal_description);
                if (items.isEmpty())
                {
                    Toast.makeText(AdminAddDeal.this, "Please enter items into deal", Toast.LENGTH_SHORT).show();
                    return;
                }
                addDeal.setFood_items(items);
                dealRef.push().setValue(addDeal);
                Toast.makeText(AdminAddDeal.this, "New deal "+addDeal.getDeal_name()+" added successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminAddDeal.this, AdminDealCategoryActivity.class);
                startActivity(intent);
            }
        });

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etItemName.getText().toString();
                String qty = etItemQty.getText().toString();
                if (name.isEmpty())
                {
                    etItemName.setError("Item name is empty");
                    etItemName.requestFocus();
                    return;
                }
                if (qty.isEmpty())
                {
                    etItemQty.setError("Item quantity is empty");
                    etItemQty.requestFocus();
                    return;
                }
                DealsFood dealsFood = new DealsFood(name, qty);
                items.add(dealsFood);
                etItemName.setText("");
                etItemQty.setText("");
                adapter = new DealAdapter(items);
                recyclerView.setAdapter(adapter);
            }
        });
    }

}