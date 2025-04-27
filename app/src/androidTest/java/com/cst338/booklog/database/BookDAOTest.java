package com.cst338.booklog.database;

import static org.junit.Assert.*;

import androidx.room.Insert;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.cst338.booklog.database.entities.Book;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class BookDAOTest {

    private BookLogDatabase database;
    private BookDAO bookDAO;

    @Before
    public void setUp() {
        database = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                BookLogDatabase.class
        ).allowMainThreadQueries().build();

        bookDAO = database.bookDAO();
    }

    @After
    public void tearDown() {
        database.close();
    }

    @Test
    public void testInsert() {
        Book book = new Book("Test Title", "Test Author", "Test Genre");
        bookDAO.insert(book);

        List<Book> books = bookDAO.getAllBooksSimple();
        assertEquals(1, books.size());
        assertEquals("Test Title", books.get(0).getTitle());
    }


    @Test
    public void testUpdate() {
        Book book = new Book("Original", "Author", "Genre");
        bookDAO.insert(book);

        List<Book> books = bookDAO.getAllBooksSimple();
        Book toUpdate = books.get(0);
        toUpdate.setTitle("Updated");
        bookDAO.update(toUpdate);

        Book updated = bookDAO.getBookByTitleSimple("Updated");
        assertNotNull(updated);
        assertEquals("Updated", updated.getTitle());
    }

    @Test
    public void testDelete() {
        // Insert
        Book book = new Book("To Delete", "Author", "Genre");
        bookDAO.insert(book);

        // Delete
        List<Book> books = bookDAO.getAllBooksSimple();
        bookDAO.delete(books.get(0));

        // Verify
        assertTrue(bookDAO.getAllBooksSimple().isEmpty());
    }
}