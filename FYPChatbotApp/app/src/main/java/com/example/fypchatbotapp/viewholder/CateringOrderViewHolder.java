package com.example.fypchatbotapp.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fypchatbotapp.ItemClickListener;
import com.example.fypchatbotapp.R;

public class CateringOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView txtOrderPhone, txtOrderDate, txtOrderTime, txtOrderName, txtOrderPeople, txtOrderType, txtOrderLocation, txtOrderBill;
    private ItemClickListener itemClickListener;

    public CateringOrderViewHolder(@NonNull View itemView) {
        super(itemView);

        txtOrderPhone = itemView.findViewById(R.id.catering_order_phone);
        txtOrderDate = itemView.findViewById(R.id.catering_order_date);
        txtOrderTime = itemView.findViewById(R.id.catering_order_time);
        txtOrderPeople = itemView.findViewById(R.id.catering_order_people);
        txtOrderName = itemView.findViewById(R.id.catering_order_name);
        txtOrderLocation = itemView.findViewById(R.id.catering_order_location);

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
