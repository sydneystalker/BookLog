package com.cst338.booklog.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.cst338.booklog.database.entities.User;
import java.util.List;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User... user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM " + BookLogDatabase.USER_TABLE+ " ORDER BY username")
    LiveData<List<User>> getAllUsers();

    @Query("DELETE from " + BookLogDatabase.USER_TABLE)
    void deleteAll();

    @Query("SELECT * from " + BookLogDatabase.USER_TABLE + " WHERE username == :username")
    LiveData<User> getUserByUserName(String username);

    @Query("SELECT * from " + BookLogDatabase.USER_TABLE + " WHERE id == :userId")
    LiveData<User> getUserByUserId(int userId);
}