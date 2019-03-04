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

import android.os.Bundle;

import com.example.android.testing.notes.Event;
import com.example.android.testing.notes.R;
import com.example.android.testing.notes.ViewModelFactory;
import com.example.android.testing.notes.util.ActivityUtils;
import com.example.android.testing.notes.util.ResultCodes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

/**
 * Displays an add note screen.
 */
public class AddEditNoteActivity extends AppCompatActivity {

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onNoteSaved() {
        setResult(ResultCodes.ADD_EDIT_RESULT_OK);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnote);

        // Set up the toolbar.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        AddEditNoteFragment addEditNoteFragment = obtainViewFragment();

        ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(),
                addEditNoteFragment, R.id.contentFrame);

        subscribeToNavigationChanges();
    }

    private void subscribeToNavigationChanges() {
        AddEditNoteViewModel viewModel = obtainViewModel(this);

        // The activity observes the navigation events in the ViewModel
        viewModel.getNoteUpdatedEvent().observe(this, new Observer<Event<Object>>() {
            @Override
            public void onChanged(Event<Object> noteIdEvent) {
                if (noteIdEvent.getContentIfNotHandled() != null) {
                    AddEditNoteActivity.this.onNoteSaved();
                }
            }
        });
    }

    public static AddEditNoteViewModel obtainViewModel(FragmentActivity activity) {
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        return ViewModelProviders.of(activity, factory).get(AddEditNoteViewModel.class);
    }

    @NonNull
    private AddEditNoteFragment obtainViewFragment() {
        // View Fragment
        AddEditNoteFragment addEditNoteFragment = (AddEditNoteFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (addEditNoteFragment == null) {
            addEditNoteFragment = AddEditNoteFragment.newInstance();

            // Send the note ID to the fragment
            Bundle bundle = new Bundle();
            bundle.putString(AddEditNoteFragment.ARGUMENT_EDIT_NOTE_ID,
                    getIntent().getStringExtra(AddEditNoteFragment.ARGUMENT_EDIT_NOTE_ID));
            addEditNoteFragment.setArguments(bundle);
        }
        return addEditNoteFragment;
    }
}
