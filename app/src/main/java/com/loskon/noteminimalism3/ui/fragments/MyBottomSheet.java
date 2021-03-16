package com.loskon.noteminimalism3.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.other.MyColor;
import com.loskon.noteminimalism3.auxiliary.other.MyIntent;
import com.loskon.noteminimalism3.auxiliary.sharedpref.GetSharedPref;
import com.loskon.noteminimalism3.auxiliary.sharedpref.MyPrefKey;
import com.loskon.noteminimalism3.auxiliary.sharedpref.MySharedPref;

/**
 * Нижнее наивгационное меню для перемещения по категориям и открытия настроек приложения
 */

public class MyBottomSheet extends BottomSheetDialogFragment {

    public static final String TAG = "BottomSheetDialog";
    public NavigationView navigationView;
    private ItemClickListenerBottomNavView itemClickListener;
    private int selNotesCategory;

    public static MyBottomSheet newInstance() {
        return new MyBottomSheet();
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetFragmentTheme;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottomsheet, container, false);
        navigationView = view.findViewById(R.id.navigation_view);
        navigationView.setBackground(ResourcesCompat
                .getDrawable(getResources(), R.drawable.bottom_sheet_round_corner_light, null));
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof ItemClickListenerBottomNavView) {
            itemClickListener = (ItemClickListenerBottomNavView) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ItemClickListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        selNotesCategory = GetSharedPref.getNotesCategory(requireContext());
        navigationView.getMenu().getItem(selNotesCategory).setChecked(true);

        MyColor.setNavMenuItemThemeColors(requireActivity(), navigationView);

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int menuId = menuItem.getItemId();

            if (menuId == R.id.nav_item_note) {
                selNotesCategory = 0;
            } else if (menuId == R.id.nav_item_favorites) {
                selNotesCategory = 1;
            } else if (menuId == R.id.nav_item_trash) {
                selNotesCategory = 2;
            } else if (menuId == R.id.nav_item_settings) {
                MyIntent.openSettings(requireContext());
            }

            MySharedPref.setInt(requireContext(), MyPrefKey.KEY_NOTES_CATEGORY, selNotesCategory);

            if (menuId != R.id.nav_item_settings) {
                itemClickListener.onItemClickBottomNavView();
            }

            dismiss();
            return true;
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        itemClickListener = null;
    }

    public interface ItemClickListenerBottomNavView {
        void onItemClickBottomNavView();
    }
}
