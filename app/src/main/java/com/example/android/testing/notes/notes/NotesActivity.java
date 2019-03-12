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
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.FragmentManager;
import com.example.android.testing.notes.ArgsFragmentFactory;
import com.example.android.testing.notes.R;
import com.example.android.testing.notes.ViewModelFactory;
import com.google.android.material.navigation.NavigationView;

public class NotesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // private DrawerLayout mDrawerLayout;
    //
    // private NotesViewModel mViewModel;
    //
    // @Override
    // protected void onCreate(Bundle savedInstanceState) {
    //     super.onCreate(savedInstanceState);
    //     setContentView(R.layout.activity_notes);
    //
    //     setupToolbar();
    //
    //     setupNavigationDrawer();
    //
    //     setupViewFragment();
    //
    //     mViewModel = obtainViewModel(this);
    //
    //     // Subscribe to "open note" event
    //     mViewModel.getOpenNoteEvent().observe(this, new Observer<Event<String>>() {
    //         @Override
    //         public void onChanged(Event<String> noteIdEvent) {
    //             String noteId = noteIdEvent.getContentIfNotHandled();
    //             if (noteId != null) {
    //                 openNoteDetails(noteId);
    //             }
    //
    //         }
    //     });
    //
    //     // Subscribe to "new note" event
    //     mViewModel.getNewNoteEvent().observe(this, new Observer<Event<Object>>() {
    //         @Override
    //         public void onChanged(Event<Object> noteIdEvent) {
    //             if (noteIdEvent.getContentIfNotHandled() != null) {
    //                 addNewNote();
    //             }
    //         }
    //     });
    // }
    //
    // public static NotesViewModel obtainViewModel(FragmentActivity activity) {
    //     // Use a Factory to inject dependencies into the ViewModel
    //     ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());
    //
    //     NotesViewModel viewModel =
    //             ViewModelProviders.of(activity, factory).get(NotesViewModel.class);
    //
    //     return viewModel;
    // }
    //
    // private void setupViewFragment() {
    //     NotesFragment notesFragment =
    //             (NotesFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
    //     if (notesFragment == null) {
    //         // Create the fragment
    //         notesFragment = NotesFragment.newInstance();
    //         ActivityUtils.replaceFragmentInActivity(
    //                 getSupportFragmentManager(), notesFragment, R.id.contentFrame);
    //     }
    // }
    //
    // private void setupToolbar() {
    //     Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    //     setSupportActionBar(toolbar);
    //     ActionBar ab = getSupportActionBar();
    //     ab.setHomeAsUpIndicator(R.drawable.ic_menu);
    //     ab.setDisplayHomeAsUpEnabled(true);
    // }

    public void setupNavigationDrawer(View v) {
        final DrawerLayout drawerLayout = v.findViewById(R.id.drawer_layout);
        drawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) v.findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.list_navigation_menu_item:
                                // Do nothing, we're already on that screen
                                break;
                            case R.id.statistics_navigation_menu_item:
                                // Intent intent =
                                //         new Intent(NotesActivity.this, StatisticsActivity.class);
                                // startActivity(intent);
                                break;
                            default:
                                break;
                        }
                        // Close the navigation drawer when an item is selected.
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    // @Override
    // public void onActivityResult(int requestCode, int resultCode, Intent data) {
    //     mViewModel.handleActivityResult(requestCode, resultCode);
    // }
    //
    // public void openNoteDetails(String noteId) {
    //     Intent intent = new Intent(this, NoteDetailActivity.class);
    //     intent.putExtra(NoteDetailActivity.EXTRA_NOTE_ID, noteId);
    //     startActivityForResult(intent, RequestCodes.ADD_EDIT_REQUEST_CODE);
    //
    // }
    //
    // public void addNewNote() {
    //     Intent intent = new Intent(this, AddEditNoteActivity.class);
    //     startActivityForResult(intent, RequestCodes.ADD_EDIT_REQUEST_CODE);
    // }
}
