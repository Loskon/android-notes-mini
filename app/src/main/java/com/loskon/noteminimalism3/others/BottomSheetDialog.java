package com.loskon.noteminimalism3.others;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.activity.SettingsActivity;

/**
 *
 */

public class BottomSheetDialog extends BottomSheetDialogFragment {

    public static final String TAG = "BottomSheetDialog";
    public NavigationView navigationView;
    private ItemClickListenerBottomNavView itemClickListener; // Для обратного вызова
    private int selectedNoteMode;

    public static BottomSheetDialog newInstance() {
        return new BottomSheetDialog();
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottomsheet, container, false);
        navigationView =  view.findViewById(R.id.navigation_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int menuId = menuItem.getItemId();
            if (menuId == R.id.nav1) selectedNoteMode = 0;
            else if (menuId == R.id.nav2) selectedNoteMode = 1;
            else if (menuId == R.id.nav3) selectedNoteMode = 2;
            else if (menuId == R.id.nav4) requireContext().startActivity((new Intent(getContext(), SettingsActivity.class)));
            if (menuId != R.id.nav4) itemClickListener.onItemClickBottomNavView(selectedNoteMode);
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
        void onItemClickBottomNavView(int selectedNoteMode);
    }
}
