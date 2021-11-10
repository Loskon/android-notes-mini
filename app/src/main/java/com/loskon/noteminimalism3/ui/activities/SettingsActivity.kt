package com.loskon.noteminimalism3.ui.activities

import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomappbar.BottomAppBar
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.other.FontManager
import com.loskon.noteminimalism3.sharedpref.PrefManager
import com.loskon.noteminimalism3.ui.fragments.SettingsFragment
import com.loskon.noteminimalism3.ui.sheets.SheetPrefSelectColor
import com.loskon.noteminimalism3.ui.sheets.SheetPrefSelectColorHex
import com.loskon.noteminimalism3.ui.snackbars.SnackbarManager
import com.loskon.noteminimalism3.utils.setMenuIconColor
import com.loskon.noteminimalism3.utils.setNavigationIconColor

/**
 * Хост представления для фрагментов
 */

class SettingsActivity : BaseActivity(),
    SheetPrefSelectColor.CallbackColorNavIcon,
    SheetPrefSelectColorHex.CallbackColorHexNavIcon {

    private lateinit var coordLayout: ConstraintLayout
    private lateinit var bottomAppBar: BottomAppBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        initViews()
        establishColorViews()
        openSettingsFragment(savedInstanceState)
        installCallbacks()
        configureBottomBarMenu()
    }

    private fun initViews() {
        coordLayout = findViewById(R.id.const_layout_settings)
        bottomAppBar = findViewById(R.id.bottom_bar_settings)
    }

    private fun establishColorViews() {
        val color = PrefManager.getAppColor(this)
        bottomAppBar.setNavigationIconColor(color)
        bottomAppBar.menu.setMenuIconColor(color)
    }

    private fun openSettingsFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container_settings, SettingsFragment())
                .commit()
        }
    }

    private fun installCallbacks() {
        SheetPrefSelectColor.listenerCallBackColorNavIcon(this)
        SheetPrefSelectColorHex.listenerCallBackColorNavIcon(this)
    }

    private fun configureBottomBarMenu() {
        visibilityMenuItemAccount(false)
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container_settings, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun showSnackbar(typeMessage: String) {
        SnackbarManager(this, coordLayout, bottomAppBar).show(typeMessage)
    }

    override fun onChangeColor(color: Int) {
        bottomAppBar.setNavigationIconColor(color)
        bottomAppBar.menu.setMenuIconColor(color)
    }

    fun visibilityMenuItemAccount(isVisible: Boolean) {
        bottomAppBar.menu.findItem(R.id.action_account).isVisible = isVisible
    }

    fun setAppFonts() {
        FontManager.setFont(this)
    }

    val bottomBar: BottomAppBar
        get() {
            return bottomAppBar
        }
}