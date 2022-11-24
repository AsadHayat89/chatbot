package com.example.fypchatbotapp.viewholder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fypchatbotapp.ItemClickListener;
import com.example.fypchatbotapp.R;
import com.example.fypchatbotapp.common.Common;

public class AdminCateringOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView txtOrderPhone, txtOrderDate, txtOrderTime, txtOrderName, txtOrderPeople, txtOrderType, txtOrderLocation, txtOrderBill;
    public AppCompatButton btnDelCateringOrder;
    private ItemClickListener itemClickListener;

    public AdminCateringOrderViewHolder(@NonNull View itemView) {
        super(itemView);

        txtOrderPhone = itemView.findViewById(R.id.catering_order_phone);
        txtOrderDate = itemView.findViewById(R.id.catering_order_date);
        txtOrderTime = itemView.findViewById(R.id.catering_order_time);
        txtOrderPeople = itemView.findViewById(R.id.catering_order_people);
        txtOrderName = itemView.findViewById(R.id.catering_order_name);
        txtOrderLocation = itemView.findViewById(R.id.catering_order_location);
        btnDelCateringOrder = itemView.findViewById(R.id.DelCateringOrder);

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
