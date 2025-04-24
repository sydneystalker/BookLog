package com.cst338.booklog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.cst338.booklog.database.BookLogRepository;
import com.cst338.booklog.database.UserRepository;
import com.cst338.booklog.database.entities.User;
import com.cst338.booklog.databinding.ActivityUserPageBinding;

public class UserPageActivity extends AppCompatActivity {
    private static final String USER_PAGE_ACTIVITY_USER_ID = "com.cst338.booklog.USER_PAGE_ACTIVITY_USER_ID";
    private ActivityUserPageBinding binding;
    private UserRepository userRepository;
    private BookLogRepository bookLogRepository;
    private int loggedInUserId = -1;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userRepository = UserRepository.getRepository(getApplication());
        bookLogRepository = BookLogRepository.getRepository(getApplication());
        loggedInUserId = getIntent().getIntExtra(USER_PAGE_ACTIVITY_USER_ID, -1);

        loadUserData();
        setupButtons();
    }

    private void loadUserData() {
        userRepository.getUserByUserId(loggedInUserId).observe(this, user -> {
            this.user = user;
            if (user != null) {
                binding.usernameTextView.setText(user.getUserName());
            }
        });
    }

    private void setupButtons() {
        binding.updateUsernameButton.setOnClickListener(v -> updateUsername());
        binding.updatePasswordButton.setOnClickListener(v -> updatePassword());
        binding.deleteBookButton.setOnClickListener(v -> deleteBook());
        binding.deleteAllBooksButton.setOnClickListener(v -> deleteAllBooks());
        binding.deleteAccountButton.setOnClickListener(v -> deleteAccount());
        binding.viewReadingListButton.setOnClickListener(v -> viewReadingList());
        binding.viewBooksReadButton.setOnClickListener(v -> viewBooksRead());
    }

    private void updateUsername() {
        String newUsername = binding.usernameEditText.getText().toString().trim();
        if (newUsername.isEmpty()) {
            toastMaker("Username cannot be empty");
            return;
        }

        userRepository.getUserByUserName(newUsername).observe(this, existingUser -> {
            if (existingUser != null && existingUser.getId() != user.getId()) {
                toastMaker("Username already taken");
            } else {
                user.setUserName(newUsername);
                userRepository.updateUser(user);
                binding.usernameTextView.setText(newUsername);
                toastMaker("Username updated successfully");
                binding.usernameEditText.setText("");
            }
        });
    }

    private void updatePassword() {
        String newPassword = binding.passwordEditText.getText().toString().trim();
        if (newPassword.isEmpty()) {
            toastMaker("Password cannot be empty");
            return;
        }

        user.setPassword(newPassword);
        userRepository.updateUser(user);
        toastMaker("Password updated successfully");
        binding.passwordEditText.setText("");
    }

    private void deleteBook() {
        String bookIdStr = binding.bookToDeleteEditText.getText().toString().trim();
        if (bookIdStr.isEmpty()) {
            toastMaker("Please enter a book ID");
            return;
        }

        try {
            int bookId = Integer.parseInt(bookIdStr);
            bookLogRepository.deleteBookAndLogs(bookId, loggedInUserId);
            toastMaker("Book and associated logs deleted");
            binding.bookToDeleteEditText.setText("");
        } catch (NumberFormatException e) {
            toastMaker("Invalid book ID");
        }
    }

    private void deleteAllBooks() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete all your books and logs?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    bookLogRepository.deleteAllBooksAndLogsForUser(loggedInUserId);
                    toastMaker("All books and logs deleted");
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteAccount() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete your account and all data?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    bookLogRepository.deleteUserAndData(user);
                    toastMaker("Account deleted");
                    startActivity(LoginPageActivity.loginIntentFactory(getApplicationContext()));
                    finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void viewReadingList() {
        // Implement view reading list functionality
        toastMaker("View Reading List clicked");
    }

    private void viewBooksRead() {
        // Implement view books read functionality
        toastMaker("View Books Read clicked");
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    static Intent userPageActivityIntentFactory(Context context, int userId) {
        Intent intent = new Intent(context, UserPageActivity.class);
        intent.putExtra(USER_PAGE_ACTIVITY_USER_ID, userId);
        return intent;
    }
}