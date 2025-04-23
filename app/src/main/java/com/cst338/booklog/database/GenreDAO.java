package com.cst338.booklog.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.cst338.booklog.database.entities.Genre;

@Dao
public interface GenreDAO {
    @Insert
    void insert(Genre fiction);

    @Query("DELETE FROM " + BookLogDatabase.GENRE_TABLE)
    void deleteAll();
}