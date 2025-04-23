/**
 * Author: Sydney Stalker
 */
package com.cst338.booklog.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.cst338.booklog.database.entities.Book;
import com.cst338.booklog.database.entities.BookLog;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class BookRepository {
    private final BookDAO bookDAO;
    private final BookLogDAO bookLogDAO;
    private final ExecutorService executor;

    private static volatile BookRepository INSTANCE;

    public static BookRepository getRepository(Application application) {
        if (INSTANCE == null) {
            synchronized (BookRepository.class) {
                if (INSTANCE == null) {
                    BookLogDatabase db = BookLogDatabase.getDatabase(application);
                    INSTANCE = new BookRepository(db.bookDAO(), db.bookLogDAO());
                }
            }
        }
        return INSTANCE;
    }

    private BookRepository(BookDAO bookDAO, BookLogDAO bookLogDAO) {
        this.bookDAO = bookDAO;
        this.bookLogDAO = bookLogDAO;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Book>> getAllBooks() {
        return bookDAO.getAllBooks();
    }
}
