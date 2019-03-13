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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import androidx.navigation.Navigation;
import com.example.android.testing.notes.Event;
import com.example.android.testing.notes.R;
import com.example.android.testing.notes.databinding.AddEditNoteFragmentBinding;
import com.example.android.testing.notes.notes.NotesActivity;
import com.example.android.testing.notes.util.SnackbarUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

/**
 * Main UI for the add note screen. Users can enter a note title and description.
 */
public class AddEditNoteFragment extends Fragment {
    public static final String ARGUMENT_EDIT_NOTE_ID = "EDIT_NOTE_ID";

    private AddEditNoteViewModel mViewModel;

    private AddEditNoteFragmentBinding mAddEditNoteFragmentBinding;

    private void loadData() {
        // Add or edit an existing note?
        if (getArguments() != null) {
            mViewModel.start(getArguments().getString(ARGUMENT_EDIT_NOTE_ID));
        } else {
            mViewModel.start(null);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mAddEditNoteFragmentBinding = AddEditNoteFragmentBinding.inflate(inflater, container, false);
        mAddEditNoteFragmentBinding.setLifecycleOwner(getActivity());
        mViewModel = NotesActivity.obtainViewModel(getActivity(), AddEditNoteViewModel.class);

        View v = mAddEditNoteFragmentBinding.getRoot();
        FloatingActionButton fab = v.findViewById(R.id.fab_save_note);
        fab.setImageResource(R.drawable.ic_done);
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.saveNote();
                Navigation.findNavController(v).navigate(R.id.action_save_and_return);
            }
        });

        setupActionBar();
        setupSnackbar();
        setHasOptionsMenu(true);
        setRetainInstance(false);
        loadData();
        return v;
    }

    private void setupSnackbar() {
        mViewModel.getSnackbarMessage().observe(this, new Observer<Event<Integer>>() {
            @Override
            public void onChanged(Event<Integer> event) {
                Integer msg = event.getContentIfNotHandled();
                if (msg != null) {
                    SnackbarUtils.showSnackbar(getView(), getString(msg));
                }
            }
        });
    }

    private void setupActionBar() {
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        actionBar.setHomeAsUpIndicator(null);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        if (getArguments() != null && getArguments().get(ARGUMENT_EDIT_NOTE_ID) != null) {
            actionBar.setTitle(R.string.edit_note);
        } else {
            actionBar.setTitle(R.string.add_note);
        }
    }
}
