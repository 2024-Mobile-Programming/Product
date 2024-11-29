package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * 데이터 - 뷰 연결
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private final List<Item> itemList;
    private final OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    public ItemAdapter(List<Item> itemList, OnItemClickListener clickListener) {
        this.itemList = itemList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 레이아웃 inflate
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // 데이터 binding
        Item currentItem = itemList.get(position);
        holder.itemTitle.setText(currentItem.getTitle());
        holder.itemAuthor.setText(currentItem.getAuthor());

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onItemClick(currentItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // ViewHolder class - View 정보 저장
    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemTitle, itemAuthor;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.itemTitle);
            itemAuthor = itemView.findViewById(R.id.itemAuthor);
        }
    }
}
