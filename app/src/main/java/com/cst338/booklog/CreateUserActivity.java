package com.cst338.booklog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cst338.booklog.databinding.ActivityCreateUserBinding;

public class CreateUserActivity extends AppCompatActivity {
    ActivityCreateUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    static Intent createUserActivityIntentFactory(Context context) {
       return new Intent(context, CreateUserActivity.class);
    }
}