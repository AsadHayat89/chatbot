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

public class AdminEventOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView txtOrderPhone, txtOrderDate, txtOrderTime, txtOrderTotal, txtOrderName, txtOrderPeople, txtOrderType, txtBalloonsDecor, txtStageDecor, txtStageWritingDecor, txtOrderCakeWriting, txtCakeType, txtSongs, txtEventHours;
    public AppCompatButton btnDelEventOrder;
    private ItemClickListener itemClickListener;

    public AdminEventOrderViewHolder(@NonNull View itemView) {
        super(itemView);

        txtOrderPhone = itemView.findViewById(R.id.event_order_phone);
        txtOrderDate = itemView.findViewById(R.id.event_order_date);
        txtOrderTime = itemView.findViewById(R.id.event_order_time);
        txtOrderPeople = itemView.findViewById(R.id.event_order_people);
        txtOrderName = itemView.findViewById(R.id.event_order_name);
        txtOrderType = itemView.findViewById(R.id.event_order_type);
        txtOrderCakeWriting = itemView.findViewById(R.id.event_order_cake_writing);
        txtBalloonsDecor = itemView.findViewById(R.id.event_balloons_decor);
        txtStageDecor = itemView.findViewById(R.id.event_stage_decor);
        txtStageWritingDecor = itemView.findViewById(R.id.event_stage_writing);
        txtEventHours = itemView.findViewById(R.id.event_hours);
        txtCakeType = itemView.findViewById(R.id.event_order_cake_type);
        txtSongs = itemView.findViewById(R.id.event_songs);
        btnDelEventOrder = itemView.findViewById(R.id.DelEventOrder);

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
