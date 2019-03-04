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

import android.app.Application;
import android.content.res.Resources;

import com.example.android.testing.notes.Event;
import com.example.android.testing.notes.R;
import com.example.android.testing.notes.data.Note;
import com.example.android.testing.notes.data.NotesRepository;
import com.example.android.testing.notes.data.NotesRepository.LoadNotesCallback;
import com.example.android.testing.notes.util.LiveDataTestUtil;
import com.example.android.testing.notes.util.RequestCodes;
import com.example.android.testing.notes.util.ResultCodes;
import com.example.android.testing.notes.util.TestUtils;
import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NotesViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private List<Note> NOTES;

    @Mock
    private NotesRepository mNotesRepository;

    @Mock
    private Application mContext;

    @Captor
    private ArgumentCaptor<NotesRepository.LoadNotesCallback> mLoadNotesCallbackCaptor;

    private NotesViewModel mNotesViewModel;

    @Before
    public void setupNotesViewModel() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        setupContext();

        // Get a reference to the class under test
        mNotesViewModel = new NotesViewModel(mNotesRepository);

        // We initialise the notes to 3, with one active and two archived
        NOTES = Lists.newArrayList(new Note("Title1", "Description1"),
                new Note("Title2", "Description2", true), new Note("Title3", "Description3", true));

        mNotesViewModel.getSnackbarMessage().removeObservers(TestUtils.TEST_OBSERVER);
    }

    private void setupContext() {
        when(mContext.getApplicationContext()).thenReturn(mContext);
        when(mContext.getString(R.string.successfully_saved_note_message))
                .thenReturn("EDIT_RESULT_OK");
        when(mContext.getString(R.string.successfully_added_note_message))
                .thenReturn("ADD_EDIT_RESULT_OK");
        when(mContext.getString(R.string.successfully_deleted_note_message))
                .thenReturn("DELETE_RESULT_OK");

        when(mContext.getResources()).thenReturn(mock(Resources.class));
    }

    @Test
    public void loadActiveNotesFromRepositoryAndLoadIntoView() {
        // Given an initialized NotesViewModel with initialized notes
        // When loading of Notes is requested
        mNotesViewModel.setShowArchived(false);
        mNotesViewModel.loadNotes(true);

        // Callback is captured and invoked with stubbed notes
        verify(mNotesRepository).getNotes(mLoadNotesCallbackCaptor.capture());
        mLoadNotesCallbackCaptor.getValue().onNotesLoaded(NOTES);

        // Then progress indicator is hidden
        assertFalse(mNotesViewModel.isDataLoading().getValue());

        // And data loaded
        assertFalse(mNotesViewModel.getItems().getValue().isEmpty());
        assertTrue(mNotesViewModel.getItems().getValue().size() == 1);
    }

    @Test
    public void loadArchivedNotesFromRepositoryAndLoadIntoView() {
        // Given an initialized NotesViewModel with initialized notes
        // When loading of Notes is requested
        mNotesViewModel.setShowArchived(true);
        mNotesViewModel.loadNotes(true);

        // Callback is captured and invoked with stubbed notes
        verify(mNotesRepository).getNotes(mLoadNotesCallbackCaptor.capture());
        mLoadNotesCallbackCaptor.getValue().onNotesLoaded(NOTES);

        // Then progress indicator is hidden
        assertFalse(mNotesViewModel.isDataLoading().getValue());

        // And data loaded
        assertFalse(mNotesViewModel.getItems().getValue().isEmpty());
        assertTrue(mNotesViewModel.getItems().getValue().size() == 2);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void clickOnFab_ShowsAddNoteUi() throws InterruptedException {
        // When adding a new note
        mNotesViewModel.addNewNote();

        // Then the event is triggered
        Event<Object> value = LiveDataTestUtil.getValue(mNotesViewModel.getNewNoteEvent());
        assertNotNull(value.getContentIfNotHandled());
    }

    @Test
    public void deleteArchivedNotes_DeletesNotes() {
        // When archived notes are cleared
        mNotesViewModel.deleteArchivedNotes();

        // Then repository is called and the view is notified
        verify(mNotesRepository).deleteArchivedNotes();
        verify(mNotesRepository).getNotes(any(LoadNotesCallback.class));
    }

    @Test
    public void handleActivityResult_editOK() throws InterruptedException {
        // When NoteDetailActivity sends a EDIT_RESULT_OK
        mNotesViewModel.handleActivityResult(
                RequestCodes.ADD_EDIT_REQUEST_CODE, ResultCodes.EDIT_RESULT_OK);

        // Then the event is triggered
        Event<Integer> value = LiveDataTestUtil.getValue(mNotesViewModel.getSnackbarMessage());
        assertEquals((long) value.getContentIfNotHandled(), R.string.successfully_saved_note_message);
    }

    @Test
    public void handleActivityResult_addEditOK() throws InterruptedException {
        // When NoteDetailActivity sends an EDIT_RESULT_OK
        mNotesViewModel.handleActivityResult(
                RequestCodes.ADD_EDIT_REQUEST_CODE, ResultCodes.ADD_EDIT_RESULT_OK);

        // Then the snackbar shows the correct message
        Event<Integer> value = LiveDataTestUtil.getValue(mNotesViewModel.getSnackbarMessage());
        assertEquals(
                (long) value.getContentIfNotHandled(),
                R.string.successfully_added_note_message
        );
    }

    @Test
    public void handleActivityResult_deleteOk() throws InterruptedException {
        // When NoteDetailActivity sends a DELETE_RESULT_OK
        mNotesViewModel.handleActivityResult(
                RequestCodes.ADD_EDIT_REQUEST_CODE, ResultCodes.DELETE_RESULT_OK);

        // Then the snackbar shows the correct message
        Event<Integer> value = LiveDataTestUtil.getValue(mNotesViewModel.getSnackbarMessage());
        assertEquals(
                (long) value.getContentIfNotHandled(),
                R.string.successfully_deleted_note_message
        );
    }

    @Test
    public void getNotesAddViewVisible() throws InterruptedException {
        // Then the "Add note" action is visible
        assertTrue(LiveDataTestUtil.getValue(mNotesViewModel.getNotesAddViewVisible()));
    }

}
