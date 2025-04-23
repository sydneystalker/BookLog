/**
 * Author: Sydney Stalker
 */
package com.cst338.booklog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cst338.booklog.database.BookAdapter;
import com.cst338.booklog.database.BookRepository;
import com.cst338.booklog.database.UserRepository;
import com.cst338.booklog.database.entities.Book;
import com.cst338.booklog.database.entities.BookLog;

import java.time.LocalDateTime;
import java.util.List;


public class BookListActivity extends AppCompatActivity {
    private ActivityBookListBinding binding;
    private BookRepository bookRepo;
    private UserRepository userRepo;
    private BookAdapter adapter;
    private String username;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bookRepo = BookRepository.getRepository(getApplication());
        userRepo = UserRepository.getRepository(getApplication());

        username = getIntent().getStringExtra("USERNAME");

        userRepo.getUserByUsername(username).observe(this, user -> {
            if (user != null) {
                userId = user.getId();
                setupRecyclerView();
                setupAddButton();
            }
        });

    }

    private void setupRecyclerView() {
        adapter = new BookAdapter(this::onMarkAsFinished);
        binding.bookListRecycler.setAdapter(adapter);
        binding.bookListRecycler.setLayoutManager(new LinearLayoutManager(this));

        bookRepo.getUnreadBooksByUser(userId).observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                adapter.submitList(books);
            }
        });
    }

    private void setupAddButton() {
        binding.addBookButton.setOnClickListener(v -> {
            String title = binding.bookTitleInput.getText().toString().trim();
            String author = binding.bookAuthorInput.getText().toString().trim();
            String genre = binding.bookGenreInput.getText().toString().trim();

            if (!title.isEmpty() && !author.isEmpty() && !genre.isEmpty()) {
                Book newBook = new Book(0, title, author, genre);
                bookRepo.insertBook(newBook, insertedBookId -> {
                    BookLog bookLog = new BookLog(userId, insertedBookId, false, null);
                    bookRepo.insertBookLog(bookLog);
                });

                binding.bookTitleInput.setText("");
                binding.bookAuthorInput.setText("");
                binding.bookGenreInput.setText("");
            }
        });
    }

    private void onMarkAsFinished(Book book) {
        bookRepo.getBookLogByUserAndBook(userId, book.getBookId()).observe(this, bookLog -> {
            if (bookLog != null) {
                bookLog.setFinished(true);
                bookLog.setFinishDate(LocalDateTime.now());
                bookRepo.updateBookLog(bookLog);
            }
        });
    }

    public static Intent intentFactory(Context context, String username) {
        Intent intent = new Intent(context, BookListActivity.class);
        intent.putExtra("USERNAME", username);
        return intent;
    }
}
