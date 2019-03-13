package com.example.android.testing.notes.notes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.example.android.testing.notes.R
import com.example.android.testing.notes.ViewModelFactory

class NotesActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState : Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  companion object {
    fun <T : ViewModel> obtainViewModel(activity : FragmentActivity, viewModelClass : Class<T>) =
        ViewModelProviders.of(activity, ViewModelFactory.getInstance(activity.application)).get(viewModelClass)
  }
}

