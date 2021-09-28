package com.loskon.noteminimalism3.ui.activities.update

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.other.MyColor
import com.loskon.noteminimalism3.auxiliary.sharedpref.GetSharedPref
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.sqlite.DateBaseAdapter.Companion.CATEGORY_FAVORITES
import com.loskon.noteminimalism3.sqlite.DateBaseAdapter.Companion.CATEGORY_TRASH
import com.loskon.noteminimalism3.ui.fragments.update.NoteFragmentUpdate
import com.loskon.noteminimalism3.ui.fragments.update.NoteTrashFragmentUpdate
import com.loskon.noteminimalism3.utils.IntentUtil
import com.loskon.noteminimalism3.viewmodel.AppShortsCommand

/**
 * Выбор фрагмента для работы с заметкой
 */

class NoteActivityUpdate : AppCompatActivity() {

    companion object {
        private val TAG = "MyLogs_${NoteActivityUpdate::class.java.simpleName}"
    }

    private lateinit var shortsCommand: AppShortsCommand

    private lateinit var note: Note2
    private var category: String = ""
    private var color: Int = 0
    private var fontSizeNote: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)

        shortsCommand = AppShortsCommand()
        color = MyColor.getMyColor(this)

        getArguments()
        if (category == CATEGORY_FAVORITES) note.isFavorite = true
        selectFragment()
    }

    private fun getArguments() {
        val bundle = intent.extras

        if (bundle != null) {
            note = bundle.getParcelable(IntentUtil.PUT_EXTRA_NOTE)!!
            category = bundle.getString(IntentUtil.PUT_EXTRA_CATEGORY)!!
        }

        fontSizeNote = GetSharedPref.getFontSizeNote(this).toFloat()
    }

    private fun selectFragment() {
        if (category == CATEGORY_TRASH) {
            startNoteFragment(NoteTrashFragmentUpdate.newInstance())
        } else {
            startNoteFragment(NoteFragmentUpdate.newInstance())
        }
    }

    private fun startNoteFragment(fragment: Fragment) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_note)

        if (currentFragment == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container_note, fragment)
                .commit()
        }
    }

    // getters
    val getShortsCommand: AppShortsCommand
        get() {
            return shortsCommand
        }

    val getColor: Int
        get() {
            return color
        }

    val getNote: Note2
        get() {
            return note
        }

    val getFontSize: Float
        get() {
            return fontSizeNote
        }
}