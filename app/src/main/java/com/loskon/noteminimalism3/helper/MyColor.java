package com.loskon.noteminimalism3.helper;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Menu;
import android.view.View;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.helper.sharedpref.MySharedPreference;
import com.loskon.noteminimalism3.helper.sharedpref.MySharedPrefKeys;

import java.util.Objects;

/**
 *
 */

public class MyColor {

    public static void setColorSwitch(Context context, SwitchMaterial switchMaterial) {
        switchMaterial.getThumbDrawable().setColorFilter(getColorCustom(context), PorterDuff.Mode.SRC_ATOP);
        switchMaterial.getTrackDrawable().setColorFilter(getColorCustom(context), PorterDuff.Mode.SRC_ATOP);
    }

    public static void setColorStatBarAndTaskDesc(Activity activity) {
        int color = Color.WHITE;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.getWindow().getDecorView().
                    setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
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
        return MySharedPreference.loadInt(context,
                MySharedPrefKeys.KEY_COLOR, context.getResources()
                        .getColor(R.color.color_default_light_blue));
    }

    public static void setNavMenuItemThemeColors(NavigationView navigationView, int color){
        //Setting default colors for menu item Text and Icon
        int navDefaultTextColor = Color.parseColor("#000000");
        int navDefaultIconColor = Color.parseColor("#000000");

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

    public static void setNavigationIconColor(Context context, BottomAppBar appBar) {
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
