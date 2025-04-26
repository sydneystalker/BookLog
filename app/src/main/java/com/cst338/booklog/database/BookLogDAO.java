package com.cst338.booklog.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cst338.booklog.database.entities.Book;
import com.cst338.booklog.database.entities.BookLog;

import java.util.List;

@Dao
public interface BookLogDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BookLog bookLog);

    @Query("Select * from " + BookLogDatabase.BOOK_LOG_TABLE + " order by userId DESC")
    List<BookLog> getAllRecords();

    @Query("Select * from " + BookLogDatabase.BOOK_LOG_TABLE + " WHERE userId = :loggedInUserId")
    LiveData<List<BookLog>> getRecordsetUserIdLiveData(int loggedInUserId);

    @Query("DELETE FROM " + BookLogDatabase.BOOK_TABLE + " WHERE id = :bookId")
    void deleteBook(int bookId);

    @Query("DELETE FROM " + BookLogDatabase.BOOK_LOG_TABLE + " WHERE bookId = :bookId")
    void deleteBookLogsForBook(int bookId);

    @Query("DELETE FROM " + BookLogDatabase.BOOK_LOG_TABLE + " WHERE userId = :userId")
    void deleteAllLogsForUser(int userId);

    @Query("DELETE FROM " + BookLogDatabase.BOOK_TABLE + " WHERE id IN " +
            "(SELECT bookId FROM " + BookLogDatabase.BOOK_LOG_TABLE + " WHERE userId = :userId)")
    void deleteAllBooksForUser(int userId);

    @Query("DELETE FROM " + BookLogDatabase.BOOK_LOG_TABLE)
    void deleteAllBookLogs();

    @Query("DELETE FROM " + BookLogDatabase.BOOK_TABLE)
    void deleteAllBooks();

    @Query("DELETE FROM " + BookLogDatabase.USER_TABLE + " WHERE isAdmin = 0")
    void deleteAllNonAdminUsers();

    @Query("SELECT * FROM " + BookLogDatabase.BOOK_LOG_TABLE + " WHERE userId = :userId & isFinished == true ")
    LiveData<List<BookLog>> getFinishedBooksByUser(int userId);
}
