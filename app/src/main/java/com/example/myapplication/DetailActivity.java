package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {
    EditText titleEditText, authorEditText, dateEditText, contentEditText;
    RatingBar ratingBar;
    Button saveButton, deleteButton;
    DatabaseHelper dbHelper;

    long itemId = -1;
    boolean isNewItem = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        titleEditText = findViewById(R.id.book_title);
        authorEditText = findViewById(R.id.book_author);
        dateEditText = findViewById(R.id.date);
        contentEditText = findViewById(R.id.book_description);
        ratingBar = findViewById(R.id.ratingBar1);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);

        dbHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        isNewItem = intent.getBooleanExtra("newItem", true);

        if (!isNewItem) {
            // 기존 데이터 수정
            itemId = intent.getLongExtra("id", -1);

            String title = intent.getStringExtra("title");
            String author = intent.getStringExtra("author");
            String date = intent.getStringExtra("date");
            float rating = intent.getFloatExtra("rating", 0.0f);
            String content = intent.getStringExtra("content");

            titleEditText.setText(title);
            authorEditText.setText(author);
            dateEditText.setText(date);
            ratingBar.setRating(rating);
            contentEditText.setText(content);
        } else {
            // 데이터 추가 - 삭제 버튼 숨김
            deleteButton.setVisibility(View.GONE);
        }

        // 레이팅바
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingBar.setRating(v);
            }
        });

        // 저장 버튼
        saveButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString().trim();
            String author = authorEditText.getText().toString().trim();
            String date = dateEditText.getText().toString().trim();
            float rating = ratingBar.getRating();
            String content = contentEditText.getText().toString().trim();

            if (!isNewItem) {
                // 존재하던 item - db update
                Item updatedItem = new Item(title, author, date, rating, content);
                updatedItem.setId(itemId);
                dbHelper.updateItem(itemId, updatedItem);
            } else {
                // 새로운 item - db insert
                Item newItem = new Item(title, author, date, rating, content);
                dbHelper.insertItem(newItem);
            }
            finish(); 
        });

        // 삭제 버튼
        deleteButton.setOnClickListener(v -> {
            if (!isNewItem && itemId != -1) {
                dbHelper.deleteItem(itemId);
            }
            finish();
        });
    }
}
