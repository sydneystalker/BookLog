package com.cst338.booklog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.cst338.booklog.database.BookLogRepository;
import com.cst338.booklog.database.UserRepository;
import com.cst338.booklog.database.entities.User;
import com.cst338.booklog.databinding.ActivityAdminPageBinding;

import kotlin.jvm.functions.Function1;

public class AdminPageActivity extends AppCompatActivity {
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
        loginUser(savedInstanceState);

        if(user != null){
            Toast.makeText(this,user.getUserName() + " is logged in.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loginUser(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);

        loggedInUserID = sharedPreferences.getInt(getString(R.string.preference_userId_key), LOGGED_OUT);

        if(loggedInUserID == LOGGED_OUT && savedInstanceState != null && savedInstanceState.containsKey(SAVED_INSTANCE_STATE_USERID_KEY)) {
            loggedInUserID = savedInstanceState.getInt(SAVED_INSTANCE_STATE_USERID_KEY, LOGGED_OUT);
        }
        if(loggedInUserID == LOGGED_OUT){
            loggedInUserID = getIntent().getIntExtra(ADMIN_PAGE_ACTIVITY_USER_ID, LOGGED_OUT);
        }
        if(loggedInUserID == LOGGED_OUT){
            return;
        }

        LiveData<User> userObserver = userRepository.getUserByUserId(loggedInUserID);
        user = getUserFromLiveData();
        userObserver.observe(this, user -> {
            this.user = user;
            if(this.user != null){
                invalidateOptionsMenu();
            }
        });
    }

    public User getUserFromLiveData() {
        LiveData<User> userLiveData = userRepository.getUserByUserId(loggedInUserID);
        return userLiveData.getValue();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_INSTANCE_STATE_USERID_KEY, loggedInUserID);
        updateSharedPreference();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.logout_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        MenuItem item = menu.findItem(R.id.logoutMenuItem);
//        item.setVisible(true);
//        if(user == null){
//            return false;
//        }
//        item.setTitle(user.getUserName());
//        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
//            @Override
//            public boolean onMenuItemClick(@NonNull MenuItem item){
//                showLogoutDialog();
//                return false;
//            }
//        });
//        return true;
//    }

    private void showLogoutDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AdminPageActivity.this);
        final AlertDialog alertDialog = alertBuilder.create();

        alertBuilder.setMessage("Logout?");
        alertBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });

        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alertBuilder.create().show();
    }

    private void logout() {
        loggedInUserID = LOGGED_OUT;
        updateSharedPreference();
        getIntent().putExtra(ADMIN_PAGE_ACTIVITY_USER_ID, LOGGED_OUT);

        startActivity(LoginPageActivity.loginIntentFactory(getApplicationContext()));
    }

    private void updateSharedPreference(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putInt(getString(R.string.preference_userId_key), loggedInUserID);
        sharedPrefEditor.apply();
    }

    static Intent adminPageActivityIntentFactory(Context context, int userId) {
        Intent intent = new Intent(context, AdminPageActivity.class);
        intent.putExtra(ADMIN_PAGE_ACTIVITY_USER_ID, userId);
        return intent;
    }
}