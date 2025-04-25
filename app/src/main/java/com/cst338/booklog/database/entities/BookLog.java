package com.cst338.booklog.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.cst338.booklog.database.BookLogDatabase;

import java.time.LocalDateTime;

@Entity (tableName = BookLogDatabase.BOOK_LOG_TABLE)

public class BookLog {
    @PrimaryKey (autoGenerate = true)
    private int id;
    private int userId;
    private int bookId;
    private boolean isFinished;
    private LocalDateTime dateFinished;


    public BookLog(int userId, int bookId, boolean isFinished) {
        this.userId = userId;
        this.bookId = bookId;
        this.isFinished = isFinished;

        if(isFinished){
            dateFinished = LocalDateTime.now();
        } else {
            dateFinished = null;
        }
    }

    public int getId(){

        return id;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
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
