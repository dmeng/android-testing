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

import com.example.android.testing.notes.Event;
import com.example.android.testing.notes.R;
import com.example.android.testing.notes.data.Note;
import com.example.android.testing.notes.data.NotesRepository;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Listens to user actions from the UI ({@link AddEditNoteFragment}), retrieves the data and updates
 * the UI as required.
 */
public class AddEditNoteViewModel extends ViewModel implements NotesRepository.GetNoteCallback {

    // Two-way databinding, exposing MutableLiveData
    public final MutableLiveData<String> title = new MutableLiveData<>();

    // Two-way databinding, exposing MutableLiveData
    public final MutableLiveData<String> description = new MutableLiveData<>();

    private final MutableLiveData<Boolean> dataLoading = new MutableLiveData<>();

    private final MutableLiveData<Event<Integer>> mSnackbarText = new MutableLiveData<>();

    private final MutableLiveData<Event<Object>> mNoteUpdated = new MutableLiveData<>();

    private final NotesRepository mNotesRepository;

    @Nullable
    private String mNoteId;

    private boolean mIsNewNote;

    private boolean mIsDataLoaded = false;

    private boolean mNoteArchived = false;

    public AddEditNoteViewModel(NotesRepository notesRepository) {
        mNotesRepository = notesRepository;
    }

    public void start(String noteId) {
        if (dataLoading.getValue() != null && dataLoading.getValue()) {
            // Already loading, ignore.
            return;
        }
        mNoteId = noteId;
        if (noteId == null) {
            // No need to populate, it's a new note
            mIsNewNote = true;
            return;
        }
        if (mIsDataLoaded) {
            // No need to populate, already have data.
            return;
        }
        mIsNewNote = false;
        dataLoading.setValue(true);

        mNotesRepository.getNote(noteId, this);
    }

    public void onNoteLoaded(Note note) {
        title.setValue(note.getTitle());
        description.setValue(note.getDescription());
        mNoteArchived = note.isArchived();
        dataLoading.setValue(false);
        mIsDataLoaded = true;
    }

    @Override
    public void onDataNotAvailable() {
        dataLoading.setValue(false);
    }

    // Called when clicking on fab.
    void saveNote() {
        Note note = new Note(title.getValue(), description.getValue());
        if (note.isEmpty()) {
            mSnackbarText.setValue(new Event<>(R.string.empty_note_message));
            return;
        }
        if (isNewNote() || mNoteId == null) {
            createNote(note);
        } else {
            note = new Note(mNoteId, title.getValue(), description.getValue(), null, mNoteArchived);
            updateNote(note);
        }
    }

    public LiveData<Event<Integer>> getSnackbarMessage() {
        return mSnackbarText;
    }

    public LiveData<Event<Object>> getNoteUpdatedEvent() {
        return mNoteUpdated;
    }

    public LiveData<Boolean> getDataLoading() {
        return dataLoading;
    }

    private boolean isNewNote() {
        return mIsNewNote;
    }

    private void createNote(Note newNote) {
        mNotesRepository.saveNote(newNote);
        mNoteUpdated.setValue(new Event<>(new Object()));
    }

    private void updateNote(Note note) {
        if (isNewNote()) {
            throw new RuntimeException("updateNote() was called but note is new.");
        }
        mNotesRepository.saveNote(note);
        mNoteUpdated.setValue(new Event<>(new Object()));
    }
}
