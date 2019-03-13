package com.example.android.testing.notes.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.android.testing.notes.R
import com.example.android.testing.notes.databinding.NotesFragmentBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class NotesFragment : Fragment() {

  private lateinit var drawerLayout : DrawerLayout
  private lateinit var notesAdapter: NotesAdapter
  private lateinit var notesFragmentBinding: NotesFragmentBinding
  private lateinit var viewModel: NotesViewModel

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.notes_fragment_menu, menu)
    val showArchive = menu.findItem(R.id.menu_show_archive)
    val showActive = menu.findItem(R.id.menu_show_active)
    showArchive.setOnMenuItemClickListener {
      it.setEnabled(false)
      it.setVisible(false)
      showActive.setEnabled(true)
      showActive.setVisible(true)
      viewModel.setShowArchived(true)
      viewModel.loadNotes(true)
      true
    }
    showActive.setOnMenuItemClickListener {
      it.setEnabled(false)
      it.setEnabled(false)
      showArchive.setEnabled(true)
      showArchive.setVisible(true)
      viewModel.setShowArchived(false)
      viewModel.loadNotes(true)
      true
    }
    menu.findItem(R.id.menu_clear_archived).setOnMenuItemClickListener {
      viewModel.deleteArchivedNotes()
      viewModel.loadNotes(true)
      true
    }
    menu.findItem(R.id.menu_refresh).setOnMenuItemClickListener {
      viewModel.loadNotes(true)
      true
    }
    super.onCreateOptionsMenu(menu, inflater)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    notesFragmentBinding = NotesFragmentBinding.inflate(inflater, container, false)
    notesFragmentBinding.viewModel = NotesActivity.obtainViewModel(activity as FragmentActivity, NotesViewModel::class.java)
    notesFragmentBinding.lifecycleOwner = activity

    val v = notesFragmentBinding.root
    setupFab(v)
    drawerLayout = v.findViewById(R.id.drawer_layout)
    setupNavigationDrawer(v, drawerLayout)
    setupListAdapter()
    setupRefreshLayout()
    setupToolbar(v, activity as AppCompatActivity)
    setHasOptionsMenu(true)

    return v
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == R.id.home) {
      drawerLayout.openDrawer(GravityCompat.START)
      return true
    } else {
      return super.onOptionsItemSelected(item)
    }
  }

  override fun onResume() {
    super.onResume()
    viewModel.start()
  }

  private fun setupDrawerContent(navigationView: NavigationView, v : View) {
    navigationView.setNavigationItemSelectedListener {
      if (it.itemId == R.id.statistics_navigation_menu_item) {
        Navigation.findNavController(v).navigate(R.id.main_to_statistics_page)
      }
      true
    }
  }

  private fun setupFab(v : View) {
    v.findViewById<FloatingActionButton>(R.id.fab_add_note)?.let {
      it.setImageResource(R.drawable.ic_add)
      it.setOnClickListener {
        Navigation.findNavController(v).navigate(R.id.action_add_note)
      }
    }
  }

  private fun setupListAdapter() {
    notesFragmentBinding.notesList.adapter = NotesAdapter(ArrayList(), viewModel, activity)
    notesFragmentBinding.notesList.layoutManager = GridLayoutManager(context, 2)
  }

  private fun setupNavigationDrawer(v : View, drawerLayout: DrawerLayout) {
    drawerLayout.setStatusBarBackground(R.color.colorPrimaryDark)
    v.findViewById<NavigationView>(R.id.nav_view)?.let {
      setupDrawerContent(it, v)
    }
  }

  private fun setupRefreshLayout() {
    notesFragmentBinding.refreshLayout.setColorSchemeColors(
        ContextCompat.getColor(activity as AppCompatActivity, R.color.colorPrimary),
        ContextCompat.getColor(activity as AppCompatActivity, R.color.colorAccent),
        ContextCompat.getColor(activity as AppCompatActivity, R.color.colorPrimaryDark)
    )
    notesFragmentBinding.refreshLayout.setScrollUpChild(notesFragmentBinding.notesList)
  }

  private fun setupToolbar(v : View, activity : AppCompatActivity) {
    activity.setSupportActionBar(v.findViewById<Toolbar>(R.id.toolbar))
    activity.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
    activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
  }
}