package com.cst338.booklog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class AdminPageActivityTest {

    @Test
    public void testAdminPageActivityIntentFactory() {
        Context context = ApplicationProvider.getApplicationContext();
        int testUserId = 123;

        Intent intent = AdminPageActivity.adminPageActivityIntentFactory(context, testUserId);

        assertNotNull("Intent should not be null", intent);
        assertEquals("Intent should point to AdminPageActivity",
                AdminPageActivity.class.getName(),
                intent.getComponent().getClassName());

        Bundle extras = intent.getExtras();
        assertNotNull("Intent should have extras", extras);
        assertTrue("Intent should contain user ID",
                extras.containsKey("com.cst338.booklog.ADMIN_PAGE_ACTIVITY_USER_ID"));
        assertEquals("User ID should be correctly passed in intent",
                testUserId,
                extras.getInt("com.cst338.booklog.ADMIN_PAGE_ACTIVITY_USER_ID", -1));
    }
}