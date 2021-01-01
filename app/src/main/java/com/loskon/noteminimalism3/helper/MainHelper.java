package com.loskon.noteminimalism3.helper;

import android.content.Context;
import android.view.Menu;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.loskon.noteminimalism3.ui.dialogs.MyDialogBottomSheet;

public class MainHelper {

    public static final int REQUEST_CODE_PERMISSIONS = 298;

    public static void bottomNavViewShow(FragmentManager fragmentManager) {
        if (fragmentManager.findFragmentByTag(MyDialogBottomSheet.TAG) == null) {
            MyDialogBottomSheet bottomSheet = MyDialogBottomSheet.newInstance();
            bottomSheet.show(fragmentManager, MyDialogBottomSheet.TAG);
        }
    }

    public static void removeFlicker(RecyclerView recyclerView) {
        // Удаляет мерцание элементов
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
    }

    public static void setMenuIcon(Context context, Menu menu, int id,  int icon) {
        if (menu != null) {
            menu.findItem(id).
                    setIcon(ResourcesCompat.getDrawable(context.getResources(),
                            icon, null));
        }
    }

    private static void Gh() {

    }
}


