package com.cst338.booklog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;

import com.cst338.booklog.database.BookLogRepository;
import com.cst338.booklog.database.BookRepository;
import com.cst338.booklog.database.UserRepository;
import com.cst338.booklog.database.entities.Book;
import com.cst338.booklog.database.entities.BookLog;
import com.cst338.booklog.database.entities.User;
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
//        isBookFinished = true;

        binding.addToListButton.setOnClickListener(v -> addBook());
    }

    public void addBook(){
        String title = binding.enterBookTitleET.getText().toString().trim();
        String author = binding.enterBookAuthorET.getText().toString().trim();
        String genre = binding.enterBookGenreET.getText().toString().trim();

        AtomicBoolean makeFinalToast = new AtomicBoolean(true);
        AtomicBoolean makeNewBookLog = new AtomicBoolean(true);
        AtomicBoolean addNewBook = new AtomicBoolean(false);

        if(title.isEmpty() || author.isEmpty() || genre.isEmpty()) {
            toastMaker("Error: All text fields must be completed");
            return;
        }

        LiveData<Book> bookObserver = bookRepo.getBookByTitle(title);
        bookObserver.observe(this, book -> {
            if(book == null) {
                this.book = new Book(title, author, genre);
                addNewBook.set(true);
            }
            else{
                if(book.getAuthor().equalsIgnoreCase(author))
                {
                    toastMaker("Book exists within database.");
                    this.book = book;
                }
                else{
                    this.book = new Book(title, author, genre);
                    addNewBook.set(true);
                }
            }
        });

        LiveData<List<BookLog>> bookLogObserver = bookLogRepo.getAllLogsByUserIdLiveData(userId);
        bookLogObserver.observe(this, bookLogs -> {
            for(BookLog bookLog : bookLogs){
                if(bookLog.getBookId() == book.getId()){
                    makeNewBookLog.set(false);
                }
            }

            if(makeNewBookLog.get()){
                if(addNewBook.get()){
                    bookRepo.insertBook(book);
                }

                if(isBookFinished){
                    this.bookLog = new BookLog(userId, book.getId(), true);
                    makeFinalToast.set(false);
                   // bookLogRepo.insertBookLog(bookLog);
                    toastMaker("Congrats! The book was added to your Finished Books.");
                    startActivity(UserPageActivity.userPageActivityIntentFactory(getApplication(),userId));
//                    startActivity(BooksReadActivity.intentFactory(getApplication(), userId));
                }
                else{
                    this.bookLog = new BookLog(userId, book.getId(), false);
                    makeFinalToast.set(false);
                   // bookLogRepo.insertBookLog(bookLog);
                    toastMaker("Congrats! The book was added to your Reading List.");
                    startActivity(UserPageActivity.userPageActivityIntentFactory(getApplication(),userId));
//                    startActivity(BookListActivity.intentFactory(getApplication(), userId));
                }
            }else{
                if(makeFinalToast.get()){
                    toastMaker("Book is already logged.");
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