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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import androidx.navigation.Navigation;
import com.example.android.testing.notes.Event;
import com.example.android.testing.notes.R;
import com.example.android.testing.notes.addeditnote.AddEditNoteFragment;
import com.example.android.testing.notes.databinding.NoteDetailFragmentBinding;
import com.example.android.testing.notes.notes.NotesActivity;
import com.example.android.testing.notes.notes.NotesViewModel;
import com.example.android.testing.notes.util.EspressoIdlingResource;
import com.example.android.testing.notes.util.SnackbarUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

/**
 * Main UI for the note detail screen.
 */
public class NoteDetailFragment extends Fragment {
    public static final String ARGUMENT_NOTE_ID = "NOTE_ID";

    private NoteDetailViewModel mViewModel;

    public static NoteDetailFragment newInstance(String noteId) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_NOTE_ID, noteId);
        NoteDetailFragment fragment = new NoteDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.start(getArguments().getString(ARGUMENT_NOTE_ID));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        NoteDetailFragmentBinding binding = NoteDetailFragmentBinding.inflate(inflater, container, false);

        mViewModel = NotesActivity.obtainViewModel(getActivity(), NoteDetailViewModel.class);

        View view = binding.getRoot();
        binding.setViewModel(mViewModel);
        binding.setLifecycleOwner(getActivity());

        FloatingActionButton fab = view.findViewById(R.id.fab_edit_note);
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.editNote();
                Bundle bundle = new Bundle();
                bundle.putString(AddEditNoteFragment.ARGUMENT_EDIT_NOTE_ID, getArguments().getString(ARGUMENT_NOTE_ID));
                Navigation.findNavController(v).navigate(R.id.action_edit_note, bundle);
            }
        });
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_archive:
                mViewModel.archiveNote();
                return true;
            case R.id.menu_delete:
                mViewModel.deleteNote();
                return true;
            case R.id.menu_restore:
                mViewModel.restoreNote();
                return true;
        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.notedetail_fragment_menu, menu);

        if (mViewModel.getNote().getValue().isArchived()) {
            MenuItem archive = menu.findItem(R.id.menu_archive);
            archive.setVisible(false);
            archive.setEnabled(false);

            MenuItem restore = menu.findItem(R.id.menu_restore);
            restore.setVisible(true);
            restore.setEnabled(true);
        }
    }
}
