package com.loskon.noteminimalism3.utils

import android.app.Activity
import android.app.ActivityManager.TaskDescription
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.TypedValue
import android.view.Menu
import android.view.View
import android.view.WindowInsetsController
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
import com.loskon.noteminimalism3.sharedpref.PrefManager
import com.loskon.noteminimalism3.utils.ColorManager.Companion.ALPHA_COLOR

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
            val currentNightMode = context
                .resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            return currentNightMode != Configuration.UI_MODE_NIGHT_NO
        }

        fun setColorApp(activity: Activity) {
            setDarkTheme(activity)
            setLightStatusBar(activity)
            setColorStatusBar(activity)
            setColorTask(activity)
        }

        private fun setDarkTheme(activity: Activity) {
            if (PrefManager.isDarkMode(activity)) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        @Suppress("DEPRECATION")
        private fun setLightStatusBar(activity: Activity) {
            activity.window.apply {
                navigationBarColor = Color.BLACK

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


        private fun setColorStatusBar(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                val color: Int = if (hasDarkMode(activity)) {
                    activity.getColor(R.color.background_dark)
                } else {
                    Color.WHITE
                }

                activity.window.statusBarColor = color
            }
        }

        @Suppress("DEPRECATION")
        private fun setColorTask(activity: Activity) {
            activity.apply {

                val color: Int = if (PrefManager.isDarkMode(this)) {
                    getShortColor(R.color.background_dark)
                } else {
                    Color.WHITE
                }

                setTaskDescription(TaskDescription(null, null, color))
            }
        }
    }
}

// Цвет пунктов меню
fun NavigationView.setColorStateMenuItem(context: Context) {
    // Цвет MenuItem
    val color: Int = PrefManager.getAppColor(context)

    val navDefaultTextColor: Int
    val navDefaultIconColor: Int

    if (ColorManager.hasDarkMode(context)) {
        navDefaultTextColor = Color.WHITE
        navDefaultIconColor = context.getShortColor(R.color.color_icon_nav_menu_dark)
    } else {
        navDefaultTextColor = Color.BLACK
        navDefaultIconColor = Color.BLACK
    }

    // ColorStateList для текста
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

    // ColorStateList для иконок
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

// Цвет иконок меню
fun Menu.setMenuIconColor(@ColorInt color: Int) {
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

// Цвет слайдера
fun Slider.setSliderColor(@ColorInt color: Int) {
    thumbTintList = ColorStateList.valueOf(color)
    trackActiveTintList = ColorStateList.valueOf(color)
    tickTintList = ColorStateList.valueOf(color)
    haloTintList = ColorStateList.valueOf(ColorUtils.setAlphaComponent(color, ALPHA_COLOR))
    trackInactiveTintList = ColorStateList.valueOf(ColorUtils.setAlphaComponent(color, ALPHA_COLOR))
}

// Цвет фона fab
fun FloatingActionButton.setFabColor(@ColorInt color: Int) {
    backgroundTintList = ColorStateList.valueOf(color)
}

// Цвет иконки навигации
fun BottomAppBar.setNavigationIconColor(@ColorInt color: Int) {
    navigationIcon?.mutate()?.setTint(color)
}

// Цвет иконки кнопки
fun MaterialButton.setButtonIconColor(@ColorInt colorId: Int) {
    iconTint = ColorStateList.valueOf(colorId)
}

// Цвет индикатора
fun CircularProgressIndicator.setColorProgressIndicator(@ColorInt color: Int) {
    setIndicatorColor(color)
    trackColor = ColorUtils.setAlphaComponent(color, ALPHA_COLOR)
}

// Цвет фона Snackbar
fun View.setColorBackgroundSnackbar(context: Context, isSuccess: Boolean) {
    val colorId: Int = context.getShortColor(context.getSuccessColor(isSuccess))
    backgroundTintList = ColorStateList.valueOf(colorId)
}

fun Context.getSuccessColor(isSuccess: Boolean): Int {
    return if (ColorManager.hasDarkMode(this)) {
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

// Цвет фона
fun View.setBackgroundTintColor(color: Int) {
    backgroundTintList = ColorStateList.valueOf(color)
}

// Цвет пульсации
fun View.ripple(): View {
    val value = TypedValue()
    context.theme.resolveAttribute(android.R.attr.colorControlHighlight, value, true)
    setBackgroundResource(value.resourceId)
    isFocusable = true // Required for some view types
    return this
}

// Цвет переключателя
fun RadioButton.setRadioButtonColor(color: Int) {
    buttonTintList = ColorStateList.valueOf(color)
}