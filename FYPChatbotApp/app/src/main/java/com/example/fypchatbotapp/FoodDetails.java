package com.example.fypchatbotapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.fypchatbotapp.common.Common;
import com.example.fypchatbotapp.model.Food;
import com.example.fypchatbotapp.model.FoodOrder;
import com.example.fypchatbotapp.model.Request;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FoodDetails extends AppCompatActivity {
    TextView food_name, food_price, food_description;
    ImageView food_image;
    AppCompatButton btnCart;
    ElegantNumberButton numberButton;

    String foodId = "";

    ArrayList<FoodOrder> items = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference foodRef, cartRef, orders;
    Food currentFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }

        database = FirebaseDatabase.getInstance();
        foodRef = database.getReference("Food");
        cartRef = database.getReference("Cart");
        orders = database.getReference("Orders");

        numberButton = findViewById(R.id.number_button);
        food_name = findViewById(R.id.food_name);
        food_price = findViewById(R.id.food_price);
        food_image = findViewById(R.id.image_food);
        food_description = findViewById(R.id.food_description);
        btnCart = findViewById(R.id.btnCart);

        if (getIntent() != null) {
            foodId = getIntent().getStringExtra("FoodId");
        }
        if (!foodId.isEmpty()) {
            getDetailFood(foodId);
        }
//        btnCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Cart cart = new Cart();
//                cart.setName(currentFood.getName());
//                cart.setPrice(currentFood.getPrice());
//                cart.setQuantity(numberButton.getNumber());
//                cartRef.child(Common.currentUser.getPhone()).push().setValue(cart);
//                Toast.makeText(FoodDetails.this, "Added to Cart", Toast.LENGTH_SHORT).show();
//            }
//        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FoodOrder f = new FoodOrder();
                f.setName(currentFood.getName());
                f.setPrice(currentFood.getPrice());
                f.setQuantity(numberButton.getNumber());
                items.add(f);

                showAlertDialog();
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FoodDetails.this);
        alertDialog.setTitle("One more step!");
        alertDialog.setMessage("Enter your address:");

        EditText etAddress = new EditText(FoodDetails.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        etAddress.setLayoutParams(lp);
        alertDialog.setView(etAddress);
        alertDialog.setIcon(R.drawable.ic_baseline_shopping_cart_24);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Request request = new Request();
                request.setUseraddress(etAddress.getText().toString());
                request.setUsername(Common.currentUser.getName());
                request.setUserphone(Common.currentUser.getPhone());
                int total = Integer.parseInt(currentFood.getPrice())*Integer.parseInt(numberButton.getNumber());
                request.setTotal_price(String.valueOf(total));
                request.setFood_items(items);

                orders.push().setValue(request);
                //Delete Cart
                //new com.example.fypchatbotapp.DatabaseHelper(getBaseContext()).clearCart();
                Toast.makeText(FoodDetails.this, "Thank you, Order Placed!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
    private void getDetailFood(String foodId) {
        foodRef.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentFood = snapshot.getValue(Food.class);
                Picasso.with(getBaseContext()).load(currentFood.getImage()).into(food_image);

                food_price.setText(currentFood.getPrice());
                food_name.setText(currentFood.getName());
                food_description.setText(currentFood.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        foodRef.child(foodId).child("price").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String small = snapshot.getValue(String.class);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
    }
}