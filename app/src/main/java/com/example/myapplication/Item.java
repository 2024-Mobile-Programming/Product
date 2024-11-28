package com.example.myapplication;

public class Item {
    private long id; // DB PRIMARY KEY
    private final String title;
    private final String thumbnail; // 이미지 리소스 ID
    private final String author; // 저자 - null 가능
    private final String date;
    private final float rating;
    private final String content;

    // 생성자 (id 포함)
    public Item(long id, String title, String thumbnail, String author, String date, float rating, String content) {
        this.id = id;
        this.title = title;
        this.thumbnail = thumbnail;
        this.author = author;
        this.date = date;
        this.rating = rating;
        this.content = content;
    }

    // 생성자 (id 미포함, 새로운 항목 생성 시 사용)
    public Item(String title, String thumbnail, String author, String date, float rating, String content) {
        this.id = -1; // 초기값
        this.title = title;
        this.thumbnail = thumbnail;
        this.author = author;
        this.date = date;
        this.rating = rating;
        this.content = content;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) { // ID 설정
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public float getRating() {
        return rating;
    }

    public String getContent() {
        return content;
    }
}
