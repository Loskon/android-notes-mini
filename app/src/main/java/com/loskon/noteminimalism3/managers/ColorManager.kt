package com.loskon.noteminimalism3.managers

import android.app.Activity
import android.app.ActivityManager.TaskDescription
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.Menu
import android.view.View
import android.view.WindowInsetsController
import android.widget.CheckBox
import android.widget.RadioButton
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
import com.loskon.noteminimalism3.managers.ColorManager.Companion.ALPHA_COLOR
import com.loskon.noteminimalism3.sharedpref.PrefHelper
import com.loskon.noteminimalism3.utils.getShortColor

/**
 * Управление цветом
 */

class ColorManager {
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
            val constUi: Int = context.resources.configuration.uiMode
            val constUiMask: Int = Configuration.UI_MODE_NIGHT_MASK
            val currentNightMode: Int = constUi and constUiMask

            return currentNightMode != Configuration.UI_MODE_NIGHT_NO
        }

        fun installAppColor(activity: Activity) {
            installDarkTheme(activity)
            installColorTask(activity)
            installIconsColorStatusBar(activity)
            installBackgroundColorStatusBar(activity)
        }

        private fun installDarkTheme(activity: Activity) {
            if (PrefHelper.hasDarkMode(activity)) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        @Suppress("DEPRECATION")
        private fun installColorTask(activity: Activity) {
            activity.apply {

                val color: Int = if (PrefHelper.hasDarkMode(this)) {
                    getShortColor(R.color.black_dark)
                } else {
                    Color.WHITE
                }

                setTaskDescription(TaskDescription(null, null, color))
            }
        }

        @Suppress("DEPRECATION")
        private fun installIconsColorStatusBar(activity: Activity) {
            activity.window.apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (hasDarkMode(activity)) {
                        insetsController?.setSystemBarsAppearance(
                            0,
                            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                        )
                    } else {
                        insetsController?.setSystemBarsAppearance(
                            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                        )
                    }
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (hasDarkMode(activity)) {
                        decorView.systemUiVisibility = 0
                    } else {
                        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    }
                }
            }
        }

        private fun installBackgroundColorStatusBar(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val color: Int = if (hasDarkMode(activity)) {
                    activity.getColor(R.color.black_dark)
                } else {
                    Color.WHITE
                }

                activity.window.statusBarColor = color
            }
        }
    }
}


fun NavigationView.setColorStateMenuItem(context: Context) {
    val color: Int = PrefHelper.getAppColor(context)

    val navDefaultTextColor: Int
    val navDefaultIconColor: Int

    if (ColorManager.hasDarkMode(context)) {
        navDefaultTextColor = Color.WHITE
        navDefaultIconColor = context.getShortColor(R.color.dark_gray_light)
    } else {
        navDefaultTextColor = Color.BLACK
        navDefaultIconColor = Color.BLACK
    }

    // Text
    val navMenuTexColor = ColorStateList(
        arrayOf(
            intArrayOf(android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_enabled),
            intArrayOf(android.R.attr.state_pressed),
            intArrayOf(android.R.attr.state_focused),
            intArrayOf(android.R.attr.state_pressed)
        ), intArrayOf(
            color,
            navDefaultTextColor,
            navDefaultTextColor,
            navDefaultTextColor,
            navDefaultTextColor
        )
    )

    // Icon
    val navMenuIconTintList = ColorStateList(
        arrayOf(
            intArrayOf(android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_enabled),
            intArrayOf(android.R.attr.state_pressed),
            intArrayOf(android.R.attr.state_focused),
            intArrayOf(android.R.attr.state_pressed)
        ), intArrayOf(
            color,
            navDefaultIconColor,
            navDefaultIconColor,
            navDefaultIconColor,
            navDefaultIconColor
        )
    )

    itemTextColor = navMenuTexColor
    itemIconTintList = navMenuIconTintList
}


fun Menu.setMenuIconsColor(@ColorInt color: Int) {
    if (this.size() != 0) {
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


fun Slider.setSliderColor(@ColorInt color: Int) {
    thumbTintList = ColorStateList.valueOf(color)
    trackActiveTintList = ColorStateList.valueOf(color)
    tickTintList = ColorStateList.valueOf(color)
    haloTintList = ColorStateList.valueOf(ColorUtils.setAlphaComponent(color, ALPHA_COLOR))
    trackInactiveTintList = ColorStateList.valueOf(ColorUtils.setAlphaComponent(color, ALPHA_COLOR))
}


fun FloatingActionButton.setFabColor(@ColorInt color: Int) {
    backgroundTintList = ColorStateList.valueOf(color)
}


fun BottomAppBar.setNavigationIconColor(@ColorInt color: Int) {
    navigationIcon?.mutate()?.setTint(color)
}


fun MaterialButton.setButtonIconColor(@ColorInt colorId: Int) {
    iconTint = ColorStateList.valueOf(colorId)
}


fun CircularProgressIndicator.setColorProgressIndicator(@ColorInt color: Int) {
    setIndicatorColor(color)
    trackColor = ColorUtils.setAlphaComponent(color, ALPHA_COLOR)
}


fun View.setColorBackgroundSnackbar(context: Context, isSuccess: Boolean) {
    val colorId: Int = context.getSuccessColor(isSuccess)
    backgroundTintList = ColorStateList.valueOf(colorId)
}

fun Context.getSuccessColor(isSuccess: Boolean): Int {
    val color: Int = if (ColorManager.hasDarkMode(this)) {
        if (isSuccess) {
            R.color.dark_green
        } else {
            R.color.dark_red
        }
    } else {
        if (isSuccess) {
            R.color.light_green
        } else {
            R.color.light_red
        }
    }

    return this.getShortColor(color)
}


fun View.setBackgroundTintColor(color: Int) {
    backgroundTintList = ColorStateList.valueOf(color)
}


fun RadioButton.setRadioButtonColor(color: Int) {
    buttonTintList = ColorStateList.valueOf(color)
}


fun MaterialButton.setStrokeBtnColor(color: Int) {
    this.strokeColor = ColorStateList.valueOf(color)
}


fun CheckBox.setStrokeCheckBoxColor(color: Int) {
    this.buttonTintList = ColorStateList.valueOf(color)
}