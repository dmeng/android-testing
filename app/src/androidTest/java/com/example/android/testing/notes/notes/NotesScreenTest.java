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

package com.example.android.testing.notes.notes;

import static com.example.android.testing.notes.util.TestUtils.getCurrentActivity;
import static com.google.common.base.Preconditions.checkArgument;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.text.TextUtils;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.testing.notes.Injection;
import com.example.android.testing.notes.R;
import com.example.android.testing.notes.ViewModelFactory;
import com.example.android.testing.notes.util.EspressoIdlingResource;
import com.example.android.testing.notes.util.TestUtils;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.filters.SdkSuppress;
import androidx.test.rule.ActivityTestRule;

/**
 * Tests for the notes screen, the main screen which contains a grid of all notes.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class NotesScreenTest {

    private final static String TITLE1 = "TITLE1";

    private final static String DESCRIPTION = "DESCR";

    private final static String TITLE2 = "TITLE2";

    /**
     * {@link ActivityTestRule} is a JUnit {@link Rule @Rule} to launch your activity under test.
     * <p>
     * Rules are interceptors which are executed for each test method and are important building
     * blocks of Junit tests.
     */
    @Rule
    public ActivityTestRule<NotesActivity> mNotesActivityTestRule =
            new ActivityTestRule<>(NotesActivity.class);

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

    @After
    public void teardown() {
        ViewModelFactory.destroyInstance();
        Injection.provideNotesRepository().deleteAllNotes();
    }

    /**
     * A custom {@link Matcher} which matches an item in a {@link RecyclerView} by its text.
     *
     * <p>
     * View constraints:
     * <ul>
     * <li>View must be a child of a {@link RecyclerView}
     * <ul>
     *
     * @param itemText the text to match
     * @return Matcher that matches text in the given view
     */
    private Matcher<View> withItemText(final String itemText) {
        checkArgument(!TextUtils.isEmpty(itemText), "itemText cannot be null or empty");
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View item) {
                return allOf(
                        isDescendantOfA(isAssignableFrom(RecyclerView.class)),
                        withText(itemText)).matches(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is isDescendantOfA RV with text " + itemText);
            }
        };
    }

    @Test
    public void clickAddNoteButton_opensAddNoteUi() throws Exception {
        // Click on the add note button
        onView(withId(R.id.fab_add_note)).perform(click());

        // Check if the add note screen is displayed
        onView(withId(R.id.add_note_title)).check(matches(isDisplayed()));
    }

    @Test
    public void editNote() throws Exception {
        // First add a note
        createNote(TITLE1, DESCRIPTION);

        // Click on the note on the list
        onView(withText(TITLE1)).perform(click());

        // Click on the edit note button
        onView(withId(R.id.fab_edit_note)).perform(click());

        String editNoteTitle = TITLE2;
        String editNoteDescription = "New Description";

        // Edit note title and description
        onView(withId(R.id.add_note_title))
                .perform(replaceText(editNoteTitle), closeSoftKeyboard()); // Type new note title
        onView(withId(R.id.add_note_description)).perform(replaceText(editNoteDescription),
                closeSoftKeyboard()); // Type new note description and close the keyboard

        // Save the note
        onView(withId(R.id.fab_save_note)).perform(click());

        // Verify note is displayed on screen in the note list.
        onView(withItemText(editNoteTitle)).check(matches(isDisplayed()));

        // Verify previous note is not displayed
        onView(withItemText(TITLE1)).check(doesNotExist());
    }

    @Test
    public void addNoteToNotesList() throws Exception {
        String newNoteTitle = "Espresso";
        String newNoteDescription = "UI testing for Android";

        // Click on the add note button
        onView(withId(R.id.fab_add_note)).perform(click());

        // Add note title and description
        // Type new note title
        onView(withId(R.id.add_note_title)).perform(typeText(newNoteTitle), closeSoftKeyboard());
        onView(withId(R.id.add_note_description)).perform(typeText(newNoteDescription),
                closeSoftKeyboard()); // Type new note description and close the keyboard

        // Save the note
        onView(withId(R.id.fab_save_note)).perform(click());

        // Scroll notes list to added note, by finding its description
        onView(withId(R.id.notes_list)).perform(
                scrollTo(hasDescendant(withText(newNoteDescription))));

        // Verify note is displayed on screen
        onView(withItemText(newNoteDescription)).check(matches(isDisplayed()));
    }

    @Test
    @Ignore("Still need to figure out why we can't always click on the menu option")
    public void archiveAndRestoreNote() throws Exception {
        // First add a note
        createNote(TITLE1, DESCRIPTION);

        // Click on the note on the list
        onView(withText(TITLE1)).perform(click());

        // Archive the note
        Espresso.openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());
        onView(withText(R.string.menu_archive_note)).perform(click());

        // Confirm the note isn't shown with the active notes
        onView(withId(R.id.notes_list)).check(matches(not(isDisplayed())));

        // Now show archived notes
        Espresso.openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());
        onView(withText(R.string.menu_show_archive)).perform(click());

        // Scroll notes list to added note, by finding its description
        onView(withId(R.id.notes_list)).perform(
            scrollTo(hasDescendant(withText(DESCRIPTION))));

        // Verify note is displayed on screen
        onView(withItemText(DESCRIPTION)).check(matches(isDisplayed()));

        // Click on the note and restore it
        onView(withText(TITLE1)).perform(click());
        Espresso.openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());
        onView(withText(R.string.menu_restore_note)).perform(click());

        // Confirm the note isn't shown with the archived notes
        onView(withId(R.id.notes_list)).check(matches(not(isDisplayed())));

        // Now show active notes
        Espresso.openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());
        onView(withText(R.string.menu_show_active)).perform(click());

        // Scroll notes list to added note, by finding its description
        onView(withId(R.id.notes_list)).perform(
            scrollTo(hasDescendant(withText(DESCRIPTION))));

        // Verify note is displayed on screen
        onView(withItemText(DESCRIPTION)).check(matches(isDisplayed()));
    }

    @Test
    @Ignore("Still need to figure out why we can't always click on the menu option")
    public void testShowArchivedNotes() {
        // First add a note
        createNote(TITLE1, DESCRIPTION);

        // Click on the note on the list
        onView(withText(TITLE1)).perform(click());

        // Archive the note
        Espresso.openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());
        onView(withText(R.string.menu_archive_note)).perform(click());

        // Now show archived notes
        Espresso.openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());
        onView(withText(R.string.menu_show_archive)).perform(click());

        // Scroll notes list to added note, by finding its description
        onView(withId(R.id.notes_list)).perform(
            scrollTo(hasDescendant(withText(DESCRIPTION))));

        // Verify note is displayed on screen
        onView(withItemText(DESCRIPTION)).check(matches(isDisplayed()));
    }

    @Test
    @Ignore("Still need to figure out why we can't always click on the menu option")
    public void testClearArchivedNotes() {
        // First add a note
        createNote(TITLE1, DESCRIPTION);

        // Click on the note on the list
        onView(withText(TITLE1)).perform(click());

        // Archive the note
        Espresso.openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());
        onView(withText(R.string.menu_archive_note)).perform(click());

        // Now show archived notes
        Espresso.openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());
        onView(withText(R.string.menu_show_archive)).perform(click());

        Espresso.openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());
        onView(withText(R.string.menu_clear_archived)).perform(click());

        // Confirm the note isn't shown on screen
        onView(withId(R.id.notes_list)).check(matches(not(isDisplayed())));
    }

    @Test
    @Ignore("Still need to figure out why we can't always click on the menu option")
    public void createOneNote_deleteNote() {
        // First add a note
        createNote(TITLE1, DESCRIPTION);

        // Click on the note on the list
        onView(withText(TITLE1)).perform(click());

        // Delete the note
        Espresso.openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());
        onView(withText(R.string.menu_delete_note)).perform(click());

        // Confirm the note isn't shown on screen
        onView(withId(R.id.notes_list)).check(matches(not(isDisplayed())));
    }

    @Test
    @Ignore("Still need to figure out why we can't always click on the menu option")
    public void createTwoNotes_deleteOneNote() {
        // First add a note
        createNote(TITLE1, DESCRIPTION);
        createNote(TITLE2, DESCRIPTION);

        // Click on the note on the list
        onView(withText(TITLE2)).perform(click());

        // Delete the note
        Espresso.openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());
        onView(withText(R.string.menu_delete_note)).perform(click());

        // Confirm the note isn't shown on screen
        onView(withText(TITLE1)).check(matches(isDisplayed()));
        onView(withText(TITLE2)).check(doesNotExist());
    }

    @Test
    @SdkSuppress(minSdkVersion = 21) // Blinking cursor after rotation breaks this in API 19
    public void orientationChange_DuringEdit_ChangePersists() throws Throwable {
        // Add an archived note
        createNote(TITLE1, DESCRIPTION);

        // Open the note in details view
        onView(withText(TITLE1)).perform(click());

        // Click on the edit note button
        onView(withId(R.id.fab_edit_note)).perform(click());

        // Change note title (but don't save)
        onView(withId(R.id.add_note_title))
                .perform(replaceText(TITLE2), closeSoftKeyboard()); // Type new note title

        // Rotate the screen
        TestUtils.rotateOrientation(getCurrentActivity());

        // Verify note title is restored
        onView(withId(R.id.add_note_title)).check(matches(withText(TITLE2)));
    }

    @Test
    @SdkSuppress(minSdkVersion = 21) // Blinking cursor after rotation breaks this in API 19
    public void orientationChange_DuringEdit_NoDuplicate() throws IllegalStateException {
        // Add an archived note
        createNote(TITLE1, DESCRIPTION);

        // Open the note in details view
        onView(withText(TITLE1)).perform(click());

        // Click on the edit note button
        onView(withId(R.id.fab_edit_note)).perform(click());

        // Rotate the screen
        TestUtils.rotateOrientation(getCurrentActivity());

        // Edit note title and description
        onView(withId(R.id.add_note_title))
                .perform(replaceText(TITLE2), closeSoftKeyboard()); // Type new note title
        onView(withId(R.id.add_note_description)).perform(replaceText(DESCRIPTION),
                closeSoftKeyboard()); // Type new note description and close the keyboard

        // Save the note
        onView(withId(R.id.fab_save_note)).perform(click());

        // Verify note is displayed on screen in the note list.
        onView(withItemText(TITLE2)).check(matches(isDisplayed()));

        // Verify previous note is not displayed
        onView(withItemText(TITLE1)).check(doesNotExist());
    }

    private void createNote(String title, String description) {
        // Click on the add note button
        onView(withId(R.id.fab_add_note)).perform(click());

        // Add note title and description
        onView(withId(R.id.add_note_title)).perform(typeText(title),
                closeSoftKeyboard()); // Type new note title
        onView(withId(R.id.add_note_description)).perform(typeText(description),
                closeSoftKeyboard()); // Type new note description and close the keyboard

        // Save the note
        onView(withId(R.id.fab_save_note)).perform(click());
    }

}
