package com.loskon.noteminimalism3.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.other.MyIntent
import com.loskon.noteminimalism3.auxiliary.sharedpref.GetSharedPref
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.ui.fragments.NoteFragmentKt
import com.loskon.noteminimalism3.ui.fragments.NoteTrashFragment
import com.loskon.noteminimalism3.viewmodel.NoteDetailViewModel
import com.loskon.noteminimalism3.viewmodel.NoteViewModel.Companion.CATEGORY_ALL_NOTES
import com.loskon.noteminimalism3.viewmodel.NoteViewModel.Companion.CATEGORY_FAVORITES
import com.loskon.noteminimalism3.viewmodel.NoteViewModel.Companion.CATEGORY_TRASH

/**
 *
 */

class NewNoteActivity : AppCompatActivity() {

    companion object {
        private val TAG = "MyLogs_${NewNoteActivity::class.java.simpleName}"
    }

    private lateinit var viewModel: NoteDetailViewModel

    private lateinit var note: Note2
    private var category: String = CATEGORY_ALL_NOTES
    private var fontSizeNote: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)

        viewModel = ViewModelProvider(this).get(NoteDetailViewModel::class.java)

        getArguments()
        if (category == CATEGORY_FAVORITES) note.isFavorite = true
        selectOpenFragment()
    }

    private fun getArguments() {
        val bundle = intent.extras

        if (bundle != null) {
            note = bundle.getParcelable(MyIntent.PUT_EXTRA_NOTE)!!
            category = bundle.getString(MyIntent.PUT_EXTRA_CATEGORY)!!
        }

        fontSizeNote = GetSharedPref.getFontSizeNote(this).toFloat()
    }

    private fun selectOpenFragment() {
        if (category == CATEGORY_TRASH) {
            startNoteListFragment(NoteTrashFragment.newInstance())
        } else {
            startNoteListFragment(NoteFragmentKt.newInstance())
        }
    }

    fun startNoteListFragment(fragment: Fragment) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_note)

        if (currentFragment == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container_note, fragment)
                .commit()
        }
    }

    // getters
    val getViewModel: NoteDetailViewModel
        get() {
            return viewModel
        }

    val getNote: Note2
        get() {
            return note
        }

    val getFontSize: Float
        get() {
            return fontSizeNote
        }

/*
    override fun onBackPressed() {
        lifecycleScope.launch {
            delay(200L)
            super.onBackPressed()
        }
    }*/
}