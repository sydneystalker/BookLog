package com.cst338.booklog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.cst338.booklog.database.BookLogRepository;
import com.cst338.booklog.database.UserRepository;
import com.cst338.booklog.database.entities.User;
import com.cst338.booklog.databinding.ActivityAdminPageBinding;

public class AdminPageActivity extends AppCompatActivity {

    private boolean usernameCheckInProgress = false;

    private static final String ADMIN_PAGE_ACTIVITY_USER_ID = "com.cst338.booklog.ADMIN_PAGE_ACTIVITY_USER_ID";
    private static final String SAVED_INSTANCE_STATE_USERID_KEY = "com.cst338.booklog.SAVED_INSTANCE_STATE_USERID_KEY";
    private ActivityAdminPageBinding binding;
    private UserRepository userRepository;
    private BookLogRepository bookLogRepository;
    private static final int LOGGED_OUT = -1;
    int loggedInUserID = -1;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userRepository = UserRepository.getRepository(getApplication());
        bookLogRepository = BookLogRepository.getRepository(getApplication());
        loginUser(savedInstanceState);

        if (user != null) {
            binding.usernameTextView.setText(user.getUserName());
            Toast.makeText(this, user.getUserName() + " is logged in.", Toast.LENGTH_SHORT).show();
        }

        setupButtons();
    }

    private void setupButtons() {
        binding.changeUsernameButton.setOnClickListener(v -> changeUsername());
        binding.changePasswordButton.setOnClickListener(v -> changePassword());
        binding.deleteUserButton.setOnClickListener(v -> deleteUser());
        binding.resetDatabaseButton.setOnClickListener(v -> resetDatabase());
    }

    private void changeUsername() {
        if (usernameCheckInProgress) return;

        String newUsername = binding.usernameEditText.getText().toString().trim();
        if (newUsername.isEmpty()) {
            toastMaker("Username cannot be empty");
            return;
        }

        usernameCheckInProgress = true;

        userRepository.getUserByUserName(newUsername).observe(this, existingUser -> {
            if (!usernameCheckInProgress) return;
            usernameCheckInProgress = false;

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

    private void changePassword() {
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

    private void deleteUser() {
        String usernameToDelete = binding.deleteUserEditText.getText().toString().trim();
        if (usernameToDelete.isEmpty()) {
            toastMaker("Please enter a username to delete");
            return;
        }

        if (usernameToDelete.equals(user.getUserName())) {
            toastMaker("Cannot delete yourself");
            return;
        }

        userRepository.getUserByUserName(usernameToDelete).observe(this, userToDelete -> {
            if (userToDelete == null) {
                toastMaker("User not found");
            } else {
                showDeleteConfirmationDialog(userToDelete);
            }
        });
    }

    private void showDeleteConfirmationDialog(User userToDelete) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete user " + userToDelete.getUserName() + " and all their data?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    bookLogRepository.deleteUserAndData(userToDelete);
                    toastMaker("User and all data deleted");
                    binding.deleteUserEditText.setText("");
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void resetDatabase() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Reset")
                .setMessage("Are you sure you want to reset the database? This will delete all non-admin users, books, and logs.")
                .setPositiveButton("Reset", (dialog, which) -> {
                    bookLogRepository.resetDatabase();
                    toastMaker("Database reset complete");
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void loginUser(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);

        loggedInUserID = sharedPreferences.getInt(getString(R.string.preference_userId_key), LOGGED_OUT);

        if (loggedInUserID == LOGGED_OUT && savedInstanceState != null && savedInstanceState.containsKey(SAVED_INSTANCE_STATE_USERID_KEY)) {
            loggedInUserID = savedInstanceState.getInt(SAVED_INSTANCE_STATE_USERID_KEY, LOGGED_OUT);
        }
        if (loggedInUserID == LOGGED_OUT) {
            loggedInUserID = getIntent().getIntExtra(ADMIN_PAGE_ACTIVITY_USER_ID, LOGGED_OUT);
        }
        if (loggedInUserID == LOGGED_OUT) {
            return;
        }

        LiveData<User> userObserver = userRepository.getUserByUserId(loggedInUserID);
        userObserver.observe(this, user -> {
            this.user = user;
            if (this.user != null) {
                binding.usernameTextView.setText(user.getUserName());
            }
        });
    }



    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_INSTANCE_STATE_USERID_KEY, loggedInUserID);
        updateSharedPreference();
    }

    private void updateSharedPreference() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putInt(getString(R.string.preference_userId_key), loggedInUserID);
        sharedPrefEditor.apply();
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    static Intent adminPageActivityIntentFactory(Context context, int userId) {
        Intent intent = new Intent(context, AdminPageActivity.class);
        intent.putExtra(ADMIN_PAGE_ACTIVITY_USER_ID, userId);
        return intent;
    }
}