package com.loskon.noteminimalism3.ui.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.command.ShortsCommandNote
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.sharedpref.PrefManager
import com.loskon.noteminimalism3.sqlite.DateBaseAdapter.Companion.CATEGORY_FAVORITES
import com.loskon.noteminimalism3.sqlite.DateBaseAdapter.Companion.CATEGORY_TRASH
import com.loskon.noteminimalism3.ui.fragments.NoteFragment
import com.loskon.noteminimalism3.ui.fragments.NoteTrashFragment
import com.loskon.noteminimalism3.utils.IntentUtil

/**
 * Выбор фрагмента для работы с заметкой
 */

class NoteActivity : BaseActivity() {

    private lateinit var shortsCommand: ShortsCommandNote
    private lateinit var note: Note

    private var color: Int = 0
    private var fontSizeNote: Int = 0
    private var noteCategory: String = ""
    private var hasReceivingText: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        getArguments()
        initObjects()
        otherConfigurations()
        setStatusFavorite()
        selectFragmentOpen()
    }


    private fun getArguments() {
        intent.getParcelableExtra<Note>(IntentUtil.PUT_EXTRA_NOTE)
            ?.let { note = it }
        intent.getStringExtra(IntentUtil.PUT_EXTRA_CATEGORY)
            ?.let { noteCategory = it }
        intent.getBooleanExtra(IntentUtil.PUT_EXTRA_HAS_RECEIVING_TEXT, false)
            .let { hasReceivingText = it }
    }

    private fun initObjects() {
        shortsCommand = ShortsCommandNote()
    }

    private fun otherConfigurations() {
        color = PrefManager.getAppColor(this)
        fontSizeNote = PrefManager.getFontSizeNote(this)
    }

    private fun setStatusFavorite() {
        if (noteCategory == CATEGORY_FAVORITES) note.isFavorite = true
    }

    private fun selectFragmentOpen() {
        if (noteCategory == CATEGORY_TRASH) {
            startFragment(NoteTrashFragment.newInstance())
        } else {
            startFragment(NoteFragment.newInstance())
        }
    }

    private fun startFragment(fragment: Fragment) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_note)

        if (currentFragment == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container_note, fragment)
                .commit()
        }
    }

    // getters
    fun getShortsCommand(): ShortsCommandNote {
        return shortsCommand
    }

    fun getColor(): Int {
        return color
    }

    fun getNote(): Note {
        return note
    }

    fun getFontSize(): Int {
        return fontSizeNote
    }

    val hasReceivText: Boolean
        get() {
            return hasReceivingText
        }
}