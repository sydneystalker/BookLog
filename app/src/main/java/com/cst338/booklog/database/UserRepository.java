package com.cst338.booklog.database;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.LiveData;
import com.cst338.booklog.database.entities.User;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class UserRepository {
    private UserDAO userDAO;
    private ArrayList<User> allUsers;
    private static UserRepository repository;

    private UserRepository(Application application) {
        BookLogDatabase db = BookLogDatabase.getDatabase(application);
        this.userDAO = db.userDAO();
        this.allUsers = (ArrayList<User>) this.userDAO.getAllUsers();
    }

    public static UserRepository getRepository (Application application){
        if(repository != null){
            return repository;
        }
        Future<UserRepository> future = BookLogDatabase.databaseWriteExecutor.submit(
                new Callable<UserRepository>() {
                    @Override
                    public UserRepository call() throws Exception {
                        return new UserRepository(application);
                    }
                }
        );
        try{
            return future.get();
        } catch (InterruptedException | ExecutionException e){
            Log.d("BOOKLOG", "Problem getting GymLogRepository, thread error.");
        }
        return null;
    }

    public ArrayList<User> getAllUsers() {
        Future<ArrayList<User>> future = BookLogDatabase.databaseWriteExecutor.submit(
                new Callable<ArrayList<User>>() {
                    @Override
                    public ArrayList<User> call() throws Exception {
                        return (ArrayList<User>) userDAO.getAllUsers();
                    }
                }
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e){
            Log.i("BOOKLOG", "Problem when getting all BookLogs in the repository");
        }
        return null;
    }

    public void insertUser(User... user){
        BookLogDatabase.databaseWriteExecutor.execute(()->{
            userDAO.insert(user);
        });
    }

    public LiveData<User> getUserByUserName(String username) {
        return userDAO.getUserByUserName(username);
    }

    public LiveData<User> getUserByUserId(int userId) {
        return userDAO.getUserByUserId(userId);
    }

    public void updateUser(User user) {
        BookLogDatabase.databaseWriteExecutor.execute(() -> {
            userDAO.update(user);
        });
    }

    public void deleteUser(User user) {
        BookLogDatabase.databaseWriteExecutor.execute(() -> {
            userDAO.delete(user);
        });
    }
}
