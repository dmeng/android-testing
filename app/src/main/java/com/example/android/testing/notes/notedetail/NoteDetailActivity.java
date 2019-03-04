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

import android.content.Intent;
import android.os.Bundle;

import com.example.android.testing.notes.Event;
import com.example.android.testing.notes.R;
import com.example.android.testing.notes.ViewModelFactory;
import com.example.android.testing.notes.addeditnote.AddEditNoteActivity;
import com.example.android.testing.notes.addeditnote.AddEditNoteFragment;
import com.example.android.testing.notes.util.ActivityUtils;
import com.example.android.testing.notes.util.EspressoIdlingResource;
import com.example.android.testing.notes.util.RequestCodes;
import com.example.android.testing.notes.util.ResultCodes;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.test.espresso.IdlingResource;

/**
 * Displays note details screen.
 */
public class NoteDetailActivity extends AppCompatActivity {

    public static final String EXTRA_NOTE_ID = "NOTE_ID";

    private NoteDetailViewModel mNoteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        setupToolbar();

        NoteDetailFragment noteDetailFragment = findOrCreateViewFragment();

        ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(),
                noteDetailFragment, R.id.contentFrame);

        mNoteViewModel = obtainViewModel(this);

        subscribeToNavigationChanges(mNoteViewModel);
    }

    @NonNull
    private NoteDetailFragment findOrCreateViewFragment() {
        // Get the requested note id
        String noteId = getIntent().getStringExtra(EXTRA_NOTE_ID);

        NoteDetailFragment noteDetailFragment = (NoteDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (noteDetailFragment == null) {
            noteDetailFragment = NoteDetailFragment.newInstance(noteId);
        }
        return noteDetailFragment;
    }

    @NonNull
    public static NoteDetailViewModel obtainViewModel(FragmentActivity activity) {
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        return ViewModelProviders.of(activity, factory).get(NoteDetailViewModel.class);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
    }

    private void subscribeToNavigationChanges(NoteDetailViewModel viewModel) {
        viewModel.getArchiveNoteCommand().observe(this, new Observer<Event<Object>>() {
            @Override
            public void onChanged(Event<Object> noteEvent) {
                if (noteEvent.getContentIfNotHandled() != null) {
                    NoteDetailActivity.this.onNoteArchived();
                }
            }
        });

        // The activity observes the navigation commands in the ViewModel
        viewModel.getEditNoteCommand().observe(this, new Observer<Event<Object>>() {
            @Override
            public void onChanged(Event<Object> noteEvent) {
                if (noteEvent.getContentIfNotHandled() != null) {
                    NoteDetailActivity.this.onStartEditNote();
                }
            }
        });
        viewModel.getDeleteNoteCommand().observe(this, new Observer<Event<Object>>() {
            @Override
            public void onChanged(Event<Object> noteEvent) {
                if (noteEvent.getContentIfNotHandled() != null) {
                    NoteDetailActivity.this.onNoteDeleted();
                }
            }
        });

        viewModel.getRestoreNoteCommand().observe(this, new Observer<Event<Object>>() {
            @Override
            public void onChanged(Event<Object> noteEvent) {
                if (noteEvent.getContentIfNotHandled() != null) {
                    NoteDetailActivity.this.onNoteRestored();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCodes.NOTE_DETAIL_EDIT_REQUEST_CODE) {
            // If the note was edited successfully, go back to the list.
            if (resultCode == ResultCodes.ADD_EDIT_RESULT_OK) {
                // If the result comes from the add/edit screen, it's an edit.
                setResult(ResultCodes.EDIT_RESULT_OK);
                finish();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onNoteArchived() {
        setResult(ResultCodes.ARCHIVE_RESULT_OK);
        finish();
    }

    public void onNoteDeleted() {
        setResult(ResultCodes.DELETE_RESULT_OK);
        // If the note was deleted successfully, go back to the list.
        finish();
    }

    public void onStartEditNote() {
        String noteId = getIntent().getStringExtra(EXTRA_NOTE_ID);
        Intent intent = new Intent(this, AddEditNoteActivity.class);
        intent.putExtra(AddEditNoteFragment.ARGUMENT_EDIT_NOTE_ID, noteId);
        startActivityForResult(intent, RequestCodes.NOTE_DETAIL_EDIT_REQUEST_CODE);
    }

    public void onNoteRestored() {
        setResult(ResultCodes.RESTORE_RESULT_OK);
        finish();
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}
