package com.loskon.noteminimalism3.other

import android.content.Context
import android.view.Menu
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.managers.setBackgroundTintColor
import com.loskon.noteminimalism3.managers.setFabColor
import com.loskon.noteminimalism3.managers.setMenuIconsColor
import com.loskon.noteminimalism3.managers.setNavigationIconColor
import com.loskon.noteminimalism3.sqlite.DataBaseAdapter.Companion.CATEGORY_ALL_NOTES
import com.loskon.noteminimalism3.sqlite.DataBaseAdapter.Companion.CATEGORY_FAVORITES
import com.loskon.noteminimalism3.sqlite.DataBaseAdapter.Companion.CATEGORY_TRASH
import com.loskon.noteminimalism3.utils.getShortDrawable
import com.loskon.noteminimalism3.utils.setVisibleView
import com.loskon.noteminimalism3.utils.showKeyboard

/**
 * Помощник для управления вьюшками
 */

private const val ICON_FAB_ADD = "fab_icon_add"
private const val ICON_FAB_STAR = "fab_icon_star"
private const val ICON_FAB_DELETE = "fab_icon_delete"
private const val ICON_FAB_DELETE_FOREVER = "fab_icon_delete_forever"
private const val ICON_FAB_SEARCH_CLOSE = "fab_icon_search_close"

class MainWidgetHelper(
    private val context: Context,
    private val searchView: SearchView,
    private val fab: FloatingActionButton,
    private val cardView: CardView,
    private val tvCountItems: TextView,
    private val bottomBar: BottomAppBar
) {

    private val barMenu: Menu = bottomBar.menu

    private var color: Int = 0

    fun setColorsViews(color: Int) {
        this.color = color
        establishViewsColor()
        establishMenuIconsColor()
    }

    private fun establishViewsColor() {
        fab.setFabColor(color)
        cardView.setBackgroundTintColor(color)
        bottomBar.setNavigationIconColor(color)
    }

    private fun establishMenuIconsColor() {
        barMenu.setMenuIconsColor(color)
    }

    //--- Different actions ------------------------------------------------------------------------
    fun changingViewsForSelectionMode(
        category: String,
        isSelectionMode: Boolean,
        isSearchMode: Boolean
    ) {
        setVisibleSelectMenuItem(isSelectionMode)
        cardViewVisible(isSelectionMode)

        if (isSelectionMode) {
            setVisibleSearchAndSwitchMenuItems(false)
            setNavigationIcon(false)
            setDeleteIconFab(category)

            if (isSearchMode) bottomBarVisible(true)
        } else {
            setVisibleUnificationMenuItem(false)
            setVisibleFavoriteMenuItem(category, false)

            if (isSearchMode) {
                setVisibleSearchAndSwitchMenuItems(false)
                hideNavigationIcon()
                bottomBarVisible(false)
                setIconFab(ICON_FAB_SEARCH_CLOSE)
            } else {
                setVisibleSearchAndSwitchMenuItems(true)
                setNavigationIcon(true)
                setIconFabCategory(category)
            }
        }
    }

    fun selectingNote(
        category: String,
        selectedItemsCount: Int,
        hasAllSelected: Boolean
    ) {
        setCountItemsText(selectedItemsCount)
        changeIconMenuSelect(hasAllSelected)
        changeVisibleUnification(category, selectedItemsCount >= 2)
        setVisibleFavoriteMenuItem(category, selectedItemsCount in 1..1)
    }

    fun activatingSearchMode(category: String, isSearchMode: Boolean) {
        searchView.setQuery("", false)
        searchView.setVisibleView(isSearchMode)
        bottomBarVisible(!isSearchMode)

        if (isSearchMode) {
            val searchEditText: EditText = searchView.findViewById(R.id.search_src_text)
            searchEditText.showKeyboard(context)
            setIconFab(ICON_FAB_SEARCH_CLOSE)
        } else {
            setNavigationIcon(true)
            setVisibleSearchAndSwitchMenuItems(true)
            setIconFabCategory(category)
        }
    }

    //--- Menu -------------------------------------------------------------------------------------
    private fun changeVisibleUnification(category: String, hasRequiredRange: Boolean) {
        val isVisible: Boolean = (category != CATEGORY_TRASH && hasRequiredRange)
        setVisibleUnificationMenuItem(isVisible)
    }

    private fun setVisibleUnificationMenuItem(isVisible: Boolean) {
        setVisibleMenuItem(R.id.action_unification, isVisible)
    }

    private fun setVisibleSelectMenuItem(isVisible: Boolean) {
        setVisibleMenuItem(R.id.action_select_item, isVisible)
    }

    private fun setVisibleFavoriteMenuItem(category: String, isVisible: Boolean) {
        val isVis: Boolean = (category != CATEGORY_TRASH && isVisible)
        setVisibleMenuItem(R.id.action_favorite, isVis)
    }

    private fun setVisibleSearchAndSwitchMenuItems(isVisible: Boolean) {
        setVisibleMenuItem(R.id.action_search, isVisible)
        setVisibleMenuItem(R.id.action_switch_view, isVisible)
    }

    private fun setVisibleMenuItem(menuId: Int, isVisible: Boolean) {
        barMenu.findItem(menuId).isVisible = isVisible
    }

    private fun changeIconMenuSelect(isSelectOne: Boolean) {
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
        barMenu.findItem(menuItem).icon = context.getShortDrawable(icon)
        establishMenuIconsColor()
    }

    //--- Bottom Bar -------------------------------------------------------------------------------
    private fun setNavigationIcon(isIconClose: Boolean) {
        val navIconId: Int = if (isIconClose) {
            R.drawable.baseline_menu_black_24
        } else {
            R.drawable.baseline_close_black_24
        }

        bottomBar.setNavigationIcon(navIconId)
        bottomBar.setNavigationIconColor(color)
    }

    private fun hideNavigationIcon() {
        bottomBar.navigationIcon = null
    }

    //--- Fab --------------------------------------------------------------------------------------
    fun setIconFabCategory(category: String) {
        when (category) {
            CATEGORY_ALL_NOTES -> setIconFab(ICON_FAB_ADD)
            CATEGORY_FAVORITES -> setIconFab(ICON_FAB_STAR)
            CATEGORY_TRASH -> setIconFab(ICON_FAB_DELETE)
            else -> throw RuntimeException("Invalid icon")
        }
    }

    private fun setDeleteIconFab(category: String) {
        if (category == CATEGORY_TRASH) {
            setIconFab(ICON_FAB_DELETE_FOREVER)
        } else {
            setIconFab(ICON_FAB_DELETE)
        }
    }

    private fun setIconFab(iconName: String) {
        val drawableId: Int = when (iconName) {
            ICON_FAB_ADD -> R.drawable.baseline_add_black_24
            ICON_FAB_STAR -> R.drawable.baseline_star_black_24
            ICON_FAB_DELETE -> R.drawable.baseline_delete_black_24
            ICON_FAB_DELETE_FOREVER -> R.drawable.baseline_delete_forever_black_24
            ICON_FAB_SEARCH_CLOSE -> R.drawable.baseline_search_off_black_24
            else -> throw RuntimeException("Invalid icon")
        }

        fab.setImageDrawable(context.getShortDrawable(drawableId))
    }

    private fun bottomBarVisible(isVisible: Boolean) {
        if (isVisible) {
            bottomBar.performShow()
        } else {
            bottomBar.performHide()
        }
    }

    //--- CardView ---------------------------------------------------------------------------------
    private fun cardViewVisible(isVisible: Boolean) {
        cardView.setVisibleView(isVisible)
    }

    //--- TextView ---------------------------------------------------------------------------------
    private fun setCountItemsText(selectedItemsCount: Int) {
        tvCountItems.text = selectedItemsCount.toString()
    }
}