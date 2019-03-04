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

import com.example.android.testing.notes.data.Note;
import com.example.android.testing.notes.data.NotesRepository;

import java.util.List;

import androidx.databinding.Bindable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Exposes the data to be used in the statistics screen.
 * <p>
 * This ViewModel uses both {@link ObservableField}s ({@link ObservableBoolean}s in this case) and
 * {@link Bindable} getters. The values in {@link ObservableField}s are used directly in the layout,
 * whereas the {@link Bindable} getters allow us to add some logic to it. This is
 * preferable to having logic in the XML layout.
 */
public class StatisticsViewModel extends ViewModel {

    private final MutableLiveData<Boolean> mDataLoading = new MutableLiveData<>();

    private final MutableLiveData<Boolean> mError = new MutableLiveData<>();

    private final MutableLiveData<Integer> mActiveNotes = new MutableLiveData<>();

    private final MutableLiveData<Integer> mArchivedNotes = new MutableLiveData<>();

    private final MutableLiveData mEmpty = new MutableLiveData();

    private int mNumberOfActiveNotes = 0;

    private int mNumberOfArchivedNotes = 0;

    private final NotesRepository mNotesRepository;

    public StatisticsViewModel(NotesRepository notesRepository) {
        mNotesRepository = notesRepository;
    }

    public void start() {
        loadStatistics();
    }

    public void loadStatistics() {
        mDataLoading.setValue(true);

        mNotesRepository.getNotes(new NotesRepository.LoadNotesCallback() {
            @Override
            public void onNotesLoaded(List<Note> notes) {
                mError.setValue(false);
                computeStats(notes);
            }

            @Override
            public void onDataNotAvailable() {
                mError.setValue(true);
                mNumberOfActiveNotes = 0;
                mNumberOfArchivedNotes = 0;
                updateDataBindingObservables();
            }
        });
    }

    // LiveData getters

    public LiveData<Boolean> getDataLoading() {
        return mDataLoading;
    }

    public LiveData<Boolean> getError() {
        return mError;
    }

    public MutableLiveData<Integer> getNumberOfActiveNotes() {
        return mActiveNotes;
    }

    public MutableLiveData<Integer> getNumberOfArchivedNotes() {
        return mArchivedNotes;
    }

    /**
     * Controls whether the stats are shown or a "No data" message.
     */
    public LiveData<Boolean> getEmpty() {
        return mEmpty;
    }

    /**
     * Called when new data is ready.
     */
    private void computeStats(List<Note> notes) {
        int archived = 0;
        int active = 0;

        for (Note note : notes) {
            if (note.isArchived()) {
                archived += 1;
            } else {
                active += 1;
            }
        }
        mNumberOfActiveNotes = active;
        mNumberOfArchivedNotes = archived;

        updateDataBindingObservables();
    }

    private void updateDataBindingObservables() {
        mArchivedNotes.setValue(mNumberOfArchivedNotes);
        mActiveNotes.setValue(mNumberOfActiveNotes);
        mEmpty.setValue(mNumberOfActiveNotes + mNumberOfArchivedNotes == 0);
        mDataLoading.setValue(false);
    }
}
