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
import com.cst338.booklog.database.BookLogRepository;
import com.cst338.booklog.database.BookRepository;
import com.cst338.booklog.database.UserRepository;
import com.cst338.booklog.database.entities.Book;
import com.cst338.booklog.databinding.BooksReadPageBinding;

import java.util.List;

public class BooksReadActivity extends AppCompatActivity {
    private static final String BOOKS_READ_ACTIVITY_USER_ID = "com.cst338.booklog.BOOKS_READ_ACTIVITY_USER_ID";
    private BooksReadPageBinding binding;
    private BookRepository bookRepo;
    private UserRepository userRepo;
    private BookLogRepository bookLogRepo;
    private String username;
    private int userId;
    private BookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = BooksReadPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bookRepo = BookRepository.getRepository(getApplication());
        userRepo = UserRepository.getRepository(getApplication());

        username = getIntent().getStringExtra("USERNAME");
        binding.booksReadHeader.setText("Books Read   " + username);
    }

//        userRepo.getUserByUserName(username).observe(this, user -> {
//            if (user != null) {
//                userId = user.getId();
//                setupRecyclerView();
//                loadFinishedBooks();
//            }
//        });

//        binding.addBookButton.setOnClickListener(v -> {
//            Intent intent = BookListActivity.intentFactory(this, username);
//            startActivity(intent);
//        });
//    }

//    private void setupRecyclerView() {
//        adapter = new BookAdapter(book -> {}); // No click action needed
//        binding.booksReadRecycler.setAdapter(adapter);
//        binding.booksReadRecycler.setLayoutManager(new LinearLayoutManager(this));
//    }

//    private void loadFinishedBooks() {
//        bookLogRepo.getFinishedBooksByUser(userId).observe(this, books -> {
//            adapter.submitList(books);
//        });
//    }

    public static Intent intentFactory(Context context, String username) {
        Intent intent = new Intent(context, BooksReadActivity.class);
        intent.putExtra("USERNAME", username);
        return intent;
    }

    public static Intent intentFactory(Context context, int userId) {
        Intent intent = new Intent(context, BooksReadActivity.class);
        intent.putExtra(BOOKS_READ_ACTIVITY_USER_ID, userId);
        return intent;
    }
}
