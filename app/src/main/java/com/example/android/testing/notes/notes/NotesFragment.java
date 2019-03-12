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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.Navigation;
import com.example.android.testing.notes.R;
import com.example.android.testing.notes.ScrollChildSwipeRefreshLayout;
import com.example.android.testing.notes.data.Note;
import com.example.android.testing.notes.databinding.NotesFragmentBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Display a grid of {@link Note}s
 */
public class NotesFragment extends Fragment {

    private DrawerLayout drawerLayout;
    private NotesAdapter notesAdapter;
    private NotesFragmentBinding binding;
    private NotesViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = NotesFragmentBinding.inflate(inflater, container, false);
        viewModel = NotesActivity.obtainViewModel(getActivity(), NotesViewModel.class);
        binding.setLifecycleOwner(getActivity());
        binding.setViewModel(viewModel);

        View v = binding.getRoot();
        FloatingActionButton fab = v.findViewById(R.id.fab_add_note);
        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.add_edit_note_action);
            }
        });

        drawerLayout = v.findViewById(R.id.drawer_layout);
        setupNavigationDrawer(v, drawerLayout);
        setupListAdapter();
        setupRefreshLayout();
        setupToolbar(v, (NotesActivity) getActivity());
        setHasOptionsMenu(true);

        return v;
    }

    protected void setupToolbar(View v, NotesActivity activity) {
        Toolbar toolbar = v.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        ActionBar ab = activity.getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    public void setupNavigationDrawer(View v, DrawerLayout drawerLayout) {
        drawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = v.findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView, v);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView, final View v) {
        navigationView.setNavigationItemSelectedListener(
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.list_navigation_menu_item:
                            // Do nothing, we're already on that screen
                            break;
                        case R.id.statistics_navigation_menu_item:
                            Navigation.findNavController(v).navigate(R.id.main_to_statistics_page);
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

    @Override
    public void onResume() {
        super.onResume();
        viewModel.start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.notes_fragment_menu, menu);
        final MenuItem showArchive = menu.findItem(R.id.menu_show_archive);
        final MenuItem showActive = menu.findItem(R.id.menu_show_active);
        showArchive.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                item.setEnabled(false);
                item.setVisible(false);
                showActive.setEnabled(true);
                showActive.setVisible(true);
                viewModel.setShowArchived(true);
                viewModel.loadNotes(true);
                return true;
            }
        });
        showActive.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                item.setEnabled(false);
                item.setVisible(false);
                showArchive.setEnabled(true);
                showArchive.setVisible(true);
                viewModel.setShowArchived(false);
                viewModel.loadNotes(true);
                return true;
            }
        });

        MenuItem clearArchived = menu.findItem(R.id.menu_clear_archived);
        clearArchived.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                viewModel.deleteArchivedNotes();
                viewModel.loadNotes(true);
                return true;
            }
        });

        MenuItem refresh = menu.findItem(R.id.menu_refresh);
        refresh.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                viewModel.loadNotes(true);
                return true;
            }
        });
    }

    private void setupListAdapter() {
        RecyclerView recyclerView = binding.notesList;

        notesAdapter = new NotesAdapter(
                new ArrayList<Note>(0),
            viewModel,
                getActivity()
        );
        recyclerView.setAdapter(notesAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    private void setupRefreshLayout() {
        RecyclerView recyclerView =  binding.notesList;
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout = binding.refreshLayout;
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(recyclerView);
    }

}
