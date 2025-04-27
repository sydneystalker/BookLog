package com.cst338.booklog;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.cst338.booklog.database.UserDAO;
import com.cst338.booklog.database.UserTestDatabase;
import com.cst338.booklog.database.entities.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class UserTest {
    private UserDAO userDAO;
    private UserTestDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, UserTestDatabase.class).build();
        userDAO = db.userDAO();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void insertUserTest(){
        User testUser = new User("Tester", "password");

        List<User> userList = userDAO.getAllUsers();
        assert(userList.isEmpty());
        userDAO.insert(testUser);
        userList = userDAO.getAllUsers();
        assert(userList.size() == 1);
    }

    @Test
    public void updateUserTest() throws Exception
    {
        User testUser = new User("Tester", "password");
        userDAO.insert(testUser);
        User listUser = getOrAwaitValue(userDAO.getUserByUserName("Tester"));
        listUser.setUserName("BetaTester");
        userDAO.update(listUser);

        List<User> userList = userDAO.getAllUsers();
        assertEquals(userList.get(0).getUserName(), "BetaTester");
    }

    @Test
    public void deleteUserTest() throws Exception
    {
        User testUser = new User("Tester", "password");
        userDAO.insert(testUser);

        User listUser = getOrAwaitValue(userDAO.getUserByUserName("Tester"));
        userDAO.delete(listUser);

        List<User> userList = userDAO.getAllUsers();
        assertEquals(userList.size(), 0);
    }

    public static <T> T getOrAwaitValue(final LiveData<T> liveData) throws InterruptedException {
        final Object[] data = new Object[1];
        CountDownLatch latch = new CountDownLatch(1);

        new Handler(Looper.getMainLooper()).post(() -> {
            liveData.observeForever(value -> {
                data[0] = value;
                latch.countDown();
            });
        });
        latch.await(2, TimeUnit.SECONDS);

        return (T) data[0];
    }
}
