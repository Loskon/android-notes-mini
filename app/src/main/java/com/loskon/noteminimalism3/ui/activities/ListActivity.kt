package com.loskon.noteminimalism3.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.ui.fragments.NoteFragment
import com.loskon.noteminimalism3.ui.fragments.NoteListFragment3
import com.loskon.noteminimalism3.viewmodel.NoteViewModel

/**
 * Хост для фрагментов
 */

/*private val TAG = MainActivity::class.java.simpleName*/

class ListActivity : AppCompatActivity() {

    private lateinit var viewModel: NoteViewModel
    private lateinit var widgetsHelper: WidgetHelperList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        initializationObjects()
        startNoteListFragment()
    }

    private fun initializationObjects() {
        viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        widgetsHelper = WidgetHelperList(this)
    }

    fun startNoteListFragment() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val fragment = NoteListFragment3.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }


    // Public method
    fun openNoteFragment(note: Note2) {
        widgetsHelper.setVisibleWidgets(false)

        val fragment = NoteFragment.newInstance(note)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }


    // Getters
    val getViewModel: NoteViewModel
        get() {
            return viewModel
        }

    val getWidgetsHelper: WidgetHelperList
        get() {
            return widgetsHelper
        }
}