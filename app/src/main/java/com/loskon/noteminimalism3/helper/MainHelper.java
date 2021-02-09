package com.loskon.noteminimalism3.helper;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.loskon.noteminimalism3.ui.dialogs.MyDialogBottomSheet;

public class MainHelper {

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
}


