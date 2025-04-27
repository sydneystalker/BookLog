package com.cst338.booklog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;

public class AddBookActivityTest {
    @Test
    public void testAddUnreadBookActivityIntentFactory() {
        Context context = ApplicationProvider.getApplicationContext();
        int testUserId = 666;

        Intent intent = AddBookActivity.addUnreadBookActivityIntentFactory(context, testUserId);

        assertNotNull("Intent should not be null", intent);
        assertEquals("Intent should point to AddBookActivity",
                AddBookActivity.class.getName(),
                intent.getComponent().getClassName());

        Bundle extras = intent.getExtras();
        assertNotNull("Intent should have extras", extras);
        assertTrue("Intent should contain user ID", extras.containsKey("com.cst338.booklog.ADD_BOOK_ACTIVITY_USER_ID"));
        assertEquals("User ID should be correctly passed in intent", testUserId,
                extras.getInt("com.cst338.booklog.ADD_BOOK_ACTIVITY_USER_ID", -1));
        assertTrue("Intent should contain Boolean", extras.containsKey("com.cst338.booklog.ADD_BOOK_ACTIVITY_IS_FINISHED"));
        assertEquals("Boolean should be false, as the Book is not finished", false,
                extras.getBoolean("com.cst338.booklog.ADD_BOOK_ACTIVITY_IS_FINISHED", false));
    }

    @Test
    public void testAddReadBookIntentFactory() {
        Context context = ApplicationProvider.getApplicationContext();
        int testUserId = 666;

        Intent intent = AddBookActivity.addReadBookActivityIntentFactory(context, testUserId);

        assertNotNull("Intent should not be null", intent);
        assertEquals("Intent should point to AddBookActivity",
                AddBookActivity.class.getName(),
                intent.getComponent().getClassName());

        Bundle extras = intent.getExtras();
        assertNotNull("Intent should have extras", extras);
        assertTrue("Intent should contain user ID", extras.containsKey("com.cst338.booklog.ADD_BOOK_ACTIVITY_USER_ID"));
        assertEquals("User ID should be correctly passed in intent", testUserId,
                extras.getInt("com.cst338.booklog.ADD_BOOK_ACTIVITY_USER_ID", -1));
        assertTrue("Intent should contain Boolean", extras.containsKey("com.cst338.booklog.ADD_BOOK_ACTIVITY_IS_FINISHED"));
        assertEquals("Boolean should be false, as the Book is not finished", true,
                extras.getBoolean("com.cst338.booklog.ADD_BOOK_ACTIVITY_IS_FINISHED", false));
    }
}
