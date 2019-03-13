package com.example.android.testing.notes.notedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import com.example.android.testing.notes.R
import com.example.android.testing.notes.addeditnote.AddEditNoteFragment
import com.example.android.testing.notes.databinding.NoteDetailFragmentBinding
import com.example.android.testing.notes.notes.NotesActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NoteDetailFragment : Fragment() {
  private lateinit var viewModel : NoteDetailViewModel

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.notedetail_fragment_menu, menu)

    if (viewModel.note?.value?.isArchived) {
      val archive = menu.findItem(R.id.menu_archive)
      archive.setEnabled(false)
      archive.setVisible(false)

      val restore = menu.findItem(R.id.menu_restore)
      restore.setEnabled(true)
      restore.setVisible(true)
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val binding = NoteDetailFragmentBinding.inflate(inflater, container, false)
    viewModel = NotesActivity.obtainViewModel(activity as FragmentActivity, NoteDetailViewModel::class.java)

    binding.viewModel = viewModel
    binding.lifecycleOwner = activity

    setHasOptionsMenu(true)
    return binding.root
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.menu_archive -> {
        viewModel.archiveNote()
        return true
      }
      R.id.menu_delete -> {
        viewModel.deleteNote()
        return true
      }
      R.id.menu_restore -> {
        viewModel.restoreNote()
        return true
      }
      else -> return false
    }
  }

  override fun onResume() {
    super.onResume()
    viewModel.start(arguments?.getString(ARGUMENT_NOTE_ID))
  }

  private fun setupFab(v : View) {
    v.findViewById<FloatingActionButton>(R.id.fab_edit_note).setOnClickListener {
      viewModel.editNote()
      val bundle = Bundle()
      bundle.putString(AddEditNoteFragment.ARGUMENT_EDIT_TASK_ID, arguments?.getString(ARGUMENT_NOTE_ID))
      Navigation.findNavController(v).navigate(R.id.action_edit_note, bundle)
    }
  }

  companion object {
    const val ARGUMENT_NOTE_ID = "NOTE_ID"

    fun newInstance(noteId : String) : NoteDetailFragment {
      val arguments = Bundle()
      arguments.putString(ARGUMENT_NOTE_ID, noteId)
      val fragment = NoteDetailFragment()
      fragment.arguments = arguments
      return fragment
    }
  }
}
