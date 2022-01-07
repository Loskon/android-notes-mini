package com.loskon.noteminimalism3.other

import android.view.Menu
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.managers.setFabColor
import com.loskon.noteminimalism3.managers.setMenuIconsColor
import com.loskon.noteminimalism3.managers.setNavigationIconColor
import com.loskon.noteminimalism3.sqlite.DataBaseAdapter.Companion.CATEGORY_ALL_NOTES
import com.loskon.noteminimalism3.sqlite.DataBaseAdapter.Companion.CATEGORY_FAVORITES
import com.loskon.noteminimalism3.sqlite.DataBaseAdapter.Companion.CATEGORY_TRASH
import com.loskon.noteminimalism3.ui.activities.MainActivity
import com.loskon.noteminimalism3.utils.getShortDrawable

/**
 * Помощник для управления вьюшками
 */

class MainWidgetHelper(
    private val activity: MainActivity,
    private val fab: FloatingActionButton,
    private val bottomBar: BottomAppBar
) {

    companion object {
        const val ICON_FAB_ADD = "fab_icon_add"
        const val ICON_FAB_STAR = "fab_icon_star"
        const val ICON_FAB_DELETE = "fab_icon_delete"
        const val ICON_FAB_DELETE_FOREVER = "fab_icon_delete_forever"
        const val ICON_FAB_SEARCH_CLOSE = "fab_icon_search_close"
    }

    private val barMenu: Menu = bottomBar.menu

    private var color: Int = 0

    fun setColorViews(color: Int) {
        this.color = color
        establishViewsColor()
        establishMenuIconsColor()
    }

    private fun establishViewsColor() {
        fab.setFabColor(color)
        bottomBar.setNavigationIconColor(color)
    }

    private fun establishMenuIconsColor() {
        barMenu.setMenuIconsColor(color)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Menu
    fun changeVisibleUnification(notesCategory: String, hasRequiredRange: Boolean) {
        val isVisible: Boolean = (notesCategory != CATEGORY_TRASH && hasRequiredRange)
        setVisibleUnification(isVisible)
    }

    fun setVisibleUnification(isVisible: Boolean) {
        setVisibleMenuItem(R.id.action_unification, isVisible)
    }

    fun setVisibleSelect(isVisible: Boolean) {
        setVisibleMenuItem(R.id.action_select_item, isVisible)
    }

    fun setVisibleFavorite(notesCategory: String, isVisible: Boolean) {
        val isVis: Boolean = (notesCategory != CATEGORY_TRASH && isVisible)
        setVisibleMenuItem(R.id.action_favorite, isVis)
    }

    fun setVisibleSearchAndSwitch(isVisible: Boolean) {
        setVisibleMenuItem(R.id.action_search, isVisible)
        setVisibleMenuItem(R.id.action_switch_view, isVisible)
    }

    private fun setVisibleMenuItem(menuId: Int, isVisible: Boolean) {
        barMenu.findItem(menuId).isVisible = isVisible
    }

    fun changeIconMenuSelect(isSelectOne: Boolean) {
        val menuId: Int = if (isSelectOne) {
            R.drawable.baseline_done_black_24
        } else {
            R.drawable.baseline_done_all_black_24
        }

        replaceMenuIcon(R.id.action_select_item, menuId)
    }

    fun changeMenuIconFavorite(isFavorite: Boolean) {
        val menuId: Int = if (isFavorite) {
            R.drawable.baseline_star_black_24
        } else {
            R.drawable.baseline_star_border_black_24
        }

        replaceMenuIcon(R.id.action_favorite, menuId)
    }

    fun changeMenuItemForLinearList(hasLinearList: Boolean) {
        val menuId: Int = if (hasLinearList) {
            R.drawable.outline_dashboard_black_24
        } else {
            R.drawable.outline_view_agenda_black_24
        }

        replaceMenuIcon(R.id.action_switch_view, menuId)
    }

    private fun replaceMenuIcon(menuItem: Int, icon: Int) {
        barMenu.findItem(menuItem).icon = activity.getShortDrawable(icon)
        establishMenuIconsColor()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Bottom bar
    fun setNavigationIcon(isIconClose: Boolean) {
        val navIconId: Int = if (isIconClose) {
            R.drawable.baseline_menu_black_24
        } else {
            R.drawable.baseline_close_black_24
        }

        bottomBar.setNavigationIcon(navIconId)
        bottomBar.setNavigationIconColor(color)
    }

    fun hideNavigationIcon() {
        bottomBar.navigationIcon = null
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Fab
    fun setIconFabCategory(category: String) {
        when (category) {
            CATEGORY_ALL_NOTES -> setIconFab(ICON_FAB_ADD)
            CATEGORY_FAVORITES -> setIconFab(ICON_FAB_STAR)
            CATEGORY_TRASH -> setIconFab(ICON_FAB_DELETE)
            else -> throw RuntimeException("Invalid icon")
        }
    }

    fun setDeleteIconFab(category: String) {
        if (category == CATEGORY_TRASH) {
            setIconFab(ICON_FAB_DELETE_FOREVER)
        } else {
            setIconFab(ICON_FAB_DELETE)
        }
    }

    fun setIconFab(iconName: String) {
        val drawableId: Int = when (iconName) {
            ICON_FAB_ADD -> R.drawable.baseline_add_black_24
            ICON_FAB_STAR -> R.drawable.baseline_star_black_24
            ICON_FAB_DELETE -> R.drawable.baseline_delete_black_24
            ICON_FAB_DELETE_FOREVER -> R.drawable.baseline_delete_forever_black_24
            ICON_FAB_SEARCH_CLOSE -> R.drawable.baseline_search_off_black_24
            else -> throw RuntimeException("Invalid icon")
        }

        fab.setImageDrawable(activity.getShortDrawable(drawableId))
    }

    fun bottomBarVisible(isVisible: Boolean) {
        if (isVisible) {
            bottomBar.performShow()
        } else {
            bottomBar.performHide()
        }
    }
}