/*
 *  Copyright 2019, Google Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.example.android.testing.notes.notes;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.testing.notes.R;
import com.example.android.testing.notes.data.Note;
import com.example.android.testing.notes.databinding.NoteItemBinding;

import com.example.android.testing.notes.notedetail.NoteDetailFragment;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private final NotesViewModel mNotesViewModel;

    private List<Note> mNotes;
    private NoteItemBinding mNoteItemBinding;
    private LifecycleOwner mLifeCycleOwner;

    public NotesAdapter(List<Note> notes,
            NotesViewModel notesViewModel, LifecycleOwner activity) {
        mNotesViewModel = notesViewModel;
        setList(notes);
        mLifeCycleOwner = activity;

    }

    public void replaceData(List<Note> notes) {
        mNotes = notes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mNotes != null ? mNotes.size() : 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        mNoteItemBinding = NoteItemBinding.inflate(inflater, parent, false);
        mNoteItemBinding.setLifecycleOwner(mLifeCycleOwner);
        return new ViewHolder(mNoteItemBinding.getRoot(), mNotesViewModel);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = mNotes.get(position);
        holder.title.setText(note.getTitle());
        holder.description.setText(note.getDescription());

        mNoteItemBinding.setNote(note);
        mNoteItemBinding.setListener(new NoteItemUserActionsListener() {
            @Override
            public void onNoteClicked(Note note) {
                mNotesViewModel.openNote(note.getId());
            }
        });
        mNoteItemBinding.executePendingBindings();
    }

    private void setList(List<Note> notes) {
        mNotes = notes;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public TextView description;
        public NotesViewModel notesViewModel;

        public ViewHolder(@NonNull View itemView, NotesViewModel notesViewModel) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.note_detail_title);
            description = (TextView) itemView.findViewById(R.id.note_detail_description);
            this.notesViewModel = notesViewModel;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString(NoteDetailFragment.ARGUMENT_NOTE_ID, mNotes.get(getAdapterPosition()).getId());
            Navigation.findNavController(v).navigate(R.id.action_view_note, bundle /* arg for which note's details we're viewing */);
        }
    }
}
