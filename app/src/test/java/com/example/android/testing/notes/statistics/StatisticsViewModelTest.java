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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

public class StatisticsViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private List<Note> NOTES;

    @Mock
    private NotesRepository mNotesRepository;

    @Captor
    private ArgumentCaptor<NotesRepository.LoadNotesCallback> mLoadNotesCallbackCaptor;

    private StatisticsViewModel mStatisticsViewModel;

    @Before
    public void setupStatisticsViewModel() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mStatisticsViewModel = new StatisticsViewModel(mNotesRepository);

        // We initialise the notes to 3, with one active and two archived
        NOTES = Lists.newArrayList(new Note("Title1", "Description1"),
                new Note("Title2", "Description2", true), new Note("Title3", "Description3", true));
    }

    @Test
    public void loadEmptyNotesFromRepository_EmptyResults() {
        // Given an initialized StatisticsViewModel with no notes
        NOTES.clear();

        // When loading of Notes is requested
        mStatisticsViewModel.loadStatistics();

        // Callback is captured and invoked with stubbed notes
        verify(mNotesRepository).getNotes(mLoadNotesCallbackCaptor.capture());
        mLoadNotesCallbackCaptor.getValue().onNotesLoaded(NOTES);

        // Then the results are empty
        assertThat(mStatisticsViewModel.getEmpty().getValue(), is(true));
    }

    @Test
    public void loadNonEmptyNotesFromRepository_NonEmptyResults() {
        // When loading of Notes is requested
        mStatisticsViewModel.loadStatistics();

        // Callback is captured and invoked with stubbed notes
        verify(mNotesRepository).getNotes(mLoadNotesCallbackCaptor.capture());
        mLoadNotesCallbackCaptor.getValue().onNotesLoaded(NOTES);

        // Then the results are empty
        assertThat(mStatisticsViewModel.getEmpty().getValue(), is(false));
    }

    @Test
    public void loadStatisticsWhenNotesAreUnavailable_CallErrorToDisplay() {
        // When statistics are loaded
        mStatisticsViewModel.loadStatistics();

        // And notes data isn't available
        verify(mNotesRepository).getNotes(mLoadNotesCallbackCaptor.capture());
        mLoadNotesCallbackCaptor.getValue().onDataNotAvailable();

        // Then an error message is shown
        assertEquals(mStatisticsViewModel.getEmpty().getValue(), true);
        assertEquals(mStatisticsViewModel.getError().getValue(), true);
    }
}
