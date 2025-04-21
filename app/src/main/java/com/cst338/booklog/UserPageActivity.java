package com.cst338.booklog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cst338.booklog.databinding.ActivityUserPageBinding;

public class UserPageActivity extends AppCompatActivity {
    private static final String USER_PAGE_ACTIVITY_USER_ID = "com.cst338.booklog.USER_PAGE_ACTIVITY_USER_ID";
    ActivityUserPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    static Intent userPageActivityIntentFactory(Context context, int userId) {
        Intent intent = new Intent(context, UserPageActivity.class);
        intent.putExtra(USER_PAGE_ACTIVITY_USER_ID, userId);
        return intent;
    }
}