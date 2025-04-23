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


public class BookListActivity extends AppCompatActivity {
    private ActivityBookListBinding binding;
    private BookRepository bookRepo;
    private BookAdapter adapter;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bookRepo = BookRepository.getRepository(getApplication());
        username = getIntent().getStringExtra("USERNAME");

        setupRecyclerView();
        setupAddButton();
    }

    private void setupRecyclerView() {
        adapter = new BookAdapter(this::onMarkAsFinished);
        binding.bookListRecycler.setAdapter(adapter);
        binding.bookListRecycler.setLayoutManager(new LinearLayoutManager(this));

        bookRepo.getUnreadBooksByUser(username).observe(this, adapter::submitList);
    }

    private void setupAddButton() {
        binding.addBookButton.setOnClickListener(v -> {
            String title = binding.bookTitleInput.getText().toString().trim();
            if (!title.isEmpty()) {
                Book newBook = new Book(username, title, false, null);
                bookRepo.insertBook(newBook);
                binding.bookTitleInput.setText(""); // clear field
            }
        });
    }

    private void onMarkAsFinished(Book book) {
        book.isFinished = true;
        book.dateFinished = LocalDate.now();
        bookRepo.updateBook(book);
    }

    public static Intent intentFactory(Context context, String username) {
        Intent intent = new Intent(context, BookListActivity.class);
        intent.putExtra("USERNAME", username);
        return intent;
    }
}
