/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.testing.notes.statistics;

import android.content.Intent;

import com.example.android.testing.notes.Injection;
import com.example.android.testing.notes.R;
import com.example.android.testing.notes.data.Note;
import com.example.android.testing.notes.data.NotesRepository;
import com.example.android.testing.notes.util.EspressoIdlingResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;

/**
 * Tests for the statistics screen.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class StatisticsScreenTest {

    /**
     * {@link ActivityTestRule} is a JUnit {@link Rule @Rule} to launch your activity under test.
     *
     * <p>
     * Rules are interceptors which are executed for each test method and are important building
     * blocks of Junit tests.
     */
    @Rule
    public ActivityTestRule<StatisticsActivity> mStatisticsActivityTestRule =
            new ActivityTestRule<>(StatisticsActivity.class, false, false);

    @Before
    public void prepareRepository() throws Throwable {
        mStatisticsActivityTestRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                NotesRepository repo = Injection.provideNotesRepository();
                repo.deleteAllNotes();
                repo.saveNote(new Note("St1", "", false));
                repo.saveNote(new Note("St2", "", true));
            }
        });

        Intent intent = new Intent(ApplicationProvider.getApplicationContext(),
                StatisticsActivity.class);

        mStatisticsActivityTestRule.launchActivity(intent);
    }

    /**
     * Prepare your test fixture for this test. In this case we register an IdlingResources with
     * Espresso. IdlingResource resource is a great way to tell Espresso when your app is in an
     * idle state. This helps Espresso to synchronize your test actions, which makes tests
     * significantly more reliable.
     */
    @Before
    public void registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource());
    }

    /**
     * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
     */
    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getIdlingResource());
    }

    @Test
    public void notes_ShowsNonEmptyMessage() {
        // Check that the active and archived notes text is displayed
        String expectedActiveNoteText = String.format(ApplicationProvider.getApplicationContext()
                .getString(R.string.statistics_active_notes), 1);
        onView(withText(containsString(expectedActiveNoteText))).check(matches(isDisplayed()));
        String expectedArchivedNoteText = String.format(ApplicationProvider.getApplicationContext()
                .getString(R.string.statistics_archived_notes), 1);
        onView(withText(containsString(expectedArchivedNoteText))).check(matches(isDisplayed()));
    }
}
