package com.example.android.testing.notes.addeditnote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.testing.notes.Event
import com.example.android.testing.notes.R
import com.example.android.testing.notes.data.Note
import com.example.android.testing.notes.data.NotesRepository
import java.lang.RuntimeException

class AddEditNoteViewModel(private val notesRepository: NotesRepository) : ViewModel(), NotesRepository.GetNoteCallback {

  val title = MutableLiveData<String>()
  val description = MutableLiveData<String>()

  private val dataLoading = MutableLiveData<Boolean>()
  private val snackbarText = MutableLiveData<Event<Int>>()
  private val noteUpdated = MutableLiveData<Event<Object>>()

  private var noteId : String? = null
  private var isNewNote : Boolean = false
  private var isDataLoaded : Boolean = false
  private var isNoteArchived : Boolean = false

  fun start(noteId : String?) {
    dataLoading.value?.let { isLoading ->
      if (isLoading) return
    }
    this.noteId = noteId
    if (noteId == null) {
      isNewNote = true
      return
    }
    if (isDataLoaded) return
    dataLoading.value = false
    notesRepository.getNote(noteId, this)
  }

  fun getSnackbarText() : LiveData<Event<Int>> {
    return snackbarText
  }

  override fun onNoteLoaded(note: Note?) {
    title.value = note?.title
    description.value = note?.description
    isNoteArchived = note?.isArchived ?: false
    dataLoading.value = false
    isDataLoaded = true
  }

  override fun onDataNotAvailable() {
    dataLoading.value = false
  }

  fun createNote(newNote : Note) {
    notesRepository.saveNote(newNote)
    noteUpdated.value = Event(Object())
  }

  fun saveNote() {
    var note = Note(title.value, description.value)
    if (note.isEmpty) {
      snackbarText.value = Event(R.string.empty_note_message)
      return
    }
    if (isNewNote || noteId == null) {
      createNote(note)
    } else {
      note = Note(noteId, title.value, description.value, null, isNoteArchived)
      updateNote(note)
    }
  }

  fun updateNote(note : Note) {
    if (isNewNote) {
      throw RuntimeException("updateNote() was called but note was new")
    }
    notesRepository.saveNote(note)
    noteUpdated.value = Event(Object())
  }
}