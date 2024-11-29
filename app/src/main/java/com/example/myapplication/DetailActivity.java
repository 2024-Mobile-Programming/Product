package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class DetailActivity extends AppCompatActivity {
    EditText titleEditText, authorEditText, dateEditText, contentEditText;
    RatingBar ratingBar;
    ImageView thumbnail;
    Button selectImageButton, saveButton, deleteButton;
    DatabaseHelper dbHelper;

    long itemId = -1;
    boolean isNewItem = true;
    String imageUriString = "";

    // 이미지 선택 관련
    private ActivityResultLauncher<Intent> selectImageLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        titleEditText = findViewById(R.id.book_title);
        authorEditText = findViewById(R.id.book_author);
        dateEditText = findViewById(R.id.date);
        contentEditText = findViewById(R.id.book_description);
        ratingBar = findViewById(R.id.ratingBar1);
        thumbnail = findViewById(R.id.thumbnail);
        selectImageButton = findViewById(R.id.selectImageButton);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);

        // init
        dbHelper = new DatabaseHelper(this);
        selectImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            imageUriString = selectedImageUri.toString();
                            // Glide를 사용하여 이미지 로드
                            Glide.with(this)
                                    .load(selectedImageUri)
                                    .placeholder(R.drawable.default_image) // 로딩 전 표시할 이미지
                                    .error(R.drawable.default_image) // 로딩 실패 시 표시할 이미지
                                    .into(thumbnail);
                        }
                    }
                }
        );
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        openGallery();
                    } else {
                        Toast.makeText(this, "권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

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
            imageUriString = intent.getStringExtra("imageUri");

            titleEditText.setText(title);
            authorEditText.setText(author);
            dateEditText.setText(date);
            ratingBar.setRating(rating);
            contentEditText.setText(content);
            if (imageUriString != null && !imageUriString.isEmpty()) {
                Uri imageUri = Uri.parse(imageUriString);
                Glide.with(this)
                        .load(imageUri)
                        .placeholder(R.drawable.default_image)
                        .error(R.drawable.default_image)
                        .into(thumbnail);
            }
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

        // 이미지 선택 버튼
        selectImageButton.setOnClickListener(v -> {
            // 권한 확인
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // 권한 요청
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
                openGallery();
            }
        });

        // 저장 버튼
        saveButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString().trim();
            String author = authorEditText.getText().toString().trim();
            String date = dateEditText.getText().toString().trim();
            float rating = ratingBar.getRating();
            String content = contentEditText.getText().toString().trim();
            // imageUriString

            // 이미지 선택되지 않은 경우
            if (imageUriString == null || imageUriString.isEmpty()) {
                imageUriString = "";
            }

            if (!isNewItem) {
                // 존재하던 item - db update
                Item updatedItem = new Item(title, author, date, rating, content, imageUriString);
                updatedItem.setId(itemId);
                dbHelper.updateItem(itemId, updatedItem);
            } else {
                // 새로운 item - db insert
                Item newItem = new Item(title, author, date, rating, content, imageUriString);
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

    // 갤러리 열기
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        selectImageLauncher.launch(intent);
    }
}
