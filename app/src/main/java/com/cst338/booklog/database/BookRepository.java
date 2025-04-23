/**
 * Author: Sydney Stalker
 */
package com.cst338.booklog.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class BookRepository {
    private final BookDAO bookDAO;

    public BookRepository(Application application) {
        BookLogDatabase db = BookLogDatabase.getDatabase(application);
        bookDAO = db.bookDAO();
    }

    public static BookRepository getRepository(Application application) {
    }

    public LiveData<List<Book>> getAllBooks() {
        return bookDAO.getAllBooks();
    }

    public LiveData<List<Book>> getBooksByGenre(String genre) {
        return bookDAO.getBooksByGenre(genre);
    }

    public LiveData<Object> getUnreadBooksByUser(String username) {
    }
}
