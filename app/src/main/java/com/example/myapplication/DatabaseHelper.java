package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // DB 정보
    private static final String DATABASE_NAME = "reviews.db";
    private static final int DATABASE_VERSION = 2;

    // table 이름
    private static final String TABLE_ITEMS = "items";

    // column 이름
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_AUTHOR = "author";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_RATING = "rating";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_IMAGE_URI = "imageUri";

    // table 생성 sql
    private static final String CREATE_TABLE_ITEMS = "CREATE TABLE " + TABLE_ITEMS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TITLE + " TEXT,"
            + COLUMN_AUTHOR + " TEXT,"
            + COLUMN_DATE + " TEXT,"
            + COLUMN_RATING + " REAL,"
            + COLUMN_CONTENT + " TEXT,"
            + COLUMN_IMAGE_URI + " TEXT"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ITEMS);
    }

    // Schema 변경 시 호출
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }

    // Insert
    public long insertItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, item.getTitle());
        values.put(COLUMN_AUTHOR, item.getAuthor());
        values.put(COLUMN_DATE, item.getDate());
        values.put(COLUMN_RATING, item.getRating());
        values.put(COLUMN_CONTENT, item.getContent());
        values.put(COLUMN_IMAGE_URI, item.getImageUri());

        long id = db.insert(TABLE_ITEMS, null, values);
        db.close();
        return id;
    }

    // 모든 Item 가져오기
    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_ITEMS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
                String author = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUTHOR));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
                float rating = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_RATING));
                String content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT));
                String imageUri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URI));

                Item item = new Item(title, author, date, rating, content, imageUri);
                item.setId(id);
                items.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return items;
    }

    // Update Item
    public int updateItem(long id, Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, item.getTitle());
        values.put(COLUMN_AUTHOR, item.getAuthor());
        values.put(COLUMN_DATE, item.getDate());
        values.put(COLUMN_RATING, item.getRating());
        values.put(COLUMN_CONTENT, item.getContent());
        values.put(COLUMN_IMAGE_URI, item.getImageUri());

        return db.update(TABLE_ITEMS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    // Delete
    public void deleteItem(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
}
