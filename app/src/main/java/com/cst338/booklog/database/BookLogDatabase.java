package com.cst338.booklog.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cst338.booklog.database.entities.Book;
import com.cst338.booklog.database.entities.BookLog;
import com.cst338.booklog.database.entities.User;
import com.cst338.booklog.database.typeConverters.LocalDateTypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@TypeConverters(LocalDateTypeConverters.class)
@Database(entities = {BookLog.class, User.class, Book.class}, version = 4, exportSchema = false)
public abstract class BookLogDatabase extends RoomDatabase {
    public static final String USER_TABLE = "userTable";
    public static final String BOOK_TABLE = "bookTable";
    private static final String DATABASE_NAME = "bookLogDatabase";
    public static final String BOOK_LOG_TABLE = "bookLogTable";
    private static volatile BookLogDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 1;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static BookLogDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BookLogDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    BookLogDatabase.class,
                                    DATABASE_NAME
                            )
                            .fallbackToDestructiveMigration()
                            .addCallback(addDefaultValues)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback addDefaultValues = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.i("BOOKLOG", "DATABASE CREATED!");
            databaseWriteExecutor.execute(() -> {
                UserDAO dao = INSTANCE.userDAO();
                dao.deleteAll();
                User admin = new User("admin1", "admin1");
                admin.setAdmin(true);
                dao.insert(admin);
                User testUser1 = new User("testuser1", "testuser1");
                dao.insert(testUser1);
                BookDAO bDao = INSTANCE.bookDAO();
                bDao.deleteAllBooks();
                Book book = new Book("testtitle1","testauthor1", "testgenre1");
                bDao.insert(book);
                Book book2 = new Book("testtitle2","testauthor2", "testgenre2");
                bDao.insert(book2);

                BookLogDAO lDao = INSTANCE.bookLogDAO();
                lDao.deleteAllBookLogs();
                BookLog bookLog = new BookLog(2, 1, true);
                lDao.insert(bookLog);
                BookLog bookLog2 = new BookLog(2, 2, false);
                lDao.insert(bookLog2);

            });
        }
    };

 public abstract BookDAO bookDAO();

 public abstract UserDAO userDAO();

 public abstract BookLogDAO bookLogDAO();
}
