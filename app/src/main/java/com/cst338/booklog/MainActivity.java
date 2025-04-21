package com.cst338.booklog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.cst338.booklog.database.BookLogRepository;
import com.cst338.booklog.database.UserRepository;
import com.cst338.booklog.database.entities.User;
import com.cst338.booklog.databinding.ActivityLoginPageBinding;
import com.cst338.booklog.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final String MAIN_ACTIVITY_USER_ID ="com.cst338.booklog.MAIN_ACTIVITY_USER_ID";
    static final String SHARED_PREFERENCE_USERID_KEY = "com.cst338.booklog.SHARED_PREFERENCE_USERID_KEY";
    private static final int LOGGED_OUT = -1;
    private static final String SAVED_INSTANCE_STATE_USERID_KEY = "com.cst338.booklog.SAVED_INSTANCE_STATE_USERID_KEY";
    private ActivityMainBinding binding;
    private UserRepository userRepository;
    private BookLogRepository bookLogRepository;
    private User user;
    private int loggedInUserID =-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userRepository = UserRepository.getRepository(getApplication());

        loginUser(savedInstanceState);

        if(loggedInUserID == -1) {
            Intent intent = LoginPageActivity.loginIntentFactory(getApplicationContext());
            startActivity(intent);
        }
        updateSharedPreference();
    }

    private void loginUser(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);

        loggedInUserID = sharedPreferences.getInt(getString(R.string.preference_userId_key), LOGGED_OUT);

        if(loggedInUserID == LOGGED_OUT && savedInstanceState != null && savedInstanceState.containsKey(SAVED_INSTANCE_STATE_USERID_KEY)) {
            loggedInUserID = savedInstanceState.getInt(SAVED_INSTANCE_STATE_USERID_KEY, LOGGED_OUT);
        }
        if(loggedInUserID == LOGGED_OUT){
            loggedInUserID = getIntent().getIntExtra(MAIN_ACTIVITY_USER_ID, LOGGED_OUT);
        }
        if(loggedInUserID == LOGGED_OUT){
            return;
        }

        LiveData<User> userObserver = userRepository.getUserByUserId(loggedInUserID);
        userObserver.observe(this, user -> {
            this.user = user;
            if(this.user != null){
                invalidateOptionsMenu();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_INSTANCE_STATE_USERID_KEY, loggedInUserID);
        updateSharedPreference();
    }

    private void updateSharedPreference(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putInt(getString(R.string.preference_userId_key), loggedInUserID);
        sharedPrefEditor.apply();
    }
}