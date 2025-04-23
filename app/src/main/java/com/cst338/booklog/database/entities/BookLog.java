package com.cst338.booklog.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.cst338.booklog.database.BookLogDatabase;

import java.time.LocalDateTime;

@Entity (tableName = BookLogDatabase.BOOK_LOG_TABLE,
        foreignKeys = {@ForeignKey(
            entity = User.class,
            parentColumns = "id",
            childColumns = "userId",
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE),
        @ForeignKey(
            entity = Book.class,
            parentColumns = "id",
            childColumns = "bookId",
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE)})
public class BookLog {
    @PrimaryKey (autoGenerate = true)
    private int id;
    private int userId;
    private int bookId;
    LocalDateTime dateFinished;

    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public LocalDateTime getDateFinished() {
        return dateFinished;
    }

    public void setDateFinished(LocalDateTime dateFinished) {
        this.dateFinished = dateFinished;
    }
}
