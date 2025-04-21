package com.cst338.booklog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.cst338.booklog.database.UserRepository;
import com.cst338.booklog.database.entities.User;
import com.cst338.booklog.databinding.ActivityLoginPageBinding;

public class LoginPageActivity extends AppCompatActivity {
    private ActivityLoginPageBinding binding;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userRepository = UserRepository.getRepository(getApplication());

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyUser();
            }
        });

        binding.createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CreateUserActivity.createUserActivityIntentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
    }

    private void verifyUser() {
        String username = binding.usernameInputEditText.getText().toString();

        if(username.isEmpty()){
            toastMaker("Username should not be blank");
            return;
        }

        LiveData<User> userObserver = userRepository.getUserByUserName(username);
        userObserver.observe(this, user -> {
            if(user != null){
                String password = binding.passwordInputEditText.getText().toString();
                if(password.equals(user.getPassword())){
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(MainActivity.SHARED_PREFERENCE_USERID_KEY, Context.MODE_PRIVATE);
                    SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
                    sharedPrefEditor.putInt(MainActivity.SHARED_PREFERENCE_USERID_KEY, user.getId());
                    sharedPrefEditor.apply();
                    startActivity(UserPageActivity.userPageActivityIntentFactory(getApplicationContext(), user.getId()));
                }else {
                    toastMaker("Invalid Password");
                    binding.passwordInputEditText.setSelection(0);
                }
            }else{
                toastMaker(String.format("%s is not a valid username.", username));
                binding.usernameInputEditText.setSelection(0);
            }
        });
    }

    private void toastMaker(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    static Intent loginIntentFactory(Context context){
        return new Intent(context, LoginPageActivity.class);
    }
}