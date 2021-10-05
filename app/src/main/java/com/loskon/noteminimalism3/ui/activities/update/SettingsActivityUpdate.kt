package com.loskon.noteminimalism3.ui.activities.update

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomappbar.BottomAppBar
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.sharedpref.AppPref
import com.loskon.noteminimalism3.other.AppFont
import com.loskon.noteminimalism3.ui.fragments.update.SettingsFragmentUpdate
import com.loskon.noteminimalism3.ui.snackbars.update.SnackbarApp
import com.loskon.noteminimalism3.utils.setNavigationIconColor

/**
 * Хост представления для фрагментов
 */

class SettingsActivityUpdate : AppCompatActivity() {

    private lateinit var coordLayout: ConstraintLayout
    private lateinit var bottomAppBar: BottomAppBar


    override fun onCreate(savedInstanceState: Bundle?) {
        setAppFonts()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        initViews()
        establishColorViews()
        if (savedInstanceState == null) openSettingsFragment()
    }

    fun setAppFonts() {
        AppFont.setFont(this)
    }

    private fun initViews() {
        bottomAppBar = findViewById(R.id.btmAppBarSettings)
        coordLayout = findViewById(R.id.cstLytSettings )
    }

    private fun establishColorViews() {
        val color = AppPref.getAppColor(this)
        bottomAppBar.setNavigationIconColor(color)
    }

    private fun openSettingsFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_settings, SettingsFragmentUpdate())
            .commit()
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_settings, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun showSnackbar(message: String, isSuccess: Boolean) {
        SnackbarApp(this, coordLayout, bottomAppBar).show(message, isSuccess)
    }

    val bottomBar: BottomAppBar
        get() {
            return bottomAppBar
        }
}