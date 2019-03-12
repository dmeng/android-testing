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
import android.text.Layout;
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
import androidx.navigation.Navigation;
import com.example.android.testing.notes.Event;
import com.example.android.testing.notes.R;
import com.example.android.testing.notes.ScrollChildSwipeRefreshLayout;
import com.example.android.testing.notes.data.Note;
import com.example.android.testing.notes.databinding.NotesFragmentBinding;
import com.example.android.testing.notes.util.SnackbarUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Display a grid of {@link Note}s
 */
public class NotesFragment extends Fragment {

    private final NotesViewModel viewModel;

    public NotesFragment() {
        this(null);
    }

    public NotesFragment(NotesViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        NotesFragmentBinding binding = NotesFragmentBinding.inflate(inflater, container, false);
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
        setupToolbar((NotesActivity) getActivity());
        setHasOptionsMenu(true);

        return v;
    }

    private void setupToolbar(NotesActivity activity) {
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        ActionBar ab = activity.getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }
    // private NotesViewModel mNotesViewModel;
    //
    // private NotesFragmentBinding mNotesFragmentBinding;
    //
    // private NotesAdapter mListAdapter;
    //
    // public NotesFragment() {
    //     // Requires empty public constructor
    // }
    //
    // public static NotesFragment newInstance() {
    //     return new NotesFragment();
    // }
    //
    // @Override
    // public void onResume() {
    //     super.onResume();
    //     mNotesViewModel.start();
    // }
    //
    // @Nullable
    // @Override
    // public View onCreateView(LayoutInflater inflater, ViewGroup container,
    //         Bundle savedInstanceState) {
    //     mNotesFragmentBinding = NotesFragmentBinding.inflate(inflater, container, false);
    //
    //     mNotesViewModel = NotesActivity.obtainViewModel(getActivity());
    //
    //     mNotesFragmentBinding.setViewModel(mNotesViewModel);
    //     mNotesFragmentBinding.setLifecycleOwner(getActivity());
    //
    //     setHasOptionsMenu(true);
    //
    //     return mNotesFragmentBinding.getRoot();
    // }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.notes_fragment_menu, menu);
        final MenuItem showArchive = menu.findItem(R.id.menu_show_archive);
        final MenuItem showActive = menu.findItem(R.id.menu_show_active);
        // showArchive.setOnMenuItemClickListener(new OnMenuItemClickListener() {
        //     @Override
        //     public boolean onMenuItemClick(MenuItem item) {
        //         item.setEnabled(false);
        //         item.setVisible(false);
        //         showActive.setEnabled(true);
        //         showActive.setVisible(true);
        //         mNotesViewModel.setShowArchived(true);
        //         mNotesViewModel.loadNotes(true);
        //         return true;
        //     }
        // });
        // showActive.setOnMenuItemClickListener(new OnMenuItemClickListener() {
        //     @Override
        //     public boolean onMenuItemClick(MenuItem item) {
        //         item.setEnabled(false);
        //         item.setVisible(false);
        //         showArchive.setEnabled(true);
        //         showArchive.setVisible(true);
        //         mNotesViewModel.setShowArchived(false);
        //         mNotesViewModel.loadNotes(true);
        //         return true;
        //     }
        // });
        //
        // MenuItem clearArchived = menu.findItem(R.id.menu_clear_archived);
        // clearArchived.setOnMenuItemClickListener(new OnMenuItemClickListener() {
        //     @Override
        //     public boolean onMenuItemClick(MenuItem item) {
        //         mNotesViewModel.deleteArchivedNotes();
        //         mNotesViewModel.loadNotes(true);
        //         return true;
        //     }
        // });
        //
        // MenuItem refresh = menu.findItem(R.id.menu_refresh);
        // refresh.setOnMenuItemClickListener(new OnMenuItemClickListener() {
        //     @Override
        //     public boolean onMenuItemClick(MenuItem item) {
        //         mNotesViewModel.loadNotes(true);
        //         return true;
        //     }
        // });
    }

    // @Override
    // public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    //     super.onActivityCreated(savedInstanceState);
    //
    //     setupSnackbar();
    //
    //     setupFab();
    //
    //     setupListAdapter();
    //
    //     setupRefreshLayout();
    // }
    //
    // private void setupSnackbar() {
    //     mNotesViewModel.getSnackbarMessage().observe(this, new Observer<Event<Integer>>() {
    //         @Override
    //         public void onChanged(Event<Integer> event) {
    //             Integer msg = event.getContentIfNotHandled();
    //             if (msg != null) {
    //                 SnackbarUtils.showSnackbar(getView(), getString(msg));
    //             }
    //         }
    //     });
    // }
    //
    // private void setupFab() {
    //     FloatingActionButton fab = getActivity().findViewById(R.id.fab_add_note);
    //
    //     fab.setImageResource(R.drawable.ic_add);
    //     fab.setOnClickListener(new View.OnClickListener() {
    //         @Override
    //         public void onClick(View v) {
    //             mNotesViewModel.addNewNote();
    //         }
    //     });
    // }
    //
    // private void setupListAdapter() {
    //     RecyclerView recyclerView = mNotesFragmentBinding.notesList;
    //
    //     mListAdapter = new NotesAdapter(
    //             new ArrayList<Note>(0),
    //             mNotesViewModel,
    //             getActivity()
    //     );
    //     recyclerView.setAdapter(mListAdapter);
    //     recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    // }
    //
    // private void setupRefreshLayout() {
    //     RecyclerView recyclerView =  mNotesFragmentBinding.notesList;
    //     final ScrollChildSwipeRefreshLayout swipeRefreshLayout = mNotesFragmentBinding.refreshLayout;
    //     swipeRefreshLayout.setColorSchemeColors(
    //             ContextCompat.getColor(getActivity(), R.color.colorPrimary),
    //             ContextCompat.getColor(getActivity(), R.color.colorAccent),
    //             ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
    //     );
    //     // Set the scrolling view in the custom SwipeRefreshLayout.
    //     swipeRefreshLayout.setScrollUpChild(recyclerView);
    // }

}
