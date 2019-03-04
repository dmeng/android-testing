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

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.android.testing.notes.Event;
import com.example.android.testing.notes.R;
import com.example.android.testing.notes.ViewModelFactory;
import com.example.android.testing.notes.addeditnote.AddEditNoteActivity;
import com.example.android.testing.notes.notedetail.NoteDetailActivity;
import com.example.android.testing.notes.statistics.StatisticsActivity;
import com.example.android.testing.notes.util.ActivityUtils;
import com.example.android.testing.notes.util.RequestCodes;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class NotesActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    private NotesViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        setupToolbar();

        setupNavigationDrawer();

        setupViewFragment();

        mViewModel = obtainViewModel(this);

        // Subscribe to "open note" event
        mViewModel.getOpenNoteEvent().observe(this, new Observer<Event<String>>() {
            @Override
            public void onChanged(Event<String> noteIdEvent) {
                String noteId = noteIdEvent.getContentIfNotHandled();
                if (noteId != null) {
                    openNoteDetails(noteId);
                }

            }
        });

        // Subscribe to "new note" event
        mViewModel.getNewNoteEvent().observe(this, new Observer<Event<Object>>() {
            @Override
            public void onChanged(Event<Object> noteIdEvent) {
                if (noteIdEvent.getContentIfNotHandled() != null) {
                    addNewNote();
                }
            }
        });
    }

    public static NotesViewModel obtainViewModel(FragmentActivity activity) {
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        NotesViewModel viewModel =
                ViewModelProviders.of(activity, factory).get(NotesViewModel.class);

        return viewModel;
    }

    private void setupViewFragment() {
        NotesFragment notesFragment =
                (NotesFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (notesFragment == null) {
            // Create the fragment
            notesFragment = NotesFragment.newInstance();
            ActivityUtils.replaceFragmentInActivity(
                    getSupportFragmentManager(), notesFragment, R.id.contentFrame);
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void setupNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.list_navigation_menu_item:
                                // Do nothing, we're already on that screen
                                break;
                            case R.id.statistics_navigation_menu_item:
                                Intent intent =
                                        new Intent(NotesActivity.this, StatisticsActivity.class);
                                startActivity(intent);
                                break;
                            default:
                                break;
                        }
                        // Close the navigation drawer when an item is selected.
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mViewModel.handleActivityResult(requestCode, resultCode);
    }

    public void openNoteDetails(String noteId) {
        Intent intent = new Intent(this, NoteDetailActivity.class);
        intent.putExtra(NoteDetailActivity.EXTRA_NOTE_ID, noteId);
        startActivityForResult(intent, RequestCodes.ADD_EDIT_REQUEST_CODE);

    }

    public void addNewNote() {
        Intent intent = new Intent(this, AddEditNoteActivity.class);
        startActivityForResult(intent, RequestCodes.ADD_EDIT_REQUEST_CODE);
    }
}
