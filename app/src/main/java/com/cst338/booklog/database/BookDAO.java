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

    @Query("DELETE from " + BookLogDatabase.BOOK_TABLE)
    void deleteAllBooks();

    @Query("SELECT * FROM " + BookLogDatabase.BOOK_TABLE + " ORDER by author DESC")
    LiveData<List<Book>> getAllBooks();

    @Query("SELECT * FROM " + BookLogDatabase.BOOK_TABLE + " WHERE genre == :bookGenre")
    LiveData<List<Book>> getBooksByGenre(String bookGenre);

    @Query("SELECT * FROM " + BookLogDatabase.BOOK_TABLE + " WHERE title == :bookTitle")
    LiveData<Book> getBookByTitle(String bookTitle);

    @Query("SELECT * FROM " + BookLogDatabase.BOOK_TABLE + " WHERE id == :bookId")
    LiveData<Book> getBookById(int bookId);

    @Query("SELECT * FROM " + BookLogDatabase.BOOK_TABLE)
    List<Book> getAllBooksSimple();

    @Query("SELECT * FROM " + BookLogDatabase.BOOK_TABLE + " WHERE title = :title")
    Book getBookByTitleSimple(String title);
}