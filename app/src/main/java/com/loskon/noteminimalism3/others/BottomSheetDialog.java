package com.loskon.noteminimalism3.others;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

/**
 *
 */

public class BottomSheetDialog extends BottomSheetDialogFragment {

    private SharedPreferences mSharedPref;
    public static final String TAG = "BottomSheetDialog";
    public NavigationView navigationView;
    private ItemClickListenerBottomNavView mListener; // Для обратного вызова
    private int noteValMode;

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
        loadNoteMode();
        navigationView.getMenu().getItem(noteValMode).setChecked(true);
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int menuId = menuItem.getItemId();
            if (menuId == R.id.nav1) noteValMode = 0;
            else if (menuId == R.id.nav2) noteValMode = 1;
            else if (menuId == R.id.nav3) noteValMode = 2;
            else if (menuId == R.id.nav4) Objects.requireNonNull(getContext()).startActivity((new Intent(getContext(), SettingsActivity.class)));
            saveNoteMode();
            if (menuId != R.id.nav4) mListener.onItemClickBottomNavView(noteValMode);
            dismiss();
            return true;
        });
    }

    private void loadNoteMode() {
        mSharedPref = Objects.requireNonNull(getContext()).getSharedPreferences("saveNoteValModel", MODE_PRIVATE);
        noteValMode = mSharedPref.getInt("noteValMode", 0);
    }

    private void saveNoteMode() {
        mSharedPref = Objects.requireNonNull(getContext()).getSharedPreferences("saveNoteValModel", MODE_PRIVATE);
        SharedPreferences.Editor edit = mSharedPref.edit();
        edit.putInt("noteValMode", noteValMode);
        edit.apply();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ItemClickListenerBottomNavView) {
            mListener = (ItemClickListenerBottomNavView) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface ItemClickListenerBottomNavView {
        void onItemClickBottomNavView(int item);
    }
}
