package com.example.android.testing.notes.notedetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.testing.notes.Event
import com.example.android.testing.notes.data.Note
import com.example.android.testing.notes.data.NotesRepository

class NoteDetailViewModel(private val notesRepository: NotesRepository) : ViewModel(), NotesRepository.GetNoteCallback {

  private val note = MutableLiveData<Note>()
  private val isDataAvailable = MutableLiveData<Boolean>()
  private val isDataLoading = MutableLiveData<Boolean>()
  private val archiveNoteCommand = MutableLiveData<Event<Object>>()
  private val deleteNoteCommand = MutableLiveData<Event<Object>>()
  private val restoreNoteCommand = MutableLiveData<Event<Object>>()
  private val snackbarText = MutableLiveData<Event<Int>>()

  fun restoreNote() {
    note.value?.let {
      notesRepository.restoreNote(it)
      restoreNoteCommand.value = Event(Object())
    }
    if (note.value != null) {
      notesRepository.restoreNote(note?.value)
    }
  }
}