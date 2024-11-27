package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

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
        // item_layout.xml을 인플레이트하여 ViewHolder 생성
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // 현재 위치의 데이터를 가져와 뷰에 바인딩
        Item currentItem = itemList.get(position);
        holder.itemTitle.setText(currentItem.getTitle());
        holder.itemContent.setText(currentItem.getContent());
        holder.itemAuthor.setText(currentItem.getAuthor());
        holder.itemDate.setText(currentItem.getDate());
        holder.itemRating.setRating(currentItem.getRating());
        holder.itemImage.setImageResource(currentItem.getImage());

        // 작가 표시 여부
        if (currentItem.getAuthor() != null && !currentItem.getAuthor().isEmpty()) {
            holder.itemAuthor.setVisibility(View.VISIBLE);
            holder.itemAuthor.setText(currentItem.getAuthor());
        } else {
            holder.itemAuthor.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onItemClick(currentItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size(); // 데이터 개수 반환
    }

    // ViewHolder 클래스
    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemTitle, itemContent, itemAuthor, itemDate;
        ImageView itemImage;
        RatingBar itemRating;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.itemTitle);
            itemAuthor = itemView.findViewById(R.id.itemAuthor);
            itemDate = itemView.findViewById(R.id.itemDate);
            itemContent = itemView.findViewById(R.id.itemContent);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemRating = itemView.findViewById(R.id.itemRating);

        }
    }
}