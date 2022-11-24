package com.example.fypchatbotapp.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fypchatbotapp.ItemClickListener;
import com.example.fypchatbotapp.R;

public class TableBookingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView txtOrderPhone, txtOrderDate, txtOrderTime, txtOrderName, txtOrderPeople;
    private ItemClickListener itemClickListener;

    public TableBookingViewHolder(@NonNull View itemView) {
        super(itemView);

        txtOrderPhone = itemView.findViewById(R.id.table_order_phone);
        txtOrderDate = itemView.findViewById(R.id.table_order_date);
        txtOrderTime = itemView.findViewById(R.id.table_order_time);
        txtOrderPeople = itemView.findViewById(R.id.table_order_people);
        txtOrderName = itemView.findViewById(R.id.table_order_name);

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
