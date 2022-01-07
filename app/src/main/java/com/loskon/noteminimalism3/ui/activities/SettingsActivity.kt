package com.loskon.noteminimalism3.ui.activities

import android.os.Bundle
import android.view.Menu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomappbar.BottomAppBar
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.managers.FontManager
import com.loskon.noteminimalism3.managers.setMenuIconsColor
import com.loskon.noteminimalism3.managers.setNavigationIconColor
import com.loskon.noteminimalism3.sharedpref.PrefHelper
import com.loskon.noteminimalism3.ui.fragments.SettingsFragment
import com.loskon.noteminimalism3.ui.prefscreen.PrefScreenResetColor
import com.loskon.noteminimalism3.ui.sheets.SelectColorHexSheetDialog
import com.loskon.noteminimalism3.ui.sheets.SelectColorPickerSheetDialog
import com.loskon.noteminimalism3.ui.snackbars.SnackbarControl

/**
 * Хост представления для фрагментов
 */

class SettingsActivity : AppBaseActivity(),
    SelectColorPickerSheetDialog.ColorNavIconCallback,
    SelectColorHexSheetDialog.ColorHexNavIconCallback,
    PrefScreenResetColor.ColorResetNavIconCallback {

    private lateinit var constLayout: ConstraintLayout
    private lateinit var bottomBar: BottomAppBar
    private lateinit var fragmentManager: FragmentManager
    private lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        installCallbacks()
        setupViewDeclaration()
        initObjects()
        establishViewsColor()
        openSettingsFragment(savedInstanceState)
        installHandlersForViews()
        configureBottomBarMenu()
    }

    private fun installCallbacks() {
        SelectColorPickerSheetDialog.registerCallbackColorNavIcon(this)
        SelectColorHexSheetDialog.registerCallbackColorNavIcon(this)
        PrefScreenResetColor.registerCallbackColorNavIcon(this)
    }

    private fun setupViewDeclaration() {
        constLayout = findViewById(R.id.const_layout_settings)
        bottomBar = findViewById(R.id.bottom_bar_settings)
    }

    private fun initObjects() {
        fragmentManager = supportFragmentManager
        menu = bottomBar.menu
    }

    private fun establishViewsColor() {
        val color: Int = PrefHelper.getAppColor(this)
        establishViewsColor(color)
    }

    private fun establishViewsColor(color: Int) {
        bottomBar.setNavigationIconColor(color)
        menu.setMenuIconsColor(color)
    }

    private fun openSettingsFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            fragmentManager
                .beginTransaction()
                .add(R.id.fragment_container_settings, SettingsFragment())
                .commit()
        }
    }

    private fun installHandlersForViews() {
        bottomBar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun configureBottomBarMenu() = changeVisibilityMenuItem(false)

    fun replaceFragment(fragment: Fragment) {
        fragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container_settings, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun showSnackbar(typeMessage: String) {
        SnackbarControl(constLayout, bottomBar).show(typeMessage)
    }

    override fun onChangeColor(color: Int) = establishViewsColor(color)

    fun changeVisibilityMenuItem(isVisible: Boolean) {
        menu.findItem(R.id.action_account).isVisible = isVisible
    }

    fun setAppFonts() {
        FontManager.setFont(this)
    }

    val bottomAppBar: BottomAppBar
        get() {
            return bottomBar
        }
}