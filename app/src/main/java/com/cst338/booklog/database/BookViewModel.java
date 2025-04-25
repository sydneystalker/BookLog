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
        repository = BookRepository.getRepository(getApplication());
    }

    public LiveData<List<Book>> getAllBooks() {
        return repository.getAllBooks();
    }


}

