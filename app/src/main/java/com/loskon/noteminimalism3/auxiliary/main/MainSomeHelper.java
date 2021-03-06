package com.loskon.noteminimalism3.auxiliary.main;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.loskon.noteminimalism3.ui.dialogs.MyDialogBottomSheet;

/**
 * Помощник со статическими методами для MainActivity
 */

public class MainSomeHelper {

    public static void bottomNavViewShow(FragmentManager fragmentManager) {
        // Вызов bottomSheet
        if (fragmentManager.findFragmentByTag(MyDialogBottomSheet.TAG) == null) {
            MyDialogBottomSheet dialogBottomSheet = MyDialogBottomSheet.newInstance();
            dialogBottomSheet.show(fragmentManager, MyDialogBottomSheet.TAG);
        }
    }

    public static void removeFlicker(RecyclerView recyclerView) {
        // Удаляет мерцание элементов списка
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
    }
}


