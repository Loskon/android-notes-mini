package com.loskon.noteminimalism3.ui.mainHelper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.loskon.noteminimalism3.ui.activity.SettingsActivity;

/**
 *
 */

public class BottomSheetDialog extends BottomSheetDialogFragment {

    public static final String TAG = "BottomSheetDialog";
    public NavigationView navigationView;
    private ItemClickListenerBottomNavView itemClickListener; // Для обратного вызова
    private int selNotesCategory;

    public static BottomSheetDialog newInstance() {
        return new BottomSheetDialog();
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
        navigationView.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.ddd, null));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        selNotesCategory = SharedPrefHelper.loadInt(requireContext(),
                "selNotesCategory", 0);

        navigationView.getMenu().getItem(selNotesCategory).setChecked(true);

        //setNavMenuItemThemeColors(Color.RED);
        ColorHelper.setNavMenuItemThemeColors(navigationView, Color.RED);

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int menuId = menuItem.getItemId();

            if (menuId == R.id.nav1) {
                selNotesCategory = 0;
            }
            else if (menuId == R.id.nav2) {
                selNotesCategory = 1;
            }
            else if (menuId == R.id.nav3) {
                selNotesCategory = 2;
            }
            else if (menuId == R.id.nav4) {
                Intent intent=new Intent(requireContext(),SettingsActivity.class);
                requireContext().startActivity(intent);
            }

            SharedPrefHelper.saveInt(requireContext(),"selNotesCategory", selNotesCategory);

            if (menuId != R.id.nav4) itemClickListener.onItemClickBottomNavView(selNotesCategory);

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
