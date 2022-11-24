package com.example.fypchatbotapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fypchatbotapp.model.DealsFood;

import java.util.ArrayList;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.DealAdapterViewHolder> {

    ArrayList<DealsFood> items;
    public DealAdapter(ArrayList<DealsFood> items) {
        this.items = items;
    }


    @NonNull
    @Override
    public DealAdapter.DealAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deals_arraylist_view, parent, false);
        return new DealAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DealAdapter.DealAdapterViewHolder holder, int position) {
        holder.tvItemQty.setText(items.get(position).getQuantity());
        holder.tvItemName.setText(items.get(position).getName());
        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, items.size());
                holder.itemView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class DealAdapterViewHolder extends RecyclerView.ViewHolder {

        public TextView tvItemQty, tvItemName;
        public AppCompatButton btnRemove;

        public DealAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            tvItemQty = itemView.findViewById(R.id.tvItemQty);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            btnRemove = itemView.findViewById(R.id.btnRemoveItem);
        }
    }
}
