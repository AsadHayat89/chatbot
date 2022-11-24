package com.example.fypchatbotapp.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fypchatbotapp.ItemClickListener;
import com.example.fypchatbotapp.R;

public class FoodOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtFoodName, txtFoodPrice, txtFoodQuantity;
    private ItemClickListener itemClickListener;

    public FoodOrderViewHolder(@NonNull View itemView) {
        super(itemView);

        txtFoodName = itemView.findViewById(R.id.order_food_name);
        txtFoodPrice = itemView.findViewById(R.id.order_food_price);
        txtFoodQuantity = itemView.findViewById(R.id.food_quantity);

        itemView.setOnClickListener(this);
    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);


    }
}
