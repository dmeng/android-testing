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

package com.example.android.testing.notes.addeditnote;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;
import android.content.res.Resources;
import android.view.View;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.android.testing.notes.Injection;
import com.example.android.testing.notes.R;
import com.example.android.testing.notes.data.Note;
import com.example.android.testing.notes.data.NotesRepository;
import com.example.android.testing.notes.util.EspressoIdlingResource;
import com.example.android.testing.notes.util.TestUtils;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;


/**
 * Tests for the add note screen.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddEditNoteScreenTest {
    //
    // private static final String NOTE_ID = "1";
    //
    // @Rule
    // public ActivityTestRule<AddEditNoteActivity> mActivityTestRule =
    //         new ActivityTestRule<>(AddEditNoteActivity.class, false, false);
    //
    // /**
    //  * Prepare your test fixture for this test. In this case we register an IdlingResources with
    //  * Espresso. IdlingResource resource is a great way to tell Espresso when your app is in an
    //  * idle state. This helps Espresso to synchronize your test actions, which makes tests
    //  * significantly more reliable.
    //  */
    // @Before
    // public void registerIdlingResource() {
    //     IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource());
    // }
    //
    // /**
    //  * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
    //  */
    // @After
    // public void unregisterIdlingResource() {
    //     IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getIdlingResource());
    // }
    //
    // @Test
    // public void emptyNote_isNotSaved() {
    //     // Launch activity to add a new note
    //     launchNewNoteActivity(null);
    //
    //     // Add invalid title and description combination
    //     onView(withId(R.id.add_note_title)).perform(clearText());
    //     onView(withId(R.id.add_note_description)).perform(clearText());
    //     // Try to save the note
    //     onView(withId(R.id.fab_save_note)).perform(click());
    //
    //     // Verify that the activity is still displayed (a correct note would close it).
    //     onView(withId(R.id.add_note_title)).check(matches(isDisplayed()));
    //
    //     // TODO Also test for the Snackbar message?
    // }
    //
    // @Test
    // public void toolbarTitle_newNote_persistsRotation() {
    //     // Launch activity to add a new note
    //     launchNewNoteActivity(null);
    //
    //     // Check that the toolbar shows the correct title
    //     onView(withId(R.id.toolbar)).check(matches(withToolbarTitle(R.string.add_note)));
    //
    //     // Rotate activity
    //     TestUtils.rotateOrientation(mActivityTestRule.getActivity());
    //
    //     // Check that the toolbar title is persisted
    //     onView(withId(R.id.toolbar)).check(matches(withToolbarTitle(R.string.add_note)));
    // }
    //
    // @Test
    // public void toolbarTitle_editNote_persistsRotation() throws Throwable {
    //     mActivityTestRule.runOnUiThread(new Runnable() {
    //         @Override
    //         public void run() {
    //             NotesRepository repository = Injection.provideNotesRepository();
    //             repository.deleteAllNotes();
    //             repository.saveNote(
    //                     new Note(NOTE_ID, "AddTitle", "", null, false)
    //             );
    //         }
    //     });
    //     launchNewNoteActivity(NOTE_ID);
    //
    //     // Check that the toolbar shows the correct title
    //     onView(withId(R.id.toolbar)).check(matches(withToolbarTitle(R.string.edit_note)));
    //
    //     // Rotate activity
    //     TestUtils.rotateOrientation(mActivityTestRule.getActivity());
    //
    //     // check that the toolbar title is persisted
    //     onView(withId(R.id.toolbar)).check(matches(withToolbarTitle(R.string.edit_note)));
    // }
    //
    // /**
    //  * @param noteId is null if used to add a new note, otherwise it edits the note.
    //  */
    // private void launchNewNoteActivity(@Nullable String noteId) {
    //     Intent intent = new Intent(ApplicationProvider.getApplicationContext(),
    //             AddEditNoteActivity.class);
    //
    //     intent.putExtra(AddEditNoteFragment.ARGUMENT_EDIT_NOTE_ID, noteId);
    //     mActivityTestRule.launchActivity(intent);
    // }
    //
    // /**
    //  * Matches the toolbar title with a specific string resource.
    //  *
    //  * @param resourceId the ID of the string resource to match
    //  */
    // public static Matcher<View> withToolbarTitle(final int resourceId) {
    //     return new BoundedMatcher<View, Toolbar>(Toolbar.class) {
    //
    //         @Override
    //         public void describeTo(Description description) {
    //             description.appendText("with toolbar title from resource id: ");
    //             description.appendValue(resourceId);
    //         }
    //
    //         @Override
    //         protected boolean matchesSafely(Toolbar toolbar) {
    //             CharSequence expectedText = "";
    //             try {
    //                 expectedText = toolbar.getResources().getString(resourceId);
    //             } catch (Resources.NotFoundException ignored) {
    //                 /* view could be from a context unaware of the resource id. */
    //             }
    //             CharSequence actualText = toolbar.getTitle();
    //             return expectedText.equals(actualText);
    //         }
    //     };
    // }
}
