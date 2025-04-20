package com.cst338.booklog.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.cst338.booklog.database.entities.BookLog;
import com.cst338.booklog.MainActivity;
import com.cst338.booklog.database.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class BookLogRepository {

    private final BookLogDAO bookLogDAO;
    private final UserDAO userDAO;
    private static BookLogRepository repository;

    private BookLogRepository(Application application) {
        BookLogDatabase db = BookLogDatabase.getDatabase(application);
        this.bookLogDAO = db.bookLogDAO();
        this.userDAO = db.userDAO();
        ArrayList<BookLog> allLogs = (ArrayList<BookLog>) this.bookLogDAO.getAllRecords();
    }

    public static BookLogRepository getRepository(Application application) {
        if (repository != null) {
            return repository;
        }
        Future<BookLogRepository> future = BookLogDatabase.databaseWriteExecutor.submit(
                new Callable<BookLogRepository>() {
                    @Override
                    public BookLogRepository call() throws Exception {
                        return new BookLogRepository(application);
                    }
                }
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.d("BOOKLOG", "Problem getting BookLogRepository, thread error.");
        }
        return null;
    }

    public ArrayList<BookLog> getAllLogs() {
        Future<ArrayList<BookLog>> future = BookLogDatabase.databaseWriteExecutor.submit(
                new Callable<ArrayList<BookLog>>() {
                    @Override
                    public ArrayList<BookLog> call() throws Exception {
                        return (ArrayList<BookLog>) bookLogDAO.getAllRecords();
                    }
                }
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Log.i("BOOKLOG", "Problem when getting all BookLog in the repository.");
        }
        return null;
    }

    public void insertBookLog(BookLog bookLog) {
        BookLogDatabase.databaseWriteExecutor.execute(() ->
        {
            bookLogDAO.insert(bookLog);
        });
    }

    public void insertUser(User... user) {
        BookLogDatabase.databaseWriteExecutor.execute(() ->
        {
            userDAO.insert(user);
        });
    }

    public LiveData<User> getUserByUsername(String username) {
        return userDAO.getUserByUserName(username);
    }

    public LiveData<User> getUserByUserId(int userId) {
        return userDAO.getUserByUserId(userId);
    }

    public LiveData<List<BookLog>> getAllLogsByUserIdLiveData(int loggedInUserId) {
        return bookLogDAO.getRecordsetUserIdLiveData(loggedInUserId);
    }

}
