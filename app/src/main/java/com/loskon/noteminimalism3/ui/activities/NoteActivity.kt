package com.loskon.noteminimalism3.ui.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.managers.IntentManager
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.sharedpref.PrefHelper
import com.loskon.noteminimalism3.sqlite.DataBaseAdapter.Companion.CATEGORY_FAVORITES
import com.loskon.noteminimalism3.sqlite.DataBaseAdapter.Companion.CATEGORY_TRASH
import com.loskon.noteminimalism3.ui.fragments.NoteFragment
import com.loskon.noteminimalism3.ui.fragments.NoteTrashFragment

/**
 * Выбор фрагмента для работы с заметкой
 */

class NoteActivity : AppBaseActivity() {

    private lateinit var note: Note

    private var color: Int = 0
    private var noteFontSize: Int = 0
    private var category: String = ""
    private var hasReceivingText: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        getArguments()
        otherConfigurations()
        setStatusFavorite()
        selectFragmentOpen()
    }

    private fun getArguments() {
        intent.getParcelableExtra<Note>(IntentManager.PUT_EXTRA_NOTE)
            ?.let { note = it }
        intent.getStringExtra(IntentManager.PUT_EXTRA_CATEGORY)
            ?.let { category = it }
        intent.getBooleanExtra(IntentManager.PUT_EXTRA_HAS_RECEIVING_TEXT, false)
            .let { hasReceivingText = it }
    }

    private fun otherConfigurations() {
        color = PrefHelper.getAppColor(this)
        noteFontSize = PrefHelper.getNoteFontSize(this)
    }

    private fun setStatusFavorite() {
        if (category == CATEGORY_FAVORITES && note.id == 0L) note.isFavorite = true
    }

    private fun selectFragmentOpen() {
        if (category == CATEGORY_TRASH) {
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

    fun getColor(): Int {
        return color
    }

    fun getNote(): Note {
        return note
    }

    fun getNoteFontSize(): Int {
        return noteFontSize
    }

    val hasRecText: Boolean
        get() {
            return hasReceivingText
        }
}