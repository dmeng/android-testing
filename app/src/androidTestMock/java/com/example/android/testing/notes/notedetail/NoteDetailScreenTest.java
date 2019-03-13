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

package com.example.android.testing.notes.notedetail;

import static org.hamcrest.core.IsNot.not;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.app.Activity;
import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.android.testing.notes.Injection;
import com.example.android.testing.notes.R;
import com.example.android.testing.notes.data.Note;
import com.example.android.testing.notes.data.NotesRepository;
import com.example.android.testing.notes.util.EspressoIdlingResource;
import com.example.android.testing.notes.util.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

/**
 * Tests for the notes screen, the main screen which contains a list of all notes.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class NoteDetailScreenTest {
    //
    // private static String NOTE_TITLE = "ATSL";
    //
    // private static String NOTE_DESCRIPTION = "Rocks";
    //
    // /**
    //  * {@link Note} stub that is added to the fake service API layer.
    //  */
    // private static Note ACTIVE_NOTE = new Note(NOTE_TITLE, NOTE_DESCRIPTION, false);
    //
    // /**
    //  * {@link ActivityTestRule} is a JUnit {@link Rule @Rule} to launch your activity under test.
    //  *
    //  * <p>
    //  * Rules are interceptors which are executed for each test method and are important building
    //  * blocks of Junit tests.
    //  *
    //  * <p>
    //  * Sometimes an {@link Activity} requires a custom start {@link Intent} to receive data
    //  * from the source Activity. ActivityTestRule has a feature which let's you lazily start the
    //  * Activity under test, so you can control the Intent that is used to start the target Activity.
    //  */
    // @Rule
    // public ActivityTestRule<NoteDetailActivity> mNoteDetailActivityTestRule =
    //         new ActivityTestRule<>(NoteDetailActivity.class, true /* Initial touch mode  */,
    //                 false /* Lazily launch activity */);
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
    // private void loadActiveNote() {
    //     startActivityWithWithStubbedNote(ACTIVE_NOTE);
    // }
    //
    // /**
    //  * Setup your test fixture with a fake note id. The {@link NoteDetailActivity} is started with
    //  * a particular note id, which is then loaded from the service API.
    //  *
    //  * <p>
    //  * Note that this test runs hermetically and is fully isolated using a fake implementation of
    //  * the service API. This is a great way to make your tests more reliable and faster at the same
    //  * time, since they are isolated from any outside dependencies.
    //  */
    // private void startActivityWithWithStubbedNote(Note note) {
    //     // Add a note stub to the fake service api layer.
    //     NotesRepository repository = Injection.provideNotesRepository();
    //     repository.deleteAllNotes();
    //     repository.saveNote(note);
    //
    //     // Lazily start the Activity from the ActivityTestRule this time to inject the start Intent
    //     Intent startIntent = new Intent();
    //     startIntent.putExtra(NoteDetailActivity.EXTRA_NOTE_ID, note.getId());
    //     mNoteDetailActivityTestRule.launchActivity(startIntent);
    // }
    //
    // @Test
    // public void activeNoteDetails_DisplayedInUi() throws Exception {
    //     loadActiveNote();
    //
    //     // Check that the note title and description are displayed
    //     onView(withId(R.id.note_detail_title)).check(matches(withText(NOTE_TITLE)));
    //     onView(withId(R.id.note_detail_description)).check(matches(withText(NOTE_DESCRIPTION)));
    // }
    //
    // @Test
    // @Ignore("Need to figure out what's going on with the overflow menu")
    // public void orientationChange_menuAndNotePersist() {
    //     loadActiveNote();
    //
    //     // Check delete menu item is displayed and is unique
    //     onView(withId(R.id.menu_delete)).check(matches(isDisplayed()));
    //
    //     TestUtils.rotateOrientation(mNoteDetailActivityTestRule.getActivity());
    //
    //     // Check that the note is shown
    //     onView(withId(R.id.note_detail_title)).check(matches(withText(NOTE_TITLE)));
    //     onView(withId(R.id.note_detail_description)).check(matches(withText(NOTE_DESCRIPTION)));
    //
    //     // Check delete menu item is displayed and is unique
    //     onView(withId(R.id.menu_delete)).check(matches(isDisplayed()));
    // }

}
