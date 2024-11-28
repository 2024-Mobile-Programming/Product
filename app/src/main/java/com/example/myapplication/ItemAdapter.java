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

/**
 * RecyclerView.Adapter를 확장하여 Item 데이터를 바인딩하는 어댑터 클래스.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private final List<Item> itemList;
    private final OnItemClickListener clickListener;

    /**
     * 항목 클릭 시 호출되는 리스너 인터페이스.
     */
    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    /**
     * 어댑터 생성자.
     *
     * @param itemList      항목 목록
     * @param clickListener 항목 클릭 리스너
     */
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
        holder.itemDate.setText(currentItem.getDate());
        holder.itemRating.setRating(currentItem.getRating());

        // Glide를 사용하여 이미지 로드
        if (currentItem.getThumbnail() != null && !currentItem.getThumbnail().isEmpty()) {
            Uri imageUri = Uri.parse(currentItem.getThumbnail());
            Glide.with(holder.itemView.getContext())
                    .load(imageUri)
                    .placeholder(R.drawable.default_image) // 기본 이미지 설정
                    .into(holder.itemThumbnail);
        } else {
            holder.itemThumbnail.setImageResource(R.drawable.default_image); // 기본 이미지 설정
        }

        // 저자 표시 여부
        if (currentItem.getAuthor() != null && !currentItem.getAuthor().isEmpty()) {
            holder.itemAuthor.setVisibility(View.VISIBLE);
            holder.itemAuthor.setText("저자: " + currentItem.getAuthor());
        } else {
            holder.itemAuthor.setVisibility(View.GONE);
        }

        // 항목 클릭 시 리스너 호출
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

    /**
     * ViewHolder 클래스.
     */
    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemTitle, itemContent, itemAuthor, itemDate;
        ImageView itemThumbnail;
        RatingBar itemRating;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.itemTitle);
            itemContent = itemView.findViewById(R.id.itemContent);
            itemAuthor = itemView.findViewById(R.id.itemAuthor);
            itemDate = itemView.findViewById(R.id.itemDate);
            itemThumbnail = itemView.findViewById(R.id.itemImage);
            itemRating = itemView.findViewById(R.id.ratingBar1);
        }
    }
}
