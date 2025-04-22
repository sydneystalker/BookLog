/**
 * Author: Sydney Stalker
 */
package com.cst338.booklog;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cst338.booklog.database.BookAdapter;
import com.cst338.booklog.database.BookViewModel;

import java.util.ArrayList;

public class BookListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BookAdapter adapter;
    private BookViewModel bookViewModel;
    private Spinner genreSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list_activity);

        recyclerView = findViewById(R.id.bookRecyclerView);
        genreSpinner = findViewById(R.id.genreSpinner);

        adapter = new BookAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);

        setupSpinner();
    }

    private void setupSpinner() {
        String[] genres = {"All", "Non-Fiction", "Fantasy", "Romance", "Mystery", "Science Fiction", "Horror"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genres);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreSpinner.setAdapter(spinnerAdapter);

        genreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String selectedGenre = parent.getItemAtPosition(pos).toString();
                if (selectedGenre.equals("All")) {
                    bookViewModel.getAllBooks().observe(BookListActivity.this, adapter::setBooks);
                } else {
                    bookViewModel.getBooksByGenre(selectedGenre).observe(BookListActivity.this, adapter::setBooks);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}

