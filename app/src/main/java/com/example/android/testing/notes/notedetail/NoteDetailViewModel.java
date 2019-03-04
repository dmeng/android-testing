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

import com.example.android.testing.notes.Event;
import com.example.android.testing.notes.R;
import com.example.android.testing.notes.data.Note;
import com.example.android.testing.notes.data.NotesRepository;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

/**
 * Listens to user actions from the UI ({@link NoteDetailFragment}), retrieves the data and updates
 * the UI as required.
 */
public class NoteDetailViewModel extends ViewModel implements NotesRepository.GetNoteCallback {

    private final MutableLiveData<Note> mNote = new MutableLiveData<>();

    private final MutableLiveData<Boolean> mIsDataAvailable = new MutableLiveData<>();

    private final MutableLiveData<Boolean> mDataLoading = new MutableLiveData<>();

    private final MutableLiveData<Event<Object>> mArchiveNoteCommand = new MutableLiveData<>();

    private final MutableLiveData<Event<Object>> mEditNoteCommand = new MutableLiveData<>();

    private final MutableLiveData<Event<Object>> mDeleteNoteCommand = new MutableLiveData<>();

    private final MutableLiveData<Event<Object>> mRestoreNoteCommand = new MutableLiveData<>();

    private final MutableLiveData<Event<Integer>> mSnackbarText = new MutableLiveData<>();

    private final NotesRepository mNotesRepository;

    public NoteDetailViewModel(NotesRepository notesRepository) {
        mNotesRepository = notesRepository;
    }

    public void restoreNote()  {
        if (mNote.getValue() != null) {
            mNotesRepository.restoreNote(mNote.getValue());
            mRestoreNoteCommand.setValue(new Event<>(new Object()));
        }
    }

    public void archiveNote() {
        if (mNote.getValue() != null) {
            mNotesRepository.archiveNote(mNote.getValue());
            mArchiveNoteCommand.setValue(new Event<>(new Object()));
        }
    }
    public void deleteNote() {
        if (mNote.getValue() != null) {
            mNotesRepository.deleteNote(mNote.getValue());
            mDeleteNoteCommand.setValue(new Event<>(new Object()));
        }
    }

    public void editNote() {
        mEditNoteCommand.setValue(new Event<>(new Object()));
    }

    public LiveData<Event<Integer>> getSnackbarMessage() {
        return mSnackbarText;
    }

    public MutableLiveData<Event<Object>> getArchiveNoteCommand() {
        return mArchiveNoteCommand;
    }

    public MutableLiveData<Event<Object>> getEditNoteCommand() {
        return mEditNoteCommand;
    }

    public MutableLiveData<Event<Object>> getDeleteNoteCommand() {
        return mDeleteNoteCommand;
    }

    public MutableLiveData<Event<Object>> getRestoreNoteCommand() {
        return mRestoreNoteCommand;
    }

    public LiveData<Note> getNote() {
        return mNote;
    }

    public LiveData<Boolean> getIsDataAvailable() {
        return mIsDataAvailable;
    }

    public LiveData<Boolean> getDataLoading() {
        return mDataLoading;
    }

    public void setArchived(boolean archived) {
        if (mDataLoading.getValue()) {
            return;
        }
        Note note = this.mNote.getValue();
        if (archived) {
            mNotesRepository.archiveNote(note);
            showSnackbarMessage(R.string.note_archived_message);
        } else {
            mNotesRepository.restoreNote(note);
            showSnackbarMessage(R.string.note_restored_message);
        }
    }

    public void start(String noteId) {
        if (noteId != null) {
            mDataLoading.setValue(true);
            mNotesRepository.getNote(noteId, this);
        }
    }

    public void setNote(Note note) {
        this.mNote.setValue(note);
        mIsDataAvailable.setValue(note != null);
    }

    @Override
    public void onNoteLoaded(Note note) {
        setNote(note);
        mDataLoading.setValue(false);
    }

    @Override
    public void onDataNotAvailable() {
        mNote.setValue(null);
        mDataLoading.setValue(false);
        mIsDataAvailable.setValue(false);
    }

    public void onRefresh() {
        if (mNote.getValue() != null) {
            start(mNote.getValue().getId());
        }
    }

    @Nullable
    protected String getNoteId() {
        return mNote.getValue().getId();
    }

    private void showSnackbarMessage(@StringRes Integer message) {
        mSnackbarText.setValue(new Event<>(message));
    }
}
