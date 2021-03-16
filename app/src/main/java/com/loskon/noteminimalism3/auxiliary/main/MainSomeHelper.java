package com.loskon.noteminimalism3.auxiliary.main;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.loskon.noteminimalism3.ui.fragments.MyBottomSheet;

/**
 * Помощник со статическими методами для MainActivity
 */

public class MainSomeHelper {

    public static void bottomNavViewShow(FragmentManager fragmentManager) {
        // Вызов bottomSheet
        if (fragmentManager.findFragmentByTag(MyBottomSheet.TAG) == null) {
            MyBottomSheet dialogBottomSheet = MyBottomSheet.newInstance();
            dialogBottomSheet.show(fragmentManager, MyBottomSheet.TAG);
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


