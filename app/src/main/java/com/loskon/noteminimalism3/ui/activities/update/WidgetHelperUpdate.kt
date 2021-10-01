package com.loskon.noteminimalism3.ui.activities.update

import android.animation.ObjectAnimator
import android.content.Context
import android.util.Log
import android.view.Menu
import android.widget.GridLayout.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.sqlite.DateBaseAdapter.Companion.CATEGORY_ALL_NOTES
import com.loskon.noteminimalism3.sqlite.DateBaseAdapter.Companion.CATEGORY_FAVORITES
import com.loskon.noteminimalism3.sqlite.DateBaseAdapter.Companion.CATEGORY_TRASH
import com.loskon.noteminimalism3.utils.getShortDrawable
import com.loskon.noteminimalism3.utils.menuIconColor
import com.loskon.noteminimalism3.utils.setFabColor
import com.loskon.noteminimalism3.utils.setNavigationIconColor

/**
 * Помощник для управления элементами активити
 */

class WidgetHelperUpdate(
    private val context: Context,
    private val fab: FloatingActionButton,
    private val bottomBar: BottomAppBar
) {

    companion object {
        private val TAG = "MyLogs_${WidgetHelperUpdate::class.java.simpleName}"

        const val ICON_FAB_ADD = "fab_icon_add"
        const val ICON_FAB_SEARCH_CLOSE = "fab_icon_search_close"
        const val ICON_FAB_DELETE = "fab_icon_delete"
    }

    private val barMenu: Menu = bottomBar.menu

    private var color: Int = 0

    init {
        Log.d(TAG, "initialization")
    }

    fun setColorViews(color: Int) {
        this.color = color
        setColorWidgets()
        setMenuIconColor()
    }

    private fun setColorWidgets() {
        bottomBar.setNavigationIconColor(color)
        fab.setFabColor(color)
    }

    // Menu
    private fun setMenuIconColor() {
        barMenu.menuIconColor(color)
    }

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

    fun setVisibleSearchAndSwitch(isVisible: Boolean) {
        setVisibleMenuItem(R.id.action_switch_view, isVisible)
        setVisibleMenuItem(R.id.action_search, isVisible)
        setMenuIconColor()
    }

    private fun setVisibleMenuItem(menuId: Int, isVisible: Boolean) {
        barMenu.findItem(menuId).isVisible = isVisible
    }

    fun setSelectIcon(isSelectOne: Boolean) {
        val menuIdSelect: Int = if (isSelectOne) {
            R.drawable.baseline_done_black_24
        } else {
            R.drawable.baseline_done_all_black_24
        }

        setMenuIcon(R.id.action_select_item, menuIdSelect)
        setMenuIconColor()
    }

    fun setTypeNotes(recyclerView: RecyclerView, isTypeNotesSingle: Boolean) {
        val menuIdType: Int

        if (isTypeNotesSingle) {
            menuIdType = R.drawable.outline_dashboard_black_24
            recyclerView.layoutManager = LinearLayoutManager(context)
        } else {
            menuIdType = R.drawable.outline_view_agenda_black_24
            recyclerView.layoutManager = StaggeredGridLayoutManager(2, VERTICAL)
        }

        setMenuIcon(R.id.action_switch_view, menuIdType)
        setMenuIconColor()
    }

    private fun setMenuIcon(menuItem: Int, icon: Int) {
        barMenu.findItem(menuItem).icon = context.getShortDrawable(icon)
    }


    // Bottom Bar
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

    // Fab
    fun setIconFabCategory(notesCategory: String) {
        val iconIdFab: Int = when (notesCategory) {
            CATEGORY_ALL_NOTES -> R.drawable.baseline_add_black_24
            CATEGORY_FAVORITES -> R.drawable.baseline_star_black_24
            CATEGORY_TRASH -> R.drawable.baseline_delete_black_24
            else -> throw Exception("Invalid icon")
        }

        fab.setImageDrawable(context.getShortDrawable(iconIdFab))
    }

    fun setDeleteIconFab(notesCategory: String) {
        val iconIdFab = if (notesCategory == CATEGORY_TRASH) {
            R.drawable.baseline_delete_forever_black_24
        } else {
            R.drawable.baseline_delete_black_24
        }

        fab.setImageDrawable(context.getShortDrawable(iconIdFab))
    }

    fun startAnimateFab() {
        ObjectAnimator.ofFloat(fab, "rotation", 0f, 360f)
            .setDuration(300).start()
    }

    fun setIconFab(iconName: String) {
        val iconIdFab: Int = when (iconName) {
            ICON_FAB_ADD -> R.drawable.baseline_add_black_24
            ICON_FAB_SEARCH_CLOSE -> R.drawable.baseline_search_off_black_24
            ICON_FAB_DELETE -> R.drawable.baseline_delete_black_24
            else -> throw Exception("Invalid icon")
        }

        fab.setImageDrawable(context.getShortDrawable(iconIdFab))
    }

    fun setIconFab(category: String, isSearch: Boolean) {
        if (isSearch) {
            setIconFab(ICON_FAB_SEARCH_CLOSE)
            bottomBarVisible(false)
        } else {
            if (category == CATEGORY_TRASH) {
                setIconFab(ICON_FAB_DELETE)
            } else {
                setIconFab(ICON_FAB_ADD)
            }
        }
    }

    fun bottomBarVisible(isVisible: Boolean) {
        if (isVisible) {
            bottomBar.performShow()
        } else {
            bottomBar.performHide()
        }
    }
}