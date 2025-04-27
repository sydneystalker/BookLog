package com.cst338.booklog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import android.content.Context;
import android.content.Intent;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CreateUserPageActivityTest {
    @Test
    public void testCreateUserActivityIntentFactory() {
        Context context = ApplicationProvider.getApplicationContext();

        Intent intent = CreateUserActivity.createUserActivityIntentFactory(context);

        assertNotNull("intended: not null", intent);
        assertEquals("intended: point to CreateUserActivity",
                CreateUserActivity.class.getName(), intent.getComponent().getClassName());
    }
}
