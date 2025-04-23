/**
 * Author: Sydney Stalker
 */
package com.cst338.booklog.database;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cst338.booklog.database.entities.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private List<Book> books = new ArrayList<>();
    private final Consumer<Book> onBookClick;

    public BookAdapter(Consumer<Book> onBookClick) {
        this.onBookClick = onBookClick;
    }

    public void submitList(List<Book> newBooks) {
        this.books = newBooks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        ((TextView) holder.itemView.findViewById(android.R.id.text1)).setText(book.getTitle());
        ((TextView) holder.itemView.findViewById(android.R.id.text2)).setText(book.getGenre());

        holder.itemView.setOnClickListener(v -> onBookClick.accept(book));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
