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
import com.loskon.noteminimalism3.ui.sheets.SheetPrefSelectColor
import com.loskon.noteminimalism3.ui.snackbars.update.SnackbarApp
import com.loskon.noteminimalism3.utils.setMenuIconColor
import com.loskon.noteminimalism3.utils.setNavigationIconColor

/**
 * Хост представления для фрагментов
 */

class SettingsActivityUpdate : AppCompatActivity(),
    SheetPrefSelectColor.CallbackColorNavIcon {

    private lateinit var coordLayout: ConstraintLayout
    private lateinit var bottomAppBar: BottomAppBar

    override fun onCreate(savedInstanceState: Bundle?) {
        setAppFonts()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        initViews()
        establishColorViews()
        openSettingsFragment(savedInstanceState)
        installCallbacks()
        configureBottomBarMenu()
    }

    fun setAppFonts() {
        AppFont.setFont(this)
    }

    private fun initViews() {
        coordLayout = findViewById(R.id.cstLytSettings)
        bottomAppBar = findViewById(R.id.btmAppBarSettings)
    }

    private fun establishColorViews() {
        val color = AppPref.getAppColor(this)
        bottomAppBar.setNavigationIconColor(color)
        bottomAppBar.menu.setMenuIconColor(color)
    }

    private fun openSettingsFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_settings, SettingsFragmentUpdate())
                .commit()
        }
    }

    private fun installCallbacks() {
        SheetPrefSelectColor.regCallBackColorNavIcon(this)
    }

    private fun configureBottomBarMenu() {
        visibilityMenuItemAccount(false)
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_settings, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun showSnackbar(typeMessage: String) {
        SnackbarApp(this, coordLayout, bottomAppBar).show(typeMessage)
    }

    override fun onChangeColor(color: Int) {
        bottomAppBar.setNavigationIconColor(color)
        bottomAppBar.menu.setMenuIconColor(color)
    }

    fun visibilityMenuItemAccount(isVisible: Boolean) {
        bottomAppBar.menu.findItem(R.id.action_account).isVisible = isVisible
    }


    val bottomBar: BottomAppBar
        get() {
            return bottomAppBar
        }
}