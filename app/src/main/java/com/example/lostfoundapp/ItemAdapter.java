package com.example.lostfoundapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private List<Item> items;
    private Context context;
    public ItemAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void setItems(List<Item> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemStatus;
        ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.textViewItemName);
            itemStatus = itemView.findViewById(R.id.textViewStatus);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Item item = items.get(position);
                    Intent intent = new Intent(context, RemoveItem.class);
                    intent.putExtra("item_id", item.getId());
                    context.startActivity(intent);
                }
            });
        }
        void bind(Item item) {
            itemName.setText(item.getName());
            itemStatus.setText(item.isLost() ? "Lost -" : "Found -");
        }
    }
}