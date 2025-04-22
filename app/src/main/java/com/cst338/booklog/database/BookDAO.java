package com.cst338.booklog.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cst338.booklog.database.entities.Book;

import java.util.List;

@Dao
public interface BookDAO {
    @Insert
    void insert(Book book);

    @Update
    void update(Book book);

    @Delete
    void delete(Book book);

    @Query("SELECT * FROM " + BookLogDatabase.BOOK_TABLE)
    LiveData<List<Book>> getAllBooks();

    @Query("SELECT * FROM " + BookLogDatabase.BOOK_TABLE + " WHERE genre == :genre")
    LiveData<List<Book>> getBooksByGenre(String genre);
}