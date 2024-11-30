package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GridMainActivity extends AppCompatActivity implements GridItemAdapter.OnItemClickListener {
    ImageButton listButton;
    Button floatingButton;
    DatabaseHelper dbHelper;
    List<Item> itemList;
    GridItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.grid_main);

        dbHelper = new DatabaseHelper(this);

        // RecyclerView 설정
        RecyclerView recyclerViewGrid = findViewById(R.id.recyclerViewGrid);
        recyclerViewGrid.setLayoutManager(new GridLayoutManager(this, 2));

        // DB에서 fetch
        itemList = dbHelper.getAllItems();

        // Adapter 설정
        adapter = new GridItemAdapter(itemList, this);
        recyclerViewGrid.setAdapter(adapter);

        // List View 전환
        listButton = findViewById(R.id.toListButton);
        listButton.setOnClickListener(view -> {
            Intent intent = new Intent(GridMainActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // 추가하기 버튼
        floatingButton = findViewById(R.id.floatingButton);
        floatingButton.setOnClickListener(view -> {
            Intent intent = new Intent(GridMainActivity.this, DetailActivity.class);
            intent.putExtra("newItem", true);
            startActivity(intent);
        });
    }

    // DetailActivity -> GridMainActivity 시 데이터 갱신에 사용
    @Override
    protected void onResume() {
        super.onResume();
        itemList.clear();
        itemList.addAll(dbHelper.getAllItems());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(Item item) {
        Intent intent = new Intent(GridMainActivity.this, DetailActivity.class);
        intent.putExtra("id", item.getId());
        intent.putExtra("title", item.getTitle());
        intent.putExtra("author", item.getAuthor());
        intent.putExtra("date", item.getDate());
        intent.putExtra("rating", item.getRating());
        intent.putExtra("content", item.getContent());
        intent.putExtra("imageUri", item.getImageUri());
        intent.putExtra("newItem", false);
        startActivity(intent);
    }
}
