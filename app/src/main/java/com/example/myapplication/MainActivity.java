package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Grid;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemAdapter.OnItemClickListener{
    ImageButton gridButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // RecyclerView 참조
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // 레이아웃 설정 (세로 리스트)

        // 데이터 리스트 생성
        // item 추가 (하드 코딩)
        List<Item> itemList = new ArrayList<>();
        itemList.add(new Item("Avatar", "An epic sci-fi movie", null, "2024.11.23.", 0, 4.5f));
        itemList.add(new Item("Harry Potter", "A magical journey", null, "2024.11.23.", 0, 3.5f));
        itemList.add(new Item("Inception", "A mind-bending thriller", null, "2024.11.23.", 0, 4.0f));
        itemList.add(new Item("Avatar", "An epic sci-fi movie", null, "2024.11.23.", 0, 4.5f));
        itemList.add(new Item("Harry Potter", "A magical journey", null, "2024.11.23.", 0, 3.5f));
        itemList.add(new Item("Inception", "A mind-bending thriller", null, "2024.11.23.", 0, 4.0f));

        // 어댑터 설정
        ItemAdapter adapter = new ItemAdapter(itemList, this);
        recyclerView.setAdapter(adapter);

        // 그리드뷰로 전환
        gridButton = (ImageButton) findViewById(R.id.toGridButton);

        // 아이콘 클릭 시 화면 전환
        gridButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GridMainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemClick(Item item) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        // Pass data to DetailActivity
        intent.putExtra("title", item.getTitle());
        intent.putExtra("content", item.getContent());
        intent.putExtra("author", item.getAuthor());
        intent.putExtra("date", item.getDate());
        intent.putExtra("rating", item.getRating());
        intent.putExtra("image", item.getImage());
        // Add other data as needed
        startActivity(intent);
    }
}