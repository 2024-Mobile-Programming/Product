package com.example.myapplication;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * 데이터 - 뷰 연결
 */
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
        // 레이아웃 inflate
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grid_layout, parent, false);
        return new GridItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridItemViewHolder holder, int position) {
        // 데이터 binding
        Item currentItem = itemList.get(position);
        holder.itemTitle.setText(currentItem.getTitle());

        // Glide를 사용하여 이미지 로드
        if (currentItem.getImageUri() != null && !currentItem.getImageUri().isEmpty()) {
            Uri imageUri = Uri.parse(currentItem.getImageUri());
            Glide.with(holder.itemView.getContext())
                    .load(imageUri)
                    .placeholder(R.drawable.ic_launcher_foreground) // 이미지 로딩 전 표시할 이미지
                    .error(R.drawable.ic_launcher_foreground) // 로딩 실패 시 표시할 이미지
                    .into(holder.itemImage);
        } else {
            // 이미지가 없는 경우 기본 이미지 표시
            holder.itemImage.setImageResource(R.drawable.ic_launcher_foreground);
        }

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
    public static class GridItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemTitle;
        ImageView itemImage;

        public GridItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.itemTitle);
            itemImage = itemView.findViewById(R.id.itemImage);
        }
    }
}
