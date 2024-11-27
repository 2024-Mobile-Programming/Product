package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {
    EditText title, content, author, date;
    ImageView thumbnail;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        title = findViewById(R.id.book_title);
        content = findViewById(R.id.book_description);
        author = findViewById(R.id.book_author);
        date = findViewById(R.id.date);
        ratingBar = findViewById(R.id.ratingBar1);
        thumbnail = findViewById(R.id.book_image);
        Button clearButton = findViewById(R.id.clearButton);
        Button plusButton = findViewById(R.id.plusButton);
        Button minusButton = findViewById(R.id.minusButton);

        Intent intent = getIntent();
        title.setText(intent.getStringExtra("title"));
        content.setText(intent.getStringExtra("content"));
        author.setText(intent.getStringExtra("author"));
        date.setText(intent.getStringExtra("date"));
        ratingBar.setRating(intent.getFloatExtra("rating", 0.0f));
        thumbnail.setImageResource(intent.getIntExtra("thumbnail", 0));

        //+버튼과 -버튼의 기능입니다. step size는 0.5로 했습니다.
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingBar.setRating(ratingBar.getRating()+ratingBar.getStepSize());
            }
        });
        minusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ratingBar.setRating(ratingBar.getRating()-ratingBar.getStepSize());
            }
        });


        // 삭제 버튼
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // EditText 내용 지우기
                title.setText("");
                content.setText("");
                author.setText("");
                date.setText("");
            }
        });
    }
}
