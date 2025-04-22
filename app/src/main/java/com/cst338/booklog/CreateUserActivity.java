package com.cst338.booklog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.cst338.booklog.database.UserRepository;
import com.cst338.booklog.database.entities.User;
import com.cst338.booklog.databinding.ActivityCreateUserBinding;

import java.util.concurrent.atomic.AtomicBoolean;

public class CreateUserActivity extends AppCompatActivity {
    ActivityCreateUserBinding binding;
    UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userRepository = UserRepository.getRepository(getApplication());

        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });
    }

    private void createUser() {
        String username = binding.enterUsernameET.getText().toString();
        AtomicBoolean makeFinalToast = new AtomicBoolean(true);

        if(username.isEmpty()) {
            toastMaker("Error: Text entry for username is empty.");
            return;
        }

        LiveData<User> userObserver = userRepository.getUserByUserName(username);
        userObserver.observe(this, user -> {
            if(user == null){
                String password = binding.enterPasswordET.getText().toString();
                String cPassword = binding.confirmPasswordET.getText().toString();
                if(!password.isEmpty() && password.equals(cPassword)){
                    User tempUser = new User(username, password);
                    userRepository.insertUser(tempUser);
                    makeFinalToast.set(false);
                    toastMaker("Congrats! Your user account was created.");
                    startActivity(LoginPageActivity.loginIntentFactory(getApplication()));
                }else {
                    if(password.isEmpty()){
                        toastMaker("Error: Text entry for password is empty.");
                    } else {
                        toastMaker("Error: Passwords do not match.");
                        binding.enterPasswordET.setSelection(0);
                        binding.confirmPasswordET.setSelection(0);
                    }
                }
            }else{
                if(makeFinalToast.get()) {
                    toastMaker(String.format("ERROR!: %s is not available.", username));
                    binding.enterUsernameET.setSelection(0);
                }
            }
        });
    }

    private void toastMaker(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    static Intent createUserActivityIntentFactory(Context context) {
       return new Intent(context, CreateUserActivity.class);
    }
}