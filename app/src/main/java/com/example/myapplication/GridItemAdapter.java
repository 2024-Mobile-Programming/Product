package com.example.myapplication;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class GridItemAdapter extends RecyclerView.Adapter<GridItemAdapter.GridItemViewHolder> {
    private final List<Item> itemList;
    private final OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    public GridItemAdapter(List<Item> itemList, OnItemClickListener clickListener) {
        this.itemList = itemList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public GridItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grid_layout, parent, false);
        return new GridItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridItemViewHolder holder, int position) {
        Item currentItem = itemList.get(position);
        holder.itemTitle.setText(currentItem.getTitle());

        // Glide를 사용하여 이미지 로드
        if (currentItem.getThumbnail() != null && !currentItem.getThumbnail().isEmpty()) {
            Uri imageUri = Uri.parse(currentItem.getThumbnail());
            Glide.with(holder.itemView.getContext())
                    .load(imageUri)
                    .placeholder(R.drawable.default_image) // 로딩 중 표시할 기본 이미지
                    .error(R.drawable.default_image) // 로딩 실패 시 표시할 이미지
                    .into(holder.itemImage);
        } else {
            holder.itemImage.setImageResource(R.drawable.default_image); // 기본 이미지 설정
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
        return itemList.size();
    }

    public static class GridItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemTitle;
        // itemContent, itemAuthor, itemDate;
        ImageView itemImage;
//        RatingBar itemRating;

        public GridItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.itemTitle);
//            itemAuthor = itemView.findViewById(R.id.itemAuthor);
//            itemDate = itemView.findViewById(R.id.itemDate);
//            itemContent = itemView.findViewById(R.id.itemContent);
            itemImage = itemView.findViewById(R.id.itemImage);
//            itemRating = itemView.findViewById(R.id.itemRating);

        }
    }
}