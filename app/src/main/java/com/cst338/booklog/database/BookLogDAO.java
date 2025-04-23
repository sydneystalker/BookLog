package com.cst338.booklog.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cst338.booklog.database.entities.BookLog;

import java.util.List;

@Dao
public interface BookLogDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BookLog bookLog);

    @Query("Select * from " + BookLogDatabase.BOOK_LOG_TABLE + " order by userId DESC")
    List<BookLog> getAllRecords();

    @Query("Select * from " + BookLogDatabase.BOOK_LOG_TABLE + " WHERE userId = :loggedInUserId order by dateFinished DESC")
    LiveData<List<BookLog>> getRecordsetUserIdLiveData(int loggedInUserId);
}
