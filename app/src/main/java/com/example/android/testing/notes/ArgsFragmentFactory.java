package com.example.android.testing.notes;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.navigation.fragment.NavHostFragment;
import com.example.android.testing.notes.addeditnote.AddEditNoteFragment;
import com.example.android.testing.notes.notedetail.NoteDetailFragment;
import com.example.android.testing.notes.notes.NotesFragment;
import com.example.android.testing.notes.notes.NotesViewModel;
import com.example.android.testing.notes.statistics.StatisticsFragment;
import com.google.android.material.navigation.NavigationView;


public class ArgsFragmentFactory extends FragmentFactory {

  private final ViewModelFactory factory;

  public ArgsFragmentFactory(ViewModelFactory factory) {
    this.factory = factory;
  }

  @Override
  public Fragment instantiate(ClassLoader classLoader, String clazzName, Bundle args) {
    /* Ideally there'd be some java reflection here, but doesn't look like
     * any of the reflection methods I want to use are supported in lower
     * API versions.
     *
     * Also, doing an if-else chain instead of a switch statement to avoid
     * having to hardcode class names (which would scream potential bugs
     * if we ever moved classes around again)
     */
    if (clazzName.equals(AddEditNoteFragment.class.getName())) {
      return new AddEditNoteFragment();
    } else if (clazzName.equals(NoteDetailFragment.class.getName())) {
      return new NoteDetailFragment();
    } else if (clazzName.equals(NotesFragment.class.getName())) {
      return new NotesFragment(factory.create(NotesViewModel.class));
    } else if (clazzName.equals(StatisticsFragment.class.getName())) {
      return new StatisticsFragment();
    } else if (clazzName.equals(NavHostFragment.class.getName())) {
      return super.instantiate(classLoader, clazzName, args);
    } else {
      throw new IllegalArgumentException("Unknown class type: " + clazzName);
    }
  }
}
