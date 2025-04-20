package com.cst338.booklog;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.cst338.booklog.database.UserRepository;
import com.cst338.booklog.databinding.ActivityLoginPageBinding;

public class LoginPageActivity extends AppCompatActivity {
    private ActivityLoginPageBinding binding;
    private UserRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}