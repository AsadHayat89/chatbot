package com.example.fypchatbotapp.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fypchatbotapp.ItemClickListener;
import com.example.fypchatbotapp.R;

public class DealViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtDealName, txtDealPrice, txtDealDescription;
    private ItemClickListener itemClickListener;
    public RecyclerView recyclerFood;
    public RecyclerView.LayoutManager manager;

    public DealViewHolder(@NonNull View itemView) {
        super(itemView);

        manager = new LinearLayoutManager(itemView.getContext(),LinearLayoutManager.VERTICAL,false);
        txtDealName = itemView.findViewById(R.id.deal_head);
        txtDealPrice = itemView.findViewById(R.id.deal_price);
        txtDealDescription = itemView.findViewById(R.id.deal_des);
        recyclerFood = itemView.findViewById(R.id.deals_foods);
        recyclerFood.setLayoutManager(manager);

        itemView.setOnClickListener(this);
    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
