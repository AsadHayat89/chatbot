package com.example.fypchatbotapp.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fypchatbotapp.ItemClickListener;
import com.example.fypchatbotapp.R;

public class DealCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtCatName;
    private ItemClickListener itemClickListener;
    public ImageView iv;

    public DealCategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        txtCatName = itemView.findViewById(R.id.tvDealCatName);
        iv = itemView.findViewById(R.id.ivDealCat);

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
