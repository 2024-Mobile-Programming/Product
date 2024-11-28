package com.example.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 책 감상평을 관리하는 SQLite 데이터베이스 헬퍼 클래스.
 * 이 클래스는 데이터베이스 테이블 생성, 관리, CRUD 작업을 처리합니다.
 */
public class BookDatabaseHelper extends SQLiteOpenHelper {

    // 데이터베이스와 테이블 정보
    private static final String DATABASE_NAME = "bookReviews.db";  // 데이터베이스 이름
    private static final int DATABASE_VERSION = 2;  // 데이터베이스 버전

    // 테이블 이름과 컬럼 이름 정의
    private static final String TABLE_NAME = "book_reviews";  // 테이블 이름
    private static final String COLUMN_ID = "id";  // 기본 키
    private static final String COLUMN_TITLE = "title";  // 제목
    private static final String COLUMN_THUMBNAIL = "thumbnail"; // 이미지 리소스 ID
    private static final String COLUMN_AUTHOR = "author";  // 저자 (null 가능)
    private static final String COLUMN_DATE = "date";  // 날짜
    private static final String COLUMN_RATING = "rating";  // 평점 (0.0 - 5.0)
    private static final String COLUMN_CONTENT = "content";  // 내용

    // 생성자
    public BookDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 데이터베이스가 처음 생성될 때 호출되는 메서드
    @Override
    public void onCreate(SQLiteDatabase db) {
        // book_reviews 테이블을 생성하는 SQL 문
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT NOT NULL, " +
                COLUMN_THUMBNAIL + " TEXT, " +
                COLUMN_AUTHOR + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_RATING + " REAL, " +
                COLUMN_CONTENT + " TEXT);";
        db.execSQL(CREATE_TABLE);  // SQL 문 실행
    }

    // DB 업그레이드 시 호출 (예: 스키마 변경)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 기존 테이블 삭제 후 새로 생성
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * 새로운 책 감상평을 데이터베이스에 추가하는 메서드
     *
     * @param title     제목
     * @param thumbnail 이미지 리소스 ID
     * @param author    저자 (null 가능)
     * @param date      날짜
     * @param rating    평점
     * @param content   내용
     * @return 새로 삽입된 행의 ID, 오류 발생 시 -1 반환
     */
    public long insertBookReview(String title, String thumbnail, String author, String date, float rating, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_THUMBNAIL, thumbnail);
        values.put(COLUMN_AUTHOR, author);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_RATING, rating);
        values.put(COLUMN_CONTENT, content);
        return db.insert(TABLE_NAME, null, values);  // 데이터 삽입 후 새 행의 ID 반환
    }

    /**
     * 기존의 책 감상평을 데이터베이스에서 수정하는 메서드.
     *
     * @param id        수정할 책 감상평의 ID
     * @param title     수정된 제목
     * @param thumbnail 수정된 이미지 리소스 ID
     * @param author    수정된 저자 (null 가능)
     * @param date      수정된 날짜
     * @param rating    수정된 평점
     * @param content   수정된 내용
     * @return 업데이트된 행 수
     */
    public int updateBookReview(long id, String title, String thumbnail, String author, String date, float rating, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_THUMBNAIL, thumbnail);
        values.put(COLUMN_AUTHOR, author);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_RATING, rating);
        values.put(COLUMN_CONTENT, content);
        // 특정 ID를 기준으로 행 업데이트
        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    /**
     * 책 감상평을 데이터베이스에서 삭제하는 메서드.
     *
     * @param id 삭제할 책 감상평의 ID
     * @return 삭제된 행 수
     */
    public int deleteBookReview(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // 특정 ID를 기준으로 행 삭제
        return db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    /**
     * 데이터베이스에 있는 모든 책 감상평을 조회하는 메서드.
     *
     * @return 모든 책 감상평을 포함하는 Cursor 객체
     */
    public Cursor getAllBookReviews() {
        SQLiteDatabase db = this.getReadableDatabase();
        // 테이블의 모든 행 선택
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    /**
     * 특정 ID를 기준으로 책 감상평을 조회하는 메서드.
     *
     * @param id 조회할 책 감상평의 ID
     * @return 책 감상평을 가리키는 Cursor 객체, 찾지 못한 경우 null 반환
     */
    public Cursor getBookReviewById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        // 특정 ID에 해당하는 행을 조회
        return db.query(TABLE_NAME, null, COLUMN_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null);
    }
}
