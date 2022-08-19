package com.loskon.noteminimalism3.app.base.extension.view

import android.graphics.drawable.Drawable
import android.os.SystemClock
import android.view.MenuItem
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.Toolbar
import androidx.core.view.doOnPreDraw
import androidx.core.view.get
import com.google.android.material.bottomappbar.BottomAppBar
import com.loskon.noteminimalism3.app.base.extension.context.getDrawableKtx

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

fun BottomAppBar.setOnMenuItemClickListener(
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

fun BottomAppBar.setMenuIcon(menuItemId: Int, icon: Drawable) {
    menu.findItem(menuItemId).icon = icon
}

fun BottomAppBar.setMenuIconColor(menuItemId: Int, color: Int) {
    menu.findItem(menuItemId).icon.setTint(color)
}

fun BottomAppBar.setMenuIconWithColor(menuItemId: Int, icon: Drawable, color: Int) {
    menu.findItem(menuItemId).icon = icon
    setMenuIconColor(menuItemId, color)
}

fun BottomAppBar.setMenuIconWithColor(menuItemId: Int, drawableId: Int, color: Int) {
    menu.findItem(menuItemId).icon = context.getDrawableKtx(drawableId)
    setMenuIconColor(menuItemId, color)
}

fun BottomAppBar.setAllMenuItemsVisibility(visible: Boolean) {
    for (i in 0 until menu.size()) {
        menu[i].isVisible = visible
    }
}

fun BottomAppBar.show(animate: Boolean = true) {
    doOnPreDraw { (it as BottomAppBar).performShow(animate) }
}

fun BottomAppBar.hide(animate: Boolean = true) {
    doOnPreDraw { (it as BottomAppBar).performHide(animate) }
}

fun BottomAppBar.setNavigationIconColorKtx(@ColorInt color: Int) {
    navigationIcon?.mutate()?.setTint(color)
}

fun BottomAppBar.setNavigationIconWithColor(@DrawableRes drawable: Int, @ColorInt color: Int) {
    setNavigationIcon(drawable)
    setNavigationIconColorKtx(color)
}