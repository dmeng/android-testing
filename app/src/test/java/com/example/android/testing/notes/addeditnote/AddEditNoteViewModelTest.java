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

import com.example.android.testing.notes.data.Note;
import com.example.android.testing.notes.data.NotesRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class AddEditNoteViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private NotesRepository mNotesRepository;

    private AddEditNoteViewModel mAddEditNoteViewModel;

    @Before
    public void setupAddEditNoteViewModel() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mAddEditNoteViewModel = new AddEditNoteViewModel(mNotesRepository);
    }

    @Test
    public void saveNewNoteToRepository_showsSuccessMessageUi() {
        // When the ViewModel is asked to save a note
        mAddEditNoteViewModel.description.setValue("Some Note Description");
        mAddEditNoteViewModel.title.setValue("New Note Title");
        mAddEditNoteViewModel.saveNote();

        ArgumentCaptor<Note> captor = ArgumentCaptor.forClass(Note.class);
        // Then a note is saved in the repository and the view updated
        verify(mNotesRepository).saveNote(captor.capture()); // saved to the model
        assertEquals("New Note Title", captor.getValue().getTitle());
        assertEquals("Some Note Description", captor.getValue().getDescription());
    }

    @Test
    public void populateNote_callsRepoAndUpdatesView() {
        Note testNote = new Note("TITLE", "DESCRIPTION", "1");

        // Get a reference to the class under test
        mAddEditNoteViewModel = new AddEditNoteViewModel(mNotesRepository);
        ArgumentCaptor<NotesRepository.GetNoteCallback> mGetNoteCallbackCaptor = ArgumentCaptor.forClass(NotesRepository.GetNoteCallback.class);

        // When the ViewModel is asked to populate an existing note
        mAddEditNoteViewModel.start(testNote.getId());

        // Then the note repository is queried and the view updated
        verify(mNotesRepository).getNote(eq(testNote.getId()), mGetNoteCallbackCaptor.capture());

        // Simulate callback
        mGetNoteCallbackCaptor.getValue().onNoteLoaded(testNote);

        // Verify the fields were updated
        assertThat(mAddEditNoteViewModel.title.getValue(), is(testNote.getTitle()));
        assertThat(mAddEditNoteViewModel.description.getValue(), is(testNote.getDescription()));
    }
}
