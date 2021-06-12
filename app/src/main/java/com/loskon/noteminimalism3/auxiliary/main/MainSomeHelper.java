package com.loskon.noteminimalism3.auxiliary.main;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.loskon.noteminimalism3.ui.fragments.BottomSheetFragment;

/**
 * Помощник со статическими методами для MainActivity
 */

public class MainSomeHelper {

    public static void bottomNavViewShow(FragmentManager fragmentManager) {
        // Вызов bottomSheet
        if (fragmentManager.findFragmentByTag(BottomSheetFragment.TAG) == null) {
            BottomSheetFragment dialogBottomSheet = BottomSheetFragment.newInstance();
            dialogBottomSheet.show(fragmentManager, BottomSheetFragment.TAG);
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


