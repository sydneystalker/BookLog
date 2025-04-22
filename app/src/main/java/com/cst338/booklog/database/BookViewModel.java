/**
 * Author: Sydney Stalker
 */
package com.cst338.booklog.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cst338.booklog.database.entities.Book;

import java.util.List;

public class BookViewModel extends AndroidViewModel {
    private BookRepository repository;

    public BookViewModel(@NonNull Application application) {
        super(application);
        repository = new BookRepository(application);
    }

    public LiveData<List<Book>> getAllBooks() {
        return repository.getAllBooks();
    }

    public LiveData<List<Book>> getBooksByGenre(String genre) {
        return repository.getBooksByGenre(genre);
    }
}

