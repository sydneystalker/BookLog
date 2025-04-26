package com.cst338.booklog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import com.cst338.booklog.database.BookLogRepository;
import com.cst338.booklog.database.BookRepository;
import com.cst338.booklog.database.UserRepository;
import com.cst338.booklog.database.entities.Book;
import com.cst338.booklog.database.entities.BookLog;
import com.cst338.booklog.databinding.ActivityAddBookBinding;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class AddBookActivity extends AppCompatActivity {
    private static final String ADD_BOOK_ACTIVITY_USER_ID = "com.cst338.booklog.ADD_BOOK_ACTIVITY_USER_ID";
    private static final String ADD_BOOK_ACTIVITY_IS_FINISHED = "com.cst338.booklog.ADD_BOOK_ACTIVITY_IS_FINISHED";
    ActivityAddBookBinding binding;
    UserRepository userRepo;
    BookRepository bookRepo;
    BookLogRepository bookLogRepo;
    int userId;
    Boolean isBookFinished;
    Book book;
    BookLog bookLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userRepo = UserRepository.getRepository(getApplication());
        bookRepo = BookRepository.getRepository(getApplication());
        bookLogRepo = BookLogRepository.getRepository(getApplication());

        userId = getIntent().getIntExtra(ADD_BOOK_ACTIVITY_USER_ID, -1);
        isBookFinished = getIntent().getBooleanExtra(ADD_BOOK_ACTIVITY_IS_FINISHED, false);

        binding.addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBook();
            }
        });

        binding.addBookButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(book.getId() >= 0){
                    binding.bookSubmitStatus.setText("Ready to add!");
                    binding.bookSubmitStatus.setTextColor(getResources().getColor(R.color.green));
                }else{
                    binding.bookSubmitStatus.setText("Book is not ready for List");
                    binding.bookSubmitStatus.setTextSize(30);
                    binding.bookSubmitStatus.setTextColor(getResources().getColor(R.color.red));
                }
                return true;
            }
        });

        binding.addToListButton.setOnClickListener(v -> addBookLog());
    }

    private void addBookLog() {
        AtomicBoolean need = new AtomicBoolean(false);

        LiveData<List<BookLog>> bookObserver = bookLogRepo.getAllLogsByUserIdLiveData(userId);
        bookObserver.observe(this, bookLogs -> {
            for(BookLog bLog: bookLogs){
                if(bLog.getBookId() == book.getId()){
                    if(!need.get()) {
                        toastMaker("Book is already listed.");
                        need.set(true);
                        return;
                    }
                }
            }

            if(!need.get()){
                if(isBookFinished){
                    bookLog = new BookLog(userId, book.getId(),true);
                    bookLogRepo.insertBookLog(bookLog);
                    toastMaker("Congrats! The book was added to your 'Books Read' List");
                    need.set(true);
                }
                else{
                    bookLog = new BookLog(userId, book.getId(),false);
                    bookLogRepo.insertBookLog(bookLog);
                    toastMaker("Congrats! The book was added to your 'Reading' List");
                    need.set(true);
                }
            }
        });
    }

    public void addBook(){
        String title = binding.enterBookTitleET.getText().toString().trim();
        String author = binding.enterBookAuthorET.getText().toString().trim();
        String genre = binding.enterBookGenreET.getText().toString().trim();

        if(title.isEmpty() || author.isEmpty() || genre.isEmpty()) {
            toastMaker("Error: All text fields must be completed");
            return;
        }

        AtomicBoolean need = new AtomicBoolean(false);

        LiveData<Book> bookObserver = bookRepo.getBookByTitle(title);
        bookObserver.observe(this, book -> {
            this.book = book;
            if(book == null){
                this.book = new Book(title, author, genre);
                bookRepo.insertBook(this.book);
                need.set(true);
            }
            else{
                if(author.equalsIgnoreCase(book.getAuthor()))
                {
                    if(!need.get()) {
                        toastMaker("Book is already in the database");
                    }
                }
                else{
                    this.book = new Book(title, author, genre);
                    bookRepo.insertBook(this.book);
                    need.set(true);
                }
            }
        });
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    static Intent addUnreadBookActivityIntentFactory(Context context, int userId) {
        Intent intent = new Intent(context, AddBookActivity.class);
        intent.putExtra(ADD_BOOK_ACTIVITY_USER_ID, userId);
        intent.putExtra(ADD_BOOK_ACTIVITY_IS_FINISHED, false);
        return intent;
    }

    static Intent addReadBookActivityIntentFactory(Context context, int userId) {
        Intent intent = new Intent(context, AddBookActivity.class);
        intent.putExtra(ADD_BOOK_ACTIVITY_USER_ID, userId);
        intent.putExtra(ADD_BOOK_ACTIVITY_IS_FINISHED, true);
        return intent;
    }
}