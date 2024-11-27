package com.example.myapplication;

public class Item {
    private final String title;
    private final String content;
    private final String author;
    private final String date;
    private final int imageResId;  // 이미지 리소스 ID로 처리
    private final float rating;

    public Item(String title, String content, String author, String date, int imageResId, float rating) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.date = date;
        this.imageResId = imageResId;
        this.rating = rating;
    }


    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public int getImage() {
        return imageResId;  // 이미지 리소스 ID 반환
    }

    public float getRating() {
        return rating;
    }
}
