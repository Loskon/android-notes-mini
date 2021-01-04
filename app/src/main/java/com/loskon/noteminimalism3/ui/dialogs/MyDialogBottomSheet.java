package com.loskon.noteminimalism3.ui.dialogs;

import android.content.Context;
import android.content.Intent;
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
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.helper.sharedpref.MySharedPref;
import com.loskon.noteminimalism3.helper.sharedpref.MyPrefKey;
import com.loskon.noteminimalism3.ui.activity.settings.SettingsActivity;

/**
 *
 */

public class MyDialogBottomSheet extends BottomSheetDialogFragment {

    public static final String TAG = "BottomSheetDialog";
    public NavigationView navigationView;
    private ItemClickListenerBottomNavView itemClickListener; // Для обратного вызова
    private int selNotesCategory;

    public static MyDialogBottomSheet newInstance() {
        return new MyDialogBottomSheet();
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottomsheet, container, false);
        navigationView =  view.findViewById(R.id.navigation_view);
        //navigationView.setBackgroundColor(Color.BLUE);
        navigationView.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.bottom_sheet_round_corner, null));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        selNotesCategory = MySharedPref.getInt(requireContext(),
                MyPrefKey.KEY_SEL_CATEGORY, 0);

        navigationView.getMenu().getItem(selNotesCategory).setChecked(true);

        MyColor.setNavMenuItemThemeColors(navigationView, MyColor.getColorCustom(getContext()));

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int menuId = menuItem.getItemId();

            if (menuId == R.id.nav1_note) {
                selNotesCategory = 0;
            }
            else if (menuId == R.id.nav2_fav) {
                selNotesCategory = 1;
            }
            else if (menuId == R.id.nav3_trash) {
                selNotesCategory = 2;
            }
            else if (menuId == R.id.nav4_settings) {
                Intent intent=new Intent(requireContext(), SettingsActivity.class);
                requireContext().startActivity(intent);
            }

            MySharedPref.setInt(requireContext(),
                    MyPrefKey.KEY_SEL_CATEGORY, selNotesCategory);

            if (menuId != R.id.nav4_settings) itemClickListener.onItemClickBottomNavView(selNotesCategory);

            dismiss();
            return true;
        });
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
    public void onDetach() {
        super.onDetach();

        itemClickListener = null;
    }

    public interface ItemClickListenerBottomNavView {
        void onItemClickBottomNavView(int selectedNotePage);
    }
}
