package com.loskon.noteminimalism3.auxiliary.other;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.BlendModeColorFilterCompat;
import androidx.core.graphics.BlendModeCompat;
import androidx.core.graphics.ColorUtils;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.slider.Slider;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.sharedpref.MyPrefKey;
import com.loskon.noteminimalism3.auxiliary.sharedpref.MySharedPref;

/**
 * Установка цветов для элементов
 */

public class MyColor {

    public static void setDarkTheme(boolean isDarkModeOn) {
        // Установка темной темы
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public static boolean isDarkMode(Activity activity) {
        // Проверка установлена ли темная тема
        int currentNightMode = activity
                .getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return currentNightMode != Configuration.UI_MODE_NIGHT_NO;
    }

    public static int getColorBackgroundSnackbar(Activity activity) {
        // Получение цвета фона Snackbar
        int color;

        if (isDarkMode(activity)) {
            color = R.color.snackbar_background_dark;
        } else {
            color = R.color.snackbar_background_light;
        }

        return activity.getResources().getColor(color);
    }

    public static void setColorToast(Activity activity, View toastView, boolean isSuccess) {
        // Цвет фона Toast
        toastView.getBackground()
                .setColorFilter(BlendModeColorFilterCompat
                        .createBlendModeColorFilterCompat(activity
                                .getResources().getColor(getSuccessColor(activity,
                                        isSuccess)), BlendModeCompat.SRC_IN));
    }

    public static void setColorSnackbar(Activity activity, View snackbarView, boolean isSuccess) {
        // Цвет фона Snackbar
        snackbarView.setBackgroundTintList(ColorStateList
                .valueOf(activity.getResources().getColor(getSuccessColor(activity, isSuccess))));
    }

    private static int getSuccessColor(Activity activity, boolean isSuccess) {
        int color;

        if (isDarkMode(activity)) {
            if (isSuccess) {
                color = R.color.snackbar_completed_dark;
            } else {
                color = R.color.snackbar_no_completed_dark;
            }
        } else {
            if (isSuccess) {
                color = R.color.snackbar_completed_light;
            } else {
                color = R.color.snackbar_no_completed_light;
            }
        }

        return color;
    }

    public static void setColorSlider(Context context, Slider slider) {
        // Цвет Slider
        int color = getColorCustom(context);
        slider.setThumbTintList(ColorStateList.valueOf(color));
        slider.setTrackActiveTintList(ColorStateList.valueOf(color));
        slider.setTickTintList(ColorStateList.valueOf(color));

        slider.setHaloTintList(ColorStateList.valueOf(ColorUtils.setAlphaComponent(color, 70)));
        slider.setTrackInactiveTintList(ColorStateList.valueOf(ColorUtils.setAlphaComponent(color, 70)));
    }

    public static void setColorStatBarAndTaskDesc(Activity activity) {
        // Цвет Status Bar и Task Description
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int color;

            if (isDarkMode(activity)) {
                color = activity.getColor(R.color.dark_light);
                activity.getWindow().getDecorView().setSystemUiVisibility(0);
            } else {
                color = Color.WHITE;
                activity.getWindow().getDecorView().
                        setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }

            activity.getWindow().setStatusBarColor(color);
        }

        setColorTask(activity);
    }

    private static void setColorTask(Activity activity) {
        int color;

        if (isDarkMode(activity)) {
            color = activity.getResources().getColor(R.color.dark_light);
        } else {
            color = Color.WHITE;
        }
        activity.setTaskDescription(new ActivityManager
                .TaskDescription(null, null, color));
    }

    public static void setColorMenuItem(Context context, Menu menu) {
        // Цвет MenuItem для BottomAppBar
        if (menu != null) {
            for (int i = 0; i < menu.size(); i++) {
                Drawable drawable = menu.getItem(i).getIcon();
                if (drawable != null) {
                    drawable.mutate();
                    drawable.setColorFilter(getColorCustom(context), PorterDuff.Mode.SRC_ATOP);
                }
            }
        }
    }

    public static void setColorFab(Context context, FloatingActionButton fab) {
        // Цвет FloatingActionButton
        fab.setBackgroundTintList(ColorStateList.valueOf(getColorCustom(context)));
    }

    public static void setColorMaterialBtn(Context context, MaterialButton materialBtn) {
        // Цвет MaterialButton
        materialBtn.setIconTint(ColorStateList.valueOf(getColorCustom(context)));
    }

    public static int getColorCustom(Context context) {
        return MySharedPref.getInt(context,
                MyPrefKey.KEY_COLOR, context.getResources()
                        .getColor(R.color.color_default_light_blue));
    }

    public static void setNavMenuItemThemeColors(Activity activity,
                                                 NavigationView navigationView) {
        // Цвет MenuItem для NavigationView
        int color = MyColor.getColorCustom(activity);
        int navDefaultTextColor = Color.BLACK;
        int navDefaultIconColor = Color.BLACK;

        if (isDarkMode(activity)) {
            navDefaultTextColor = Color.WHITE;
            navDefaultIconColor = activity
                    .getResources().getColor(R.color.color_icon_nav_menu_dark);
        }


        //Defining ColorStateList for menu item Text
        ColorStateList navMenuTextList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{android.R.attr.state_enabled},
                        new int[]{android.R.attr.state_pressed},
                        new int[]{android.R.attr.state_focused},
                        new int[]{android.R.attr.state_pressed}
                },
                new int[]{
                        color,
                        navDefaultTextColor,
                        navDefaultTextColor,
                        navDefaultTextColor,
                        navDefaultTextColor
                }
        );

        //Defining ColorStateList for menu item Icon
        ColorStateList navMenuIconList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{android.R.attr.state_enabled},
                        new int[]{android.R.attr.state_pressed},
                        new int[]{android.R.attr.state_focused},
                        new int[]{android.R.attr.state_pressed}
                },
                new int[]{
                        color,
                        navDefaultIconColor,
                        navDefaultIconColor,
                        navDefaultIconColor,
                        navDefaultIconColor
                }
        );

        navigationView.setItemTextColor(navMenuTextList);
        navigationView.setItemIconTintList(navMenuIconList);
    }

    public static void setNavIconColor(Context context, BottomAppBar appBar) {
        // Цвет NavigationIcon
        if (appBar.getNavigationIcon() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                appBar.getNavigationIcon()
                        .setColorFilter(new BlendModeColorFilter(
                                getColorCustom(context), BlendMode.SRC_ATOP));
            } else {
                appBar.getNavigationIcon()
                        .setColorFilter(getColorCustom(context), PorterDuff.Mode.SRC_ATOP);
            }
        }
    }
}
