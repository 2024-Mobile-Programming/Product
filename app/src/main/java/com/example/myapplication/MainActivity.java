package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemAdapter.OnItemClickListener {
    ImageButton gridButton;
    DatabaseHelper dbHelper;
    List<Item> itemList;
    ItemAdapter adapter;
    Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        // RecyclerView 설정
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // DB에서 fetch
        itemList = dbHelper.getAllItems();

        // Adapter 설정
        adapter = new ItemAdapter(itemList, this);
        recyclerView.setAdapter(adapter);

        // Grid 전환
        gridButton = findViewById(R.id.toGridButton);
        gridButton.setOnClickListener(view -> finish());

        // 추가하기 버튼
        addBtn = findViewById(R.id.floatingButton);
        addBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("newItem", true);
            startActivity(intent);
        });
    }

    // DetailActivity -> MainActivity 시 데이터 갱신에 사용
    @Override
    protected void onResume() {
        super.onResume();
        itemList.clear();
        itemList.addAll(dbHelper.getAllItems());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(Item item) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
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
