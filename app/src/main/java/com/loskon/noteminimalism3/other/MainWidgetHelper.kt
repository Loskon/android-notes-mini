package com.loskon.noteminimalism3.other

import android.content.Context
import android.view.Menu
import android.view.View
import android.view.ViewGroup
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
import com.loskon.noteminimalism3.utils.setVisibilityKtx
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

        if (isSelectionMode) {
            if (isSearchMode) {
                searchView.enableSearchView(false)
                visibilityBottomBar(true)
            }

            changeFabIconForTrash(category)
        } else {
            if (isSearchMode) {
                searchView.enableSearchView(true)
                visibilityBottomBar(false)
                showKeyboardInSearchView()
                setIconFab(ICON_FAB_SEARCH_CLOSE)
            } else {
                changeFabIcon(category)
            }

            visibilityUnificationMenuItem(false)
            visibilityFavoriteMenuItem(category, false)
        }

        hasNavigationButtonIsCloseIcon(!isSelectionMode && !isSearchMode)
        visibilityHomeMenuItems(!isSelectionMode && !isSearchMode)
        visibilitySelectMenuItem(isSelectionMode)
        visibilityCardView(isSelectionMode)
    }

    fun selectingNote(
        category: String,
        selectedItemsCount: Int,
        hasAllSelected: Boolean
    ) {
        setCountItemsText(selectedItemsCount)
        changeIconSelectMenuItem(hasAllSelected)
        visibilityUnificationMenu(category, selectedItemsCount >= 2)
        visibilityFavoriteMenuItem(category, selectedItemsCount in 1..1)
    }

    fun togglingSearchMode(category: String, isSearchMode: Boolean) {
        searchView.setQuery("", false)
        searchView.setVisibilityKtx(isSearchMode)

        if (isSearchMode) {
            showKeyboardInSearchView()
            setIconFab(ICON_FAB_SEARCH_CLOSE)
        } else {
            hasNavigationButtonIsCloseIcon(true)
            visibilityHomeMenuItems(true)
            changeFabIcon(category)
        }

        visibilityBottomBar(!isSearchMode)
    }

    private fun showKeyboardInSearchView() {
        val searchEditText: EditText = searchView.findViewById(R.id.search_src_text)
        searchEditText.showKeyboard()
    }

    //--- Menu -------------------------------------------------------------------------------------
    private fun visibilityUnificationMenu(category: String, hasRequiredRange: Boolean) {
        val isVisible: Boolean = (category != CATEGORY_TRASH && hasRequiredRange)
        visibilityUnificationMenuItem(isVisible)
    }

    private fun visibilityUnificationMenuItem(isVisible: Boolean) {
        setVisibleMenuItem(R.id.action_unification, isVisible)
    }

    private fun visibilitySelectMenuItem(isVisible: Boolean) {
        setVisibleMenuItem(R.id.action_select_item, isVisible)
    }

    private fun visibilityFavoriteMenuItem(category: String, isVisible: Boolean) {
        val isVis: Boolean = (category != CATEGORY_TRASH && isVisible)
        setVisibleMenuItem(R.id.action_favorite, isVis)
    }

    private fun visibilityHomeMenuItems(isVisible: Boolean) {
        setVisibleMenuItem(R.id.action_search, isVisible)
        setVisibleMenuItem(R.id.action_toggle_view, isVisible)
    }

    private fun setVisibleMenuItem(menuId: Int, isVisible: Boolean) {
        barMenu.findItem(menuId).isVisible = isVisible
    }

    private fun changeIconSelectMenuItem(isSelectOne: Boolean) {
        val menuId: Int = if (isSelectOne) {
            R.drawable.baseline_done_black_24
        } else {
            R.drawable.baseline_done_all_black_24
        }

        replaceMenuIcon(R.id.action_select_item, menuId)
    }

    fun changeIconFavoriteMenuItem(isFavorite: Boolean) {
        val menuId: Int = if (isFavorite) {
            R.drawable.baseline_star_black_24
        } else {
            R.drawable.baseline_star_border_black_24
        }

        replaceMenuIcon(R.id.action_favorite, menuId)
    }

    fun changeIconToggleViewMenuItem(hasLinearList: Boolean) {
        val menuId: Int = if (hasLinearList) {
            R.drawable.outline_dashboard_black_24
        } else {
            R.drawable.outline_view_agenda_black_24
        }

        replaceMenuIcon(R.id.action_toggle_view, menuId)
    }

    private fun replaceMenuIcon(menuItem: Int, icon: Int) {
        barMenu.findItem(menuItem).icon = context.getShortDrawable(icon)
        establishMenuIconsColor()
    }

    //--- Bottom Bar -------------------------------------------------------------------------------
    private fun hasNavigationButtonIsCloseIcon(isIconClose: Boolean) {
        val navIconId: Int = if (isIconClose) {
            R.drawable.baseline_menu_black_24
        } else {
            R.drawable.baseline_close_black_24
        }

        bottomBar.setNavigationIcon(navIconId)
        bottomBar.setNavigationIconColor(color)
    }

    //--- Fab --------------------------------------------------------------------------------------
    fun changeFabIcon(category: String) {
        when (category) {
            CATEGORY_ALL_NOTES -> setIconFab(ICON_FAB_ADD)
            CATEGORY_FAVORITES -> setIconFab(ICON_FAB_STAR)
            CATEGORY_TRASH -> setIconFab(ICON_FAB_DELETE)
            else -> throw RuntimeException("Invalid icon")
        }
    }

    private fun changeFabIconForTrash(category: String) {
        if (category == CATEGORY_TRASH) {
            setIconFab(ICON_FAB_DELETE_FOREVER)
        } else {
            setIconFab(ICON_FAB_DELETE)
        }
    }

    private fun setIconFab(iconType: String) {
        val drawableId: Int = when (iconType) {
            ICON_FAB_ADD -> R.drawable.baseline_add_black_24
            ICON_FAB_STAR -> R.drawable.baseline_star_black_24
            ICON_FAB_DELETE -> R.drawable.baseline_delete_black_24
            ICON_FAB_DELETE_FOREVER -> R.drawable.baseline_delete_forever_black_24
            ICON_FAB_SEARCH_CLOSE -> R.drawable.baseline_search_off_black_24
            else -> throw RuntimeException("Invalid icon")
        }

        fab.setImageDrawable(context.getShortDrawable(drawableId))
    }

    private fun visibilityBottomBar(isVisible: Boolean) {
        if (isVisible) {
            bottomBar.performShow()
        } else {
            bottomBar.performHide()
        }
    }

    //--- CardView ---------------------------------------------------------------------------------
    private fun visibilityCardView(isVisible: Boolean) {
        cardView.setVisibilityKtx(isVisible)
    }

    //--- TextView ---------------------------------------------------------------------------------
    private fun setCountItemsText(selectedItemsCount: Int) {
        tvCountItems.text = selectedItemsCount.toString()
    }
}

// Extension functions
fun View.enableSearchView(enabled: Boolean) {
    this.isEnabled = enabled
    if (this is ViewGroup) {
        val viewGroup = this
        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)
            child.enableSearchView(enabled)
        }
    }
}