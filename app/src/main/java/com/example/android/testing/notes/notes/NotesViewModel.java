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

import com.example.android.testing.notes.Event;
import com.example.android.testing.notes.R;
import com.example.android.testing.notes.data.Note;
import com.example.android.testing.notes.data.NotesRepository;
import com.example.android.testing.notes.util.RequestCodes;
import com.example.android.testing.notes.util.ResultCodes;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

/**
 * Listens to user actions from the UI ({@link NotesFragment}), retrieves the data and updates the
 * UI as required.
 */
public class NotesViewModel extends ViewModel {

    private final MutableLiveData<List<Note>> mItems = new MutableLiveData<>();

    private final MutableLiveData<Boolean> mDataLoading = new MutableLiveData<>();

    private final MutableLiveData<Boolean> mNotesAddViewVisible = new MutableLiveData<>();

    private final MutableLiveData<Event<Integer>> mSnackbarText = new MutableLiveData<>();

    private boolean showArchived = false;

    private final NotesRepository mNotesRepository;

    // Not used at the moment
    private final MutableLiveData<Boolean> mIsDataLoadingError = new MutableLiveData<>();

    private final MutableLiveData<Event<String>> mOpenNoteEvent = new MutableLiveData<>();

    private final MutableLiveData<Event<Object>> mNewNoteEvent = new MutableLiveData<>();

    // This LiveData depends on another so we can use a transformation.
    public final LiveData<Boolean> empty = Transformations.map(mItems,
            new Function<List<Note>, Boolean>() {
                @Override
                public Boolean apply(List<Note> input) {
                    return input.isEmpty();

                }
            });

    public NotesViewModel(NotesRepository repository) {
        mNotesRepository = repository;
        mNotesAddViewVisible.setValue(true);
    }

    public void start() {
        loadNotes(false);
    }

    public void loadNotes(boolean forceUpdate) {
        loadNotes(forceUpdate, true);
    }

    public void deleteArchivedNotes() {
        mNotesRepository.deleteArchivedNotes();
        mSnackbarText.setValue(new Event<>(R.string.archived_notes_cleared_message));
        loadNotes(false, false);
    }

    // LiveData getters

    public LiveData<Boolean> getNotesAddViewVisible() {
        return mNotesAddViewVisible;
    }

    public LiveData<Boolean> isDataLoading() {
        return mDataLoading;
    }

    public LiveData<Event<Integer>> getSnackbarMessage() {
        return mSnackbarText;
    }

    public LiveData<Event<String>> getOpenNoteEvent() {
        return mOpenNoteEvent;
    }

    public LiveData<Event<Object>> getNewNoteEvent() {
        return mNewNoteEvent;
    }

    public LiveData<List<Note>> getItems() {
        return mItems;
    }

    /**
     * Called by the Data Binding library and the FAB's click listener.
     */
    public void addNewNote() {
        mNewNoteEvent.setValue(new Event<>(new Object()));
    }

    public boolean showArchived() {
        return showArchived;
    }

    public void setShowArchived(boolean showArchived) {
        this.showArchived = showArchived;
    }

    /**
     * Called by the {@link NotesAdapter}.
     */
    void openNote(String noteId) {
        mOpenNoteEvent.setValue(new Event<>(noteId));
    }

    void handleActivityResult(int requestCode, int resultCode) {
        if (requestCode == RequestCodes.ADD_EDIT_REQUEST_CODE) {
            switch (resultCode) {
                case ResultCodes.EDIT_RESULT_OK:
                    mSnackbarText.setValue(new Event<>(R.string.successfully_saved_note_message));
                    break;
                case ResultCodes.ADD_EDIT_RESULT_OK:
                    mSnackbarText.setValue(new Event<>(R.string.successfully_added_note_message));
                    break;
                case ResultCodes.DELETE_RESULT_OK:
                    mSnackbarText.setValue(new Event<>(R.string.successfully_deleted_note_message));
                    break;
                case ResultCodes.ARCHIVE_RESULT_OK:
                    mSnackbarText.setValue(new Event<>(R.string.successfully_archived_note_message));
                    break;
                case ResultCodes.RESTORE_RESULT_OK:
                    mSnackbarText.setValue(new Event<>(R.string.successfully_restored_note_message));
                    break;
                case Activity.RESULT_CANCELED:
                    break;
                default:
                    mSnackbarText.setValue(new Event<>(R.string.error_unknown_command));
                    break;
            }
        }
    }

    private void showSnackbarMessage(Integer message) {
        mSnackbarText.setValue(new Event<>(message));
    }

    /**
     * @param forceUpdate   Pass in true to refresh the data in the {@link NotesRepository}
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadNotes(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            mDataLoading.setValue(true);
        }
        if (forceUpdate) {
            mNotesRepository.refreshData();
        }

        mNotesRepository.getNotes(new NotesRepository.LoadNotesCallback() {
            @Override
            public void onNotesLoaded(List<Note> notes) {
                List<Note> notesToShow = new ArrayList<>();

                for (Note note : notes) {
                    if (note.isArchived() == showArchived) {
                        notesToShow.add(note);
                    }
                }
                if (showLoadingUI) {
                    mDataLoading.setValue(false);
                }
                mIsDataLoadingError.setValue(false);

                List<Note> itemsValue = new ArrayList<>(notesToShow);
                mItems.setValue(itemsValue);
            }

            @Override
            public void onDataNotAvailable() {
                mIsDataLoadingError.setValue(true);
            }
        });
    }

}
