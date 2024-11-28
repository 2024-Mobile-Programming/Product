package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GridMainActivity extends AppCompatActivity implements GridItemAdapter.OnItemClickListener {
    ImageButton listButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.grid_main);

        // RecyclerView 초기화
        RecyclerView recyclerViewGrid = findViewById(R.id.recyclerViewGrid);

        // GridLayoutManager 설정 (2열 그리드)
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);  // 2열 그리드
        recyclerViewGrid.setLayoutManager(gridLayoutManager);

        // 데이터 리스트 생성
        // item 추가 (하드 코딩)
        List<Item> itemList = new ArrayList<>();
        /*
        itemList.add(new Item("Avatar", "An epic sci-fi movie", null, "2024.11.23.", 0, 4.5f));
        itemList.add(new Item("Harry Potter", "A magical journey", null, "2024.11.23.", 0, 3.5f));
        itemList.add(new Item("Inception", "A mind-bending thriller", null, "2024.11.23.", 0, 4.0f));
        itemList.add(new Item("Avatar", "An epic sci-fi movie", null, "2024.11.23.", 0, 4.5f));
        itemList.add(new Item("Harry Potter", "A magical journey", null, "2024.11.23.", 0, 3.5f));
        itemList.add(new Item("Inception", "A mind-bending thriller", null, "2024.11.23.", 0, 4.0f));
         */

        // 어댑터 설정
        GridItemAdapter adapter = new GridItemAdapter(itemList, this);
        recyclerViewGrid.setAdapter(adapter);

        // 리스트뷰로 전환
        listButton = (ImageButton) findViewById(R.id.toListButton);

        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
       }

    @Override
    public void onItemClick(Item item){
        Intent intent = new Intent(GridMainActivity.this, DetailActivity.class);
        intent.putExtra("ITEM_ID", item.getId());
        startActivity(intent);
    }

}