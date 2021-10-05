package com.loskon.noteminimalism3.ui.activities.update

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.sharedpref.AppPref
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.other.AppFont
import com.loskon.noteminimalism3.sqlite.AppShortsCommand
import com.loskon.noteminimalism3.sqlite.DateBaseAdapter.Companion.CATEGORY_FAVORITES
import com.loskon.noteminimalism3.sqlite.DateBaseAdapter.Companion.CATEGORY_TRASH
import com.loskon.noteminimalism3.ui.fragments.update.NoteFragmentUpdate
import com.loskon.noteminimalism3.ui.fragments.update.NoteTrashFragmentUpdate
import com.loskon.noteminimalism3.utils.IntentUtil

/**
 * Выбор фрагмента для работы с заметкой
 */

class NoteActivityUpdate : AppCompatActivity() {

    private lateinit var shortsCommand: AppShortsCommand
    private lateinit var note: Note2

    private var color: Int = 0
    private var fontSizeNote: Int = 0
    private var noteCategory: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        AppFont.setFont(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)

        getArguments()
        initObjects()
        otherConfigurations()
        setStatusFavorite()
        selectFragmentOpen()
    }

    private fun initObjects() {
        shortsCommand = AppShortsCommand()
    }

    private fun otherConfigurations() {
        color = AppPref.getAppColor(this)
        fontSizeNote = AppPref.getFontSizeNote(this)
    }

    private fun getArguments() {
        intent.getParcelableExtra<Note2>(IntentUtil.PUT_EXTRA_NOTE)?.let { note = it }
        intent.getStringExtra(IntentUtil.PUT_EXTRA_CATEGORY)?.let { noteCategory = it }
    }

    private fun setStatusFavorite() {
        if (noteCategory == CATEGORY_FAVORITES) note.isFavorite = true
    }

    private fun selectFragmentOpen() {
        if (noteCategory == CATEGORY_TRASH) {
            startFragment(NoteTrashFragmentUpdate.newInstance())
        } else {
            startFragment(NoteFragmentUpdate.newInstance())
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
    fun getShortsCommand(): AppShortsCommand {
        return shortsCommand
    }

    fun getColor(): Int {
        return color
    }

    fun getNote(): Note2 {
        return note
    }

    fun getFontSize(): Int {
        return fontSizeNote
    }
}