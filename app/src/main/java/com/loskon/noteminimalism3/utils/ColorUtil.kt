package com.loskon.noteminimalism3.utils

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.Menu
import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.graphics.ColorUtils
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.slider.Slider
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.sharedpref.AppPref
import com.loskon.noteminimalism3.utils.ColorUtil.Companion.ALPHA_COLOR

/**
 *
 */

class ColorUtil {
    companion object {

        const val ALPHA_COLOR = 70

        fun setDarkTheme(isDarkMode: Boolean) {
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        fun hasDarkMode(context: Context): Boolean {
            val currentNightMode = context
                .resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            return currentNightMode != Configuration.UI_MODE_NIGHT_NO
        }
    }
}

//
fun NavigationView.setColorStateMenuItem(context: Context) {
    // Цвет MenuItem для NavigationView
    val colorChecked = AppPref.getAppColor(context)

    var navDefaultTextColor = Color.BLACK
    var navDefaultIconColor = Color.BLACK

    if (ColorUtil.hasDarkMode(context)) {
        navDefaultTextColor = Color.WHITE
        navDefaultIconColor = context.getShortColor(R.color.color_icon_nav_menu_dark)
    }


    // ColorStateList для текста пункта меню
    val navMenuTextList = ColorStateList(
        arrayOf(
            intArrayOf(android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_enabled),
            intArrayOf(android.R.attr.state_pressed),
            intArrayOf(android.R.attr.state_focused),
            intArrayOf(android.R.attr.state_pressed)
        ), intArrayOf(
            colorChecked,
            navDefaultTextColor,
            navDefaultTextColor,
            navDefaultTextColor,
            navDefaultTextColor
        )
    )

    // ColorStateList для иконки пункта меню
    val navMenuIconList = ColorStateList(
        arrayOf(
            intArrayOf(android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_enabled),
            intArrayOf(android.R.attr.state_pressed),
            intArrayOf(android.R.attr.state_focused),
            intArrayOf(android.R.attr.state_pressed)
        ), intArrayOf(
            colorChecked,
            navDefaultIconColor,
            navDefaultIconColor,
            navDefaultIconColor,
            navDefaultIconColor
        )
    )

    itemTextColor = navMenuTextList
    itemIconTintList = navMenuIconList
}


//
fun Menu.menuIconColor(@ColorInt color: Int) {
    if (size() != 0) {
        for (i in 0 until size()) {
            val drawable: Drawable = getItem(i).icon
            drawable.mutate()
            drawable.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                color,
                BlendModeCompat.SRC_ATOP
            )
        }
    }
}


//
fun Slider.setColorSlider(@ColorInt color: Int) {
    thumbTintList = ColorStateList.valueOf(color)
    trackActiveTintList = ColorStateList.valueOf(color)
    tickTintList = ColorStateList.valueOf(color)
    haloTintList = ColorStateList.valueOf(ColorUtils.setAlphaComponent(color, ALPHA_COLOR))
    trackInactiveTintList = ColorStateList.valueOf(ColorUtils.setAlphaComponent(color, ALPHA_COLOR))
}


//
fun FloatingActionButton.setFabColor(@ColorInt color: Int) {
    backgroundTintList = ColorStateList.valueOf(color)
}


//
fun BottomAppBar.setNavigationIconColor(@ColorInt color: Int) {
    navigationIcon?.mutate()?.setTint(color)
}

//
fun MaterialButton.setButtonIconColor(@ColorInt colorId: Int) {
    iconTint = ColorStateList.valueOf(colorId)
}


//
fun CircularProgressIndicator.setColorProgressIndicator(@ColorInt color: Int) {
    setIndicatorColor(color)
    trackColor = ColorUtils.setAlphaComponent(color, ALPHA_COLOR)
}


//
fun View.setColorBackgroundSnackbar(context: Context, isSuccess: Boolean) {
    val colorId: Int = context.getShortColor(context.getSuccessColor(isSuccess))
    backgroundTintList = ColorStateList.valueOf(colorId)
}

fun Context.getSuccessColor(isSuccess: Boolean): Int {
    return if (ColorUtil.hasDarkMode(this)) {
        if (isSuccess) {
            R.color.snackbar_completed_dark
        } else {
            R.color.snackbar_no_completed_dark
        }
    } else {
        if (isSuccess) {
            R.color.snackbar_completed_light
        } else {
            R.color.snackbar_no_completed_light
        }
    }
}


//
fun View.setBackgroundTintColor(color: Int) {
    backgroundTintList = ColorStateList.valueOf(color)
}


//
fun View.ripple(): View {
    val value = TypedValue()
    context.theme.resolveAttribute(android.R.attr.colorControlHighlight, value, true)
    setBackgroundResource(value.resourceId)
    isFocusable = true // Required for some view types
    return this
}