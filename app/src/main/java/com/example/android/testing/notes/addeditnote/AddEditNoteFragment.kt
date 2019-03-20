package com.example.android.testing.notes.addeditnote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.android.testing.notes.R
import com.example.android.testing.notes.databinding.AddEditNoteFragmentBinding
import com.example.android.testing.notes.notes.NotesActivity
import com.example.android.testing.notes.util.SnackbarUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class AddEditNoteFragment : Fragment() {

  private lateinit var viewDataBinding: AddEditNoteFragmentBinding

  private fun loadData() {
    viewDataBinding.viewModel?.start(arguments?.getString(ARGUMENT_EDIT_TASK_ID))
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
          savedInstanceState: Bundle?): View? {
    viewDataBinding = AddEditNoteFragmentBinding.inflate(inflater, container, false)
    viewDataBinding.viewModel = NotesActivity.obtainViewModel(activity as FragmentActivity, AddEditNoteViewModel::class.java)
    viewDataBinding.lifecycleOwner = activity

    val v = viewDataBinding.root
    v.findViewById<FloatingActionButton>(R.id.fab_add_note)?.let {
      it.setImageResource(R.drawable.ic_done)
      it.setOnClickListener {
        (viewDataBinding.viewModel as AddEditNoteViewModel).saveNote()
        Navigation.findNavController(it).navigate(R.id.action_save_and_return)
      }
    }

    setupActionBar()
    setupSnackBar()
    setHasOptionsMenu(true)
    setRetainInstance(false)
    loadData()

    return v
  }

  private fun setupActionBar() {
    (activity as AppCompatActivity).supportActionBar?.setTitle(
        if (arguments != null && arguments?.get(ARGUMENT_EDIT_TASK_ID) != null)
          R.string.edit_note
        else
          R.string.add_note
    )
  }

  private fun setupSnackBar() {
    (viewDataBinding.viewModel as AddEditNoteViewModel).getSnackbarText()
        .observe(activity as AppCompatActivity, Observer {
          val msg = it.contentIfNotHandled
          if (msg != null) SnackbarUtils.showSnackbar(view, getString(msg))
        })
  }

  companion object {
    const val ARGUMENT_EDIT_TASK_ID = "EDIT_TASK_ID"

    fun newInstance() = AddEditNoteFragment()
  }
}