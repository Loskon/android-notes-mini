package com.loskon.noteminimalism3.helper;

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
import androidx.core.graphics.ColorUtils;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.slider.Slider;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.helper.sharedpref.MyPrefKey;
import com.loskon.noteminimalism3.helper.sharedpref.MySharedPref;

import java.util.Objects;

/**
 *
 */

public class MyColor {

    public static void setDarkTheme(boolean isDarkModeOn) {
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public static boolean isDarkMode(Activity activity) {
        int currentNightMode = activity
                .getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        return currentNightMode != Configuration.UI_MODE_NIGHT_NO;
    }

    public static int getColorDivider(Activity activity) {
        int color;

        if (isDarkMode(activity)) {
            color = R.color.color_divider_dark;
        } else {
            color = R.color.color_divider_light;
        }

        return activity.getResources().getColor(color);
    }

    public static int getColorBackgroundSnackbar(Activity activity) {
        int color;

        if (isDarkMode(activity)) {
            color = R.color.snackbar_background_dark;
        } else {
            color = R.color.snackbar_background_light;
        }

        return activity.getResources().getColor(color);
    }

    public static void setColorToast(Activity activity, View view, boolean isSuccess) {
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

        view.getBackground()
                .setColorFilter(activity.getResources()
                        .getColor(color), PorterDuff.Mode.SRC_IN);
    }

    public static void setColorSnackbar(Activity activity, View snackbarView, boolean isSuccess) {
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

        snackbarView.setBackgroundTintList(ColorStateList
                .valueOf(activity.getResources().getColor(color)));
    }

    public static void setColorSwitch(Context context, SwitchMaterial switchMaterial) {
        switchMaterial.getThumbDrawable().setColorFilter(getColorCustom(context), PorterDuff.Mode.SRC_ATOP);
        switchMaterial.getTrackDrawable().setColorFilter(getColorCustom(context), PorterDuff.Mode.SRC_ATOP);
    }

    public static void setColorSlider(Context context, Slider slider) {
        int color = getColorCustom(context);
        slider.setThumbTintList(ColorStateList.valueOf(color));
        slider.setTrackActiveTintList(ColorStateList.valueOf(color));
        slider.setTickTintList(ColorStateList.valueOf(color));

        slider.setHaloTintList(ColorStateList.valueOf(ColorUtils.setAlphaComponent(color, 70)));
        slider.setTrackInactiveTintList(ColorStateList.valueOf(ColorUtils.setAlphaComponent(color, 70)));
    }

    public static void setColorStatBarAndTaskDesc(Activity activity) {
        int color = 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

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

        activity.setTaskDescription(new ActivityManager
                .TaskDescription(null, null, color));

    }

    public static void setColorMenuIcon(Context context, Menu menu) {
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
        if (fab != null) {
            fab.setBackgroundTintList(ColorStateList.valueOf(getColorCustom(context)));
        }
    }

    public static void setColorMaterialBtn(Context context, MaterialButton materialBtn) {
        if (materialBtn != null) {
            materialBtn.setIconTint(ColorStateList.valueOf(getColorCustom(context)));
        }
    }

    public static int getColorCustom(Context context) {
        return MySharedPref.getInt(context,
                MyPrefKey.KEY_COLOR, context.getResources()
                        .getColor(R.color.color_default_light_blue));
    }

    public static void setNavMenuItemThemeColors(Activity activity,
                                                 NavigationView navigationView) {
        int color = MyColor.getColorCustom(activity);
        //Setting default colors for menu item Text and Icon
        int navDefaultTextColor;
        int navDefaultIconColor;

        if (isDarkMode(activity)) {
            navDefaultTextColor = Color.WHITE;
            navDefaultIconColor = activity.getResources().getColor(R.color.color_icon_nav_menu_dark);
        } else {
            navDefaultTextColor = Color.BLACK;
            navDefaultIconColor = Color.BLACK;
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
                new int[] {
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
                new int[] {
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
        if (appBar != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Objects.requireNonNull(appBar.getNavigationIcon())
                        .setColorFilter(new BlendModeColorFilter(
                                getColorCustom(context), BlendMode.SRC_ATOP));
            } else {
                Objects.requireNonNull(appBar.getNavigationIcon())
                        .setColorFilter(getColorCustom(context), PorterDuff.Mode.SRC_ATOP);
            }
        }
    }
}
