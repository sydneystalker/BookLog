package com.cst338.booklog.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.cst338.booklog.database.BookLogDatabase;
import java.util.Objects;

@Entity (tableName = BookLogDatabase.BOOK_LOG_TABLE)
public class BookLog {
    @PrimaryKey (autoGenerate = true)
    private int id;
    private int userId;
    private int bookId;
    private boolean isFinished;

    public BookLog(int userId, int bookId, boolean isFinished) {
        this.userId = userId;
        this.bookId = bookId;
        this.isFinished = isFinished;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BookLog bookLog = (BookLog) o;
        return id == bookLog.id && userId == bookLog.userId && bookId == bookLog.bookId && isFinished == bookLog.isFinished;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, bookId, isFinished);
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
}
