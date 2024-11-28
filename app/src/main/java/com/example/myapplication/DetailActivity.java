package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.database.BookDatabaseHelper;

public class DetailActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    EditText title, content, author, date;
    ImageView thumbnail;
    RatingBar ratingBar;
    Button saveButton, deleteButton, plusButton, minusButton;

    BookDatabaseHelper dbHelper;
    long itemId = -1; // 전달받은 항목의 ID
    String currentImageUri = ""; // 현재 이미지 URI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail); // 레이아웃 파일이 존재해야 함

        // 뷰 초기화
        title = findViewById(R.id.book_title);
        content = findViewById(R.id.book_description);
        author = findViewById(R.id.book_author);
        date = findViewById(R.id.date);
        ratingBar = findViewById(R.id.ratingBar1);
        thumbnail = findViewById(R.id.book_image);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);
        plusButton = findViewById(R.id.plusButton);
        minusButton = findViewById(R.id.minusButton);

        // 데이터베이스 헬퍼 초기화
        dbHelper = new BookDatabaseHelper(this);

        // 인텐트로부터 ITEM_ID 받기
        if (getIntent() != null && getIntent().hasExtra("ITEM_ID")) {
            itemId = getIntent().getLongExtra("ITEM_ID", -1);
            if (itemId != -1) {
                fetchAndRenderData(itemId);
            } else {
                Toast.makeText(this, "잘못된 항목 ID입니다.", Toast.LENGTH_SHORT).show();
                finish(); // 활동 종료
            }
        } else {
            Toast.makeText(this, "항목 ID가 전달되지 않았습니다.", Toast.LENGTH_SHORT).show();
            finish(); // 활동 종료
        }

        // +버튼과 -버튼의 기능 설정
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float newRating = ratingBar.getRating() + ratingBar.getStepSize();
                if (newRating <= ratingBar.getNumStars()) {
                    ratingBar.setRating(newRating);
                }
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float newRating = ratingBar.getRating() - ratingBar.getStepSize();
                if (newRating >= 0) {
                    ratingBar.setRating(newRating);
                }
            }
        });

        // Save 버튼의 기능 설정
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 데이터 유효성 검사
                if (title.getText().toString().trim().isEmpty()) {
                    title.setError("제목을 입력하세요.");
                    title.requestFocus();
                    return;
                }

                // 데이터베이스에 업데이트
                String updatedTitle = title.getText().toString().trim();
                String updatedContent = content.getText().toString().trim();
                String updatedAuthor = author.getText().toString().trim();
                String updatedDate = date.getText().toString().trim();
                float updatedRating = ratingBar.getRating();
                String updatedThumbnail = currentImageUri.isEmpty() ? "" : currentImageUri;

                // 업데이트를 별도의 스레드에서 실행
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int rowsAffected = dbHelper.updateBookReview(
                                itemId,
                                updatedTitle,
                                updatedThumbnail,
                                updatedAuthor.isEmpty() ? null : updatedAuthor,
                                updatedDate,
                                updatedRating,
                                updatedContent
                        );

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (rowsAffected > 0) {
                                    Toast.makeText(DetailActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(DetailActivity.this, "저장에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).start();
            }
        });

        // Delete 버튼의 기능 설정
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 삭제를 확인하는 다이얼로그를 추가하는 것이 좋습니다.
                // 여기서는 단순히 삭제를 실행합니다.

                // 삭제를 별도의 스레드에서 실행
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int rowsDeleted = dbHelper.deleteBookReview(itemId);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (rowsDeleted > 0) {
                                    Toast.makeText(DetailActivity.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                    finish(); // 활동 종료
                                } else {
                                    Toast.makeText(DetailActivity.this, "삭제에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).start();
            }
        });

        // ImageView 클릭 시 갤러리에서 이미지 선택
        thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openImageChooser();
            }
        });
    }

    /**
     * 갤러리에서 이미지를 선택하기 위한 인텐트 실행 메소드
     */
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // 이미지 타입만 선택하도록 설정
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요"), PICK_IMAGE_REQUEST);
    }

    /**
     * 갤러리에서 이미지를 선택한 후 호출되는 메소드
     *
     * @param requestCode 요청 코드
     * @param resultCode  결과 코드
     * @param data        인텐트 데이터
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 이미지 선택 결과 처리
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            currentImageUri = selectedImageUri.toString();

            // Glide를 사용하여 선택한 이미지 로드
            Glide.with(this)
                    .load(selectedImageUri)
                    .placeholder(R.drawable.default_image) // 기본 이미지 설정
                    .into(thumbnail);
        }
    }

    /**
     * 데이터베이스에서 항목을 조회하고 UI에 표시하는 메소드
     *
     * @param id 조회할 항목의 ID
     */
    private void fetchAndRenderData(long id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = dbHelper.getBookReviewById(id);
                if (cursor != null && cursor.moveToFirst()) {
                    // 컬럼 인덱스 가져오기
                    int titleIndex = cursor.getColumnIndex("title");
                    int contentIndex = cursor.getColumnIndex("content");
                    int authorIndex = cursor.getColumnIndex("author");
                    int dateIndex = cursor.getColumnIndex("date");
                    int ratingIndex = cursor.getColumnIndex("rating");
                    int thumbnailIndex = cursor.getColumnIndex("thumbnail");

                    // 인덱스가 유효한지 확인
                    if (titleIndex == -1 || contentIndex == -1 || dateIndex == -1 || ratingIndex == -1 || thumbnailIndex == -1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(DetailActivity.this, "데이터베이스 컬럼 오류.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                        cursor.close();
                        return;
                    }

                    // 데이터 가져오기
                    String fetchedTitle = cursor.getString(titleIndex);
                    String fetchedContent = cursor.getString(contentIndex);
                    String fetchedAuthor = cursor.isNull(authorIndex) ? "" : cursor.getString(authorIndex);
                    String fetchedDate = cursor.getString(dateIndex);
                    float fetchedRating = cursor.getFloat(ratingIndex);
                    String fetchedThumbnail = cursor.getString(thumbnailIndex);

                    cursor.close();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // UI에 데이터 설정
                            title.setText(fetchedTitle);
                            content.setText(fetchedContent);
                            author.setText(fetchedAuthor);
                            date.setText(fetchedDate);
                            ratingBar.setRating(fetchedRating);

                            if (fetchedThumbnail != null && !fetchedThumbnail.isEmpty()) {
                                Uri imageUri = Uri.parse(fetchedThumbnail);
                                Glide.with(DetailActivity.this)
                                        .load(imageUri)
                                        .placeholder(R.drawable.default_image)
                                        .into(thumbnail);
                            } else {
                                thumbnail.setImageResource(R.drawable.default_image);
                            }
                        }
                    });
                }
            }
        }).start();
    }
}
