package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GridItemAdapter extends RecyclerView.Adapter<GridItemAdapter.GridItemViewHolder> {
    private final List<Item> itemList;

    public GridItemAdapter(List<Item> itemList) {
        this.itemList = itemList;
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
        // 현재 위치의 데이터를 가져와 뷰에 바인딩
        Item currentItem = itemList.get(position);
        holder.itemTitle.setText(currentItem.getTitle());
//        holder.itemContent.setText(currentItem.getContent());
//        holder.itemAuthor.setText(currentItem.getAuthor());
//        holder.itemDate.setText(currentItem.getDate());
//        holder.itemRating.setRating(currentItem.getRating());
        holder.itemImage.setImageResource(currentItem.getImage());
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