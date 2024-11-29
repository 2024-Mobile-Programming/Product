package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {
    EditText titleEditText, authorEditText;
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

            titleEditText.setText(title);
            authorEditText.setText(author);
        } else {
            // 데이터 추가 - 삭제 버튼 숨김
            deleteButton.setVisibility(View.GONE);
        }

        // 저장 버튼
        saveButton.setOnClickListener(v -> {
            String newTitle = titleEditText.getText().toString().trim();
            String newAuthor = authorEditText.getText().toString().trim();

            if (!isNewItem) {
                // 존재하던 item - db update
                Item updatedItem = new Item(newTitle, newAuthor);
                updatedItem.setId(itemId);
                dbHelper.updateItem(itemId, updatedItem);
            } else {
                // 새로운 item - db insert
                Item newItem = new Item(newTitle, newAuthor);
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
