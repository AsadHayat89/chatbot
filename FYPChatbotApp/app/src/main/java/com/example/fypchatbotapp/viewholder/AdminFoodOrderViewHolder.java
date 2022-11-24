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

public class AdminFoodOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView txtOrderPhone, txtOrderAddress, txtOrderTotal, txtOrderName;
    public AppCompatButton btnDelOrder;
    private ItemClickListener itemClickListener;

    public AdminFoodOrderViewHolder(@NonNull View itemView) {
        super(itemView);

        txtOrderPhone = itemView.findViewById(R.id.order_phone);
        txtOrderAddress = itemView.findViewById(R.id.order_address);
        txtOrderTotal = itemView.findViewById(R.id.order_total);
        txtOrderName = itemView.findViewById(R.id.order_name);
        btnDelOrder = itemView.findViewById(R.id.DelOrder);

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
