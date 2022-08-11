package com.loskon.noteminimalism3.app.base.extension.view

import android.os.SystemClock
import android.view.MenuItem
import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import com.google.android.material.bottomappbar.BottomAppBar

fun BottomAppBar.setDebounceNavigationClickListener(
    debounceTime: Long = 600L,
    navigationOnClick: () -> Unit
) {
    setNavigationOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0

        override fun onClick(view: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) {
                return
            } else {
                navigationOnClick()
            }

            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}

fun BottomAppBar.setDebounceMenuItemClickListener(
    debounceTime: Long = 600L,
    onMenuItemClick: (item: MenuItem) -> Unit
) {
    setOnMenuItemClickListener(object : Toolbar.OnMenuItemClickListener {
        private var lastClickTime: Long = 0

        override fun onMenuItemClick(item: MenuItem): Boolean {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) {
                return false
            } else {
                onMenuItemClick(item)
            }

            lastClickTime = SystemClock.elapsedRealtime()
            return true
        }
    })
}

fun BottomAppBar.setDebounceMenuItemClickListener(
    menuItemId: Int,
    debounceTime: Long = 600L,
    onMenuItemClick: (item: MenuItem) -> Unit
) {
    menu.findItem(menuItemId).setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
        private var lastClickTime: Long = 0

        override fun onMenuItemClick(item: MenuItem): Boolean {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) {
                return false
            } else {
                onMenuItemClick(item)
            }

            lastClickTime = SystemClock.elapsedRealtime()
            return true
        }
    })
}

fun BottomAppBar.setShortMenuItemClickListener(
    menuItemId: Int,
    onMenuItemClick: () -> Unit
) {
    menu.findItem(menuItemId).setOnMenuItemClickListener {
        onMenuItemClick()
        true
    }
}

fun BottomAppBar.setNavigationIconColor(@ColorInt color: Int) {
    navigationIcon?.mutate()?.setTint(color)
}

fun BottomAppBar.setMenuItemsColor(@ColorInt color: Int) {
    for (i in 0 until menu.size()) {
        menu[i].icon.setTint(color)
    }
}

fun BottomAppBar.setAllItemsColor(@ColorInt color: Int) {
    setNavigationIconColor(color)
    setMenuItemsColor(color)
}

fun BottomAppBar.setMenuItemVisibility(menuItemId: Int, visible: Boolean) {
    menu.findItem(menuItemId).isVisible = visible
}

fun BottomAppBar.setAllMenuItemsVisibility(visible: Boolean) {
    for (i in 0 until menu.size()) {
        menu[i].isVisible = visible
    }
}
