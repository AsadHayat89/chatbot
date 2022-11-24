package com.example.fypchatbotapp.viewholder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fypchatbotapp.ItemClickListener;
import com.example.fypchatbotapp.R;
import com.example.fypchatbotapp.common.Common;

public class AdminDealViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView txtDealName, txtDealPrice, txtDealDescription;
    private ItemClickListener itemClickListener;
    public RecyclerView recyclerFood;
    public RecyclerView.LayoutManager manager;
    public AppCompatButton btnDealUp, btnDealDel;

    public AdminDealViewHolder(@NonNull View itemView) {
        super(itemView);

        manager = new LinearLayoutManager(itemView.getContext(),LinearLayoutManager.VERTICAL,false);
        txtDealName = itemView.findViewById(R.id.admin_deal_head);
        txtDealPrice = itemView.findViewById(R.id.admin_deal_price);
        txtDealDescription = itemView.findViewById(R.id.admin_deal_des);
        btnDealUp = itemView.findViewById(R.id.btnDealUp);
        btnDealDel = itemView.findViewById(R.id.btnDealDel);
        recyclerFood = itemView.findViewById(R.id.admin_deals_foods);
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
