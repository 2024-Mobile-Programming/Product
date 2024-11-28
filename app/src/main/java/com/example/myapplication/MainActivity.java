package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.database.BookDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button addReviewButton;

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private List<Item> itemList;

    private BookDatabaseHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // 메인 레이아웃 파일

        addReviewButton = findViewById(R.id.floatingButton);

        // 데이터베이스 헬퍼 초기화
        dbHelper = new BookDatabaseHelper(this);

        // RecyclerView 초기화
        recyclerView = findViewById(R.id.recyclerView); // 실제 RecyclerView ID로 수정
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // DB에서 로드
        loadDataFromDatabase();

        // 어댑터 설정
        adapter = new ItemAdapter(itemList, new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Item item) {
                // 클릭된 항목의 ID를 DetailActivity로 전달
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("ITEM_ID", item.getId());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    /**
     * DB에서 감상평 불러오기
     */
    private void loadDataFromDatabase() {
        itemList = new ArrayList<>();
        Cursor cursor = dbHelper.getAllBookReviews();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));
                String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String thumbnail = cursor.getString(cursor.getColumnIndexOrThrow("thumbnail"));
                float rating = cursor.getFloat(cursor.getColumnIndexOrThrow("rating"));

                Item item = new Item(id, title, thumbnail, author, date, rating, content);
                itemList.add(item);
            } while (cursor.moveToNext());
            cursor.close();
        }
    }
}
