package com.loskon.noteminimalism3.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.ui.fragments.NoteFragment
import com.loskon.noteminimalism3.ui.fragments.NoteListFragment

/**
 *
 */

class ListActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private lateinit var widgetHelper: WidgetHelperList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        initialiseSettings()


        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val fragment = NoteListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    private fun initialiseSettings() {
        widgetHelper = WidgetHelperList(this)
    }

    fun openItem(note: Note2) {
        val fragment = NoteFragment.newInstance(note)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    val getWidgetsHelper: WidgetHelperList
        get() {
            return widgetHelper
        }

    fun op() {
        val intent = Intent(this, BackupActivity::class.java)
        startActivity(intent)
    }
}