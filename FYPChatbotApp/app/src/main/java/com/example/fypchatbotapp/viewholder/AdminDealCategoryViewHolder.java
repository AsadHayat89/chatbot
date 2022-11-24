package com.example.fypchatbotapp.viewholder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fypchatbotapp.ItemClickListener;
import com.example.fypchatbotapp.R;
import com.example.fypchatbotapp.common.Common;

public class AdminDealCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txtCatName;
    private ItemClickListener itemClickListener;
    public ImageView iv;
    public AppCompatButton btnDealCatUp, btnDealCatDel;

    public AdminDealCategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        txtCatName = itemView.findViewById(R.id.tvDealCatName);
        iv = itemView.findViewById(R.id.ivDealCat);
        btnDealCatUp = itemView.findViewById(R.id.btnDealCatUp);
        btnDealCatDel = itemView.findViewById(R.id.btnDealCatDel);

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
