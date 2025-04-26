/**
 * Author: Sydney Stalker
 */
package com.cst338.booklog;

import android.content.Context;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cst338.booklog.database.BookLogDAO;
import com.cst338.booklog.database.BookLogDatabase;
import com.cst338.booklog.database.entities.BookLog;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class BookLogTest {
    private BookLogDatabase database;
    private BookLogDAO bookLogDao;
    private final int TEST_USER_ID = 1;
    private final int TEST_BOOK_ID = 100;
    private final int TEST_BOOK_ID_2 = 101;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        database = Room.inMemoryDatabaseBuilder(context, BookLogDatabase.class)
                .allowMainThreadQueries()
                .build();
        bookLogDao = database.bookLogDAO();
    }

    @After
    public void closeDb() {
        database.close();
    }

    /**
     * Test Insert Book Log
     */
    @Test
    public void testInsertBookLog() {
        BookLog bookLog = new BookLog(TEST_USER_ID, TEST_BOOK_ID, false);
        bookLogDao.insert(bookLog);

        List<BookLog> allRecords = bookLogDao.getAllRecords();
        assertEquals(1, allRecords.size());
        assertEquals(TEST_USER_ID, allRecords.get(0).getUserId());
    }

    /**
     * Test update
     */
    @Test
    public void testUpdateIsFinished() {
        BookLog bookLog = new BookLog(TEST_USER_ID, TEST_BOOK_ID, false);
        bookLogDao.insert(bookLog);

        List<BookLog> allRecords = bookLogDao.getAllRecords();
        BookLog toUpdate = allRecords.get(0);
        toUpdate.setFinished(true);
        bookLogDao.insert(toUpdate);

        List<BookLog> updatedRecords = bookLogDao.getAllRecords();
        assertTrue(updatedRecords.get(0).isFinished());
    }

    /**
     * Test Delete Book Logs for Book
     */
    @Test
    public void testDeleteBookLogsForBook() {
        BookLog bookLog1 = new BookLog(TEST_USER_ID, TEST_BOOK_ID, false);
        BookLog bookLog2 = new BookLog(TEST_USER_ID, TEST_BOOK_ID_2, true);
        bookLogDao.insert(bookLog1);
        bookLogDao.insert(bookLog2);

        bookLogDao.deleteBookLogsForBook(TEST_BOOK_ID);

        List<BookLog> remaining = bookLogDao.getAllRecords();
        assertEquals(1, remaining.size());
        assertEquals(TEST_BOOK_ID_2, remaining.get(0).getBookId());
    }

    /**
     * Test Delete All Logs for User
     */
    @Test
    public void testDeleteAllLogsForUser() {
        BookLog bookLog1 = new BookLog(TEST_USER_ID, TEST_BOOK_ID, false);
        BookLog bookLog2 = new BookLog(TEST_USER_ID + 1, TEST_BOOK_ID_2, true);
        bookLogDao.insert(bookLog1);
        bookLogDao.insert(bookLog2);

        bookLogDao.deleteAllLogsForUser(TEST_USER_ID);

        List<BookLog> remaining = bookLogDao.getAllRecords();
        assertEquals(1, remaining.size());
        assertEquals(TEST_USER_ID + 1, remaining.get(0).getUserId());
    }

    /**
     * Test deleteAllBookLogs
     */
    @Test
    public void testDeleteAllBookLogs() {
        bookLogDao.insert(new BookLog(TEST_USER_ID, TEST_BOOK_ID, false));
        bookLogDao.insert(new BookLog(TEST_USER_ID, TEST_BOOK_ID_2, true));

        bookLogDao.deleteAllBookLogs();

        List<BookLog> allRecords = bookLogDao.getAllRecords();
        assertEquals(0, allRecords.size());
    }

    /**
     * Test equals and hashCode
     */
    @Test
    public void testEqualsAndHashCode() {
        BookLog log1 = new BookLog(TEST_USER_ID, TEST_BOOK_ID, false);
        BookLog log2 = new BookLog(TEST_USER_ID, TEST_BOOK_ID, false);

        log1.setId(1);
        log2.setId(1);

        assertEquals(log1, log2);
        assertEquals(log1.hashCode(), log2.hashCode());

        log2.setFinished(true);
        assertNotEquals(log1, log2);
    }
}