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

import android.app.Application;
import android.content.res.Resources;

import com.example.android.testing.notes.Event;
import com.example.android.testing.notes.R;
import com.example.android.testing.notes.data.Note;
import com.example.android.testing.notes.data.NotesRepository;
import com.example.android.testing.notes.util.LiveDataTestUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NoteDetailViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private static final String TITLE_TEST = "title";

    private static final String DESCRIPTION_TEST = "description";

    private static final String NO_DATA_STRING = "NO_DATA_STRING";

    @Mock
    private NotesRepository mNotesRepository;

    @Mock
    private Application mContext;

    @Captor
    private ArgumentCaptor<NotesRepository.GetNoteCallback> mGetNoteCallbackCaptor;

    private NoteDetailViewModel mNoteDetailViewModel;

    private Note mNote;

    @Before
    public void setupNotesViewModel() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        setupContext();

        mNote = new Note(TITLE_TEST, DESCRIPTION_TEST);

        // Get a reference to the class under test
        mNoteDetailViewModel = new NoteDetailViewModel(mNotesRepository);
    }

    private void setupContext() {
        when(mContext.getApplicationContext()).thenReturn(mContext);
        when(mContext.getString(R.string.no_data)).thenReturn(NO_DATA_STRING);
        when(mContext.getResources()).thenReturn(mock(Resources.class));
    }

    @Test
    public void getActiveNoteFromRepositoryAndLoadIntoView() {
        setupViewModelRepositoryCallback();

        // Then verify that the view was notified
        assertEquals(mNoteDetailViewModel.getNote().getValue().getTitle(), mNote.getTitle());
        assertEquals(mNoteDetailViewModel.getNote().getValue().getDescription(), mNote.getDescription());
    }

    @Test
    public void deleteNote() {
        setupViewModelRepositoryCallback();

        // When the deletion of a note is requested
        mNoteDetailViewModel.deleteNote();

        // Then the repository is notified
        verify(mNotesRepository).deleteNote(mNote);
    }

    @Test
    public void archiveNote() throws InterruptedException {
        setupViewModelRepositoryCallback();

        // When the ViewModel is asked to archive the note
        mNoteDetailViewModel.setArchived(true);

        // Then a request is sent to the note repository and the UI is updated
        Event<Integer> value = LiveDataTestUtil.getValue(mNoteDetailViewModel.getSnackbarMessage());
        Assert.assertEquals(
                (long) value.getContentIfNotHandled(),
                R.string.note_archived_message
        );
    }

    @Test
    public void restoreNote() throws InterruptedException {
        setupViewModelRepositoryCallback();

        // When the ViewModel is asked to restore the note
        mNoteDetailViewModel.setArchived(false);

        // Then a request is sent to the note repository and the UI is updated
        Event<Integer> value = LiveDataTestUtil.getValue(mNoteDetailViewModel.getSnackbarMessage());
        Assert.assertEquals(
                (long) value.getContentIfNotHandled(),
                R.string.note_restored_message
        );
    }

    @Test
    public void NoteDetailViewModel_repositoryError() throws InterruptedException {
        // Given an initialized ViewModel with an active note
        mNoteDetailViewModel.start(mNote.getId());

        // Use a captor to get a reference for the callback.
        verify(mNotesRepository).getNote(eq(mNote.getId()), mGetNoteCallbackCaptor.capture());

        // When the repository returns an error
        mGetNoteCallbackCaptor.getValue().onDataNotAvailable(); // Trigger callback error

        // Then verify that data is not available
        assertFalse(LiveDataTestUtil.getValue(mNoteDetailViewModel.getIsDataAvailable()));
    }

    @Test
    public void NoteDetailViewModel_repositoryNull() throws InterruptedException {
        setupViewModelRepositoryCallback();

        // When the repository returns a null note
        mGetNoteCallbackCaptor.getValue().onNoteLoaded(null); // Trigger callback error

        // Then verify that data is not available
        assertFalse(LiveDataTestUtil.getValue(mNoteDetailViewModel.getIsDataAvailable()));

        // Then note detail UI is shown
        assertThat(mNoteDetailViewModel.getNote().getValue(), is(nullValue()));
    }

    private void setupViewModelRepositoryCallback() {
        // Given an initialized ViewModel with an active note
        mNoteDetailViewModel.start(mNote.getId());

        // Use a captor to get a reference for the callback.
        verify(mNotesRepository).getNote(eq(mNote.getId()), mGetNoteCallbackCaptor.capture());

        mGetNoteCallbackCaptor.getValue().onNoteLoaded(mNote); // Trigger callback
    }

}
